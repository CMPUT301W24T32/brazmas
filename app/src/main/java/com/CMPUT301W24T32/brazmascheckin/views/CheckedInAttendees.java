package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.AttendeeCheckedInRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity to display checked-in attendees
 */
public class CheckedInAttendees extends AppCompatActivity implements
        AttendeeCheckedInRecyclerViewAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private Button share;
    private Button map;
    //private Button notify;
    private ArrayList<User> userDataList;
    private ArrayList<Integer> userCheckIns;

    private AttendeeCheckedInRecyclerViewAdapter attendeeCheckedInRecyclerViewAdapter;

    /**
     * Called when the activity is created
     * @param savedInstanceState previous information (if it exists)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_in_attendees);

        Intent intent = getIntent();
        Event e = (Event) intent.getSerializableExtra("EVENT");
        configureViews();
        configureControllers(e);

       //notify.setOnClickListener(v -> {
           // new AddAnnouncementFragment().show(getSupportFragmentManager(), "Add Announcement");
       // });
    }

    /**
     * This method configures the views of the activity
     */
    private void configureViews() {
        userDataList = new ArrayList<>();
        userCheckIns = new ArrayList<>();
        recyclerView = findViewById(R.id.checked_in_attendees_attendees_rv);
        share = findViewById(R.id.checked_in_attendees_share_btn);
        map = findViewById(R.id.checked_in_attendees_map_btn);
        //notify = findViewById(R.id.checked_in_attendees_notification_btn);
        attendeeCheckedInRecyclerViewAdapter = new AttendeeCheckedInRecyclerViewAdapter(this, userDataList,
                userCheckIns,
                this);
        recyclerView.setAdapter(attendeeCheckedInRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This method configures the controllers of the activity base don an event
     * @param e
     */
    private void configureControllers(Event e) {
        CollectionReference eventsRef = FirestoreDB.getEventsRef();
        DocumentReference eventDoc = eventsRef.document(e.getID());
        //notify.setOnClickListener(v -> {
                    //new AddAnnouncementFragment().show(getSupportFragmentManager(), "Add Announcement");
                   // });


        CollectionReference attendeesRef = FirestoreDB.getUsersRef();
        eventDoc.addSnapshotListener((value, error) -> {
            userDataList.clear();
            userCheckIns.clear();
            attendeeCheckedInRecyclerViewAdapter.notifyDataSetChanged();
            Event dbEvent = value.toObject(Event.class);
            ArrayList<String> attendeeIDs = dbEvent.helperKeys();
            HashMap<String, Integer> checkIns = dbEvent.getCheckIns();

            for(String id : attendeeIDs) {
                attendeesRef.document(id).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if(documentSnapshot != null) {
                                User user = documentSnapshot.toObject(User.class);
                                if(user != null) {
                                    userDataList.add(user);
                                    userCheckIns.add(checkIns.get(id));
                                    attendeeCheckedInRecyclerViewAdapter.notifyItemInserted(userDataList.size() - 1);
                                }
                            }
                        }).addOnFailureListener(e1 -> {

                        });
            }
        });

        //notify.setOnClickListener(v -> {
            //new AddAnnouncementFragment().show(getSupportFragmentManager(), "Add Announcement");
        //});
    }


    /**
     * A listener for the click action on an item of the recycler view
     * @param position position of the clicked item
     */
    @Override
    public void onItemClick(int position) {

    }

    /**
     * A listener for the long click action of an item of the recycler view
     * @param position position of the clicked item
     */
    @Override
    public void onItemLongClick(int position) {

    }
}