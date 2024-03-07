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
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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
    private Button checkedInAttendeesBtn;
    private Button signedUpAttendeesBtn;
    private CheckBox signedUpCB;
    private ImageView QRCode;

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
        checkedInAttendeesBtn = view.findViewById(R.id.view_event_see_checked_in_attendees_btn);
        signedUpAttendeesBtn = view.findViewById(R.id.view_event_see_signed_up_attendees_btn);

        signedUpCB = view.findViewById(R.id.signed_up_CB);
        String deviceID = DeviceID.getDeviceID(getContext());
        ArrayList<String> signUps = e.getSignUps();
        if (signUps.contains(deviceID)){
            signedUpCB.setChecked(true);
        }
        QRCode = view.findViewById(R.id.view_event_QR_iv);
        displayQRCode(e.getQRCode(), e.getID());
        displayImage(e.getPoster());
    }

    /**
     * This method configures the controllers required by the fragment
     * @param e
     */
    private void configureControllers(Event e) {
        String deviceID = DeviceID.getDeviceID(this.requireContext());

        CollectionReference eventsRef = FirestoreDB.getEventsRef();
        CollectionReference usersRef = FirestoreDB.getUsersRef();
        DocumentReference eventDoc = eventsRef.document(e.getID());
        DocumentReference userDoc = usersRef.document(deviceID);

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

                // The number of attendees does not increase past the maximum number of attendees specified by the organizer.
                ArrayList<String> signUps = dbEvent.getSignUps();
                int signUpsCount = signUps.size();
                int maxSignUps = dbEvent.getAttendeeLimit();

                if((signUpsCount + 1 > maxSignUps) && (!signUps.contains(DeviceID.getDeviceID(this.getContext())))) {
                    signedUpCB.setEnabled(false);
                } else {
                    signedUpCB.setEnabled(true);
                }
            }
        });



        signedUpCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Perform actions based on checkbox state
            if (isChecked) {
                eventDoc.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Convert document snapshot to a User object
                            Event e1 = document.toObject(Event.class);
                            // Now you can use the user object
//                            ArrayList<String> signUps = e1.getSignUps();
//                            signUps.add(deviceID);
                            e1.signUp(deviceID);
                            eventDoc.set(e1);
                        }
                    } else {
                        Toast.makeText(getContext(), "Unable to update event",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                userDoc.get().addOnCompleteListener(task -> {
                   if(task.isSuccessful()) {
                       DocumentSnapshot document = task.getResult();
                       User user = document.toObject(User.class);

//                       ArrayList<String> signUps = user.getSignedUpEvents();
//                       signUps.add(e.getID());
                       user.signUpEvent(e.getID());
                       userDoc.set(user);
                   } else {
                       Toast.makeText(getContext(), "Unable to update user",
                               Toast.LENGTH_SHORT).show();
                   }
                });


            } else {
                eventDoc.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String deviceID1 = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                            Event e1 = document.toObject(Event.class);
//                            ArrayList<String> signUps = e1.getSignUps();
//                            signUps.remove(deviceID1);
                            e1.unSignUp(deviceID);
                            eventDoc.set(e1);

                        }
                    } else {
                        Toast.makeText(getContext(), "Unable to update event",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                userDoc.get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        User user = document.toObject(User.class);

//                        ArrayList<String> signUps = user.getSignedUpEvents();
//                        signUps.remove(e.getID());
                        user.unSignUpEvent(e.getID());
                        userDoc.set(user);
                    } else {
                        Toast.makeText(getContext(), "Unable to update user",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        checkedInAttendeesBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CheckedInAttendees.class);
            intent.putExtra("EVENT", e);
            startActivity(intent);
        });

        signedUpAttendeesBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SignedUpAttendees.class);
            intent.putExtra("EVENT", e);
            startActivity(intent);
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

    /**
     * This method retrieves the QR code from the database and displays it in the view
     * @param code the ID of the QRC code in the database
     * @param ID the ID of the event
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
}