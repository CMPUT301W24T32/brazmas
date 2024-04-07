package com.CMPUT301W24T32.brazmascheckin;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;

import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorBrowseProfiles;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminDeleteProfileTest {

    @Test
    public void deleteProfileAdmin() {

        // controllers
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        UserController userController = new UserController(database);

        // retrieve all profiles
        userController.getAllUsers(users -> {
            // iterate through all users and delete each
            AtomicInteger counter = new AtomicInteger(users.size());
            for (User user : users) {
                userController.deleteUser(user, () -> {
                    // success listener
                    if (counter.decrementAndGet() == 0) {
                        // all users have been deleted, now create mock user
                        createMockUser(userController);
                    }
                }, e -> {
                    // failure listener
                    if (counter.decrementAndGet() == 0) {
                        // all users have been deleted, now create mock user
                        createMockUser(userController);
                    }
                });
            }
        }, e -> {
            // failure for getting all users
            createMockUser(userController); // launch anyway, perseverance
        });
    }

    private void createMockUser(UserController userController) {
        // create mock user

        // list of signed up events
        ArrayList<String> signedUpEvents = new ArrayList<>();  // it can be empty

        // list of checked-in events
        ArrayList<String> checkedInEvents = new ArrayList<>();  // can be empty

        // create mock user
        User mockUser = new User("123456", "John", "Doe", signedUpEvents, true, checkedInEvents);

    }

    private void launchAdministratorBrowseProfiles() {
        // AdministratorBrowseProfiles activity
        ActivityScenario.launch(AdministratorBrowseProfiles.class).onActivity(activity -> {
            // wait for recyclerView
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // click on the first item of the recyclerView
            activity.runOnUiThread(() -> {
                RecyclerView recyclerView = activity.findViewById(R.id.all_profiles_rv_admin);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                if (viewHolder != null) {
                    viewHolder.itemView.performClick();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // clicked on profile, then click yes to delete

                }
            });
        });
    }

}
