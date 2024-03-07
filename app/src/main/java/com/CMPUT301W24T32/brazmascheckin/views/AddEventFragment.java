package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * AddEventFragment is a DialogFragment used for adding new events.
 * It includes UI elements for event details input, image selection, and date picking.
 */
public class AddEventFragment extends DialogFragment {
    private ImageView imageView;
    private Uri imageUri;
    private final int IMG_REQ = 200; // TODO: random request number for between-activity communication
    private EditText editName;
    private EditText editDesc;
    private DatePicker datePicker;
    private EditText editLimit;
    private Button chooseImage;

    private StorageReference storageRef;


    /**
     * interface having an addEvent() function that adds an event as an organiser
     */
    interface AddEventDialogListener {
        /**
         *
         * @param event
         */
        void addEvent(Event event);
    }

    private AddEventDialogListener listener;

    /**
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddEventDialogListener) {
            listener = (AddEventDialogListener) context;
        } else {
            throw new RuntimeException(context + "must implement AddEventDialogListener");
        }
    }

    /**
     * function that takes an event as input and passes it to an add event fragment, returning the generated fragment.
     * @param event
     * @return
     */
    public AddEventFragment newInstance(Event event) {
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        AddEventFragment fragment = new AddEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_event_fragment, null);
        configureViews(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("Add an event")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Add", (dialog, which) -> {
                    retrieveInput();
                });

        //TODO: idk how to refactor this
        String[] options = {"Generate new QR code", "Use existing QR code"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, options);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);

        return builder.create();
    }

    /**
     * This method retrieves the input from the view and adds the information to the database.
     */
    private void retrieveInput() {
        String title = editName.getText().toString();
        String desc = editDesc.getText().toString();
        int limit = Integer.parseInt(editLimit.getText().toString());
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() ;
        int year = datePicker.getYear();
        Date date = new Date(day, month, year);
        HashMap<String, Integer> checkIns = new HashMap<String, Integer>();
        ArrayList<String> signUps = new ArrayList<String>();
        String posterID = uploadFile();
        String QRCodeID = "id";
        String shareQRCodeID = "id";
        String id = "1";
        listener.addEvent(new Event(id, title, date, desc, checkIns, signUps, limit, posterID, QRCodeID, shareQRCodeID, ""));
    }

    /**
     * This method configures the views required by the fragment.
     * @param view
     */
    private void configureViews(View view) {
        editDesc = view.findViewById(R.id.add_event_desc_et);
        editName = view.findViewById(R.id.add_event_name_tv);
        datePicker = view.findViewById(R.id.add_event_date_dp);
        editLimit = view.findViewById(R.id.add_event_limit_et);
        chooseImage = view.findViewById(R.id.add_event_choose_image_button);
        imageView = view.findViewById(R.id.add_event_image_view);
        storageRef = FirestoreDB.getStorageReference("uploads");
        chooseImage.setOnClickListener(view1 -> openFileChooser());
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
     * This method uploads an image to the Firebase Storage Database
     * @return ID of the uploaded file
     */
    private String uploadFile() {
        String fileID = String.valueOf(System.currentTimeMillis());

        if(imageUri != null) {
            Log.d("URI", "works");
            StorageReference fileReference = storageRef.child(fileID);

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot ->{
                        Log.d("URI", "success");
                    })
                    .addOnFailureListener(e -> {
                        Log.d("URI", "failure");
                        Log.d("URI", e.toString());
                    });
        } else {
            Toast.makeText(requireContext(), "Unable to" +
                    " upload event poster", Toast.LENGTH_SHORT).show();
            fileID = null;
        }
        return fileID;
    }


    //TODO: replace with ActivityResultLauncher
    /**
     * Deprecated method that needs to be replaced
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
        if(requestCode == IMG_REQ && resultCode == Activity.RESULT_OK && data != null
            && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setTag(imageUri);
        }
    }


}
