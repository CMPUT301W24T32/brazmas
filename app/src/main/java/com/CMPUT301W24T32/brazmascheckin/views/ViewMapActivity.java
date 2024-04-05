package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;

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
    private Geocoder geocoder;
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
     * Configures the views and controllers of the activity.
     */
    private void configureActivity() {
        mapView = (MapView) findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setVerticalMapRepetitionEnabled(false);
        mapView.setScrollableAreaLimitLatitude(MapView.getTileSystem().getMaxLatitude(),
                MapView.getTileSystem().getMinLatitude(), 0);
        mapView.setMinZoomLevel(3.0);
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        chooseLocationBtn = findViewById(R.id.view_map_done_btn);
        IMapController mapController = mapView.getController();
        mapController.setZoom(3);

        deviceID = DeviceID.getDeviceID(this);
    }

    private void configureControllers() {
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        imageController = new ImageController(FirestoreDB.getStorageInstance());
        geocoder = new Geocoder(this);
    }

    /**
     * Method to change the state of the map to display customized markers to indicate check-in
     * locations of attendees for specified event.
     * @param event The event for which markers are to be displayed.
     * @param locations A HashMap containing the locations of the checked-in attendees.
     */
    private void displayCheckedInAttendees(Event event, HashMap<String, Location> locations) {
        // event location
        Location location = event.getEventLocation();
        Marker eventMarker = new Marker(mapView);
        GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());
        eventMarker.setPosition(point);

        if(event.getPoster() != null && !event.getPoster().isEmpty()) {
            imageController.getImage(ImageController.EVENT_POSTER, event.getPoster(), byteArray -> {
                Bitmap rawBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                Bitmap bitmap = Bitmap.createScaledBitmap(rawBitmap, 100, 100, false);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
                        bitmap);
                eventMarker.setIcon(bitmapDrawable);
            }, e -> {
                Toast.makeText(this, "Unable to retrieve event poster for " + event.getName(), Toast.LENGTH_SHORT).show();
            });
        }
        eventMarker.setTitle(event.getName());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            eventMarker.setSnippet(event.getDescription() + "<br>" + addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            eventMarker.setSnippet(event.getDescription());
        }
        mapView.getOverlays().add(eventMarker);

        for(String id : locations.keySet()) {
            Location attendeeLocation = locations.get(id);
            Marker attendeeMarker = new Marker(mapView);
            GeoPoint attendeePoint = new GeoPoint(attendeeLocation.getLatitude(), attendeeLocation.getLongitude());
            userController.getUser(id, user -> {
                attendeeMarker.setPosition(attendeePoint);
                String name = user.getFirstName() + " " + user.getLastName();
                attendeeMarker.setTitle(name);
                String profilePicture = null;
                String folder;


                if(user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                    profilePicture = user.getProfilePicture();
                    folder = ImageController.PROFILE_PICTURE;
                } else if (user.getDefaultProfilePicture() != null && !user.getDefaultProfilePicture().isEmpty()) {
                    profilePicture = user.getDefaultProfilePicture();
                    folder = ImageController.DEFAULT_PROFILE_PICTURE_PATH;
                } else {
                    folder = ImageController.DEFAULT_PROFILE_PICTURE_PATH;
                }


                imageController.getImage(folder, profilePicture,
                        byteArray -> {
                            Bitmap rawBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            Bitmap bitmap = Bitmap.createScaledBitmap(rawBitmap, 100, 100, false);
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
                                    bitmap);
                            attendeeMarker.setIcon(bitmapDrawable);
                        },
                        e -> Toast.makeText(this, "Unable to retrieve profile picture", Toast.LENGTH_SHORT).show());


                String snippet;
                try {
                    List<Address> addresses = geocoder.getFromLocation(attendeeLocation.getLatitude(),
                            attendeeLocation.getLongitude(), 1);
                    snippet = "User ID: " + id + "<br>" + addresses.get(0).getAddressLine(0);
                } catch (Exception e) {
                    snippet = "User ID: " + id + "<br>Latitude: " + attendeeLocation.getLatitude() + "<br>Longitude: " + attendeeLocation.getLongitude();
                }
                attendeeMarker.setSnippet(snippet);
                mapView.getOverlays().add(attendeeMarker);
            }, e -> Toast.makeText(ctx, "Unable to display user " + id, Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Method to change the state of the activity to allow an organizer to select a location
     * for their event by dropping a marker.
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
                String location;
                try {
                    List<Address> addresses = geocoder.getFromLocation(p.getLatitude(),
                            p.getLongitude(), 1);
                    location = addresses.get(0).getAddressLine(0);
                } catch (Exception e) {
                     location = "Latitude: " + p.getLatitude() + "<br> Longitude: " + p.getLongitude();
                }
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

    /**
     * Method to change the state of the activity to display all the user's recorded
     * check-in locations for events they have checked into, with customized markers.
     */
    private void showAttendeeCheckIns() {
        userController.getUser(deviceID, user -> {
            ArrayList<String> checkIns = user.getCheckInEvents();
            for(String eventID : checkIns) {
                eventController.getEvent(eventID, event -> {
                    HashMap<String, Location> checkInLocationPairs = event.getUserLocationPairs();
                    if (checkInLocationPairs.containsKey(deviceID)){
                        Location checkInLocation = checkInLocationPairs.get(deviceID);
                        Marker marker = new Marker(mapView);
                        GeoPoint point = new GeoPoint(checkInLocation.getLatitude(),
                                checkInLocation.getLongitude());
                        marker.setPosition(point);
                        marker.setTitle(event.getName());

                        String snippet;
                        try {
                            List<Address> addresses = geocoder.getFromLocation(checkInLocation.getLatitude(),
                                    checkInLocation.getLongitude(), 1);
                            snippet = event.getDescription() + "<br>" + addresses.get(0).getAddressLine(0);
                        } catch (Exception e) {
                            snippet = event.getDescription() + " <br> Latitude: " + checkInLocation.getLatitude()
                                + "<br> Longitude: " + checkInLocation.getLongitude();
                        }

                        marker.setSnippet(snippet);

                        String folder = ImageController.DEFAULT_EVENT_POSTER;
                        if(!event.getPoster().equals("defaultPoster.png")) {
                            folder = ImageController.EVENT_POSTER;
                        }

                        imageController.getImage(folder, event.getPoster(),
                                byteArray -> {
                                    Bitmap rawBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                    Bitmap bitmap = Bitmap.createScaledBitmap(rawBitmap, 100, 100, false);
                                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
                                            bitmap);
                                    marker.setIcon(bitmapDrawable);
                                }, e -> Toast.makeText(ViewMapActivity.this,
                                        "Unable to retrieve image for " + event.getName(), Toast.LENGTH_SHORT).show());

                    mapView.getOverlays().add(marker);
                }
                }, e -> Toast.makeText(ViewMapActivity.this,
                        "Unable to retrieve check-in location for event " + eventID,
                        Toast.LENGTH_SHORT).show());
            }
        }, e -> Toast.makeText(ViewMapActivity.this, "Unable to retrieve user data", Toast.LENGTH_SHORT).show());
    }

    /**
     * Method to change the state of the activity to display all the events with recorded locations,
     * with customized markers.
     */
    private void showAllEvents() {
        eventController.getAllEvents(events -> {
            for(Event event : events) {
                Location eventLocation = event.getEventLocation();
                Marker marker = new Marker(mapView);
                GeoPoint point = new GeoPoint(eventLocation.getLatitude(),
                        eventLocation.getLongitude());
                marker.setPosition(point);
                marker.setTitle(event.getName());
                String snippet;
                try {
                    List<Address> addresses = geocoder.getFromLocation(eventLocation.getLatitude(),
                            eventLocation.getLongitude(), 1);
                    snippet = event.getDescription() + "<br>" + addresses.get(0).getAddressLine(0);
                } catch (Exception e) {
                    snippet = event.getDescription() + " <br> Latitude: " + eventLocation.getLatitude()
                            + "<br> Longitude: " + eventLocation.getLongitude();
                }

                marker.setSnippet(snippet);
                String folder = ImageController.DEFAULT_EVENT_POSTER;
                if(!event.getPoster().equals("defaultPoster.png")) {
                    folder = ImageController.EVENT_POSTER;
                }

                imageController.getImage(folder, event.getPoster(),
                        byteArray -> {
                            Bitmap rawBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            Bitmap bitmap = Bitmap.createScaledBitmap(rawBitmap, 100, 100, false);
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
                                    bitmap);
                            marker.setIcon(bitmapDrawable);
                        }, e -> Toast.makeText(ViewMapActivity.this,
                                "Unable to retrieve image for " + event.getName(), Toast.LENGTH_SHORT).show());

                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                mapView.getOverlays().add(marker);
            }
        }, e -> Toast.makeText(ViewMapActivity.this, "Unable to retrieve all events", Toast.LENGTH_SHORT).show());
    }

    /**
     * Method to change the state of the activity to display all the user's organized events
     * that have recorded locations, with customized markers.
     */
    private void showOrganizedEvents() {
        userController.getUser(deviceID, user -> {
            ArrayList<String> organizedEvents = user.getOrganizedEvents();
            for(String eventID : organizedEvents) {
                eventController.getEvent(eventID, event -> {
                    if(event.getGeoLocationEnabled()) {
                        Location eventLocation = event.getEventLocation();
                        Marker marker = new Marker(mapView);
                        GeoPoint point = new GeoPoint(eventLocation.getLatitude(), eventLocation.getLongitude());
                        marker.setPosition(point);
                        marker.setTitle(event.getName());

                        String snippet;
                        try {
                            List<Address> addresses = geocoder.getFromLocation(eventLocation.getLatitude(),
                                    eventLocation.getLongitude(), 1);
                            snippet = event.getDescription() + "<br>" + addresses.get(0).getAddressLine(0);
                        } catch (Exception e) {
                            snippet = event.getDescription() + " <br> Latitude: " + eventLocation.getLatitude()
                                    + "<br> Longitude: " + eventLocation.getLongitude();
                        }
                        marker.setSnippet(snippet);

                        String folder = ImageController.DEFAULT_EVENT_POSTER;
                        if(!event.getPoster().equals("defaultPoster.png")) {
                            folder = ImageController.EVENT_POSTER;
                        }

                        imageController.getImage(folder, event.getPoster(),
                                byteArray -> {
                                    Bitmap rawBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                    Bitmap bitmap = Bitmap.createScaledBitmap(rawBitmap, 100, 100, false);
                                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
                                            bitmap);
                                    marker.setIcon(bitmapDrawable);
                                }, e -> Toast.makeText(ViewMapActivity.this,
                                        "Unable to retrieve image for " + event.getName(), Toast.LENGTH_SHORT).show());

                        mapView.getOverlays().add(marker);
                    }
                }, e -> Toast.makeText(ViewMapActivity.this,
                        "Unable to retrieve check-in location for event " + eventID,
                        Toast.LENGTH_SHORT).show());
            }
        }, e -> Toast.makeText(ViewMapActivity.this, "Unable to retrieve user data", Toast.LENGTH_SHORT).show());
    }
}