package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.AttendeeRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.User;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewAttendeesActivity extends AppCompatActivity implements AddAnnouncementFragment.AddAnnouncementDialogListener{
    private RecyclerView recyclerView;
    private AttendeeRecyclerViewAdapter recyclerViewAdapter;
    private Button notifyButton;
    private LinearLayout organizerActionsLinearLayout;

    private ArrayList<User> userDataList;
    private ArrayList<Integer> userCheckIns;
    private EventController eventController;
    private UserController userController;
    private Event event;
    public static final String EXTRA_EVENT = "extra_event";
    public static final String EXTRA_MODE = "extra_mode";
    public static final int CHECK_IN_MODE = 0;
    public static final int SIGN_UP_MODE = 1;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendees);

        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra(EXTRA_EVENT);

        mode = intent.getIntExtra(EXTRA_MODE, -1);
        configureViews();
        configureControllers(event);
    }

    /**
     * Assigns the views and adapters required for the activity; sets the visibility based on state.
     */
    private void configureViews() {
        userDataList = new ArrayList<>();
        notifyButton = findViewById(R.id.view_attendees_notify_btn);
        recyclerView = findViewById(R.id.view_attendees_attendee_rv);

        if(mode == CHECK_IN_MODE) {
            userCheckIns = new ArrayList<>();
            recyclerViewAdapter = new AttendeeRecyclerViewAdapter(this, userDataList,
                    userCheckIns, CHECK_IN_MODE);

        } else if (mode == SIGN_UP_MODE) {
            //organizerActionsLinearLayout.setVisibility(View.GONE);
            recyclerViewAdapter = new AttendeeRecyclerViewAdapter(this, userDataList,
                    SIGN_UP_MODE);
            Toast.makeText(ViewAttendeesActivity.this, "test", Toast.LENGTH_SHORT).show();
            notifyButton.setVisibility(View.VISIBLE); //bc we need it for organizer -> notify for signed attendees not checked in
            recyclerViewAdapter = new AttendeeRecyclerViewAdapter(this, userDataList,
                    SIGN_UP_MODE);
            notifyButton.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Configures the controllers required by the activity.
     * @param event the event for which attendees are being viewed
     */
    private void configureControllers(Event event) {
        eventController = new EventController(this);
        userController = new UserController(this);

        eventController.addSingleSnapshotListener(event.getID(), new SnapshotListener<Event>() {
            @Override
            public void snapshotListenerCallback(ArrayList<Event> events) {
                Event dbEvent = events.get(0);
                ArrayList<String> attendeeIDs = null;
                HashMap<String, Integer> checkIns = null;
                if(mode == CHECK_IN_MODE) {
                    userCheckIns.clear();
                    attendeeIDs = dbEvent.helperKeys();
                    checkIns = dbEvent.getCheckIns();
                } else if (mode == SIGN_UP_MODE) {
                    attendeeIDs = dbEvent.getSignUps();
                }
                userDataList.clear();
                recyclerViewAdapter.notifyDataSetChanged();


                for(String id: attendeeIDs) {
                    HashMap<String, Integer> finalCheckIns = checkIns;
                    userController.getUser(id, user -> {
                        if(mode == CHECK_IN_MODE) {
                            userCheckIns.add(finalCheckIns.get(id));
                        }
                        userDataList.add(user);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }, e -> {
                        Toast.makeText(ViewAttendeesActivity.this, "Unable to retrieve user " +
                                id, Toast.LENGTH_SHORT).show();
                    });
                }
                notifyButton.setOnClickListener(v -> {
                    new AddAnnouncementFragment().show(getSupportFragmentManager(), "Add Announcement");
                });
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(ViewAttendeesActivity.this, "Unable to retrieve " +
                        "details for event " + event.getID(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addAnnouncement(Announcement announcement) {
        eventController = new EventController(this);
        event.getAnnouncements().add(announcement);
        eventController.setEvent(event,null,null);


    }

    private void populateRecyclerView(Event event) {

    }

}