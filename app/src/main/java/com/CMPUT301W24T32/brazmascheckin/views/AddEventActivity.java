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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.AddFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import com.CMPUT301W24T32.brazmascheckin.helper.OrphanedQRCodeFinder;
import com.CMPUT301W24T32.brazmascheckin.helper.QRCodeGenerator;
import com.CMPUT301W24T32.brazmascheckin.helper.QRCodeSpinnerAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {
    // CONSTANTS
    private final int IMG_REQ = 200;

    // Views
    private ImageView imageView;
    private Uri imageUri;
    private EditText editName;
    private EditText editDesc;
    private DatePicker datePicker;
    private EditText editLimit;
    private Button chooseImage;
    private Button addButton;
    private SwitchCompat geoLocationSwitch;
    private LinearLayout geoLocationLinearLayout;
    private TextView addEventLocationTextView;
    private Button chooseLocation;
    private Button addShareQRCode;
    private String deviceID;
    private Spinner qrCodeSpinner;

    // Controllers
    private ImageController imageController;
    private EventController eventController;
    private UserController userController;
    private QRCodeSpinnerAdapter qrCodeAdapter;
    private ArrayList<Bitmap> listOfBitmaps;
    private ArrayList<String> listOfEventIDs;

    private Location location;
    private AutoCompleteTextView autoCompleteTextView;

    //conditional
    private boolean shareQRCodeClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);
        listOfBitmaps = new ArrayList<>();
        listOfEventIDs = new ArrayList<>();
        setContentView(R.layout.activity_add_event);

        // Configuring views and controllers
        qrCodeAdapter = new QRCodeSpinnerAdapter(this, listOfBitmaps, listOfEventIDs);
        configureViews();
        qrCodeChoice();
        configureControllers();
        populateOrphanedQRCodeSpinner();

    }

    /**
     * Setup autocomplete dropdown for QR code options
     */
    private void qrCodeChoice() {
        String[] options = {"Generate new QR code", "Use existing QR code"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, options);
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
        geoLocationSwitch = findViewById(R.id.add_event_geolocation_sw);
        geoLocationLinearLayout = findViewById(R.id.add_event_geolocation_display_ll);
        chooseLocation = findViewById(R.id.add_event_choose_location_btn);
        addEventLocationTextView = findViewById(R.id.add_event_location_tv);
        addShareQRCode = findViewById(R.id.add_event_generate_promo_qr_btn);
        deviceID = DeviceID.getDeviceID(this);
        chooseImage.setOnClickListener(view -> openFileChooser());
        qrCodeSpinner = findViewById(R.id.orphaned_qr_code_spinner);
        autoCompleteTextView = findViewById(R.id.add_event_select_qr_code_actv);

        // prevent choosing date in the past
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());

    }

    /**
     * Function to populate qr code spinner with qr codes not associated with event
     */

    private void populateOrphanedQRCodeSpinner() {
        OrphanedQRCodeFinder qrCodeFinder = new OrphanedQRCodeFinder(imageController, eventController);
        qrCodeFinder.findAndProcessOrphanedQRCodes(orphanedQRCodeFileIDs -> {
            Log.d("tag1", "OrphanedQRCodeSpinner size: "+orphanedQRCodeFileIDs.size());
            for (String fileID : orphanedQRCodeFileIDs) {
                imageController.getImage("QR_CODE", fileID, bytes -> {
                    Log.d("tag1", "file ID: "+fileID);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    listOfEventIDs.add(fileID);
                    listOfBitmaps.add(bitmap);
                    qrCodeAdapter.notifyDataSetChanged();
                    Log.d("tag1", "count of qrcode adapter "+qrCodeAdapter.getCount());
                    qrCodeSpinner.setAdapter(qrCodeAdapter);
                }, e -> {
                    Log.e("failure", "processOrphanesQRCodes: " + e.getMessage());
                });
            }
        });
    }
    /**
     * method to configure controllers
     */
    private void configureControllers() {
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        imageController = new ImageController(FirestoreDB.getStorageInstance());
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        addButton.setOnClickListener(v -> {
            retrieveInput(this);
        });

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(i == 0) {
                qrCodeSpinner.setVisibility(View.GONE);
            } else {
                qrCodeSpinner.setVisibility(View.VISIBLE);
            }
        });

        geoLocationSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                geoLocationLinearLayout.setVisibility(View.VISIBLE);
                chooseLocation.setVisibility(View.VISIBLE);
            } else {
                geoLocationLinearLayout.setVisibility(View.GONE);
                chooseLocation.setVisibility(View.GONE);
            }
        });

        chooseLocation.setOnClickListener(view -> {
            Intent intent = new Intent(AddEventActivity.this, ViewMapActivity.class);
            intent.putExtra(ViewMapActivity.EXTRA_MODE, ViewMapActivity.CHOOSE_LOCATION);
            intent.putExtra(ViewMapActivity.EXTRA_PREV_LOCATION, location);
            viewMapLauncher.launch(intent);
        });

        addShareQRCode.setOnClickListener(view -> {
            shareQRCodeClicked = true;
        });
   }

    /**
     * Method to generate a promotional QR Code for an event
     * @param event event for which promo qr code is being generated
     */

    private void generateShareQRCode(Event event) {
        Bitmap qrCodeBitmap = QRCodeGenerator.generateQRCode(event.getID()+"-SHARE-QRCODE");
        byte[] imageData = QRCodeGenerator.getQRCodeByteArray(qrCodeBitmap);
        String fileID = event.getID()+"-SHARE-QRCODE";
        event.setShareQRCode(fileID);
        imageController.uploadQRCode("SHARE",fileID, imageData, uri -> {
            String QRCodeURI = uri.toString();
        }, e -> {
            Toast.makeText(this, "Unable to store share QR code", Toast.LENGTH_SHORT).show();
        });
    }

    private ActivityResultLauncher<Intent> viewMapLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if(data != null) {
                        int mode = data.getIntExtra(ViewMapActivity.EXTRA_MODE, -1);
                        if(mode == ViewMapActivity.CHOOSE_LOCATION) {
                            location = (Location) data.getSerializableExtra(ViewMapActivity.RESULT_LOCATION);
                            addEventLocationTextView.setText(
                                    location.getLatitude() + " " + location.getLongitude()
                            );
                        } else {
                            // TODO: error handling
                        }
                    }
                }
            }
    );

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

        int limit = -1;
        if (!limitText.isEmpty()) {
            limit = Integer.parseInt(limitText);
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Date date = new Date(day, month, year);
        HashMap<String, Integer> checkIns = new HashMap<>();
        ArrayList<String> signUps = new ArrayList<>();
        String posterID = uploadFile();
        boolean geoLocationEnabled = geoLocationSwitch.isChecked();

        if (title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Enter all text fields", Toast.LENGTH_SHORT).show();
        } if (geoLocationEnabled && location == null) {
            Toast.makeText(context, "Location enabled: Choose Location", Toast.LENGTH_SHORT).show();
        } else {
            String selectedOption = autoCompleteTextView.getText().toString();
            boolean generateNewQRCode = selectedOption.equals("Generate new QR code");

            if(!geoLocationEnabled) {
                location = null;
            }

            Event event = new Event("", title, date,
                    desc, checkIns, signUps,
                    limit, posterID, "",
                    "", "",
                    geoLocationEnabled, location, new HashMap<>(), new ArrayList<Announcement>());
            addEvent(event, generateNewQRCode);
        }
    }


    /**
     * Method to upload selected image file
     * @return file ID generated based on current system time
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
    public void addEvent(Event event, boolean generateNewQRCode) {
        event.setOrganizer(deviceID);

        //choice
        if (generateNewQRCode) {
            // Generate new QR code and add event
            generateAndAddQRCode(event);
        } else {
            // Use existing QR code and add event
            qrCodeSpinner.setVisibility(View.VISIBLE);
            useExistingQRCode(event);
        }

    }

    /**
     * Function that allows user to select a qr code in database not associated with event
     * @param event event for which function is assigning a qr code already in database
     */
    private void useExistingQRCode(Event event) {
        String selectQRCodeFileID = (String) qrCodeSpinner.getSelectedItem();
        String newID = selectQRCodeFileID.substring(0, selectQRCodeFileID.indexOf('-'));
        event.setID(newID);
        event.setQRCode(selectQRCodeFileID);

        //generate share QR code
        if (shareQRCodeClicked) {
            generateShareQRCode(event);
        }
        
        eventController.setEvent(event, () -> userController.getUser(deviceID, user -> {
            user.createEvent(newID);
            userController.setUser(user, null ,null);
        }, null), null);
        finish();
    }

    /**
     * Function to generate a new qr code and add it to the database
     * @param event event for which qr code is being generated
     */
    private void generateAndAddQRCode(Event event) {
        eventController.addEvent(event, ID -> {
            event.setID(ID);

            //generate share QR code
            if (shareQRCodeClicked) {
                generateShareQRCode(event);
            }

            // Generate and upload QR code
            Bitmap bitmap = QRCodeGenerator.generateQRCode(ID);
            byte[] imageData = QRCodeGenerator.getQRCodeByteArray(bitmap);

            String fileID = event.getID() + "-QRCODE";

            imageController.uploadQRCode("CHECK-IN",fileID, imageData, uri -> {
                String QRCodeURI = uri.toString();
                event.setQRCode(fileID);
                eventController.setEvent(event, null, e -> Toast.makeText(AddEventActivity.this, "no qr", Toast.LENGTH_SHORT).show());
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
