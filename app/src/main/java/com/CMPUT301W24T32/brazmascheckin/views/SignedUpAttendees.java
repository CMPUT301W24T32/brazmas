package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.AttendeeSignedUpRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
// TODO: ADD BACK BUTTON AND MOVE NOTIFY

/**
 * Activity to display signed-up attendees
 */
public class SignedUpAttendees extends AppCompatActivity implements AddAnnouncementFragment.AddAnnouncementDialogListener {

    private RecyclerView recyclerView;
    private Button notify;
    private ArrayList<User> userDataList;
    private AttendeeSignedUpRecyclerViewAdapter attendeeSignedUpRecyclerViewAdapter;

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
        notify = findViewById(R.id.signed_up_attendees_notification_btn);
    }

    /**
     * This method configures the controllers of the activity based on an event
     * @param e an event
     */
    private void configureControllers(Event e) {
        CollectionReference eventsRef = FirestoreDB.getEventsRef();
        DocumentReference eventDoc = eventsRef.document(e.getID());

        CollectionReference attendeesRef = FirestoreDB.getUsersRef();
        eventDoc.addSnapshotListener((value, error) -> {
            userDataList.clear();
            attendeeSignedUpRecyclerViewAdapter.notifyDataSetChanged();
            assert value != null;
            Event dbEvent = value.toObject(Event.class);
            assert dbEvent != null;
            ArrayList<String> attendeeIDs = dbEvent.getSignUps();

            for(String id: attendeeIDs) {
                attendeesRef.document(id).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if(documentSnapshot != null) {
                                User user = documentSnapshot.toObject(User.class);
                                if(user != null) {
                                    userDataList.add(user);
//                                    attendeeSignedUpRecyclerViewAdapter.notifyDataSetChanged();
                                    attendeeSignedUpRecyclerViewAdapter.notifyItemInserted(userDataList.size() - 1);
                                }
                            }
                        });
            }
        });
        notify.setOnClickListener(v -> {
            new AddAnnouncementFragment().show(getSupportFragmentManager(), "Add Announcement");
        });
    }

    @Override
    public void addAnnouncement(Announcement announcement) {
        //here add the annoucnment to users firebase -> then access it and put it into annoucnement activity
    }
}