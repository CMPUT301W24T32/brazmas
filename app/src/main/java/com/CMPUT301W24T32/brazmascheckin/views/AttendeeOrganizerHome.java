package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

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

        eventDataList = new ArrayList<>();
        eventList = findViewById(R.id.allEventList);
        eventAdapter = new EventArrayAdapter(this,eventDataList);

        eventsRef = FirestoreDB.getEventsRef();
        eventsRef.addSnapshotListener((value, error) -> {
            if(error != null) {

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
        eventList.setAdapter(eventAdapter);
        addButton = findViewById(R.id.addButton);
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

    @Override
    public void addEvent(Event event) {
         eventsRef.add(new Event()).onSuccessTask(
                 documentReference -> {
                     event.setID(documentReference.getId());
                     return eventsRef.document(documentReference.getId()).set(event);

                 });

    }

}