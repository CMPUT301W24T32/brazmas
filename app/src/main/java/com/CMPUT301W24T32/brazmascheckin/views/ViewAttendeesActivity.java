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
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.User;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewAttendeesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AttendeeRecyclerViewAdapter recyclerViewAdapter;
    private Button shareButton;
    private Button mapButton;
    private Button notifyButton;
    private LinearLayout organizerActionsLinearLayout;

    private ArrayList<User> userDataList;
    private ArrayList<Integer> userCheckIns;
    private EventController eventController;
    private UserController userController;

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
        Event event = (Event) intent.getSerializableExtra(EXTRA_EVENT);

        mode = intent.getIntExtra(EXTRA_MODE, -1);
        configureViews();
        configureControllers(event);
    }

    private void configureViews() {
        userDataList = new ArrayList<>();
        shareButton = findViewById(R.id.view_attendees_share_btn);
        mapButton = findViewById(R.id.view_attendees_map_btn);
        notifyButton = findViewById(R.id.view_attendees_notify_btn);
        organizerActionsLinearLayout = findViewById(R.id.view_attendees_organizer_action_ll);
        recyclerView = findViewById(R.id.view_attendees_attendee_rv);

        if(mode == CHECK_IN_MODE) {
            userCheckIns = new ArrayList<>();
            recyclerViewAdapter = new AttendeeRecyclerViewAdapter(this, userDataList,
                    userCheckIns, CHECK_IN_MODE);

        } else if (mode == SIGN_UP_MODE) {
            organizerActionsLinearLayout.setVisibility(View.GONE);
            recyclerViewAdapter = new AttendeeRecyclerViewAdapter(this, userDataList,
                    SIGN_UP_MODE);
            mapButton.setVisibility(View.GONE);
            shareButton.setVisibility(View.GONE);
            notifyButton.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

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
                    attendeeIDs = event.helperKeys();
                    checkIns = dbEvent.getCheckIns();
                } else if (mode == SIGN_UP_MODE) {
                    attendeeIDs = event.getSignUps();
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

            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(ViewAttendeesActivity.this, "Unable to retrieve " +
                        "details for event " + event.getID(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}