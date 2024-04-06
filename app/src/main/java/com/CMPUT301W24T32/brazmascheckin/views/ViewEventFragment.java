
package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.DeleteFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.DeleteSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;

import java.util.ArrayList;

/**
 * This class is the fragment for the individual event view
 */
public class ViewEventFragment extends DialogFragment {
    private String deviceID;
    private TextView eventName;
    private TextView  eventDescription;

    private TextView eventDate;

    private ImageView eventPoster;
    private TextView eventCheckIns;
    private TextView qrCodeTitle;
    private TextView shareqrCodeTitle;
    private Button checkedInAttendeesBtn;
    private Button signedUpAttendeesBtn;
    private Button geoLocationBtn;
    private Button sharePromoQRCode;
    private Button eventAnalytics;
    private Button deleteEventBtn;
    private CheckBox signedUpCB;
    private ImageView QRCode;
    private ImageView shareQRCode;
    private TextView shareQRCodeLabel;
    private EventController eventController;
    private UserController userController;
    private ImageController imageController;

    public static final String EXTRA_VIEW_MODE = "view_mode";
    public static final int ATTENDEE_VIEW = 0;
    public static final int ORGANIZER_VIEW = 1;
    public static final int ADMIN_VIEW = 2;
    private int mode;

    /**
     * This function allows me to accept a bundle so i can access event data
     *
     * @param e event
     * @return fragment
     */
    public static ViewEventFragment sendEvent(Event e, int viewMode) {
        Bundle args = new Bundle();
        args.putSerializable("Event", e);
        args.putInt(EXTRA_VIEW_MODE, viewMode);
        ViewEventFragment fragment = new ViewEventFragment();
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

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_event_fragment_layout,null);
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        imageController = new ImageController(FirestoreDB.getStorageInstance());
        // retrieving from the bundle
        Bundle bundle = getArguments();
        Event e = (Event) bundle.getSerializable("Event");
        mode = bundle.getInt(EXTRA_VIEW_MODE, -1);

