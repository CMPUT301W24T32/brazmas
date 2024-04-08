package com.CMPUT301W24T32.brazmascheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorBrowseProfiles;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.Length;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Test class for deletion of profiles by an administrator.
 * This test ensures that an administrator can successfully delete profiles from the system.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminDeleteProfileTest {

    @Rule
    public ActivityScenarioRule<AdministratorBrowseProfiles> scenario = new ActivityScenarioRule<>(AdministratorBrowseProfiles.class);

    private UserController userController;

    @Before
    public void setUp() {
        userController = new UserController(FirestoreDB.getDatabaseInstance());

        // create mock profile/user
        // list of signed up events
        ArrayList<String> signedUpEvents = new ArrayList<>();  // it can be empty

        // list of checked-in events
        ArrayList<String> checkedInEvents = new ArrayList<>();  // can be empty

        // create mock user
        User mockUser = new User("000000000", "Mock", "User", signedUpEvents, true, checkedInEvents);

        // Add the mock event to Firestore using the EventController
        userController.setUser(mockUser, () -> {
            // Success listener
        }, failure -> {
            // Failure listener
        });
    }

    @Test
    public void testDeleteProfile() {
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withText("Mock User")).perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withText("Yes")).perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        Espresso.onView(withText("Mock User")).check(ViewAssertions.doesNotExist());

    }
}