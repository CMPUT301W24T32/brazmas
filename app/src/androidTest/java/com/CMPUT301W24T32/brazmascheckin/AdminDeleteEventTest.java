/*
package com.CMPUT301W24T32.brazmascheckin;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;
import com.google.firebase.firestore.FirebaseFirestore;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminDeleteEventTest {

    @Rule
    public ActivityScenarioRule<AdministratorHome> scenario = new ActivityScenarioRule<>(AdministratorHome.class);
    private EventController eventController;

    @Before
    public void setUp() {
        eventController = new EventController(FirestoreDB.getDatabaseInstance());

        // create a mock event
        HashMap<String, Integer> checkIns = new HashMap<>();
        ArrayList<String> signUps = new ArrayList<>();
        ArrayList<Announcement> announcements = new ArrayList<>();

        Event mockEvent = new Event(
                "00000000000", "Test Delete Admin Event",
                new Date(11, 11, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                DeviceID.getDeviceID(ApplicationProvider.getApplicationContext()),
                false,
                null,
                new HashMap<>(),
                new ArrayList<>()
        );

        // Add the mock event to Firestore using the EventController
        eventController.setEvent(mockEvent, () -> {
            // Success listener
        }, failure -> {
            // Failure listener
        });

    }

    @Test
    public void testDeleteEvent() {
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withText("Test Delete Admin Event")).perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.delete_event_button)).perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        Espresso.onView(withText("Test Delete Admin Event")).check(ViewAssertions.doesNotExist());

    }
}
*/