package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.SerialBitmap;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class AddEventFragment extends DialogFragment {

    private boolean shouldGenQR;
    private Event newEvent;
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
        EditText editName = view.findViewById(R.id.editTextName);
        EditText editDesc = view.findViewById(R.id.editTextDesc);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        EditText editLimit = view.findViewById(R.id.editTextLimit);
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
                    String posterID = "id";
                    String QRCodeID = "id";
                    String shareQRCodeID = "id";
                    String eventID = "1";
                    if (shouldGenQR) {
                        SerialBitmap bitmap = new SerialBitmap(generateQRCode(eventID));
                        newEvent.setBitmap(bitmap);
                    }
                    newEvent = new Event(eventID, title, date, desc, checkIns, signUps, limit, posterID, QRCodeID, shareQRCodeID);
                    listener.addEvent(newEvent);
                });
        String[] options = {"Generate new QR code", "Use existing QR code"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, options);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                if (selectedItem.equals("Generate new QR code")) {
                    // Generate QR code based on event ID
                    boolean shouldGenQR = true;
                }
            }
        });
        return builder.create();
    }

    public Bitmap generateQRCode(String eventID) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(eventID, BarcodeFormat.QR_CODE, 300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
}
