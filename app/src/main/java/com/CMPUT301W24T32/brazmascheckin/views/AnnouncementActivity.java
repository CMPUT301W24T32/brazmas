package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.AnnouncementRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Announcement activity displays announcements related to events.
 * Users can navigate to other activities using the bottom navigation bar.
 */

public class AnnouncementActivity extends AppCompatActivity {

    /**
     * variables
     */
    private ArrayList<Announcement> announcementDataList;
    private RecyclerView recyclerView;
    private AnnouncementRecyclerViewAdapter adapter;
    /**
     *This method initializes the Announcement activity.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */

    private EventController eventController;
    private UserController userController;

    /**
     * Initializes the Announcement activity.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState.
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        // setting recycler view
        recyclerView = findViewById(R.id.announcement_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        configureViews();
        configureControllers();

        // Allows the app to switch between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.announcement_bnv);
        bottomNavigationView.setSelectedItemId(R.id.bottom_announcement);
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
                    startActivity(new Intent(getApplicationContext(), UserHome.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;

            }
        });

    }

    /**
     * Configures the views, including the RecyclerView.
     */
    public void configureViews(){
        recyclerView = findViewById(R.id.announcement_rv);
        announcementDataList = new ArrayList<>();
        adapter = new AnnouncementRecyclerViewAdapter(this, announcementDataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * configures the controllers
     */
    public void configureControllers(){
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        String deviceID = DeviceID.getDeviceID(this);
        // change these from null
        userController.getUser(deviceID, user -> {
            user.setLastAnnouncementCheck(System.currentTimeMillis());
            userController.setUser(user, null ,null);

            ArrayList<String> signedUp = user.getSignedUpEvents();
            ArrayList<String> checkIns = user.getCheckInEvents();
            eventController.addSnapshotListener(new SnapshotListener<Event>() {
                @Override
                public void snapshotListenerCallback(ArrayList<Event> events) {
                    announcementDataList.clear();
                    for(Event event: events) {
                        if(signedUp.contains(event.getID()) || checkIns.contains(event.getID())){
                            //Toast.makeText(getBaseContext(), "Unable to connect to the database", Toast.LENGTH_LONG).show();
                            ArrayList<Announcement> announcements = event.getAnnouncements();
                            if (announcements != null) {
                                announcementDataList.addAll(announcements);
                            }
                        }
                    }
                    Collections.sort(announcementDataList, new Comparator<Announcement>() {
                        @Override
                        public int compare(Announcement a, Announcement b) {
                            if (a.getTimeCreated() > b.getTimeCreated()){
                                return -1;
                            }
                            else{
                                return 1;
                            }
                        }
                    });
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Exception e) {
                    //do soon**********8
                }
            });

        }, null);

    }

}