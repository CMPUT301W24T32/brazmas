package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserGetListener;
import com.CMPUT301W24T32.brazmascheckin.helper.AttendeeSignedUpRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity to display signed-up attendees
 */
public class SignedUpAttendees extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> userDataList;
    private AttendeeSignedUpRecyclerViewAdapter attendeeSignedUpRecyclerViewAdapter;
    private EventController eventController;
    private UserController userController;

    /**
     * Called when the activity is created
     * @param savedInstanceState previous information (if it exists)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_up_attendees);

        Intent intent = getIntent();
        Event e = (Event) intent.getSerializableExtra("EVENT");
        configureViews();
        configureControllers(e);
    }

    /**
     * This method configures the views of the activity
     */
    private void configureViews() {
        userDataList = new ArrayList<>();
        recyclerView = findViewById(R.id.signed_up_attendees_attendees_rv);
        attendeeSignedUpRecyclerViewAdapter = new AttendeeSignedUpRecyclerViewAdapter(
                this, userDataList
        );
        recyclerView.setAdapter(attendeeSignedUpRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This method configures the controllers of the activity based on an event
     * @param e an event
     */
    private void configureControllers(Event e) {
        eventController = new EventController(this);
        userController = new UserController(this);
        eventController.addSingleSnapshotListener(e.getID(), new SnapshotListener<Event>() {
            @Override
            public void snapshotListenerCallback(ArrayList<Event> events) {
                userDataList.clear();
                attendeeSignedUpRecyclerViewAdapter.notifyDataSetChanged();

                Event event = events.get(0);
                ArrayList<String> attendeeIDs = event.getSignUps();


                for(String id: attendeeIDs) {
                    userController.getUser(id, new UserGetListener() {
                        @Override
                        public void onUserGetSuccess(User user) {
                            userDataList.add(user);
                            attendeeSignedUpRecyclerViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onUserGetFailure(Exception e) {

                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}