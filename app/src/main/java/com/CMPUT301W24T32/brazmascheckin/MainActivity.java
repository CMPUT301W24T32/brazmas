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
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.CMPUT301W24T32.brazmascheckin.views.AttendeeOrganizerHome;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;

/**
 * This class represents the main activity of the application.
 * It handles the device verification process and redirects users based on their status.
 */

public class MainActivity extends AppCompatActivity {

    private UserController userController;
    private AdminController adminController;
    private ImageController imageController;
    private String deviceID;
    private Uri imageUri;

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
                                firstName, lastName, new ArrayList<String>(), null, null,null,
                                new ArrayList<String>(), false, new ArrayList<String>()
                        );
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        user.setID(deviceID);

                        // creating random pfp
                        String firstLetter = String.valueOf(firstName.charAt(0));
                        int color = ContextCompat.getColor(getBaseContext(),R.color.darkGrey);
                        Bitmap bitmap = textAsBitmap(firstLetter,45,color);
                        imageUri = getImageUri(getBaseContext(),bitmap);
                        user.setDefaultProfilePicture(uploadFile());


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

    /**
     * converts text into  a bitmap
     * used from stack overflow from user Ted Hopp
     * @param text text to convert
     * @param textSize the size of the text wanted
     * @param textColor the color wanted for the text
     * @return the Bitmap
     */
    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
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
     * Method to upload selected image file
     * @return String of the fileid
     */
    private String uploadFile() {
        imageController = new ImageController(this);
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


}