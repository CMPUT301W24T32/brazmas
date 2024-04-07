package com.CMPUT301W24T32.brazmascheckin;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.CMPUT301W24T32.brazmascheckin.views.AdministratorBrowseImages;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorBrowseProfiles;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)

public class AdminBarTest {

    /**
     * Tests to see if navigation controller can switch from event(main page for admin) to other activities
     */

    @Rule
    public ActivityScenarioRule<AdministratorHome> scenario = new ActivityScenarioRule<AdministratorHome>(AdministratorHome.class);


        /**
         * Tests to see if navigation controller can switch from event(main page for admin) to images
         */
        @Test
        public void clickImagesNavTest() {
            Intents.init();
            Espresso.onView(ViewMatchers.withId(R.id.admin_image)).perform(ViewActions.click());
            intended(hasComponent(AdministratorBrowseImages.class.getName()));
            Intents.release();

        }
        /**
         * Tests to see if navigation controller can switch from event(main page for admin) to profiles
         */
        @Test
        public void clickAdminProfileNavTest() {
            Intents.init();
            Espresso.onView(ViewMatchers.withId(R.id.admin_profile)).perform(ViewActions.click());
            intended(hasComponent(AdministratorBrowseProfiles.class.getName()));
            Intents.release();

        }





    }



