package com.CMPUT301W24T32.brazmascheckin;


import static android.app.PendingIntent.getActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.google.common.base.CharMatcher.is;

import static org.hamcrest.CoreMatchers.instanceOf;
import static java.util.function.Predicate.not;

import android.app.Activity;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.views.AttendeeOrganizerHome;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * tests to control the event organizer
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class EventsOrganizerTest {

    private View decorView;
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);
    /*
    @Rule
    public ActivityScenarioRule<AttendeeOrganizerHome> mActivityScenarioRule =
            new ActivityScenarioRule<AttendeeOrganizerHome>(AttendeeOrganizerHome.class);
            */

    /**
     * test adds a new event
     */
    @Test
    public void eventsOrganizerTest() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * test to check filtering of events
     */
    @Test
    public void attendingFilterTest(){
        // hardcoded for a specific event
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("BleeBloo Event")).perform(ViewActions.click());
        onView(withId(R.id.signed_up_CB)).perform(ViewActions.click());
        onView(withText("Back")).perform(ViewActions.click());
        onView(withId(R.id.user_home_attending_btn)).perform(click());
        onView(withText("BleeBloo Event")).perform(ViewActions.click());
        onView(withId(R.id.signed_up_CB)).perform(ViewActions.click());
    }

    @Test
    public void organizingFilterTest(){
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(ViewMatchers.withId(R.id.user_home_add_event_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_name_tv))
                .perform(ViewActions.typeText("Event Name"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_limit_et))
                .perform(ViewActions.typeText("1"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_desc_et))
                .perform(ViewActions.typeText("Event Description"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withText("Add"))
                .perform(ViewActions.click());

        onView(withId(R.id.user_home_organizing_btn)).perform(click());
        onView(withText("Event Name")).perform(ViewActions.click());




    }

    @Test
    public void given_when_thenShouldShowToast() {
        // Launch the activity and get its scenario
        //ActivityScenario<AttendeeOrganizerHome> scenario = mActivityScenarioRule.getScenario();

        // Register a callback to get the decor view once the activity is resumed
        //scenario.onActivity(activity -> decorView = activity.getWindow().getDecorView());

        // Define the expected toast message
        String expectedWarning = "Enter all text fields";

        Espresso.onView(ViewMatchers.withId(R.id.user_home_add_event_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Add"))
                .perform(ViewActions.click());


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Check if the toast message with the expected text is displayed
        onView(withText(expectedWarning))
                .inRoot(withDecorView(Matchers.is(decorView)))// Here we use decorView
                .check(matches(isDisplayed()));
    }
}
