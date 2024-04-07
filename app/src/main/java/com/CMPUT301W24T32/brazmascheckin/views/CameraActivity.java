package com.CMPUT301W24T32.brazmascheckin.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.SetFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * CameraActivity allows users to scan QR codes and perform check-ins to events.
 * It also handles navigation using the bottom navigation bar.
 */
public class CameraActivity extends AppCompatActivity {

    // Scan button
    Button btn_scan;

    private EventController eventController;
    private UserController userController;
    private String deviceID;
    private FusedLocationProviderClient fusedLocationProviderClient;


    /**
     *This method initializes the Camera activity.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        deviceID = DeviceID.getDeviceID(this);

        if (checkLocationPermissions()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        }

        // Initialize scan button
        btn_scan = findViewById(R.id.qr_code_scan_btn);
        btn_scan.setOnClickListener(v -> scanCode());

        // Allows the app to switch between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_camera);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             * This method determines something from navigation bar has been selected or not.
             * @param menuItem The selected item
             * @return True if selected, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.bottom_announcement) {
                    startActivity(new Intent(getApplicationContext(), AnnouncementActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (id == R.id.bottom_camera) {
                    return true;
                }
                if (id == R.id.bottom_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (id == R.id.bottom_event) {
                    startActivity(new Intent(getApplicationContext(), UserHome.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * This method initiates the QR code scanning process.
     */
    public void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    // Activity result launcher for QR code scanning
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        handleScanResult(result);
    });

    /**
     * Handles the result of the QR code scanning.
     * @param result The result of the QR code scanning.
     */
    private void handleScanResult(ScanIntentResult result) {

        // Get the scanned QR code content (event ID)
        String qrID = result.getContents();
        if (qrID.endsWith("SHARE-QRCODE")) {
            openEventViewFragment(qrID);
        } else if (qrID.endsWith("-QRCODE")){
            handleCheckIn(qrID);
        } else {
            Toast.makeText(this, "Invalid QR Code for this app", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens the event view fragment for a shared event.
     * @param shareqrID The QR code ID of the shared event.
     */
    private void openEventViewFragment(String shareqrID) {
        String eventID = shareqrID.replace("-SHARE-QRCODE","");
        eventController.getEvent(eventID, event -> {
            // Open the view fragment for the event
            ViewEventFragment viewEventFragment = ViewEventFragment.sendEvent(event, ViewEventFragment.ATTENDEE_VIEW);
            viewEventFragment.show(getSupportFragmentManager(), "view_event_fragment");
        }, e -> {
            Toast.makeText(CameraActivity.this, "Unable to load event details a", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Handles the check-in process for an event.
     * @param qrID The QR code ID of the event.
     */
    @SuppressLint("MissingPermission")
    private void handleCheckIn(String qrID) {
        qrID = qrID.replace("-QRCODE", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
        eventController.getEvent(qrID, event -> userController.getUser(deviceID, user -> {
            if (user.isGeoLocationEnabled() && event.getGeoLocationEnabled()) {
                if (checkLocationPermissions()) {
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                        Location l = new Location(location.getLatitude(), location.getLongitude());
                        checkIntoEvent(builder, event, l, user);
                    });
                } else {
                    Toast.makeText(CameraActivity.this, "Checked into event without location", Toast.LENGTH_SHORT).show();
                    checkIntoEvent(builder, event, null, user);
                }
            } else {
                checkIntoEvent(builder, event, null, user);
            }
        }, e -> Toast.makeText(CameraActivity.this, "Unable to check into event", Toast.LENGTH_SHORT).show()), e -> {
            Toast.makeText(this, "Unable to check into event", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Helper method for the check-in process.
     * @param builder AlertDialog.Builder to display the check-in result
     * @param event the event to heck into
     * @param location the location of the check-in
     * @param user the user performing the check-in
     */
    private void checkIntoEvent(AlertDialog.Builder builder, Event event, Location location, User user) {
        event.checkIn(deviceID, location);
        eventController.setEvent(event, () -> {
            builder.setTitle("Successfully checked in!");
            builder.setMessage(event.getName());
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();
            user.checkIn(event.getID());
            userController.setUser(user, null, e -> Toast.makeText(CameraActivity.this, "Unable to register location", Toast.LENGTH_SHORT).show());
        }, e -> Toast.makeText(CameraActivity.this, "Unable to check into event", Toast.LENGTH_SHORT).show());
    }

    // TODO: put this into utility class
    /**
     * Checks if the necessary location permissions are granted.
     * @return True if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted, false otherwise.
     */
    private boolean checkLocationPermissions() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

}
