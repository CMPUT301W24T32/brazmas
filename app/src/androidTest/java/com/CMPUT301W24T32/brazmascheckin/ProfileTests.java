package com.CMPUT301W24T32.brazmascheckin;

import android.provider.ContactsContract;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.ProfileActivity;

import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProfileTests {
    User user = new User("john", "testy", null, "ProfileTest", null, null, 0,"profile");
    public ActivityScenarioRule<ProfileActivity> scenario = new
            ActivityScenarioRule<ProfileActivity>(ProfileActivity.class);


}
