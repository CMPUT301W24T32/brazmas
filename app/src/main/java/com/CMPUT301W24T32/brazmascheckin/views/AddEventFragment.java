package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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


import java.util.ArrayList;
import java.util.HashMap;

public class AddEventFragment extends DialogFragment {
    private ImageView imageView;
    private Uri imageUri;
    private final int IMG_REQ = 200;


    interface AddEventDialogListener {
        void addEvent(Event event);
    }

    private AddEventDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddEventDialogListener) {
            listener = (AddEventDialogListener) context;
        } else {
            throw new RuntimeException(context + "must implement AddEventDialogListener");
        }
    }

    public AddEventFragment newInstance(Event event) {
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        AddEventFragment fragment = new AddEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_event_fragment, null);

        //TODO: create method to configure views
        EditText editName = view.findViewById(R.id.editTextName);
        EditText editDesc = view.findViewById(R.id.editTextDesc);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        EditText editLimit = view.findViewById(R.id.editTextLimit);
        Button chooseImage = view.findViewById(R.id.add_event_choose_image_button);
        imageView = view.findViewById(R.id.add_event_image_view);

        chooseImage.setOnClickListener(view1 -> openFileChooser());

        //TODO: create method to build AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("Add a event")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = editName.getText().toString();
                    String desc = editDesc.getText().toString();
                    int limit = Integer.parseInt(editLimit.getText().toString());
                    int day = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth();
                    int year = datePicker.getYear();
                    Date date = new Date(day, month, year);
                    HashMap<String, Integer> checkIns = new HashMap<String, Integer>();
                    ArrayList<String> signUps = new ArrayList<String>();
                    Uri posterURI = (Uri) imageView.getTag();
                    String posterID = "id";
//                    Toast.makeText(requireContext(), getFileExtension(posterURI), Toast.LENGTH_SHORT).show();
                    String QRCodeID = "id";
                    String shareQRCodeID = "id";
                    String id = "1";
                    listener.addEvent(new Event(id, title, date, desc, checkIns, signUps, limit, posterID, QRCodeID, shareQRCodeID));
                });
        String[] options = {"Generate new QR code", "Use existing QR code"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, options);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);

        return builder.create();
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == IMG_REQ && resultCode == Activity.RESULT_OK && data != null
            && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageView.setTag(imageUri);
        }
    }

//    private String getFileExtension(Uri uri) {
//        ContentResolver resolver = requireContext().getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(resolver.getType(uri));
//    }
}