        configureViews(view, e, mode);
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
    private void configureViews(View view, Event e, int mode) {
        // views
        eventName = view.findViewById(R.id.view_event_name_tv);
        eventDescription = view.findViewById(R.id.view_event_description_tv);
        eventDate = view.findViewById(R.id.view_event_date_tv);
        eventCheckIns = view.findViewById(R.id.view_event_social_tv);
        qrCodeTitle = view.findViewById(R.id.view_event_check_in_qr_code_tv);
        shareqrCodeTitle = view.findViewById(R.id.view_event_share_qr_code_tv);
        eventPoster = view.findViewById(R.id.view_event_poster_iv);
        checkedInAttendeesBtn = view.findViewById(R.id.view_event_see_checked_in_attendees_btn);
        signedUpAttendeesBtn = view.findViewById(R.id.view_event_see_signed_up_attendees_btn);
        geoLocationBtn = view.findViewById(R.id.view_event_map_btn);
        signedUpCB = view.findViewById(R.id.view_event_signed_up_cb);
        QRCode = view.findViewById(R.id.view_event_QR_iv);
        shareQRCode = view.findViewById(R.id.view_event_share_QR_iv);
        shareQRCodeLabel = view.findViewById(R.id.view_event_share_qr_code_tv);
        sharePromoQRCode = view.findViewById(R.id.share_promo_qr_code_btn);
        eventAnalytics = view.findViewById(R.id.statistics_button);
        // admin functionality
        deleteEventBtn = view.findViewById(R.id.delete_event_button);


        deviceID = DeviceID.getDeviceID(getContext());

        eventName.setText(e.getName());
        eventDate.setText(e.getDate().getPrettyDate());
        eventDescription.setText(e.getDescription());
        eventCheckIns.setText(String.valueOf(e.helperCount()));

        ArrayList<String> signUps = e.getSignUps();
        if (signUps.contains(deviceID)){
            signedUpCB.setChecked(true);
        }

        if(e.getPoster() != null && !e.getPoster().isEmpty()) {
            displayImage(e.getPoster());
        }

        displayQRCode(e.getQRCode(), QRCode, false);
        displayQRCode(e.getShareQRCode(), shareQRCode, true);
        if(e.getQRCode() != null && !e.getQRCode().isEmpty()) {
            displayQRCode(e.getQRCode(), QRCode, false);
        }

        if(e.getShareQRCode() != null && !e.getShareQRCode().isEmpty()) {
            displayQRCode(e.getShareQRCode(), shareQRCode, true);
        } else {
            shareQRCodeLabel.setVisibility(View.GONE);
            shareQRCode.setVisibility(View.GONE);
        }

        if(mode == ATTENDEE_VIEW) {
            eventCheckIns.setVisibility(View.GONE);
            checkedInAttendeesBtn.setVisibility(View.GONE);
            signedUpAttendeesBtn.setVisibility(View.GONE);
            geoLocationBtn.setVisibility(View.GONE);
            QRCode.setVisibility(View.GONE);
            shareQRCode.setVisibility(View.GONE);
            shareqrCodeTitle.setVisibility(View.GONE);
            qrCodeTitle.setVisibility(View.GONE);
            sharePromoQRCode.setVisibility(View.GONE);
            eventAnalytics.setVisibility(View.GONE);
            deleteEventBtn.setVisibility(View.GONE);
        } else if (mode == ORGANIZER_VIEW) {
            if (!e.getGeoLocationEnabled()) {
                geoLocationBtn.setVisibility(View.GONE);
            }
            if(e.getShareQRCode() == null) {
                sharePromoQRCode.setVisibility(View.GONE);
            }
            deleteEventBtn.setVisibility(View.GONE);
        } else if(mode == ADMIN_VIEW) {
            eventCheckIns.setVisibility(View.GONE);
            checkedInAttendeesBtn.setVisibility(View.GONE);
            signedUpAttendeesBtn.setVisibility(View.GONE);
            geoLocationBtn.setVisibility(View.GONE);
            QRCode.setVisibility(View.GONE);
            shareQRCode.setVisibility(View.GONE);
            shareqrCodeTitle.setVisibility(View.GONE);
            qrCodeTitle.setVisibility(View.GONE);
            sharePromoQRCode.setVisibility(View.GONE);
            eventAnalytics.setVisibility(View.GONE);
            geoLocationBtn.setVisibility(View.GONE);
            sharePromoQRCode.setVisibility(View.GONE);
            signedUpCB.setVisibility(View.GONE);
        }
    }

