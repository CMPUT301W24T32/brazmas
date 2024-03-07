package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

public class OrganizerEventView extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_view_event_page);

        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");

        TextView eventName = findViewById(R.id.organizerEventName);
        ImageView qr_code = findViewById(R.id.qr_code);
        if (event.getBitmap() != null) {
            qr_code.setImageBitmap(event.getBitmap().getBitmap());
        }
        else {
            qr_code.setImageResource(R.drawable.sad);
        }
    }
}
