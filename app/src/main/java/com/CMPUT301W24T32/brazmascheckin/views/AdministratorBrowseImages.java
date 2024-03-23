package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdministratorBrowseImages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_image);
        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_image_bnv);
        bottomNavigationView.setSelectedItemId(R.id.admin_image);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             * This method determines something from navigation bar has been selected or not.
             * @param menuItem The selected item
             * @return True if selected, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == (R.id.admin_event)){
                    startActivity(new Intent(getApplicationContext(), AdministratorHome.class));
                    overridePendingTransition(0,0);
                    return true;
                }

                if (id == (R.id.admin_profile)){
                    startActivity(new Intent(getApplicationContext(), AdministratorBrowseProfiles.class));
                    overridePendingTransition(0,0);
                    return true;
                }

                if (id == (R.id.admin_image)){
                    return true;
                }

                return false;

            }
        });
    }
}
