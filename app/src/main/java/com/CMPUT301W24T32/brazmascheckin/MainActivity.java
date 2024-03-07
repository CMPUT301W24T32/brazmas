package com.CMPUT301W24T32.brazmascheckin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;


import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;

import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.CMPUT301W24T32.brazmascheckin.views.AttendeeOrganizerHome;
import com.google.firebase.firestore.CollectionReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting device ID and storing it in a string
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // control flow of different types of users
        verifyAdministratorStatus(deviceID);
        verifyUserStatus(deviceID);
    }

    /**
     * This method determines if the device belongs to an administrator, and redirects them accordingly.
     * @param deviceID the device ID
     */
    private void verifyAdministratorStatus(String deviceID) {
        CollectionReference adminsRef = FirestoreDB.getAdminsRef();
        adminsRef.document(deviceID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    // if the device ID is registered as an administrator
                    if(documentSnapshot.exists()) {
                        Intent intent = new Intent(MainActivity.this, AdministratorHome.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    // TODO: handle failure
                });
    }

    /**
     * This method determines if the device belongs to a user, and redirects them accordingly.
     * @param deviceID the device ID
     */
    private void verifyUserStatus(String deviceID) {
        CollectionReference usersRef = FirestoreDB.getUsersRef();
        usersRef.document(deviceID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Intent intent = new Intent(MainActivity.this, AttendeeOrganizerHome.class);
                    if(documentSnapshot.exists()) {
                        startActivity(intent);
                        finish();
                    } else {
                        createUserProfile(deviceID, usersRef, intent);
                    }
                })
                .addOnFailureListener(e -> {
                    // TODO: handle failure
                });
    }

    /**
     * This method attempts to create a new user profile
     * @param deviceID the device ID
     * @param usersRef the FirestoreDB collection for users
     * @param intent the Intent object to go to the AttendeeOrganizer homepage
     */
    private void createUserProfile(String deviceID, CollectionReference usersRef, Intent intent) {
        TextView firstNameEditText = findViewById(R.id.main_firstname_textview);
        TextView lastNameEditText = findViewById(R.id.main_lastname_textview);
        Button submitButton = findViewById(R.id.main_submit_button);

        submitButton.setOnClickListener(view -> {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            //TODO: check if attributes can be empty/null

            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setID(deviceID);

            usersRef.document(deviceID).set(user)
                    .addOnSuccessListener(documentReference -> {
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Unable to create " +
                                "profile", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}