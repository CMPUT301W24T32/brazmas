
package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.DeleteFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.helper.AdminProfileRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * This class will be the page to browse profiles for admin.
 */
public class AdministratorBrowseProfiles extends AppCompatActivity {

    private ArrayList<User> userDataList;
    private AdminProfileRecyclerViewAdapter profileRecyclerViewAdapter;
    private RecyclerView profileRecyclerView;
    private UserController userController;

    /**
     * This method initializes the browse profiles admin activity.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_profile);

        configureViews();
        configureControllers();

        //TODO: need to add navigation bar.
        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_profile_bnv);
        bottomNavigationView.setSelectedItemId(R.id.admin_profile);
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
                    return true;
                }

                if (id == (R.id.admin_image)){
                    startActivity(new Intent(getApplicationContext(), AdministratorBrowseImages.class));
                    overridePendingTransition(0,0);
                    return true;
                }

                return false;

            }
        });
    }


    /**
     * This method initializes the views, adapters, and models required for the activity.
     */
    private void configureViews() {
        userDataList = new ArrayList<>();

        profileRecyclerViewAdapter = new AdminProfileRecyclerViewAdapter(this, userDataList);
        profileRecyclerView = findViewById(R.id.all_profiles_rv_admin);
        profileRecyclerView.setAdapter(profileRecyclerViewAdapter);
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // deal with this
    }

    /**
     * This method defines the controllers for the views of the activity.
     */
    private void configureControllers() {
        userController = new UserController(FirestoreDB.getDatabaseInstance());

        showAllUsers();

        //TODO: need to implement long click or slide feature to delete a user

        // set click listener for recyclerView items
        profileRecyclerViewAdapter.setOnItemClickListener(position -> {
            User clickedUser = userDataList.get(position);
            showConfirmationDialog(clickedUser);
        });
    }

    private void showAllUsers() {
        userController.addSnapshotListener(new SnapshotListener<User>() {
            @Override
            public void snapshotListenerCallback(ArrayList<User> users) {
                userDataList.clear();
                for (User user : users) {
                    userDataList.add(user);
                }
                profileRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(AdministratorBrowseProfiles.this, "Unable to connect to the " + "database", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void deleteUser(User user) {

        userController.deleteUser(user, () -> {
            // profile deleted successfully
            Toast.makeText(AdministratorBrowseProfiles.this, "Profile deleted", Toast.LENGTH_SHORT).show();
        }, new DeleteFailureListener() {
            @Override
            public void onDeleteFailure(Exception e) {
                // failed to delete event
                Toast.makeText(AdministratorBrowseProfiles.this, "Failed to delete profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this profile?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User confirmed, delete the profile
            deleteUser(user);
        });
        builder.setNegativeButton("No", null); // Do nothing if user cancels
        builder.show();
    }
}
