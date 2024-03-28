package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
/**
 * Activity for users to edit pfp and name
 */
public class EditProfileActivity extends AppCompatActivity {
    private ImageView profilePicture;
    private FloatingActionButton changeProfileBtn;
    private FloatingActionButton removeProfileBtn;
    private Button doneBtn;
    private EditText firstName;
    private EditText lastName;
    private StorageReference storageRef;
    private Uri imageUri;
    private final int IMG_REQ = 300;
    private UserController userController;
    private ImageController imageController;
    private String deviceID;

    /**
     * This method initializes the EditProfileActivity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        configureViews();
        configureControllers();


    }
    /**
     * configures the views for edit profile activity
     */
    public void configureViews() {
        profilePicture = findViewById(R.id.profile_picture_edit);
        firstName = findViewById(R.id.first_name_et);
        lastName = findViewById(R.id.last_name_et);
        changeProfileBtn = findViewById(R.id.change_profile_picture_btn);
        removeProfileBtn = findViewById(R.id.remove_profile_picture_btn);
        storageRef = FirestoreDB.getStorageReference("uploads");
        deviceID = DeviceID.getDeviceID(this);
        doneBtn = findViewById(R.id.done_btn);
        changeProfileBtn.setOnClickListener(view1 -> openFileChooser());
    }

    /**
     * configure controllers for edit profile activity
     */

    public void configureControllers() {
        userController = new UserController(this);
        imageController = new ImageController(this);


        userController.getUser(deviceID, user ->{
            String firstNameS = user.getFirstName();
            String lastNameS = user.getLastName();
            firstName.setText(firstNameS);
            lastName.setText(lastNameS);
            if (user.getProfilePicture() != null) {
                displayImage(user.getProfilePicture());
            }
            else{
                displayImage(user.getDefaultProfilePicture());
            }

        },null);
        removeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userController.getUser(deviceID, user -> {
                    if (user.getProfilePicture() == null) {
                        Toast.makeText(getBaseContext(), "cannot remove default profile picture",Toast.LENGTH_LONG).show();
                    } else {
                    user.setProfilePicture(null);
                    userController.setUser(user,null,null);
                    displayImage(user.getDefaultProfilePicture());
                }
                },null);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }

    /**
     * gets the input on done button clicked and sets the values changed into the user
     */
    public void getInput(){
        String firstNameS = String.valueOf(firstName.getText());
        String lastNameS = String.valueOf(lastName.getText());
        String posterID = uploadFile();

        userController.getUser(deviceID,user -> {
            user.setFirstName(firstNameS);
            user.setLastName(lastNameS);
            if (posterID != null) {
                user.setProfilePicture(posterID);
            }

            userController.setUser(user,null,null);
        },null);
    }

    /**
     * This method opens the device storage to retrieve a file.
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQ);
    }

    /**
     * This method stores the picture in the uploads collection in the database
     *
     * @return
     */
    private String uploadFile() {
        String fileID = String.valueOf(System.currentTimeMillis());

        if (imageUri != null) {
            imageController.uploadImage(ImageController.PROFILE_PICTURE, fileID, imageUri,
                    object -> Toast.makeText(this, "Image uploaded!", Toast.LENGTH_SHORT).show(), e -> {
                    });
        } else {
            fileID = null;
        }
        return fileID;
    }

    /**
     * method to retrieve imageUri
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        userController.getUser(deviceID,user-> {
        if (requestCode == IMG_REQ && resultCode == Activity.RESULT_OK && data != null
                && data.getData() != null && user.getProfilePicture() == null) {
            imageUri = data.getData();
                //so that the profile can be removed after adding a new one
                String posterID = uploadFile();
                user.setProfilePicture(posterID);
                userController.setUser(user,null,null);

            profilePicture.setImageURI(imageUri);
            profilePicture.setTag(imageUri);
        }
        },null);
    }

    /**
     * This method retrieves the poster image from the database and displays it in the view.
     * @param posterID the ID of the image in the database
     */
    private void displayImage(String posterID) {
        if (posterID != null) {
            //Toast.makeText(getBaseContext(), "Unable to connect to the " + posterID, Toast.LENGTH_LONG).show();
            imageController.getImage(ImageController.PROFILE_PICTURE, posterID, bytes -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePicture.setImageBitmap(bitmap);
            }, null);
        }
    }


}