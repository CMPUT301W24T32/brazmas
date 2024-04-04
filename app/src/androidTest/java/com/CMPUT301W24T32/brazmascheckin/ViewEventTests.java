package com.CMPUT301W24T32.brazmascheckin;

import static androidx.test.espresso.assertion.ViewAssertions.matches;

import android.util.Log;
import android.widget.Toast;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.controllers.AddSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ViewEventTests {
    @Rule
    public ActivityScenarioRule<UserHome> scenario = new ActivityScenarioRule<>(UserHome.class);

    private UserController userController;
    private EventController eventController;

    private User user = new User(DeviceID.getDeviceID(
            ApplicationProvider.getApplicationContext()
    ), "test", "device", new ArrayList<>(), false, new ArrayList<>());

    @Before
    public void setUp() {
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        userController.setUser(user, null ,null);
    }

    @After
    public void cleanUp() {
        userController.getUser(user.getID(), object -> user = object, null);

        ArrayList<String> signedUpEvents = user.getSignedUpEvents();
        ArrayList<String> organizedEvents = user.getOrganizedEvents();

        for(String event: signedUpEvents) {
            eventController.deleteEvent(event, null, null);
        }
        for(String event: organizedEvents) {
            eventController.deleteEvent(event, null, null);
        }
        userController.deleteUser(user, null, null);
    }

    @Test
    public void attendBelowLimit() {
        Event mockAttendEvent = new Event(
                null, "Test Attend Event",
                new Date(11, 4, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                user.getID(),
                false,
                null,
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn)).perform(
                ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        Espresso.onView(ViewMatchers.withText("Test Attend Event")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.view_event_signed_up_cb)).perform(ViewActions.click());

        Espresso.pressBack();

        Espresso.onView(ViewMatchers.withId(R.id.user_home_attending_btn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Test Attend Event"))
                .check(matches(ViewMatchers.isDisplayed()));

//        try {
//            Thread.sleep(3000);
//        } catch (Exception e) {
//
//        }
//
//        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn));

        // move to another test
//        Espresso.onView(ViewMatchers.withText("Test Attend Event")).perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withId(R.id.view_event_see_signed_up_attendees_btn))
//                        .perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withText(user.getID())).check(matches(ViewMatchers.isDisplayed()));


        eventController.deleteEvent(mockAttendEvent.getID(), null, null);
    }

    @Test
    public void checkAttendeeList() {
        Event mockAttendEvent = new Event(
                null, "Test Attend Event",
                new Date(11, 4, 2024),
                "Event to test attending",
                new HashMap<>(),
                new ArrayList<>(),
                1,
                "default_poster.png",
                null,
                null,
                user.getID(),
                false,
                null,
                new HashMap<>(),
                new ArrayList<>()
        );


        eventController.addEvent(mockAttendEvent, id -> {
            mockAttendEvent.setID(id);
            eventController.setEvent(mockAttendEvent, null, null);
            ArrayList<String> events = new ArrayList<>();
            events.add(id);
            user.setEvent(events);
            userController.setUser(user, null, null);

        }, null);

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn)).perform(
                ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        Espresso.onView(ViewMatchers.withText("Test Attend Event")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.view_event_signed_up_cb)).perform(ViewActions.click());

        Espresso.pressBack();

        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn));
        Espresso.onView(ViewMatchers.withText("Test Attend Event")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.view_event_see_signed_up_attendees_btn))
                        .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText(user.getID())).check(matches(ViewMatchers.isDisplayed()));


        eventController.deleteEvent(mockAttendEvent.getID(), null, null);
    }
}
