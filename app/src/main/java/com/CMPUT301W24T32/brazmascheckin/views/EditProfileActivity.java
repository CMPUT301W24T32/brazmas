package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView profilePicture;
    private FloatingActionButton changeProfileBtn;
    private Button doneBtn;
    private EditText firstName;
    private EditText lastName;
    private StorageReference storageRef;
    private Uri imageUri;
    private final int IMG_REQ = 300;
    private DocumentReference userDoc;
    private CollectionReference usersRef;
    private UserController userController;
    private String deviceID;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        configureViews();
        configureControllers();


    }

    public void configureViews() {
        profilePicture = findViewById(R.id.profile_picture_edit);
        firstName = findViewById(R.id.first_name_et);
        lastName = findViewById(R.id.last_name_et);
        changeProfileBtn = findViewById(R.id.change_profile_picture_btn);
        storageRef = FirestoreDB.getStorageReference("uploads");
        deviceID = DeviceID.getDeviceID(this);
        usersRef = FirestoreDB.getUsersRef();
        userDoc = usersRef.document(deviceID);
        doneBtn = findViewById(R.id.done_btn);
        changeProfileBtn.setOnClickListener(view1 -> openFileChooser());
    }

    public void configureControllers() {
        userController = new UserController(this);

        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            String firstNameS = user.getFirstName();
            String lastNameS = user.getLastName();
            firstName.setText(firstNameS);
            lastName.setText(lastNameS);
        });
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }
    public void getInput(){
        String firstNameS = String.valueOf(firstName.getText());
        String lastNameS = String.valueOf(lastName.getText());
        String posterID = uploadFile();

        userController.getUser(deviceID,user -> {
            user.setFirstName(firstNameS);
            user.setLastName(lastNameS);
            user.setProfilePicture(posterID);
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
            Log.d("URI", "works");
            StorageReference fileReference = storageRef.child(fileID);

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d("URI", "success");
                    })
                    .addOnFailureListener(e -> {
                        Log.d("URI", "failure");
                        Log.d("URI", e.toString());
                    });
        } else {
//            Toast.makeText(requireContext(), "Unable to" +
//                    " upload event poster", Toast.LENGTH_SHORT).show();
            fileID = null;
        }
        return fileID;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ && resultCode == Activity.RESULT_OK && data != null
                && data.getData() != null) {
            imageUri = data.getData();
            profilePicture.setImageURI(imageUri);
            profilePicture.setTag(imageUri);
        }
    }


}