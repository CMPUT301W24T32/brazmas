package com.CMPUT301W24T32.brazmascheckin;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;

//TODO: check that mock event is on screen before deleting

/**
 * Test for deletion of event as an administrator.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminDeleteEventTest {

    @Test
    public void testDeleteEvent() {
        // controllers
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        EventController eventController = new EventController(database);

        // retrieve all events
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
        });
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

        Event event = new Event("1", "Test Event", "Test Description", new HashMap<>(), new ArrayList<>(),
                "organizer", true, new HashMap<>());

        // Add the mock event to Firestore using the EventController
        eventController.addEvent(event, eventId -> {
            // Success listener
            launchAdministratorHomeActivity();
        }, failure -> {
            // Failure listener
        });
    }

    /**
     * Launches activities and fragments necessary.
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

            // click on the first item of the RecyclerView
            activity.runOnUiThread(() -> {
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
                    Espresso.onView(withText("Mock Event")).check(ViewAssertions.doesNotExist());
                }
            });
        });
    }
}

