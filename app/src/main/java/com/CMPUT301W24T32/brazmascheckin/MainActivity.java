package com.CMPUT301W24T32.brazmascheckin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.controllers.AdminController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.CMPUT301W24T32.brazmascheckin.views.AttendeeOrganizerHome;

import java.util.ArrayList;

/**
 * This class represents the main activity of the application.
 * It handles the device verification process and redirects users based on their status.
 */

public class MainActivity extends AppCompatActivity {

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
        adminController.getAdmin(deviceID, object -> {
            Intent intent = new Intent(MainActivity.this, AdministratorHome.class);
            startActivity(intent);
            finish();
        }, null);
        userController.getUser(deviceID, object -> {
            Intent intent = new Intent(MainActivity.this, AttendeeOrganizerHome.class);
            startActivity(intent);
            finish();
        }, new GetFailureListener() {
            @Override
            public void onFailure(Exception e) {
                TextView firstNameEditText = findViewById(R.id.main_firstname_textview);
                TextView lastNameEditText = findViewById(R.id.main_lastname_textview);
                Button submitButton = findViewById(R.id.main_submit_button);

                submitButton.setOnClickListener(view -> {
                    String firstName = firstNameEditText.getText().toString();
                    String lastName = lastNameEditText.getText().toString();

                    if(firstName.isEmpty() || lastName.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        User user = new User(
                                firstName, lastName, new ArrayList<String>(), null, null,
                                new ArrayList<String>()
                        );
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        user.setID(deviceID);
                        userController.setUser(user, () -> {
                            Intent intent = new Intent(MainActivity.this, AttendeeOrganizerHome.class);
                            startActivity(intent);
                            //TODO: verify the context works
                            Toast.makeText(getApplicationContext(), "user", Toast.LENGTH_SHORT).show();
                            finish();
                        }, e1 -> {
                            Toast.makeText(getApplicationContext(), "Unable to create profile", Toast.LENGTH_SHORT).show();

                        });
                    }
                });
            }
        });
    }


}