package com.CMPUT301W24T32.brazmascheckin.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;



/**
 * This class is the fragment for the organizer to send Announcement to signed-up users
 */
public class AddAnnouncementFragment extends DialogFragment {
    private EditText editTitle;
    private EditText editDesc;

    interface AddAnnouncementDialogListener{
        void addAnnouncement(Announcement announcement);
    }

    private AddAnnouncementDialogListener listener;

    /**
     * checking if the activity implements a specific interface so fragment can interact with Activity
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddAnnouncementDialogListener) {
            listener = (AddAnnouncementFragment.AddAnnouncementDialogListener) context;
        } else {
            throw new RuntimeException(context + "must implement AddAnnouncementDialogListener");
        }
    }


    /**
     * This method creates the dialog box and sets all the text.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * if it exists.
     * @return the builder
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_announcement_fragment, null);
        configureViews(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("Send Announcement")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Send", (dialog, which) -> {
                    retrieveInput();
                    Toast.makeText(getContext(), "Announcement Sent", Toast.LENGTH_SHORT).show();

                });

        return builder.create();
    }

    /**
     * This method creates retrives input from fragment and creates new Announcement
     *
     */
    private void retrieveInput() {

        String title = editTitle.getText().toString();
        String desc = editDesc.getText().toString();


        long time = System.currentTimeMillis();
        if(title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(getContext(), "Enter all text fields", Toast.LENGTH_SHORT).show();
        } else {
            listener.addAnnouncement(new Announcement(title, desc, "hello", time));
        }

    }

    /**
     * configures views
     * @param view
     */
    private void configureViews(View view) {
        editDesc = view.findViewById(R.id.add_announcement_desc_et);
        editTitle = view.findViewById(R.id.add_announcement_name_tv);



    }


}
