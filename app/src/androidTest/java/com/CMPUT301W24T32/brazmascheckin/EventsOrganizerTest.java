package com.CMPUT301W24T32.brazmascheckin;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

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

    /**
     * test adds a new event
     */
    @Test
    public void eventsOrganizerTest() {
        try {
            Thread.sleep(1000);
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
    }
    /**
     * test to check filtering of events
     */
    @Test
    public void attendingFilterTest(){

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("BleeBloo Event")).perform(ViewActions.click());
        onView(withId(R.id.view_event_signed_up_cb)).perform(ViewActions.click());
        onView(withText("Back")).perform(ViewActions.click());
        onView(withId(R.id.user_home_attending_btn)).perform(click());
        onView(withText("BleeBloo Event")).perform(ViewActions.click());
        onView(withId(R.id.view_event_signed_up_cb)).perform(ViewActions.click());
    }

    /**
     * Attending Signup test
     */
    @Test
    public void AttendingSignUpTest(){
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("BleeBloopEvent")).perform(click());
        onView(withId(R.id.view_event_signed_up_cb)).perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withId(R.id.view_scroll_bar)).perform(ViewActions.swipeUp());
        onView(withId(R.id.view_event_see_signed_up_attendees_btn)).perform(ViewActions.click());
    }

    /**
     * Organizing Filter Test
     */
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
                .perform(ViewActions.typeText("Mehar cool events"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_limit_et))
                .perform(ViewActions.typeText("1"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_desc_et))
                .perform(ViewActions.typeText("Event Description"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withText("Add"))
                .perform(ViewActions.click());

        onView(withId(R.id.user_home_organizing_btn)).perform(click());
    }

    /**
     * toast check
     */
    @Test
    public void given_when_thenShouldShowToast() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String expectedWarning = "Enter all text fields";

        Espresso.onView(ViewMatchers.withId(R.id.user_home_add_event_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Add"))
                .perform(ViewActions.click());


    }


}
