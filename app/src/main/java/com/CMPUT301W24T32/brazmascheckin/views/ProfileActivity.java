package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * Profile activity made for testing navigation
 */

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private TextView userName;
    private CollectionReference usersRef;
    private DocumentReference userDoc;
    /**
     *This method initializes the Profile activity.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Allows the app to switch between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        configureViews();
        configureControllers();
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             * This method determines something from navigation bar has been selected or not.
             * @param menuItem The selected item
             * @return True if selected, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == (R.id.bottom_announcement)){
                    startActivity(new Intent(getApplicationContext(), AnnouncementActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_camera)){
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_profile)){
                    return true;
                }
                if (id == (R.id.bottom_event)){
                    startActivity(new Intent(getApplicationContext(), AttendeeOrganizerHome.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;

            }
        });
    }

    /**
     * configures the views for profile activity
     */
    public void configureViews(){
        profilePicture = findViewById(R.id.profile_picture);
        userName = findViewById(R.id.name_tv);
        usersRef = FirestoreDB.getUsersRef();
        String deviceID = DeviceID.getDeviceID(this);
        userDoc = usersRef.document(deviceID);
    }

    /**
     * configure controllers for profile activity
     */
    public void configureControllers(){
        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            String firstName = user.getFirstName();
            String lastName = user.getLastName();
            String fullName = firstName + " " + lastName;
            userName.setText(fullName);
        });
    }
}