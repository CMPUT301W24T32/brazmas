package com.CMPUT301W24T32.brazmascheckin;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.views.AnnouncementActivity;
import com.CMPUT301W24T32.brazmascheckin.views.AttendeeOrganizerHome;
import com.CMPUT301W24T32.brazmascheckin.views.CameraActivity;
import com.CMPUT301W24T32.brazmascheckin.views.ProfileActivity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NavBarTest {

        @Rule
        public ActivityScenarioRule<AttendeeOrganizerHome> mActivityScenarioRule =
                new ActivityScenarioRule<AttendeeOrganizerHome>(AttendeeOrganizerHome.class);

        @Test
        public void clickAnnouncmentNavTest() {
            Intents.init();
            Espresso.onView(ViewMatchers.withId(R.id.bottom_announcement)).perform(ViewActions.click());
            intended(hasComponent(AnnouncementActivity.class.getName()));
            Intents.release();

    }
    @Test
    public void clickProfileNavTest() {
        Intents.init();
        Espresso.onView(ViewMatchers.withId(R.id.bottom_profile)).perform(ViewActions.click());
        intended(hasComponent(ProfileActivity.class.getName()));
        Intents.release();

    }
    @Test
    public void clickCameraNavTest() {
        Intents.init();
        Espresso.onView(ViewMatchers.withId(R.id.bottom_camera)).perform(ViewActions.click());
        intended(hasComponent(CameraActivity.class.getName()));
        Intents.release();

    }
}

