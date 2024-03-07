package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

//import com.CMPUT301W24T32.brazmascheckin.AttendeeViewEventFragment;
import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.EventRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.helper.QRCodeGenerator;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AttendeeViewEventFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import java.io.ByteArrayOutputStream;
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
    private CollectionReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_organizer_home);
        configureViews();
        configureControllers();


        // Allows the app to switch between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.user_home_bnv);
        bottomNavigationView.setSelectedItemId(R.id.bottom_event);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == (R.id.bottom_announcement)){
                    startActivity(new Intent(getApplicationContext(), AnnouncementActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_camera)){
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_profile)){
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_event)){
                    return true;
                }
                return false;

            }
        });



    }

    /**
     * This method initializes the views, adapters, and models required for the activity.
     */
    private void configureViews() {
        eventDataList = new ArrayList<>();
        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(this, eventDataList);
        eventRecyclerView = findViewById(R.id.user_home_all_events_rv);
        eventRecyclerView.setAdapter(eventRecyclerViewAdapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addButton = findViewById(R.id.user_home_add_event_btn);
        eventsRef = FirestoreDB.getEventsRef();
        usersRef = FirestoreDB.getUsersRef();
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
            AttendeeViewEventFragment fragment = AttendeeViewEventFragment.sendEvent(clickedEvent);
            fragment.show(getSupportFragmentManager(), "Display Event");
        });

        addButton.setOnClickListener(v -> {
            new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
        });
    }

    /**
     * This method adds an event to the database along with a generated QR code.
     * @param event
     */
    @Override
    public void addEvent(Event event) {
        String deviceID = DeviceID.getDeviceID(this);
        event.setOrganizer(deviceID);
        eventsRef.add(event)
                .addOnSuccessListener(documentReference -> {
           // set ID of the event after it has been added to database
           event.setID(documentReference.getId());
           Bitmap bitmap = QRCodeGenerator.generateQRCode(documentReference.getId());

           // uploading the QR code and linking it to the event

           ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
           byte[] imageData = outputStream.toByteArray();

           StorageReference storageRef = FirestoreDB.getStorageReference("QRCodes");
           StorageReference imageRef = storageRef.child(event.getID() + "-QRCODE");

           UploadTask uploadTask = imageRef.putBytes(imageData);
           uploadTask.addOnSuccessListener(taskSnapshot ->
                   imageRef.getDownloadUrl()
                           .addOnSuccessListener(uri -> {
                               String QRCodeURI = uri.toString();
                               event.setQRCode(QRCodeURI);
                           }));

           uploadTask.addOnFailureListener(e -> Toast.makeText(AttendeeOrganizerHome.this,
                   "Unable to " +
                   "store QR code", Toast.LENGTH_SHORT).show());

           // update document with entire event object
            eventsRef.document(documentReference.getId()).set(event);

            usersRef.document(deviceID).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        User user = documentSnapshot.toObject(User.class);
//                        ArrayList<String> organizedEvents = user.getOrganizedEvents();
//                        organizedEvents.add(documentReference.getId());
                        assert user != null;
                        user.createEvent(documentReference.getId());
                        usersRef.document(deviceID).set(user);
                    });
                }).addOnFailureListener(e -> {

        })
                .addOnFailureListener(e -> {

                    // for failure
                    Toast.makeText(this, "Failed to add event to database", Toast.LENGTH_LONG).show();
                });
    }





}