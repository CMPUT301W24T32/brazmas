package com.CMPUT301W24T32.brazmascheckin;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.InjectEventSecurityException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;


import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.views.MapView;


import java.util.ArrayList;
import java.util.List;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddEventTests {

    @Rule
    public ActivityScenarioRule<UserHome> scenario = new ActivityScenarioRule<>(UserHome.class);

    private UserController userController;

    @Before
    public void setUp() {
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        User user = new User(DeviceID.getDeviceID(
                ApplicationProvider.getApplicationContext()
        ), "test", "device", new ArrayList<>(), false, new ArrayList<>());
        userController.setUser(user, null ,null);
    }

    @Test
    public void testShowAllEvents() {


        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.user_home_add_event_btn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.add_event_name_tv))
                .perform(ViewActions.typeText("Test Event"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_desc_et))
                .perform(ViewActions.typeText("Test Event Description"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_geolocation_sw))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_choose_location_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.view_map_done_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_generate_promo_qr_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_sv))
                        .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        Espresso.onView(ViewMatchers.withId(R.id.add_event_button))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());


    }


}
