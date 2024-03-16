package com.CMPUT301W24T32.brazmascheckin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.controllers.AdminController;
import com.CMPUT301W24T32.brazmascheckin.controllers.AdminGetListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserGetListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserSetListener;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.CMPUT301W24T32.brazmascheckin.views.AttendeeOrganizerHome;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

/**
 * This class represents the main activity of the application.
 * It handles the device verification process and redirects users based on their status.
 */

public class MainActivity extends AppCompatActivity implements UserGetListener, UserSetListener,
        AdminGetListener {

    private UserController userController;
    private AdminController adminController;
    private String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting device ID and storing it in a string
        deviceID = DeviceID.getDeviceID(this);
        userController = new UserController(this);
        adminController = new AdminController(this);

        // control flow of different types of users
        adminController.getAdmin(deviceID, this);
        userController.getUser(deviceID, this);
    }

    @Override
    public void onUserGetSuccess(User user) {
        Intent intent = new Intent(MainActivity.this, AttendeeOrganizerHome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserGetFailure(Exception e) {
        TextView firstNameEditText = findViewById(R.id.main_firstname_textview);
        TextView lastNameEditText = findViewById(R.id.main_lastname_textview);
        Button submitButton = findViewById(R.id.main_submit_button);

        submitButton.setOnClickListener(view -> {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();

            if(firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                User user = new User(
                        firstName, lastName, new ArrayList<String>(), null, null,
                        new ArrayList<String>()
                );
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setID(deviceID);
                userController.setUser(user, this);
            }
        });
    }

    @Override
    public void onUserSetSuccess() {
        Intent intent = new Intent(MainActivity.this, AttendeeOrganizerHome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUserSetFailure() {
        Toast.makeText(this, "Unable to create profile", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdminGetSuccess() {
        Intent intent = new Intent(MainActivity.this, AdministratorHome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAdminGetFailure(Exception e) {

    }
}