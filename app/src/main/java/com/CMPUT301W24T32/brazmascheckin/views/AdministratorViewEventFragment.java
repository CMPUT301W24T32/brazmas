package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * This class is the fragment for the individual event view for Administrator.
 */
public class AdministratorViewEventFragment extends DialogFragment {

    private TextView eventName;
    private TextView  eventDescription;

    private TextView eventDate;
    private TextView eventAnnouncements;
    private ImageView eventPoster;

    // need button to delete event
    // need button to delete poster

    /**
     * This function lets us to accept a bundle so event data can be accessed.
     * @param e event
     * @return fragment
     */
    public static AdministratorViewEventFragment sendEvent(Event e) {
        Bundle args = new Bundle();
        args.putSerializable("Event", e);
        AdministratorViewEventFragment fragment = new AdministratorViewEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This creates the dialog box and sets all the text.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if recently created Fragment.
     *
     * @return the builder
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.administrator_view_event_fragment_layout, null);

        // retrieve from bundle
        Bundle bundle = getArguments();
        Event e = (Event) bundle.getSerializable("Event");
        configureViews(view, e);
        configureControllers(e);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setNegativeButton("Back", null)
                .setView(view).create();
    }

    /**
     * This method configures the views required by the fragment
     * @param view view of the fragment
     * @param e event to be display
     */
    private void configureViews(View view, Event e) {
        eventName = view.findViewById(R.id.view_event_name_tv_admin);
        eventDescription = view.findViewById(R.id.view_event_description_tv_admin);
        eventDate = view.findViewById(R.id.view_event_date_tv_admin);
        eventAnnouncements = view.findViewById(R.id.view_event_announcement_tv1_admin);
        //eventCheckIns = view.findViewById(R.id.view_event_checkins_tv);
        eventName.setText(e.getName());
        eventDate.setText(e.getDate().getPrettyDate());
        eventDescription.setText(e.getDescription());
        //eventCheckIns.setText(String.valueOf(e.helperCount()));
        eventPoster = view.findViewById(R.id.view_event_poster_iv_admin);
        //checkedInAttendeesBtn = view.findViewById(R.id.view_event_see_checked_in_attendees_btn);
        //signedUpAttendeesBtn = view.findViewById(R.id.view_event_see_signed_up_attendees_btn);

    }

    /**
     * This method configures the controllers required by the fragment.
     * @param e
     */
    private void configureControllers(Event e) {

    }

}
