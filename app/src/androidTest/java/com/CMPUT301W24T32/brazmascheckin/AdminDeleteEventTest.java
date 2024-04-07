package com.CMPUT301W24T32.brazmascheckin;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.InjectEventSecurityException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static org.mockito.ArgumentMatchers.any;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.instanceOf;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.CMPUT301W24T32.brazmascheckin.controllers.AdminController;
import com.CMPUT301W24T32.brazmascheckin.controllers.DeleteFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.DeleteSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Admin;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AddEventActivity;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.views.MapView;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import androidx.test.filters.LargeTest;

import org.junit.Rule;

/**
 * test for deletion of event as an administrator
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminDeleteEventTest {

    @Test
    public void testEventDeletion() {

        // mock EventController
        EventController eventController = Mockito.mock(EventController.class);

        // create mock event to be deleted
        //Event mockEvent = new Event("event_id_to_be_deleted", "Mock Event", "Mock Description", "Organizer ID");

        // create mock event to be deleted
        Date eventDate = new Date();
        HashMap<String, Integer> emptyMap = new HashMap<>();
        ArrayList<String> signUps = new ArrayList<>();
        Event mockEvent = new Event("event_id", "Mock Event", eventDate, "Mock Description", emptyMap, signUps, 1, null, null, null, "Organizer ID", false, null, null, null);

        // launch AdministratorHome activity, mock event will be displayed
        ActivityScenario<AdministratorHome> activityScenario = ActivityScenario.launch(AdministratorHome.class);

        activityScenario.onActivity(activity -> {
            // verify the event is displayed in the UI
            Espresso.onView(withText("Mock Event")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

            // click on the mock event to view its details
            Espresso.onView(withText("Mock Event")).perform(ViewActions.click());

            // click on the "Delete Event" button to delete the event
            Espresso.onView(withText("Delete Event")).perform(ViewActions.click());

            // wrap the verification inside runOnUiThread
            /*activity.runOnUiThread(() -> {
                // verify that the event deletion is triggered in the controller
                verify(eventController).deleteEvent(any(String.class), any(DeleteSuccessListener.class), any(DeleteFailureListener.class));
            });*/

            // verify that the event is no longer displayed in the UI after deletion
            Espresso.onView(withText("Mock Event")).check(ViewAssertions.doesNotExist());

        });
    }
}
