package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

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
    private Uri imageUri;
    private final int IMG_REQ = 300;
    private UserController userController;
    private ImageController imageController;
    private String deviceID;
    private ArrayList<Integer> colors;

    /**
     * This method initializes the EditProfileActivity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        removeProfileBtn = findViewById(R.id.remove_profile_picture_btn);;
        deviceID = DeviceID.getDeviceID(this);
        doneBtn = findViewById(R.id.done_btn);
        changeProfileBtn.setOnClickListener(view1 -> openFileChooser());
    }

    /**
     * configure controllers for edit profile activity
     */

    public void configureControllers() {
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        imageController = new ImageController(FirestoreDB.getStorageInstance());
        colors = new ArrayList<>();


        userController.getUser(deviceID, user ->{
            String firstNameS = user.getFirstName();
            String lastNameS = user.getLastName();
            firstName.setText(firstNameS);
            lastName.setText(lastNameS);
            if (user.getProfilePicture() != null) {
                displayImage(user.getProfilePicture());
                profilePicture.setContentDescription("user pfp");
            }
            else{
                displayDefaultImage(user.getDefaultProfilePicture());
                profilePicture.setContentDescription("default pfp");
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
                    displayDefaultImage(user.getDefaultProfilePicture());
                    profilePicture.setContentDescription("default pfp");
                }
                },null);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object lock = new Object();
                getInput();
                synchronized (lock){
                    try {
                        lock.wait(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
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

        userController.getUser(deviceID,user -> {
            String old_first = user.getFirstName();
            String first = String.valueOf(old_first.charAt(0));
            String firstNew = String.valueOf(firstNameS.charAt(0));
            if (!first.equals(firstNew)){
                generateNewDefault(firstNameS);
                user.setDefaultProfilePicture(uploadDefaultFile());

            }
            user.setFirstName(firstNameS);
            user.setLastName(lastNameS);

            userController.setUser(user,null,null);
        },null);
    }

    /**
     * generates a new deafult profile picture if the name has changed
     */
    private void generateNewDefault(String firstName) {

        int sage = 0xFF8A9A5B;
        int teal = 0xFF036C5F;
        int light_pink = 0xFFF7CFE5;
        int purple = 0xFFC8A4D4;
        int jade_green = 0xFF2BAF6A;

        colors.add(sage);
        colors.add(teal);
        colors.add(light_pink);
        colors.add(purple);
        colors.add(jade_green);
        // creating random pfp
        String firstLetter = String.valueOf(firstName.charAt(0));
        int color = ContextCompat.getColor(getBaseContext(),R.color.black);
        Bitmap bitmap = textAsBitmap(firstLetter,70,color);
        imageUri = getImageUri(getBaseContext(),bitmap);
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
                userController.setUser(user,null,
                        e -> Toast.makeText(EditProfileActivity.this, "Unable to connect to the " + "database", Toast.LENGTH_LONG).show());

            profilePicture.setImageURI(imageUri);
            profilePicture.setTag(imageUri);
        }
        },
                e -> Toast.makeText(EditProfileActivity.this, "Unable to connect to the " + "database", Toast.LENGTH_LONG).show());
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
            }, e -> Toast.makeText(EditProfileActivity.this, "Unable to connect to the " + "database", Toast.LENGTH_LONG).show());
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
            }, e -> Toast.makeText(EditProfileActivity.this, "Unable to connect to the " + "database", Toast.LENGTH_LONG).show());
        }
    }

    /**
     * converts text into  a bitmap
     * used from stack overflow from user Ted Hopp
     * @param text text to convert
     * @param textSize the size of the text wanted
     * @param textColor the color wanted for the text
     * @return the Bitmap
     */
    public Bitmap textAsBitmap(String text, float textSize, int textColor) {

        Random random = new Random();
        int randomIndex = random.nextInt(colors.size());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);

        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 60); // round
        int height = (int) (baseline + paint.descent() + 40);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        int color = colors.get(randomIndex);
        canvas.drawColor(color);
        canvas.drawText(text, 30, baseline + 20, paint);
        return image;
    }

    /**
     * converts the Bitmap into an image Uri
     * used from stack overflow from user Ajay
     * @param inContext the current context
     * @param inImage the bitmap
     * @return the image uri
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Method to upload selected default profile picture
     * @return String of the fileid
     */
    private String uploadDefaultFile() {
        imageController = new ImageController(FirestoreDB.getStorageInstance());
        String fileID = String.valueOf(System.currentTimeMillis());

        if (imageUri != null) {
            imageController.uploadImage(ImageController.DEFAULT_PROFILE_PICTURE_PATH, fileID, imageUri,
                    object -> Toast.makeText(this, "Image uploaded!", Toast.LENGTH_SHORT).show(), e -> {
                    });
        } else {
            fileID = null;
        }
        return fileID;
    }


}