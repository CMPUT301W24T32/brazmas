package com.CMPUT301W24T32.brazmascheckin.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * Profile activity
 */

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private TextView userName;
    private Button editProfileBtn;
    private Button checkInGeoLocationBtn;
    private Button allEventsGeoLocationBtn;
    private Button organizedEventsGeoLocationBtn;
    private SwitchCompat geoLocationSwitch;
    private LinearLayout extraGeoLocationLinearLayout;
    private String deviceID;
    private UserController userController;
    private ImageController imageController;
    private static final int PERMISSION_FINE_COARSE_LOCATION = 99;



    /**
     * This method initializes the Profile activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        configureViews();
        configureControllers();
        // Allows the app to switch between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);



        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             * This method determines something from navigation bar has been selected or not.
             * @param menuItem The selected item
             * @return True if selected, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == (R.id.bottom_announcement)) {
                    startActivity(new Intent(getApplicationContext(), AnnouncementActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (id == (R.id.bottom_camera)) {
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (id == (R.id.bottom_profile)) {
                    return true;
                }
                if (id == (R.id.bottom_event)) {
                    startActivity(new Intent(getApplicationContext(), UserHome.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;

            }
        });
    }

    /**
     * configures the views for profile activity
     */
    public void configureViews() {
        profilePicture = findViewById(R.id.profile_picture);
        userName = findViewById(R.id.name_tv);
        editProfileBtn = findViewById(R.id.edit_profile_btn);
        geoLocationSwitch = findViewById(R.id.profile_geolocation_sw);
        checkInGeoLocationBtn = findViewById(R.id.profile_geolocation_attendee_checkins_btn);
        allEventsGeoLocationBtn = findViewById(R.id.profile_geolocation_all_events_btn);
        organizedEventsGeoLocationBtn = findViewById(R.id.profile_geolocation_organized_events_btn);
        extraGeoLocationLinearLayout = findViewById(R.id.profile_geolocation_extra_ll);
        deviceID = DeviceID.getDeviceID(this);
    }

    /**
     * configure controllers for profile activity
     */
    public void configureControllers() {
        userController = new UserController(this);
        imageController = new ImageController(this);

        userController.getUser(deviceID, user ->{
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String fullName = firstName + " " + lastName;
            if (user.getProfilePicture() != null) {
                displayImage(user.getProfilePicture());
            }
            else{
                displayDefaultImage(user.getDefaultProfilePicture());
            }
            userName.setText(fullName);

            if(user.isGeoLocationEnabled()) {
                geoLocationSwitch.setChecked(true);
                extraGeoLocationLinearLayout.setVisibility(View.VISIBLE);
            }

        },null);


        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
            }
        });
        locationSwitch();
        handleEventMaps();
    }

    /**
     * Handles the toggle action for enabling or disabling geolocation for the user.
     * When the switch state changes, this method retrieves the user's data and updates
     * the geolocation setting accordingly.
     */
    private void locationSwitch() {
        geoLocationSwitch.setOnClickListener(view -> {
            if(geoLocationSwitch.isChecked()) {
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_FINE_COARSE_LOCATION);
                }
            } else {
                userController.getUser(deviceID, user -> {
                    user.setGeoLocationEnabled(false);
                    userController.setUser(user, () -> {
                        Toast.makeText(this, "Geolocation disabled", Toast.LENGTH_SHORT).show();
                        extraGeoLocationLinearLayout.setVisibility(View.GONE);
                            },
                            e -> {
                                Toast.makeText(this, "Unable to disable geolocation", Toast.LENGTH_SHORT).show();
                                geoLocationSwitch.setChecked(true);
                                user.setGeoLocationEnabled(true);
                            });
                }, e -> {
                    Toast.makeText(ProfileActivity.this, "Unable to disable geolocation", Toast.LENGTH_SHORT).show();
                    geoLocationSwitch.setChecked(true);
                });
            }
        });
    }

    /**
     * Enables geolocation for the user if the necessary location permission is granted.
     * If the permission is not granted, requests it from the user.
     */
    private void enableLocation() {
        userController.getUser(deviceID, user -> {
            user.setGeoLocationEnabled(true);
            extraGeoLocationLinearLayout.setVisibility(View.VISIBLE);
            userController.setUser(user, () -> Toast.makeText(ProfileActivity.this, "Geolocation enabled", Toast.LENGTH_SHORT).show(),
                    e -> {
                        Toast.makeText(ProfileActivity.this, "Unable to enable geolocation", Toast.LENGTH_SHORT).show();
                        geoLocationSwitch.setChecked(false);
                        user.setGeoLocationEnabled(false);
                    });
        }, e -> {
            Toast.makeText(ProfileActivity.this, "Unable to enable geolocation", Toast.LENGTH_SHORT).show();
            geoLocationSwitch.setChecked(false);
        });
    }

    /**
     * This method retrieves the poster image from the database and displays it in the view.
     * @param posterID the ID of the image in the database
     */
    private void displayImage(String posterID) {
        if (posterID != null) {
            imageController.getImage(ImageController.PROFILE_PICTURE, posterID, bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePicture.setImageBitmap(bitmap);
            }, null);
        }
    }

    /**
     * This method retrieves the default poster image from the database and displays it in the view.
     * @param posterID the ID of the image in the database
     */
    private void displayDefaultImage(String posterID){
        if (posterID != null) {
            imageController.getImage(ImageController.DEFAULT_PROFILE_PICTURE_PATH, posterID, bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePicture.setImageBitmap(bitmap);
            }, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_FINE_COARSE_LOCATION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            } else {
                Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();
                geoLocationSwitch.setChecked(false);
            }
        }
    }

    private void handleEventMaps() {
        checkInGeoLocationBtn.setOnClickListener(view -> {
            Intent i = new Intent(ProfileActivity.this, ViewMapActivity.class);
            i.putExtra(ViewMapActivity.EXTRA_MODE, ViewMapActivity.VIEW_ATTENDEE_CHECK_INS);
            startActivity(i);
        });

        allEventsGeoLocationBtn.setOnClickListener(view -> {
            Intent i = new Intent(ProfileActivity.this, ViewMapActivity.class);
            i.putExtra(ViewMapActivity.EXTRA_MODE, ViewMapActivity.VIEW_ALL_EVENTS);
            startActivity(i);
        });

        organizedEventsGeoLocationBtn.setOnClickListener(view -> {
            Intent i = new Intent(ProfileActivity.this, ViewMapActivity.class);
            i.putExtra(ViewMapActivity.EXTRA_MODE, ViewMapActivity.VIEW_ORGANIZED_EVENTS);
            startActivity(i);
        });
    }
}

