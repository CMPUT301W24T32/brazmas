package com.CMPUT301W24T32.brazmascheckin;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;


import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;

import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.ArrayList;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddEventTests {

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
        userController.setUser(user, null, null);
    }

    @After
    public void cleanUp() {
        userController.getUser(user.getID(), object -> user = object, null);

        ArrayList<String> signedUpEvents = user.getSignedUpEvents();
        ArrayList<String> organizedEvents = user.getOrganizedEvents();

        for (String event : signedUpEvents) {
            eventController.deleteEvent(event, null, null);
        }
        for (String event : organizedEvents) {
            eventController.deleteEvent(event, null, null);
        }
        userController.deleteUser(user, null, null);
    }

    @Test
    public void testShowAllEvents() {
        onView(withId(R.id.user_home_organizing_btn))
                .perform(click());
        onView(withId(R.id.user_home_add_event_btn))
                .perform(click());

        onView(withId(R.id.add_event_name_tv))
                .perform(typeText("Test Event"));
        onView(withId(R.id.add_event_desc_et))
                .perform(typeText("Test Event Description"));
        onView(withId(R.id.add_event_limit_et))
                .perform(typeText("1"));
        onView(withId(R.id.add_event_geolocation_sw))
                .perform(click());
        onView(withId(R.id.add_event_choose_location_btn))
                .perform(click());
        onView(withId(R.id.view_map_done_btn))
                .perform(click());
        onView(withId(R.id.add_event_promo_code_sw))
                .perform(click());
        onView(withId(R.id.add_event_sv))
                .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.add_event_button))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn))
                .perform(click());

        onView(withId(R.id.user_home_organizing_btn));
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.user_home_all_events_rv))
                .check(matches(hasDescendant(withText("Test Event"))));
    }

    @Test
    public void testAddEventWithoutName() {
        onView(withId(R.id.user_home_organizing_btn))
                .perform(click());
        onView(withId(R.id.user_home_add_event_btn))
                .perform(click());

        // TESTING BY NOT ADDING THE EVENT NAME

        onView(withId(R.id.add_event_desc_et))
                .perform(typeText("Test Event Description"));
        onView(withId(R.id.add_event_geolocation_sw))
                .perform(click());
        onView(withId(R.id.add_event_choose_location_btn))
                .perform(click());
        onView(withId(R.id.view_map_done_btn))
                .perform(click());
        onView(withId(R.id.add_event_promo_code_sw))
                .perform(click());
        onView(withId(R.id.add_event_sv))
                .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.add_event_button))
                .perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        //TESTING TO SEE IF EVENT NOT ADDED IF ADD BUTTON STILL VISIBLE
        onView(withId(R.id.add_event_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddEventOptionalLimit() {
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.user_home_organizing_btn))
                .perform(click());
        onView(withId(R.id.user_home_add_event_btn))
                .perform(click());

        onView(withId(R.id.add_event_name_tv))
                .perform(typeText("Test Event"));
        onView(withId(R.id.add_event_desc_et))
                .perform(typeText("Test Event Description"));
        // TEST BY NOT ADDING EVENT DESCRIPTION
//        Espresso.onView(ViewMatchers.withId(R.id.add_event_limit_et))
//                .perform(ViewActions.typeText("1"));
        onView(withId(R.id.add_event_geolocation_sw))
                .perform(click());
        onView(withId(R.id.add_event_choose_location_btn))
                .perform(click());
        onView(withId(R.id.view_map_done_btn))
                .perform(click());
        onView(withId(R.id.add_event_promo_code_sw))
                .perform(click());
        onView(withId(R.id.add_event_sv))
                .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.add_event_button))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn))
                .perform(click());

        onView(withId(R.id.user_home_organizing_btn));
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.user_home_all_events_rv))
                .check(matches(hasDescendant(withText("Test Event"))));
    }

    @Test
    public void testNoPromoView() {
        onView(withId(R.id.user_home_organizing_btn))
                .perform(click());
        onView(withId(R.id.user_home_add_event_btn))
                .perform(click());

        onView(withId(R.id.add_event_name_tv))
                .perform(typeText("Test No Promo Event"));
        onView(withId(R.id.add_event_desc_et))
                .perform(typeText("Test Event Description"));
        onView(withId(R.id.add_event_limit_et))
                .perform(typeText("1"));
        onView(withId(R.id.add_event_sv))
                .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.add_event_button))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.user_home_organizing_btn))
                .perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(ViewMatchers.withText("Test No Promo Event"))
                        .perform(click());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }

        onView(withId(R.id.view_event_sv))
                .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {

        }
        onView(withId(R.id.view_event_share_QR_iv))
                .check(matches(
                        withEffectiveVisibility(ViewMatchers.Visibility.GONE)
                ));
    }


}
