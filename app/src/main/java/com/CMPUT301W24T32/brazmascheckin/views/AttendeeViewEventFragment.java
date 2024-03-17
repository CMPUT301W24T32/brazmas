package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventGetListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventSetListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageGetListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserGetListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserSetListener;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.User;


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
    private EventController eventController;
    private UserController userController;
    private ImageController imageController;

    private String deviceID;

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
        eventController = new EventController(getContext());
        userController = new UserController(getContext());
        imageController = new ImageController(getContext());
        // retrieving from the bundle
        Bundle bundle = getArguments();
        Event e = (Event) bundle.getSerializable("Event");
        configureViews(view, e);
        configureControllers(e, getContext());
        deviceID = DeviceID.getDeviceID(getContext());



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
        displayImage(e.getPoster());
    }

    /**
     * This method configures the controllers required by the fragment
     * @param e
     */
    private void configureControllers(Event e, Context context) {


        handleCheckedInNumber();


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
                    int signUpsCount = signUps.size();
                    int maxSignUps = event.getAttendeeLimit();

                    if((signUpsCount + 1 > maxSignUps) && (!signUps.contains(deviceID))) {
                        signedUpCB.setEnabled(false);
                    } else {
                        signedUpCB.setEnabled(true);
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void handleCheckBox(String ID) {
        signedUpCB.setOnCheckedChangeListener((buttonView ,isChecked) -> {
            if(isChecked) {
                handleChecked(ID);
            } else {

            }
        });
    }

    private void handleChecked(String ID) {
        eventController.getEvent(ID, new EventGetListener() {
            @Override
            public void onEventGetSuccess(Event event) {
                ArrayList<String> signUps = event.getSignUps();
                signUps.add(deviceID);
                eventController.setEvent(event, new EventSetListener() {
                    @Override
                    public void onEventSetSuccess() {
                        // Toast
                    }

                    @Override
                    public void onEventSetFailure(Exception e) {
                        // Toast
                    }
                });
            }

            @Override
            public void onEventGetFailure(Exception e) {

            }
        });

        userController.getUser(deviceID, new UserGetListener() {
            @Override
            public void onUserGetSuccess(User user) {
                ArrayList<String> signUps = user.getSignedUpEvents();
                signUps.add(ID);
                userController.setUser(user, new UserSetListener() {
                    @Override
                    public void onUserSetSuccess() {

                    }

                    @Override
                    public void onUserSetFailure() {

                    }
                });
            }

            @Override
            public void onUserGetFailure(Exception e) {

            }
        });
    }

    private void handleUnChecked(String ID) {
        eventController.getEvent(ID, new EventGetListener() {
            @Override
            public void onEventGetSuccess(Event event) {
                ArrayList<String> signUps = event.getSignUps();
                signUps.remove(deviceID);
                eventController.setEvent(event, new EventSetListener() {
                    @Override
                    public void onEventSetSuccess() {
                        // Toast
                    }

                    @Override
                    public void onEventSetFailure(Exception e) {
                        // Toast
                    }
                });
            }

            @Override
            public void onEventGetFailure(Exception e) {

            }
        });

        userController.getUser(deviceID, new UserGetListener() {
            @Override
            public void onUserGetSuccess(User user) {
                ArrayList<String> signUps = user.getSignedUpEvents();
                signUps.remove(ID);
                userController.setUser(user, new UserSetListener() {
                    @Override
                    public void onUserSetSuccess() {

                    }

                    @Override
                    public void onUserSetFailure() {

                    }
                });
            }

            @Override
            public void onUserGetFailure(Exception e) {

            }
        });
    }

    /**
     * This method retrieves the poster image from the database and displays it in the view
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
     * This method retrieves the QR code from the database and displays it in the view
     * @param code the ID of the QRC code in the database
     */

    private void displayQRCode(String code) {

        imageController.getQRCode(code, new ImageGetListener() {
            @Override
            public void onImageGetSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                QRCode.setImageBitmap(bitmap);
            }

            @Override
            public void onImageGetFailure(Exception e) {

            }
        });
    }
}