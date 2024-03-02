package com.CMPUT301W24T32.brazmascheckin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.EventArrayAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView eventList;
    ArrayList<Event> eventDataList;
    EventArrayAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String names = "beebeebooboo";
        String description = "Seminar where you learn to beep";
        Date date = new Date(11,02,2005);
        //how to do date -> not wokring in array class so idk ask -DATE

        eventDataList = new ArrayList<>();

        eventDataList.add(new Event(names, date, description));  //NEED DATE IN MIDDLE


        eventList = findViewById(R.id.allEventList);
        eventAdapter = new EventArrayAdapter(this,eventDataList);
        eventList.setAdapter(eventAdapter);



    }
}