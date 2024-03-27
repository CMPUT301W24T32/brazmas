package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.AnnouncementRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Announcement activity is made for testing navigation
 */

public class AnnouncementActivity extends AppCompatActivity {

    /**
     * variables
     */
    private RecyclerView recyclerView;
    private AnnouncementRecyclerViewAdapter adapter;
    /**
     *This method initializes the Announcement activity.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcement);

        // setting recycler view
        recyclerView = findViewById(R.id.announcement_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Allows the app to switch between activities
        BottomNavigationView bottomNavigationView = findViewById(R.id.announcement_bnv);
        bottomNavigationView.setSelectedItemId(R.id.bottom_announcement);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             * This method determines something from navigation bar has been selected or not.
             * @param menuItem The selected item
             * @return True if selected, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == (R.id.bottom_announcement)){
                    return true;
                }
                if (id == (R.id.bottom_camera)){
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_profile)){
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (id == (R.id.bottom_event)){
                    startActivity(new Intent(getApplicationContext(), UserHome.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;

            }
        });

        //end baab
    }
}