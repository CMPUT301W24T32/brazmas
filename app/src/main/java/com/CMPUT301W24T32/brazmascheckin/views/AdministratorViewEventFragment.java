package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.AdminController;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventDeleteListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageGetListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * This class is the fragment for the individual event view for Administrator
 */
public class AdministratorViewEventFragment extends DialogFragment {
    private TextView eventName;
    private TextView  eventDescription;
    private TextView eventDate;
    private TextView eventCheckIns;

    private ImageView eventPoster;

    // don't display them? but make sure they are removed when event is deleted
    private TextView eventAnnouncements;
    private Button checkedInAttendeesBtn;
    private Button signedUpAttendeesBtn;
    private CheckBox signedUpCB;
    private ImageView QRCode;

    // controllers
    private EventController eventController;
    private ImageController imageController;
    private Button deleteEventBtn;

    /**
     * This method allows for the acceptance of bundle to access event data
     * @param e event that has been selected by admin
     * @return fragment, will contain event data
     */
    public static AdministratorViewEventFragment sendEvent(Event e) {
        Bundle args = new Bundle();
        args.putSerializable("Event", e);
        AdministratorViewEventFragment fragment = new AdministratorViewEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * This method creates the dialog box and sets all the text.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * if it exists.
     * @return the builder
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.administrator_view_event_fragment_layout,null);
        eventController = new EventController(getContext());
        imageController = new ImageController(getContext());

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
     * This method configures the views required by the fragment.
     * @param view view of the fragment
     * @param e event to be displayed
     */
    //TODO: decide what is actually shown to admin from the event details
    private void configureViews(View view, Event e) {

        if (e != null) {
            eventName = view.findViewById(R.id.view_event_name_tv_admin);
            eventDescription = view.findViewById(R.id.view_event_description_tv_admin);
            eventDate = view.findViewById(R.id.view_event_date_tv_admin);
            eventAnnouncements = view.findViewById(R.id.view_event_announcement_tv1_admin);
            eventCheckIns = view.findViewById(R.id.view_event_checkins_tv_admin);
            eventName.setText(e.getName());
            eventDate.setText(e.getDate().getPrettyDate());
            eventDescription.setText(e.getDescription());
            eventCheckIns.setText(String.valueOf(e.helperCount()));
            eventPoster = view.findViewById(R.id.view_event_poster_iv_admin);

            deleteEventBtn = view.findViewById(R.id.view_event_delete_btn_admin);
            displayImage(e.getPoster());
        }
    }

    /**
     * This method configures the controllers required by the fragment.
     * @param e the event selected by the admin
     */
    //TODO: decide what is the actually shown to the admin from the event details
    private void configureControllers(Event e) {

        if (e != null) {
            handleCheckedInNumber();

            deleteEventBtn.setOnClickListener(view -> {
                // call the deleteEvent method of EventController
                eventController.deleteEvent(e.getID(), new EventDeleteListener() {
                    @Override
                    public void onEventDeleteSuccess() {
                        // event is deleted successfully
                        Toast.makeText(getContext(), "Event deleted successfully", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                    @Override
                    public void onEventDeleteFailure(Exception e) {
                        // failed to delete an event
                        Toast.makeText(getContext(), "Failed to delete event", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

    /**
     * This method retrieves the poster image from the database and displays it in the view.
     * @param posterID the ID of the image in the database
     */
    private void displayImage(String posterID) {
        imageController.getEventPoster(posterID, new ImageGetListener() {
            @Override
            public void onImageGetSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                eventPoster.setImageBitmap(bitmap);
            }

            @Override
            public void onImageGetFailure(Exception e) {
                // Toast
            }
        });
    }

    /**
     * This method retrieves the amount of attendees checked into the event
     * and displays it in the view.
     */
    private void handleCheckedInNumber() {
        eventController.addSnapshotListener(new SnapshotListener<Event>() {
            @Override
            public void snapshotListenerCallback(ArrayList<Event> events) {
                Event event = events.get(0);
                if(event != null) {
                    int checkIns = event.helperCount();
                    if(checkIns != -1) {
                        eventCheckIns.setText(String.valueOf(checkIns));
                    } else {
//                        Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                    }

                    ArrayList<String> signUps = event.getSignUps();
                    int signUpsCount = signUps.size();  // do I need this?

                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
