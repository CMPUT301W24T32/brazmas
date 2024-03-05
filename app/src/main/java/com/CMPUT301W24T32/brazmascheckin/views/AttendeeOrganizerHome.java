package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

//import com.CMPUT301W24T32.brazmascheckin.AttendeeViewEventFragment;
import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.EventRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.views.AttendeeViewEventFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;


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

    private CollectionReference eventsRef;

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

        /*String names = "beebeebooboo";
        String description = "Seminar where you learn to beep";
        Date date = new Date(11, 02, 2005);

        // new added from event update
        HashMap<String, Integer> checkins = null;
        ArrayList<String> signups = null;
        String ID = "42069";
        int limit = 1;
        String posterID = "IMG";
        String QRCodeID = "QR";
        String shareQRCodeID = "shQr";
        //how to do date -> not working in array class so idk ask -DATE

        //eventDataList = new ArrayList<>();
        eventDataList.add(new Event(ID, names, date, description, checkins, signups, limit, posterID, QRCodeID, shareQRCodeID));  //NEED DATE IN MIDDLE

        // for testing purposes
        String names1 = "EVENT NAME";
        String description1 = "EVENT DESCRIPTION";
        Date date1 = new Date(10, 02, 2050);

        // new added from event update
        HashMap<String, Integer> checkins1 = null;
        ArrayList<String> signups1 = null;
        String ID1 = "11111";
        int limit1 = 1;
        String posterID1 = "IMG";
        String QRCodeID1 = "QR";
        String shareQRCodeID1 = "shQr";
        //how to do date -> not working in array class so idk ask -DATE*/

        //eventDataList.add(new Event(ID1, names1, date1, description1, checkins1, signups1, limit1, posterID1, QRCodeID1, shareQRCodeID1));  //NEED DATE IN MIDDLE

        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(this, eventDataList);
        eventRecyclerView = findViewById(R.id.all_events_rv);
        eventRecyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addButton = findViewById(R.id.add_event_button);
        eventsRef = FirestoreDB.getEventsRef();
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
        eventsRef.add(event).addOnSuccessListener(documentReference -> {
           // set ID of the event after it has been added to database
           event.setID(documentReference.getId());
           // update document with entire event object
            eventsRef.document(documentReference.getId()).set(event);
        })
                .addOnFailureListener(e -> {
                    // for failure
                    Toast.makeText(this, "Failed to add event to database", Toast.LENGTH_LONG).show();
                });
    }
}