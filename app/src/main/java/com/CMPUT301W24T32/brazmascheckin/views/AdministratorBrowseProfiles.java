package com.CMPUT301W24T32.brazmascheckin.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.helper.AdminProfileRecyclerViewAdapter;

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
        userController = new UserController(this);

        showAllUsers();

        //TODO: need to implement long click or slide feature to delete a user
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
}
