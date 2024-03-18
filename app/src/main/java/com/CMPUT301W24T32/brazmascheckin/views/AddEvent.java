/**
 * This class represents the activity responsible for adding events.
 * It allows users to input event details such as name, description, date, and image.
 * Events are added to the database along with a generated QR code.
 */
package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.AddFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.QRCodeGenerator;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;
import java.util.HashMap;

public class AddEvent extends Activity {

    // Views
    private ImageView imageView;
    private Uri imageUri;
    private final int IMG_REQ = 200;
    private EditText editName;
    private EditText editDesc;
    private DatePicker datePicker;
    private EditText editLimit;
    private Button chooseImage;
    private Button addButton;
    private String deviceID;

    // Controllers
    private ImageController imageController;
    private EventController eventController;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        // Configuring views and controllers
        configureViews();
        configureControllers();

        // Setup autocomplete dropdown for QR code options
        String[] options = {"Generate new QR code", "Use existing QR code"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, options);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setText(options[0]);
        autoCompleteTextView.setAdapter(adapter);
    }

    /**
     * method to configure views
     */
    private void configureViews() {
        editDesc = findViewById(R.id.add_event_desc_et);
        editName = findViewById(R.id.add_event_name_tv);
        datePicker = findViewById(R.id.add_event_date_dp);
        editLimit = findViewById(R.id.add_event_limit_et);
        chooseImage = findViewById(R.id.add_event_choose_image_button);
        imageView = findViewById(R.id.add_event_image_view);
        addButton = findViewById(R.id.add_event_button);
        deviceID = DeviceID.getDeviceID(this);
        chooseImage.setOnClickListener(view -> openFileChooser());
    }

    /**
     * method to configure controllers
     */
    private void configureControllers() {
        eventController = new EventController(this);
        imageController = new ImageController(this);
        userController = new UserController(this);
        addButton.setOnClickListener(v -> {
            retrieveInput(this);
        });
    }

    /**
     * Method to open file chooser for image selection
     */
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQ);
    }

    /**
     *
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ && resultCode == Activity.RESULT_OK && data != null
                && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setTag(imageUri);
        }
    }

    /**
     * This method retrieves user input and adds an event to the database.
     * @param context The context of the activity.
     */
    public void retrieveInput(Context context) {
        String title = editName.getText().toString();
        String desc = editDesc.getText().toString();
        String limitText = editLimit.getText().toString();
        int limit = 0;
        if (!limitText.isEmpty() && limitText != null) {
            limit = Integer.parseInt(limitText);
            if (limit < 0) {
                limit = 0;
            }
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Date date = new Date(day, month, year);
        HashMap<String, Integer> checkIns = new HashMap<>();
        ArrayList<String> signUps = new ArrayList<>();
        String posterID = uploadFile();
        String QRCodeID = "id";
        String shareQRCodeID = "id";
        String id = "1";
        if (title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Enter all text fields", Toast.LENGTH_SHORT).show();
        } else {
            Event event = new Event(id, title, date, desc, checkIns, signUps, limit, posterID, QRCodeID, shareQRCodeID, "");
            addEvent(event);
        }
    }

    /**
     * Method to upload selected image file
     * @return
     */
    private String uploadFile() {
        String fileID = String.valueOf(System.currentTimeMillis());

        if (imageUri != null) {
            imageController.uploadImage(ImageController.EVENT_POSTER, fileID, imageUri,
                    object -> Toast.makeText(this, "Image uploaded!", Toast.LENGTH_SHORT).show(), e -> {
                    });
        } else {
            fileID = null;
        }
        return fileID;
    }

    /**
     * This method adds an event to the database along with a generated QR code.
     * @param event The event to be added.
     */
    public void addEvent(Event event) {
        event.setOrganizer(deviceID);
        eventController.addEvent(event, ID -> {
            event.setID(ID);

            // Generate and upload QR code
            Bitmap bitmap = QRCodeGenerator.generateQRCode(ID);
            byte[] imageData = QRCodeGenerator.getQRCodeByteArray(bitmap);

            String fileID = event.getID() + "-QRCODE";

            imageController.uploadQRCode(fileID, imageData, uri -> {
                String QRCodeURI = uri.toString();
                event.setQRCode(fileID);
                eventController.setEvent(event, null, null);
            }, e -> {
                Toast.makeText(this, "Unable to store QR code", Toast.LENGTH_SHORT).show();
            });

            // Update user information and finish activity
            userController.getUser(deviceID, user -> {
                user.createEvent(ID);
                userController.setUser(user, null, null);
            }, null);
            finish();
        }, new AddFailureListener() {
            @Override
            public void onAddFailure(Exception e) {
                // Handle failure
            }
        });
    }
}