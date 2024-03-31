package com.CMPUT301W24T32.brazmascheckin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.CMPUT301W24T32.brazmascheckin.controllers.AdminController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.CMPUT301W24T32.brazmascheckin.views.SignUpActivity;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;

import java.io.ByteArrayOutputStream;
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
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        adminController = new AdminController(FirestoreDB.getDatabaseInstance());

        // control flow of different types of users
        adminController.getAdmin(deviceID, object -> {
            Intent intent = new Intent(MainActivity.this, AdministratorHome.class);
            startActivity(intent);
            finish();
        }, null);
        userController.getUser(deviceID, object -> {
            Intent intent = new Intent(MainActivity.this, UserHome.class);
            startActivity(intent);
            finish();
        }, e -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }

}