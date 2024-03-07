package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == (R.id.bottom_announcement)){
                    startActivity(new Intent(getApplicationContext(), AnnouncementActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_camera)){
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_profile)){
                    return true;
                }
                if (id == (R.id.bottom_event)){
                    startActivity(new Intent(getApplicationContext(), AttendeeOrganizerHome.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;

            }
        });

    }
}