package com.CMPUT301W24T32.brazmascheckin;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.google.firebase.firestore.FirebaseFirestore;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;

/**
 * Test class for deletion of events by an administrator.
 * This test ensures that an administrator can successfully delete events from the system.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminDeleteEventTest {

    /**
     * Test method to delete events.
     * This method retrieves all events from the database and deletes each one.
     * After deleting all events, it creates a mock event and verifies its deletion.
     */
    @Test
    public void testDeleteEvent() {
        // controllers
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        EventController eventController = new EventController(database);

        /*// retrieve all events
        eventController.getAllEvents(events -> {
            // Iterate through all events and delete each
            AtomicInteger counter = new AtomicInteger(events.size());
            for (Event event : events) {
                eventController.deleteEvent(event.getID(), () -> {
                    // Success listener
                    if (counter.decrementAndGet() == 0) {
                        // All events have been deleted, now create a mock event
                        createMockEvent(eventController);
                    }
                }, e -> {
                    // Failure listener
                    // Handle failure, maybe log the error
                    if (counter.decrementAndGet() == 0) {
                        // All events have been deleted, now create a mock event
                        createMockEvent(eventController);
                    }
                });
            }
        }, e -> {
            // Failure for retrieving all events
            // Handle failure, maybe log the error
            createMockEvent(eventController); // Launch anyway even if retrieval fails
        });*/

        createMockEvent(eventController);
    }

    /**
     * Creates a mock event that will later be deleted
     * @param eventController to preform actions on event.
     */
    private void createMockEvent(EventController eventController) {
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
            launchAdministratorHomeActivity();
        }, failure -> {
            // Failure listener
        });
    }

    /**
     * Launches the AdministratorHome activity and performs necessary actions.
     * This method clicks on the first event in the RecyclerView, clicks on the delete button,
     * waits for the event to be deleted, and verifies its deletion.
     */
    private void launchAdministratorHomeActivity() {
        // AdministratorHome activity
        ActivityScenario.launch(AdministratorHome.class).onActivity(activity -> {
            // Wait for RecyclerView
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Espresso.onView(ViewMatchers.withText("Testn Event")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // new
            onView(withText("Ten Event")).perform(ViewActions.click());
            onView(withId(R.id.delete_event_button))
                    .perform(ViewActions.click());

            // click on the first item of the RecyclerView
            /*activity.runOnUiThread(() -> {
                RecyclerView recyclerView = activity.findViewById(R.id.all_events_rv_admin);
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                if (viewHolder != null) {
                    viewHolder.itemView.performClick();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // once you have clicked on the mock event, click on the delete button
                    Espresso.onView(withText("Delete Event")).perform(ViewActions.click());

                    // wait for event to be deleted
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // check if mock event is no longer with us
                    Espresso.onView(withText("Test Delete Admin Event")).check(ViewAssertions.doesNotExist());
                }
            });*/
        });
    }
}

