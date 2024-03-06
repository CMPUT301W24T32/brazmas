package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

/**
 * This class is the fragment for the individual event view
 */
public class AttendeeViewEventFragment extends DialogFragment {
    private TextView eventName;
    private TextView  eventDescription;

    private TextView eventDate;
    private TextView eventAnnouncements;
    private ImageView eventPoster;
    private TextView eventCheckIns;

    /**
     * This function allows me to accept a bundle so i can access event data
     *
     * @param e event
     * @return fragment
     */
    public static AttendeeViewEventFragment sendEvent(Event e) {
        Bundle args = new Bundle();
        args.putSerializable("Event", e);
        AttendeeViewEventFragment fragment = new AttendeeViewEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This function creates the dialog box and sets all the texts
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the builder
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.attendee_view_event_fragment_layout,null);

        // retrieving from the bundle
        Bundle bundle = getArguments();
        Event e = (Event) bundle.getSerializable("Event");
        configureViews(view, e);
        configureControllers(e);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setNegativeButton("Back",null)
                .setView(view).create();
    }


    /**
     * This method configures the views required by the fragment
     * @param view view of the fragment
     * @param e event to be displayed
     */
    private void configureViews(View view, Event e) {
        eventName = view.findViewById(R.id.view_event_name_tv);
        eventDescription = view.findViewById(R.id.view_event_description_tv);
        eventDate = view.findViewById(R.id.view_event_date_tv);
        eventAnnouncements = view.findViewById(R.id.view_event_announcement_tv1);
        eventCheckIns = view.findViewById(R.id.view_event_checkins_tv);
        eventName.setText(e.getName());
        eventDate.setText(e.getDate().getPrettyDate());
        eventDescription.setText(e.getDescription());
        eventCheckIns.setText(String.valueOf(e.helperCount()));
        eventPoster = view.findViewById(R.id.view_event_poster_iv);
        displayImage(e.getPoster());
    }

    /**
     * This method configures the controllers required by the fragment
     * @param e
     */
    private void configureControllers(Event e) {
        CollectionReference eventsRef = FirestoreDB.getEventsRef();
        DocumentReference eventDoc = eventsRef.document(e.getID());
        eventDoc.addSnapshotListener((documentSnapshot, er) -> {
            if(e != null) {
                Log.d("testing", "check in added");
                Event dbEvent = documentSnapshot.toObject(Event.class);
                //TODO: how to check if checkIns is null
                int checkIns = dbEvent.helperCount();
                Log.d("testing", String.valueOf(checkIns));
                eventCheckIns.setText(String.valueOf(checkIns));
            } else {
                Toast.makeText(requireContext(), "Could not " +
                        "retrieve event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method retrieves the poster image from the database and displays it in the view
     * @param posterID the ID of the image in the database
     */
    private void displayImage(String posterID) {
        if(posterID != null) {
            StorageReference storage = FirestoreDB.getStorageReference("uploads");
            StorageReference imageRef = storage.child(posterID);

            // TODO: error checking to see if child with value posterID exists in storage

            imageRef.getBytes(Long.MAX_VALUE)
                    .addOnSuccessListener(bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        eventPoster.setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Unable " +
                            "to load event poster.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(requireContext(), "Unable to display event poster", Toast.LENGTH_SHORT).show();
        }
    }
}