package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.EventArrayAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AttendeeOrganizerHome extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {

    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private EventArrayAdapter eventAdapter;

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
        eventAdapter = new EventArrayAdapter(this, eventDataList);
        eventList = findViewById(R.id.allEventList);
        eventList.setAdapter(eventAdapter);
        addButton = findViewById(R.id.addButton);
        eventsRef = FirestoreDB.getEventsRef();
    }

    /**
     * This method defines the controllers for the views of the activity.
     */
    private void configureControllers() {
        eventsRef.addSnapshotListener((value, error) -> {
            if(error != null) {
                Toast.makeText(this, "Unable to connect " +
                        "to the database", Toast.LENGTH_LONG).show();
            }
            if(value != null) {
                eventDataList.clear();
                for(QueryDocumentSnapshot doc: value) {
                    Event event = doc.toObject(Event.class);
                    eventDataList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            }
        });

        //to access event details by clicking on individual item
        eventList.setOnItemClickListener((parent, view, position, id) -> {
            Event e = eventDataList.get(position);
            AttendeeViewEventFragment fragment = AttendeeViewEventFragment.sendEvent(e);
            fragment.show(getSupportFragmentManager(), "Display Event");
        });

        addButton.setOnClickListener(v -> {
            new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
        });
    }

    /**
     * This method adds an event to the database.
     * @param event
     */
    @Override
    public void addEvent(Event event) {
         eventsRef.add(new Event()).onSuccessTask(
                 documentReference -> {
                     event.setID(documentReference.getId());
                     return eventsRef.document(documentReference.getId()).set(event);

                 });
    }

}