package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.CMPUT301W24T32.brazmascheckin.AttendeeViewEventFragment;
import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.EventRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AttendeeViewEventFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class will be the home page for attendee/organizer
 */

public class AttendeeOrganizerHome extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {
    private ArrayList<Event> eventDataList;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;

    private RecyclerView eventRecyclerView;
    private Button addButton;

    private Button allEventsButton;
    private Button attendingEventsButton;
    private Button organizingEventsButton;

    private CollectionReference eventsRef;
    private CollectionReference usersRef;
    private DocumentReference userDoc;
    private DocumentReference eventDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_organizer_home);

        configureViews();
        configureControllers();

    }

    /**
     * This method initializes the views, adapters, and models required for the activity.
     */
    private void configureViews() {
        eventDataList = new ArrayList<>();

        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(this, eventDataList);
        eventRecyclerView = findViewById(R.id.all_events_rv);
        eventRecyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        allEventsButton = findViewById(R.id.all_events_button);
        attendingEventsButton = findViewById(R.id.attending_button);
        organizingEventsButton = findViewById(R.id.organizing_button);
        addButton = findViewById(R.id.add_event_button);

        eventsRef = FirestoreDB.getEventsRef();
        usersRef = FirestoreDB.getUsersRef();
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        userDoc = usersRef.document(deviceID);
    }

    /**
     * This method defines the controllers for the views of the activity.
     */
    private void configureControllers() {

        eventsRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, "Unable to connect to the database", Toast.LENGTH_LONG).show();
            }
            if (value != null) {
                eventDataList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Event event = doc.toObject(Event.class);
                    eventDataList.add(event);
                }
                eventRecyclerViewAdapter.notifyDataSetChanged();
                Log.d("Firestore", "Number of events: " + eventDataList.size());
            }
        });

        // filters for all events
        allEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsRef.addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(AttendeeOrganizerHome.this, "Unable to connect to the database", Toast.LENGTH_LONG).show();
                    }
                    if (value != null) {
                        eventDataList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Event event = doc.toObject(Event.class);
                            eventDataList.add(event);
                        }
                        eventRecyclerViewAdapter.notifyDataSetChanged();
                        Log.d("Firestore", "Number of events: " + eventDataList.size());
                    }
                });
            }
        });

        // on click of the attending button it filters through the attending events list
        attendingEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userDoc.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            ArrayList<String> signedUp = user.getSignedUpEvents();
                            eventsRef.addSnapshotListener((value, error) -> {
                                if (error != null) {
                                    Toast.makeText(AttendeeOrganizerHome.this, "Unable to connect to the database", Toast.LENGTH_LONG).show();
                                }
                                if (value != null) {
                                    eventDataList.clear();
                                    for (QueryDocumentSnapshot doc : value) {
                                        Event event = doc.toObject(Event.class);
                                        if (signedUp.contains(event.getID())){
                                            eventDataList.add(event);
                                        }
                                    }
                                    eventRecyclerViewAdapter.notifyDataSetChanged();
                                    Log.d("Firestore", "Number of events: " + eventDataList.size());
                                }
                            });

                        }
                    }
                });
            }
        });

        // on click of the organizer button it filters through the organized events list
        organizingEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userDoc.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            ArrayList<String> organizedEvents = user.getOrganizedEvents();

                            eventsRef.addSnapshotListener((value, error) -> {
                                if (error != null) {
                                    Toast.makeText(AttendeeOrganizerHome.this, "Unable to connect to the database", Toast.LENGTH_LONG).show();
                                }
                                if (value != null) {
                                    eventDataList.clear();
                                    for (QueryDocumentSnapshot doc : value) {
                                        Event event = doc.toObject(Event.class);
                                        if (organizedEvents.contains(event.getID())){
                                            eventDataList.add(event);
                                        }
                                    }
                                    eventRecyclerViewAdapter.notifyDataSetChanged();
                                    Log.d("Firestore", "Number of events: " + eventDataList.size());
                                }
                            });

                        }
                    }
                });
            }
        });



        // to access event details by clicking single event
        eventRecyclerViewAdapter.setOnItemClickListener(new EventRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Event clickedEvent = eventDataList.get(position);
                AttendeeViewEventFragment fragment = AttendeeViewEventFragment.sendEvent(clickedEvent);
                fragment.show(getSupportFragmentManager(), "Display Event");
            }
        });

        addButton.setOnClickListener(v -> {
            new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
        });


    }

    /**
     * This method adds an event to the database.
     *
     * @param event
     */
    @Override
    public void addEvent(Event event) {
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);;
        event.setOrganizer(deviceID);
        eventsRef.add(event)
                .addOnSuccessListener(documentReference -> {
           // set ID of the event after it has been added to database
           event.setID(documentReference.getId());
           // update document with entire event object
            eventsRef.document(documentReference.getId()).set(event);

            usersRef.document(deviceID).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
//                        ArrayList<String> organizedEvents = user.getOrganizedEvents();
//                        organizedEvents.add(documentReference.getId());
                        user.createEvent(documentReference.getId());
                        usersRef.document(deviceID).set(user);
                    });
                }).addOnFailureListener(e -> {
                    // for failure
                    Toast.makeText(this, "Failed to add event to database", Toast.LENGTH_LONG).show();
                });



    }
}