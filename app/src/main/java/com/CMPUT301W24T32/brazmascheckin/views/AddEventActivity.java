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
import android.location.Address;
import android.location.Geocoder;
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
import com.CMPUT301W24T32.brazmascheckin.commands.AddEventCommand;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import com.CMPUT301W24T32.brazmascheckin.helper.OrphanedQRCodeFinder;
import com.CMPUT301W24T32.brazmascheckin.helper.QRCodeSpinnerAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;

import java.util.ArrayList;
import java.util.Calendar;
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
    private SwitchCompat shareQRCodeSwitch;
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

    //etc
    private Geocoder geocoder;

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
        shareQRCodeSwitch = findViewById(R.id.add_event_promo_code_sw);
        deviceID = DeviceID.getDeviceID(this);
        chooseImage.setOnClickListener(view -> openFileChooser());
        qrCodeSpinner = findViewById(R.id.add_event_orphaned_qr_code_spinner);
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
            for (String fileID : orphanedQRCodeFileIDs) {
                imageController.getImage("QR_CODE", fileID, bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    listOfEventIDs.add(fileID);
                    listOfBitmaps.add(bitmap);
                    qrCodeAdapter.notifyDataSetChanged();
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

        shareQRCodeSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                shareQRCodeClicked = true;

            } else {
                shareQRCodeClicked = false;
            }
        });

        geocoder = new Geocoder(this);
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
                            String place;
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                        location.getLongitude(), 1);
                                place = addresses.get(0).getAddressLine(0);
                            } catch (Exception e) {
                                place = location.getLatitude() + " " + location.getLongitude();

                            }
                            addEventLocationTextView.setText(place);
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
        String name = editName.getText().toString();
        String desc = editDesc.getText().toString();
        String limitText = editLimit.getText().toString();

        int attendeeLimit = -1;
        if (!limitText.isEmpty()) {
            attendeeLimit = Integer.parseInt(limitText);
        }

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Date date = new Date(day, month, year);
        boolean geoLocationEnabled = geoLocationSwitch.isChecked();
        String selectedOption = autoCompleteTextView.getText().toString();
        boolean generateNewQRCode = selectedOption.equals("Generate new QR code"); // TODO: make this constant

        if (name.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Enter all text fields", Toast.LENGTH_SHORT).show();
        }
        else if (geoLocationEnabled && location == null) {
            Toast.makeText(context, "Location enabled: Choose Location", Toast.LENGTH_SHORT).show();

        } else if (!generateNewQRCode && qrCodeAdapter.getCount()==0 ) {
            Toast.makeText(context, "no existing qr code, generate new code",Toast.LENGTH_SHORT).show();
        } else {

            if(!geoLocationEnabled) {
                location = null;
            }

            String ID = null;
            String qrCode = null;

            if(!generateNewQRCode) {
                qrCode = (String) qrCodeSpinner.getSelectedItem();
                ID = qrCode.substring(0, qrCode.indexOf('-'));
            }

            AddEventCommand addEventCommand = new AddEventCommand(
                    eventController, imageController, userController,
                    ID, name, desc, attendeeLimit, date,
                    geoLocationEnabled, qrCode,
                    imageUri, shareQRCodeClicked, deviceID,
                    location, this);
            Event e = addEventCommand.execute();
            Intent intent = new Intent(AddEventActivity.this, UserHome.class);
            intent.putExtra("resumed",true);
            Bundle bundle = new Bundle();
            bundle.putSerializable("value", e);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
