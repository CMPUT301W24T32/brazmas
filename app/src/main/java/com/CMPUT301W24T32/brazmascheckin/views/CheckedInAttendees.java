package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.AttendeeRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Attendee;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.Organizer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class CheckedInAttendees extends AppCompatActivity implements
        AttendeeRecyclerViewAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private Button share;
    private Button map;
    private Button notify;
    private ArrayList<Attendee> attendeeDataList;

    private AttendeeRecyclerViewAdapter attendeeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_in_attendees);

        Intent intent = getIntent();
        Event e = (Event) intent.getSerializableExtra("EVENT");
        configureViews();
        configureControllers(e);
    }

    private void configureViews() {
        attendeeDataList = new ArrayList<>();
        recyclerView = findViewById(R.id.checked_in_attendees_attendees_rv);
        share = findViewById(R.id.checked_in_attendees_share_btn);
        map = findViewById(R.id.checked_in_attendees_map_btn);
        notify = findViewById(R.id.checked_in_attendees_notification_btn);
        attendeeRecyclerViewAdapter = new AttendeeRecyclerViewAdapter(this, attendeeDataList,
                this);
        recyclerView.setAdapter(attendeeRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configureControllers(Event e) {
        CollectionReference eventsRef = FirestoreDB.getEventsRef();
        DocumentReference eventDoc = eventsRef.document(e.getID());

        CollectionReference attendeesRef = FirestoreDB.getUsersRef();
        eventDoc.addSnapshotListener((value, error) -> {
            attendeeDataList.clear();
            Event dbEvent = value.toObject(Event.class);
            ArrayList<String> attendeeIDs = dbEvent.helperKeys();

            for(String id : attendeeIDs) {
                attendeesRef.document(id).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if(documentSnapshot != null) {
                                Attendee attendee = documentSnapshot.toObject(Organizer.class);
                                if(attendee != null) {
                                    attendeeDataList.add(attendee);
                                    attendeeRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            }
                        }).addOnFailureListener(e1 -> {

                        });
            }
        });
    }



    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }
}