package com.CMPUT301W24T32.brazmascheckin;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertTrue;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.views.AttendeeOrganizerHome;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EventsOrganizerTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void eventsOrganizerTest() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.user_home_add_event_btn))
                        .perform(ViewActions.click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.add_event_name_tv))
                .perform(ViewActions.typeText("Event Name"), ViewActions.closeSoftKeyboard());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewActions.pressBack();
        Espresso.onView(ViewMatchers.withId(R.id.add_event_desc_et))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Event Description"), ViewActions.closeSoftKeyboard());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withText("Add"))
                .perform(ViewActions.click());

    }
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
}
