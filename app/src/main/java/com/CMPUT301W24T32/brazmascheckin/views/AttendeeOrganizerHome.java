package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.EventRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * This class will be the home page for attendee/organizer
 */

public class AttendeeOrganizerHome extends AppCompatActivity {
    private ArrayList<Event> eventDataList;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;

    private RecyclerView eventRecyclerView;
    private Button addButton;

    private Button allEventsButton;
    private Button attendingEventsButton;
    private Button organizingEventsButton;

    private EventController eventController;
    private UserController userController;
    private ImageController imageController;
    private String deviceID;



    /**
     * This method initializes the attendee/organizer home activity.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_organizer_home);
        configureViews();
        configureControllers();


        // Allows the app to switch between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.user_home_bnv);
        bottomNavigationView.setSelectedItemId(R.id.bottom_event);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             * This method determines something from navigation bar has been selected or not.
             * @param menuItem The selected item
             * @return True if selected, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == (R.id.bottom_announcement)){
                    startActivity(new Intent(getApplicationContext(), AnnouncementActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_camera)){
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_profile)){
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_event)){
                    return true;
                }
                return false;

            }
        });



    }

    /**
     * This method initializes the views, adapters, and models required for the activity.
     */
    private void configureViews() {
        eventDataList = new ArrayList<>();

        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(this, eventDataList);
        eventRecyclerView = findViewById(R.id.user_home_all_events_rv);
        eventRecyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addButton = findViewById(R.id.user_home_add_event_btn);
        addButton.setVisibility(View.INVISIBLE);
        allEventsButton = findViewById(R.id.user_home_all_btn);
        attendingEventsButton = findViewById(R.id.user_home_attending_btn);
        organizingEventsButton = findViewById(R.id.user_home_organizing_btn);
        addButton = findViewById(R.id.user_home_add_event_btn);

        deviceID = DeviceID.getDeviceID(this);
    }

    /**
     * This method defines the controllers for the views of the activity.
     */
    private void configureControllers() {
        eventController = new EventController(this);
        userController = new UserController(this);
        imageController = new ImageController(this);

        showAllEvents();
        // filters for all events
        allEventsButton.setOnClickListener(view -> {
            showAllEvents();
        });

        handleAttendeeMode();
        handleOrganizerMode();

        // to access event details by clicking single event
        eventRecyclerViewAdapter.setOnItemClickListener(position -> {
            Event clickedEvent = eventDataList.get(position);
            AttendeeViewEventFragment fragment = AttendeeViewEventFragment.sendEvent(clickedEvent);
            fragment.show(getSupportFragmentManager(), "Display Event");
        });

        addButton.setOnClickListener(v -> {
            startActivity(new Intent(AttendeeOrganizerHome.this, AddEvent.class));
        });
    }

    private void showAllEvents() {
        eventController.addSnapshotListener(new SnapshotListener<Event>() {
            @Override
            public void snapshotListenerCallback(ArrayList<Event> events) {
                addButton.setVisibility(View.INVISIBLE);
                eventDataList.clear();
                for (Event event : events) {
                    eventDataList.add(event);
                }
                eventRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(AttendeeOrganizerHome.this, "Unable to connect to the " +
                        "database", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void handleAttendeeMode() {
        attendingEventsButton.setOnClickListener(view -> {

            addButton.setVisibility(View.INVISIBLE);

            userController.getUser(deviceID, user -> {
                ArrayList<String> signedUp = user.getSignedUpEvents();
                eventController.addSnapshotListener(new SnapshotListener<Event>() {
                    @Override
                    public void snapshotListenerCallback(ArrayList<Event> events) {
                        eventDataList.clear();
                        for(Event event: events) {
                            if(signedUp.contains(event.getID())) {
                                eventDataList.add(event);
                            }
                        }
                        eventRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AttendeeOrganizerHome.this, "Unable to connect to the " +
                                "database", Toast.LENGTH_LONG).show();
                    }
                });
            }, e -> Toast.makeText(AttendeeOrganizerHome.this, "Unable to connect to the " +
                    "database", Toast.LENGTH_LONG).show());

        });
    }

    public void handleOrganizerMode() {
        organizingEventsButton.setOnClickListener(view -> {
            addButton.setVisibility(View.VISIBLE);
            userController.getUser(deviceID, user -> {
                ArrayList<String> organizedEvents = user.getOrganizedEvents();
                eventController.addSnapshotListener(new SnapshotListener<Event>() {
                    @Override
                    public void snapshotListenerCallback(ArrayList<Event> events) {
                        eventDataList.clear();
                        for(Event event : events) {
                            if(organizedEvents.contains(event.getID())) {
                                eventDataList.add(event);
                            }
                        }
                        eventRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AttendeeOrganizerHome.this, "Unable to connect to the " +
                                "database", Toast.LENGTH_LONG).show();
                    }
                });
            }, e -> Toast.makeText(AttendeeOrganizerHome.this, "Unable to connect to the " +
                    "database", Toast.LENGTH_LONG).show());
        });

    }
}
