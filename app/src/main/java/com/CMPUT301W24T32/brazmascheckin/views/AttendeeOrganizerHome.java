package com.CMPUT301W24T32.brazmascheckin.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.CMPUT301W24T32.brazmascheckin.AttendeeViewEventFragment;
import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.EventArrayAdapter;
import com.CMPUT301W24T32.brazmascheckin.helper.EventRecyclerViewAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class will be the home page for attendee/organizer
 */

public class AttendeeOrganizerHome extends AppCompatActivity {
    ArrayList<Event> eventDataList;
    EventRecyclerViewAdapter eventRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_organizer_home);

        // for testing purposes
        String names = "beebeebooboo";
        String description = "Seminar where you learn to beep";
        Date date = new Date(11, 02, 2005);

        // new added from event update
        HashMap<String, Integer> checkins = null;
        ArrayList<String> signups = null;
        String ID = "42069";
        int limit = 1;
        String posterID = "IMG";
        String QRCodeID = "QR";
        String shareQRCodeID = "shQr";
        //how to do date -> not working in array class so idk ask -DATE

        eventDataList = new ArrayList<>();
        eventDataList.add(new Event(ID, names, date, description, checkins, signups, limit, posterID, QRCodeID, shareQRCodeID));  //NEED DATE IN MIDDLE

        // for testing purposes
        String names1 = "EVENT NAME";
        String description1 = "EVENT DESCRIPTION";
        Date date1 = new Date(10, 02, 2050);

        // new added from event update
        HashMap<String, Integer> checkins1 = null;
        ArrayList<String> signups1 = null;
        String ID1 = "11111";
        int limit1 = 1;
        String posterID1 = "IMG";
        String QRCodeID1 = "QR";
        String shareQRCodeID1 = "shQr";
        //how to do date -> not working in array class so idk ask -DATE

        eventDataList.add(new Event(ID1, names1, date1, description1, checkins1, signups1, limit1, posterID1, QRCodeID1, shareQRCodeID1));  //NEED DATE IN MIDDLE

        RecyclerView recyclerView = findViewById(R.id.all_events);
        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(this, eventDataList);

        recyclerView.setAdapter(eventRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // notify the adapter of the change
        //eventRecyclerViewAdapter.notifyDataSetChanged();

        // set onClickListener for each item maybe?
        eventRecyclerViewAdapter.setOnItemClickListener(new EventRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Event clickedEvent = eventDataList.get(position);
                AttendeeViewEventFragment fragment = AttendeeViewEventFragment.sendEvent(clickedEvent);
                fragment.show(getSupportFragmentManager(), "Display Event");
            }
        });
    }
}