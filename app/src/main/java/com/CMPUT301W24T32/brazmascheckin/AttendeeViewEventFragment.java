package com.CMPUT301W24T32.brazmascheckin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301W24T32.brazmascheckin.models.Event;

/**
 * This class is the fragment for the individual event view
 */
public class AttendeeViewEventFragment extends DialogFragment {
    private TextView eventName;
    private TextView  eventDescription;

    private TextView eventDate;
    private TextView eventAnnouncements;

    /**
     * This function allows me to accept a bundle so i can access event data
     *
     * @param e event
     * @return fragment
     */
    static AttendeeViewEventFragment sendEvent(Event e) {
        Bundle args = new Bundle();
        args.putSerializable("Event", e);
        AttendeeViewEventFragment fragment = new AttendeeViewEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return
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

        // need announcements for event class
        eventAnnouncements.setText("blahblahblah");


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view).create();
    }
}