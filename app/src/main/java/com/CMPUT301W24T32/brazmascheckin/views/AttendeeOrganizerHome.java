package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
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
        eventsRef.add(event).addOnSuccessListener(documentReference -> {
           // set ID of the event after it has been added to database
           event.setID(documentReference.getId());
           Bitmap bitmap = generateQRCode(documentReference.getId());

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

        })
                .addOnFailureListener(e -> {
                    // for failure
                    Toast.makeText(this, "Failed to add event to database", Toast.LENGTH_LONG).show();
                });
    }

    /**
     * This method generates a QR code based on a seed
     * @param eventID the seed
     * @return bitmap of the QR code
     */
    private Bitmap generateQRCode(String eventID) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = writer.encode(eventID, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = new BarcodeEncoder().createBitmap(bitMatrix);
            return bitmap;
        } catch(WriterException e) {
            throw new RuntimeException(e);
        }
    }
}