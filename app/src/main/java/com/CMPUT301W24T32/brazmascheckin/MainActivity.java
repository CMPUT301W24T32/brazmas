package com.CMPUT301W24T32.brazmascheckin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.CMPUT301W24T32.brazmascheckin.controllers.AdminController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents the main activity of the application.
 * It handles the device verification process and redirects users based on their status.
 */

public class MainActivity extends AppCompatActivity {


    private UserController userController;
    private AdminController adminController;
    private ImageController imageController;
    private String deviceID;
    public ArrayList<Integer> colors;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting device ID and storing it in a string
        deviceID = DeviceID.getDeviceID(this);
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        adminController = new AdminController(FirestoreDB.getDatabaseInstance());
        imageController = new ImageController(FirestoreDB.getStorageInstance());

        colors = new ArrayList<>();

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

            // manually create one
            User user = new User(
                    "John", "Doe", new ArrayList<>(), null, new ArrayList<>(),
                    false, 0, null,
                    null, new ArrayList<>()
            );
            user.setID(deviceID);

            // creating random pfp
            String firstLetter = "J";
            int color = ContextCompat.getColor(getBaseContext(),R.color.black);
            Bitmap bitmap = textAsBitmap(firstLetter,70,color);
            imageUri = getImageUri(getBaseContext(),bitmap);
            user.setDefaultProfilePicture(uploadFile());

            userController.setUser(user, () -> {
                Intent intent = new Intent(MainActivity.this, UserHome.class);
                startActivity(intent);
                finish();
            }, e1 -> {
                Toast.makeText(getApplicationContext(), "Unable to create profile", Toast.LENGTH_SHORT).show();
            });
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
     * Method to upload selected image file
     * @return String of the fileid
     */
    private String uploadFile() {
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