package com.CMPUT301W24T32.brazmascheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testSignUp(){
        Intents.init();
        onView(withId(R.id.main_firstname_textview)).perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.main_lastname_textview)).perform(typeText("White"), closeSoftKeyboard());
        onView(withId(R.id.main_submit_button)).perform(click());
        Intents.release();
    }

}