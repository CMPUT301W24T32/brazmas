package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.EventRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.views.AttendeeViewEventFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This class will be the home page for organizer.
 * This is where the admin will browse events.
 */
public class AdministratorHome extends AppCompatActivity {

    private ArrayList<Event> eventDataList;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;

    private RecyclerView eventRecyclerView;

    private CollectionReference eventsRef;

    /**
     * Called when activity is created.
     * @param savedInstanceState is the previous information if it creates
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        configureViews();
        configureControllers();
    }

    /**
     * This method initializes the views, adapters, and models required for this activity.
     */
    private void configureViews() {
        eventDataList = new ArrayList<>();
        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(this, eventDataList);
        eventRecyclerView = findViewById(R.id.all_events_rv_admin);
        eventRecyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        eventRecyclerViewAdapter.setOnItemClickListener(position -> {
            Event clickedEvent = eventDataList.get(position);
            AdministratorViewEventFragment fragment = AdministratorViewEventFragment.sendEvent(clickedEvent);
            fragment.show(getSupportFragmentManager(), "Display Event");
        });
    }
}