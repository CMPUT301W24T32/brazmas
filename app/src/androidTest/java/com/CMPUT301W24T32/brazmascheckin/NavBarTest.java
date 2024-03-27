package com.CMPUT301W24T32.brazmascheckin;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import com.CMPUT301W24T32.brazmascheckin.views.AnnouncementActivity;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;
import com.CMPUT301W24T32.brazmascheckin.views.CameraActivity;
import com.CMPUT301W24T32.brazmascheckin.views.ProfileActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests to see if navigation controller can switch from event(main page for users) to other activities
 */
@RunWith(AndroidJUnit4.class)
public class NavBarTest {



        @Rule
        public ActivityScenarioRule<UserHome> mActivityScenarioRule =
                new ActivityScenarioRule<UserHome>(UserHome.class);

        /**
        * Tests to see if navigation controller can switch from event(main page for users) to announcement
        */
        @Test
        public void clickAnnouncementNavTest() {
            Intents.init();
            Espresso.onView(ViewMatchers.withId(R.id.bottom_announcement)).perform(ViewActions.click());
            intended(hasComponent(AnnouncementActivity.class.getName()));
            Intents.release();

        }
        /**
        * Tests to see if navigation controller can switch from event(main page for users) to profule
        */
        @Test
        public void clickProfileNavTest() {
            Intents.init();
            Espresso.onView(ViewMatchers.withId(R.id.bottom_profile)).perform(ViewActions.click());
            intended(hasComponent(ProfileActivity.class.getName()));
            Intents.release();

        }
        /**
        * Tests to see if navigation controller can switch from event(main page for users) to camera
        */
        @Test
        public void clickCameraNavTest() {
            Intents.init();
            Espresso.onView(ViewMatchers.withId(R.id.bottom_camera)).perform(ViewActions.click());
            intended(hasComponent(CameraActivity.class.getName()));
            Intents.release();

        }
}