    /**
     * This method configures the controllers required by the fragment
     * @param e
     */
    private void configureControllers(Event e, Context context) {
        handleCheckedInNumber(e.getID());
        handleCheckBox(e.getID());

        checkedInAttendeesBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ViewAttendeesActivity.class);
            intent.putExtra(ViewAttendeesActivity.EXTRA_EVENT, e);
            intent.putExtra(ViewAttendeesActivity.EXTRA_MODE, ViewAttendeesActivity.CHECK_IN_MODE);
            startActivity(intent);
        });

        signedUpAttendeesBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ViewAttendeesActivity.class);
            intent.putExtra(ViewAttendeesActivity.EXTRA_EVENT, e);
            intent.putExtra(ViewAttendeesActivity.EXTRA_MODE, ViewAttendeesActivity.SIGN_UP_MODE);
            startActivity(intent);
        });

        geoLocationBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ViewMapActivity.class);
            intent.putExtra(ViewMapActivity.EXTRA_LOCATION_PAIRS, e.getUserLocationPairs());
            intent.putExtra(ViewMapActivity.EXTRA_EVENT, e);
            intent.putExtra(ViewMapActivity.EXTRA_MODE, ViewMapActivity.VIEW_ATTENDEES);
            startActivity(intent);
        });
        sharePromoQRCode.setOnClickListener(view -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            imageController.getImageURL("SHARE_QR_CODE",e.getShareQRCode(), imageURL -> {
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, imageURL);
                startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
            }, error -> {
                Log.d("access error", "cannot access ");
            });
        });

        eventAnalytics.setOnClickListener(view -> {
            // GraphAnalyticsFragment activity, pass current event
            Intent intent = new Intent(getActivity(), GraphAnalyticsActivity.class);
            intent.putExtra("event", e);
            startActivity(intent);
        });

        deleteEventBtn.setOnClickListener(view -> {
            // need to delete event poster before deleting the actual event
            if (!e.getPoster().equals("defaultPoster.png")) {  // so default poster doesn't get deleted
                imageController.deleteImage("EVENT_POSTER", e.getPoster(), new DeleteSuccessListener() {
                    @Override
                    public void onDeleteSuccess() {
                        Log.d("ImageDeletion", "Event poster deleted successfully");
                    }
                }, new DeleteFailureListener() {
                    @Override
                    public void onDeleteFailure(Exception e) {
                        Log.e("ImageDeletion", "Failed to delete event poster: " + e.getMessage());
                    }
                });
            }
            // does actual deletion of event
            eventController.deleteEvent(e.getID(), () -> {
                Toast.makeText(getContext(), "Event deleted successfully", Toast.LENGTH_SHORT).show();
                dismiss();
            }, e1 -> Toast.makeText(getContext(), "Failed to delete event", Toast.LENGTH_SHORT).show());
        });
    }

    private void handleCheckedInNumber(String ID) {
        eventController.addSingleSnapshotListener(ID, new SnapshotListener<Event>() {
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

                    if(maxSignUps != -1) {
                        signedUpCB.setEnabled((signUpsCount + 1 <= maxSignUps) || (signUps.contains(deviceID)));
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
                handleUnChecked(ID);
            }
        });
    }

    private void handleChecked(String ID) {
        eventController.getEvent(ID, event -> {
            ArrayList<String> signUps = event.getSignUps();
            signUps.add(deviceID);
            eventController.setEvent(event, null, null);
        }, e -> {

        });

        userController.getUser(deviceID, user -> {
            ArrayList<String> signUps = user.getSignedUpEvents();
            signUps.add(ID);
            userController.setUser(user, null, null);
        }, e -> {

        });
    }

    private void handleUnChecked(String ID) {
        eventController.getEvent(ID, event -> {
            ArrayList<String> signUps = event.getSignUps();
            signUps.remove(deviceID);
            eventController.setEvent(event, null, null);
        }, e -> {

        });


        userController.getUser(deviceID, user -> {
            ArrayList<String> signUps = user.getSignedUpEvents();
            signUps.remove(ID);
            userController.setUser(user, null, null);
        }, e -> {

        });
    }

    /**
     * This method retrieves the poster image from the database and displays it in the view
     * @param posterID the ID of the image in the database
     */
    private void displayImage(String posterID) {
        if(posterID != null && !posterID.isEmpty()) {
            String folder = ImageController.DEFAULT_EVENT_POSTER;
            if(!posterID.equals("defaultPoster.png")) {
                folder = ImageController.EVENT_POSTER;
            }
            imageController.getImage(folder, posterID,
                    bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        eventPoster.setImageBitmap(bitmap);
                    }, e -> {

                    });
        }
    }

    /**
     * This method retrieves the QR code from the database and displays it in the view
     * @param code the ID of the QRC code in the database
     */

    private void displayQRCode(String code, ImageView QRCodeType, boolean share) {
        if(code != null && !code.isEmpty()) {
            String type;
            if (share) {
                type = ImageController.SHARE_QR_CODE;
            } else {
                type = ImageController.QR_CODE;
            }
            imageController.getImage(type, code, bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                QRCodeType.setImageBitmap(bitmap);
            }, e -> {

            });
        }
    }
}
