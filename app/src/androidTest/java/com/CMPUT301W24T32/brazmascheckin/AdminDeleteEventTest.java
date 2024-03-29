package com.CMPUT301W24T32.brazmascheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * test for deletion of event as an administrator
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminDeleteEventTest {
    @Rule
    public ActivityScenarioRule<AdministratorHome> scenario = new ActivityScenarioRule<AdministratorHome>(AdministratorHome.class);

    @Test
    public void adminSelectEventTest() {
        try {
            Thread.sleep(3000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Mehar cool events")).perform(ViewActions.click());
        onView(withId(R.id.view_event_delete_btn_admin))
                .perform(ViewActions.click());

    }

}
