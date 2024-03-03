package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301W24T32.brazmascheckin.models.Event;

public class AddEventFragment extends DialogFragment {
    interface AddEventDialogListener {
        void addEvent(Event event);
        void updateCount();
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
}
