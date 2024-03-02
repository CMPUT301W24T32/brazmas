package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    public EventArrayAdapter(@NonNull Context context, ArrayList<Event> events) {
        super(context,0, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.content, parent, false);
        } else {
            view = convertView;
        }
        Event event = events.get(position);
        TextView eventName = view.findViewById(R.id.nameText);
        TextView eventDescription = view.findViewById(R.id.descriptionText);
        TextView eventDate = view.findViewById(R.id.dateText);

        eventName.setText(event.getName());
        eventDescription.setText(event.getDescription());
        eventDate.setText(event.getDate().getPrettyDate());



        return view;
    }
}
