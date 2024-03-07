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
    private ImageView eventPoster;
    private TextView eventCheckIns;

    // don't display them? but make sure they are removed when event is deleted
    private TextView eventAnnouncements;
    private Button checkedInAttendeesBtn;
    private Button signedUpAttendeesBtn;
    private CheckBox signedUpCB;
    private ImageView QRCode;

    /**
     * This function allows for the acceptance of bundle to access event data
     *
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
     * This method creates the dialog box and sets all the texts
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the builder
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.administrator_view_event_fragment_layout,null);

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
        //checkedInAttendeesBtn = view.findViewById(R.id.view_event_see_checked_in_attendees_btn);
        //signedUpAttendeesBtn = view.findViewById(R.id.view_event_see_signed_up_attendees_btn);

        /*signedUpCB = view.findViewById(R.id.signed_up_CB);
        String deviceID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        ArrayList<String> signUps = e.getSignUps();
        if (signUps.contains(deviceID)){
            signedUpCB.setChecked(true);
        }*/
        //QRCode = view.findViewById(R.id.view_event_QR_iv);
        //displayQRCode(e.getQRCode(), e.getID());
        displayImage(e.getPoster());

    }

    /**
     * This method configures the controllers required by the fragment.
     * @param e the event
     */
    private void configureControllers(Event e) {

        String deviceID = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        CollectionReference eventsRef = FirestoreDB.getEventsRef();
        //CollectionReference usersRef = FirestoreDB.getUsersRef();
        DocumentReference eventDoc = eventsRef.document(e.getID());
        //DocumentReference userDoc = usersRef.document(deviceID);


        // TODO: do we need this for the admin?
        eventDoc.addSnapshotListener((documentSnapshot, er) -> {
            Event dbEvent = documentSnapshot.toObject(Event.class);
            if(dbEvent != null) {
                int checkIns = dbEvent.helperCount();
                if(checkIns != -1) {
                    eventCheckIns.setText(String.valueOf(checkIns));
                } else {
                    Toast.makeText(requireContext(), "Error retrieving attendance information",
                            Toast.LENGTH_SHORT).show();
                }
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

            // TODO: error checking to see child with value posterID exists in storage

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

    /**
     * This method retrieves the QR code from the database and displays it in the view
     * DO WE NEED THIS FOR THE ADMIN?
     */
    private void displayQRCode(String code, String ID) {
        if(code != null) {
            StorageReference storage = FirestoreDB.getStorageReference("QRCodes");
            StorageReference imageRef = storage.child(ID + "-QRCODE");

            imageRef.getBytes(Long.MAX_VALUE)
                    .addOnSuccessListener(bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        QRCode.setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // TODO: need to have delete event button and delete poster button
}
