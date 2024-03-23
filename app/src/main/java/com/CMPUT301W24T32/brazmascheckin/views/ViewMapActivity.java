package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
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

import java.util.HashMap;

public class ViewMapActivity extends AppCompatActivity {
    private UserController userController;
    private MapView mapView;
    private Button chooseLocationBtn;
    private Context ctx;
    public static final String EXTRA_LOCATION_PAIRS = "user_location_pair";
    public static final String EXTRA_EVENT = "event";
    public static final String EXTRA_MODE = "mode";
    public static final int VIEW_ATTENDEES = 0;
    public static final int CHOOSE_LOCATION = 1;
    public static final String RESULT_LOCATION = "result_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // default configuration
        configureActivity();

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
                chooseLocationBtn.setVisibility(View.VISIBLE);
                chooseLocation();
                break;
        }

        mapView.invalidate();
    }

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
    }

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

    private void chooseLocation() {
        Marker marker = new Marker(mapView);
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
}