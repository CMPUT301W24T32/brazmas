package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.android.gms.tasks.OnSuccessListener;
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
     * This fucntion creates the dialog box and sets all the texts
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
        //getting textviews
        eventName = view.findViewById(R.id.EventNameTextView);
        eventDescription = view.findViewById(R.id.EventDescriptionTextView);
        eventDate = view.findViewById(R.id.EventDateTextView);
        eventAnnouncements = view.findViewById(R.id.AnnoucementExample1);
        //retrieving from the bundle
        Bundle bundle = getArguments();
        Event e = (Event) bundle.getSerializable("Event");

        // sets the text based on the event
        eventName.setText(e.getName());
        eventDate.setText(e.getDate().getPrettyDate());
        eventDescription.setText(e.getDescription());

        // need to add one for poster
        eventPoster = view.findViewById(R.id.view_event_image);
        displayImage(e.getPoster());
        // need announcements for event class
        eventAnnouncements.setText("blahblahblah");


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setNegativeButton("Back",null)
                .setView(view).create();
    }

    /**
     * This method retrieves the poster image from the database and displays it in the view
     * @param posterID the ID of the image in the database
     */
    private void displayImage(String posterID) {
        StorageReference storage = FirestoreDB.getStorageReference("uploads");
        StorageReference imageRef = storage.child(posterID);

        imageRef.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    eventPoster.setImageBitmap(bitmap);
                });
    }
}