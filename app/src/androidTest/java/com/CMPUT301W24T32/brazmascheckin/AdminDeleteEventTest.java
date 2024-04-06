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

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.Matchers.instanceOf;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

import com.CMPUT301W24T32.brazmascheckin.controllers.AdminController;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.Admin;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AddEventActivity;
import com.CMPUT301W24T32.brazmascheckin.views.AdministratorHome;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.views.MapView;


import java.util.ArrayList;
import java.util.List;
import androidx.test.filters.LargeTest;

import org.junit.Rule;

/**
 * test for deletion of event as an administrator
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AdminDeleteEventTest {

    @Rule
    public ActivityScenarioRule<AdministratorHome> scenario = new ActivityScenarioRule<>(AdministratorHome.class);

    private AdminController adminController;
    private EventController eventController;
    private Admin admin = new Admin(DeviceID.getDeviceID(
            ApplicationProvider.getApplicationContext()
    ), "test", "device", new ArrayList<>(), false, new ArrayList<>());

    @Before
    public void setUp() {
        adminController = new AdminController(FirestoreDB.getDatabaseInstance());
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        adminController.setAdmin(admin, null, null);
    }

    @After
    public void cleanUp() {
        adminController.getAdmin(admin.getID(), object -> admin = object, null);

        // need to add other stuff?

        // do I need this?
        //adminController.deleteAdmin(admin, null, null);  // do I need to delete this?
    }

    @Test
    public void testShowAllEvents() {
        Espresso.onView(ViewMatchers.withId(R.id.event_text_view_admin))
                .perform(ViewActions.typeText("Events"));

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }


    }

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
