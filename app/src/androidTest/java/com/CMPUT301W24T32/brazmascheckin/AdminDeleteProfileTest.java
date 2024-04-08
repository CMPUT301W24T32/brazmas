package com.CMPUT301W24T32.brazmascheckin;

import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorBrowseProfiles;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.Length;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: OpenAI
 * Tool: ChatGPT
 * Prompt: "Please help me create a mock event."
 * Date: April 4, 2024
 */

/**
 * Test class for deletion of profiles by an administrator.
 * This test ensures that an administrator can successfully delete profiles from the system.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminDeleteProfileTest {

    /**
     * Test method to delete profiles.
     * This method retrieves all profiles from the database and deletes each one.
     * After deleting all profiles, it creates a mock user profile and verifies its deletion.
     */
    @Test
    public void testDeleteProfileAdmin() {

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

    /**
     * Creates a mock user that will later be deleted.
     * @param userController to preform actions on user.
     */
    private void createMockUser(UserController userController) {
        // create mock user

        // list of signed up events
        ArrayList<String> signedUpEvents = new ArrayList<>();  // it can be empty

        // list of checked-in events
        ArrayList<String> checkedInEvents = new ArrayList<>();  // can be empty

        // create mock user
        User mockUser = new User("123456", "John", "Doe", signedUpEvents, true, checkedInEvents);

        // add mock user to Firestore using UserController
        userController.addUser(mockUser, userID -> {
            // success listener
            launchAdministratorBrowseProfiles();
        }, failure -> {
            // failure listener
        });
    }

    /**
     * Launches the AdministratorBrowseProfiles activity and performs necessary actions.
     * This method clicks on the first profile in the RecyclerView, clicks on the delete button,
     * waits for the profile to be deleted, and verifies its deletion.
     */
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
                    Espresso.onView(withText("Yes")).perform(ViewActions.click());

                    // wait for profile to be deleted
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // check if mock user is no longer with us
                    Espresso.onView(withText("John Doe")).check(ViewAssertions.doesNotExist());

                }
            });
        });
    }
}
