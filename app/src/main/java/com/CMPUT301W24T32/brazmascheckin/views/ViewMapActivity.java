package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.User;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewMapActivity extends AppCompatActivity {
    private MapView mapView;
    private Button chooseLocationBtn;
    private Context ctx;
    private String deviceID;
    private UserController userController;
    private EventController eventController;
    private ImageController imageController;
    public static final String EXTRA_LOCATION_PAIRS = "user_location_pair";
    public static final String EXTRA_EVENT = "event";
    public static final String EXTRA_MODE = "mode";
    public static final int VIEW_ATTENDEES = 0;
    public static final int CHOOSE_LOCATION = 1;
    public static final int VIEW_ATTENDEE_CHECK_INS = 2;
    public static final int VIEW_ALL_EVENTS = 3;
    public static final int VIEW_ORGANIZED_EVENTS = 4;
    public static final String EXTRA_PREV_LOCATION = "previous_location";
    public static final String RESULT_LOCATION = "result_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // default configuration
        configureActivity();
        configureControllers();

        Intent i = getIntent();

        int mode = i.getIntExtra(EXTRA_MODE, -1);

        switch(mode) {
            case VIEW_ATTENDEES:
                chooseLocationBtn.setVisibility(View.GONE);
                HashMap<String, Location> locations = (HashMap<String, Location>) i.getSerializableExtra(EXTRA_LOCATION_PAIRS);
                Event event = (Event) i.getSerializableExtra(EXTRA_EVENT);
                displayCheckedInAttendees(event, locations);
                break;
            case CHOOSE_LOCATION:
                Location previousLocation = (Location) i.getSerializableExtra(EXTRA_PREV_LOCATION);
                chooseLocationBtn.setVisibility(View.VISIBLE);
                chooseLocation(previousLocation);
                break;
            case VIEW_ATTENDEE_CHECK_INS:
                chooseLocationBtn.setVisibility(View.GONE);
                showAttendeeCheckIns();
                break;
            case VIEW_ALL_EVENTS:
                chooseLocationBtn.setVisibility(View.GONE);
                showAllEvents();
                break;
            case VIEW_ORGANIZED_EVENTS:
                chooseLocationBtn.setVisibility(View.GONE);
                showOrganizedEvents();
                break;
        }

        mapView.invalidate();
    }

    /**
     * Configures the activity by initializing the map view, setting its properties,
     * and initializing necessary controllers and UI elements.
     */
    private void configureActivity() {
        mapView = (MapView) findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setVerticalMapRepetitionEnabled(false);
        mapView.setScrollableAreaLimitLatitude(MapView.getTileSystem().getMaxLatitude(),
                MapView.getTileSystem().getMinLatitude(), 0);
        mapView.setMinZoomLevel(3.0);
        userController = new UserController(this);
        chooseLocationBtn = findViewById(R.id.view_map_done_btn);
        IMapController mapController = mapView.getController();
        mapController.setZoom(3);

        deviceID = DeviceID.getDeviceID(this);
    }

    private void configureControllers() {
        userController = new UserController(this);
        eventController = new EventController(this);
        imageController = new ImageController(this);
    }

    /**
     * Displays markers for the event location and checked-in attendees on the map.
     * @param event The event for which markers are to be displayed.
     * @param locations A HashMap containing the locations of the checked-in attendees.
     */
    private void displayCheckedInAttendees(Event event, HashMap<String, Location> locations) {
        // event location
        Bitmap bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(25, 25, 25, paint);

        Location location = event.getEventLocation();
        Marker marker = new Marker(mapView);
        GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());
        marker.setPosition(point);
        marker.setIcon(new BitmapDrawable(getResources(), bitmap));
        marker.setTitle(event.getName());
        marker.setSnippet(event.getDescription());
        mapView.getOverlays().add(marker);

        for(String id : locations.keySet()) {
            Location l = locations.get(id);
            Marker m = new Marker(mapView);
            GeoPoint p = new GeoPoint(l.getLatitude(), l.getLongitude());

            userController.getUser(id, user -> {
                m.setPosition(p);

                String name = user.getFirstName() + " " + user.getLastName();
                m.setTitle(name);

                /*
                TODO:
                1. instead of latitude/longitude, include address
                2. maybe include address of event in event details
                 */
                String snippet = "User ID: " + id + "<br>Latitude: " + l.getLatitude() + "<br>Longitude: " + l.getLongitude();
                m.setSnippet(snippet);

                //TODO: add icon for profile

                mapView.getOverlays().add(m);
            }, e -> Toast.makeText(ctx, "Unable to display user " + id, Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Allows the user to choose a location on the map by tapping. Once a location is chosen,
     * a marker is placed on the map to represent the chosen location.
     * If the user confirms the chosen location, the result is returned to the calling activity.
     */
    private void chooseLocation(Location previousLocation) {
        Marker marker = new Marker(mapView);
        if(previousLocation != null) {
            GeoPoint p = new GeoPoint(previousLocation.getLatitude(), previousLocation.getLongitude());
            marker.setPosition(p);
        }
        marker.setTitle("Event Location");
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                marker.setPosition(p);
                String location = "Latitude: " + p.getLatitude() + "<br> Longitude: " + p.getLongitude();
                marker.setSnippet(location);
                mapView.invalidate();

                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        mapView.getOverlays().add(0, mapEventsOverlay);
        mapView.getOverlays().add(marker);

        chooseLocationBtn.setOnClickListener(view -> {
            GeoPoint position = marker.getPosition();
            if(position != null) {
                Location location = new Location(position.getLatitude(), position.getLongitude());
                Intent result = new Intent();
                result.putExtra(EXTRA_MODE, CHOOSE_LOCATION);
                result.putExtra(RESULT_LOCATION, location);
                setResult(Activity.RESULT_OK, result);
                finish();
            } else {
                Toast.makeText(ctx, "Choose a location for the event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAttendeeCheckIns() {
        userController.getUser(deviceID, user -> {
            ArrayList<String> checkIns = user.getCheckInEvents();
            for(String eventID : checkIns) {
                eventController.getEvent(eventID, event -> {
                    HashMap<String, Location> checkInLocationPairs = event.getUserLocationPairs();
                    Location checkInLocation = checkInLocationPairs.get(deviceID);
                    Marker m = new Marker(mapView);
                    GeoPoint point = new GeoPoint(checkInLocation.getLatitude(),
                            checkInLocation.getLongitude());
                    m.setPosition(point);
                    m.setTitle(event.getName());
                    m.setSnippet(event.getDescription());

                    // TODO: add image

                    mapView.getOverlays().add(m);
                }, e -> Toast.makeText(ViewMapActivity.this,
                        "Unable to retrieve check-in location for event " + eventID,
                        Toast.LENGTH_SHORT).show());
            }
        }, e -> Toast.makeText(ViewMapActivity.this, "Unable to retrieve user data", Toast.LENGTH_SHORT).show());
    }

    private void showAllEvents() {
        eventController.getAllEvents(events -> {
            for(Event event : events) {
                Location eventLocation = event.getEventLocation();
                Marker m = new Marker(mapView);
                GeoPoint point = new GeoPoint(eventLocation.getLatitude(),
                        eventLocation.getLongitude());
                m.setPosition(point);
                m.setTitle(event.getName());
                m.setSnippet(eventLocation.getLatitude() + " <br> " + eventLocation.getLongitude());
                mapView.getOverlays().add(m);
            }
        }, e -> Toast.makeText(ViewMapActivity.this, "Unable to retrieve all events", Toast.LENGTH_SHORT).show());
    }

    private void showOrganizedEvents() {
        userController.getUser(deviceID, user -> {
            ArrayList<String> organizedEvents = user.getOrganizedEvents();
            for(String eventID : organizedEvents) {
                eventController.getEvent(eventID, event -> {
                    Location eventLocation = event.getEventLocation();
                    Marker m = new Marker(mapView);
                    GeoPoint p = new GeoPoint(eventLocation.getLatitude(), eventLocation.getLongitude());
                    m.setPosition(p);
                    m.setTitle(event.getID());
                    m.setSnippet(event.getDescription());
                    mapView.getOverlays().add(m);
                }, e -> {
                    Toast.makeText(ViewMapActivity.this,
                            "Unable to retrieve check-in location for event " + eventID,
                            Toast.LENGTH_SHORT).show();
                });
            }
        }, e -> {
            Toast.makeText(ViewMapActivity.this, "Unable to retrieve user data", Toast.LENGTH_SHORT).show();
        });
    }
}