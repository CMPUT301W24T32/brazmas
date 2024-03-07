package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.CMPUT301W24T32.brazmascheckin.AttendeeViewEventFragment;
import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.EventArrayAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;
import java.util.HashMap;

public class AttendeeOrganizerHome extends AppCompatActivity implements AddEventFragment.AddEventDialogListener {

    ListView eventList;
    ArrayList<Event> eventDataList;
    EventArrayAdapter eventAdapter;

    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_organizer_home);

        // for testing purposes
        String names = "beebeebooboo";
        String description = "Seminar where you learn to beep";
        Date date = new Date(11,02,2005);

        // new added from event update
        HashMap<String, Integer> checkins = null;
        ArrayList<String> signups = null;
        String ID = "42069";
        int limit  = 1;
        String posterID = "IMG";
        String QRCodeID = "QR";
        String shareQRCodeID = "shQr";
        //how to do date -> not wokring in array class so idk ask -DATE

        eventDataList = new ArrayList<>();

        eventDataList.add(new Event(ID,names, date, description, checkins, signups, limit, posterID, QRCodeID, shareQRCodeID));  //NEED DATE IN MIDDLE


        eventList = findViewById(R.id.allEventList);
        eventAdapter = new EventArrayAdapter(this,eventDataList);
        eventList.setAdapter(eventAdapter);
        addButton = findViewById(R.id.addButton);
        //to access event details by clicking on indivdual item
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = eventDataList.get(position);
                //AttendeeViewEventFragment fragment = AttendeeViewEventFragment.sendEvent(e);
                //fragment.show(getSupportFragmentManager(), "Display Event");
                Intent intent = new Intent(AttendeeOrganizerHome.this, OrganizerEventView.class);
                intent.putExtra("event", e);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(v -> {
            new AddEventFragment().show(getSupportFragmentManager(), "Add Event");
        });
    }

    @Override
    public void addEvent(Event event) {
        eventAdapter.add(event);
        eventAdapter.notifyDataSetChanged();
    }

}