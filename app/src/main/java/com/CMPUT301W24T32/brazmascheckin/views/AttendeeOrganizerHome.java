package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.CMPUT301W24T32.brazmascheckin.AttendeeViewEventFragment;
import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.EventArrayAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

//        // for testing purposes
//        String names = "beebeebooboo";
//        String description = "Seminar where you learn to beep";
//        Date date = new Date(11,02,2005);
//
//        // new added from event update
//        HashMap<String, Integer> checkins = null;
//        ArrayList<String> signups = null;
//        String ID = "42069";
//        int limit  = 1;
//        String posterID = "IMG";
//        String QRCodeID = "QR";
//        String shareQRCodeID = "shQr";
//        //how to do date -> not wokring in array class so idk ask -DATE
//
//        eventDataList = new ArrayList<>();
//
//        eventDataList.add(new Event(ID,names, date, description, checkins, signups, limit, posterID, QRCodeID, shareQRCodeID));  //NEED DATE IN MIDDLE

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
        //to access event details by clicking on indivdual item
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = eventDataList.get(position);
                AttendeeViewEventFragment fragment = AttendeeViewEventFragment.sendEvent(e);
                fragment.show(getSupportFragmentManager(), "Display Event");
            }
        });

        addButton.setOnClickListener(v -> {
            new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
        });
    }

    @Override
    public void addEvent(Event event) {
         eventsRef.add(new Event()).onSuccessTask(
                new SuccessContinuation<DocumentReference, Void>() {
                    @NonNull
                    @Override
                    public Task<Void> then(DocumentReference documentReference) throws Exception {
                        event.setID(documentReference.getId());
                        return eventsRef.document(documentReference.getId()).set(event);

                    }
                });
//        event.setID(doc.getId());
//        eventsRef.document(doc.getId()).set(event);
    }

}