package com.CMPUT301W24T32.brazmascheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.AnnouncementActivity;
import com.CMPUT301W24T32.brazmascheckin.views.UserHome;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;


@RunWith(AndroidJUnit4.class)
public class AnnouncementsTest {

    @Rule
    public ActivityScenarioRule<UserHome> scenario = new ActivityScenarioRule<>(UserHome.class);
    private UserController userController;
    private EventController eventController;
    private  User user = new User("John", "Doe", new ArrayList<>(), DeviceID.getDeviceID(ApplicationProvider.getApplicationContext()), new ArrayList<>(),
            false, 0, null,
            null, new ArrayList<>());

    @Before
    public void setUp() {
        userController = new UserController(FirestoreDB.getDatabaseInstance());
        eventController = new EventController(FirestoreDB.getDatabaseInstance());
        userController.setUser(user, null ,null);
    }

    @After
    public void cleanUp() {
        userController.getUser(user.getID(), object -> user = object, null);

        ArrayList<String> signedUpEvents = user.getSignedUpEvents();
        ArrayList<String> organizedEvents = user.getOrganizedEvents();

        for(String event: signedUpEvents) {
            eventController.deleteEvent(event, null, null);
        }

        for(String event: organizedEvents) {
            eventController.deleteEvent(event, null, null);
        }
        userController.deleteUser(user, null, null);


    }


    /**
     * Tests to see if Announcement is input but cancelled instead of sent-> doesn't add to signed-up Announcements
     * test for cancel button
     */
    @Test
    public void cancelAnnouncement() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.user_home_add_event_btn))
                .perform(ViewActions.click());


        Espresso.onView(ViewMatchers.withId(R.id.add_event_name_tv))
                .perform(ViewActions.typeText("Announcement Event1"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_desc_et))
                .perform(ViewActions.typeText("Description1"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_geolocation_sw))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_choose_location_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.view_map_done_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_sv))
                .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.add_event_button))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn));
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        onView(withText("Announcement Event1")).perform(click());
        onView(withId(R.id.view_event_signed_up_cb)).perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withId(R.id.view_scroll_bar)).perform(ViewActions.swipeUp());
        onView(withId(R.id.view_event_see_signed_up_attendees_btn)).perform(ViewActions.click());
        onView(withId(R.id.view_attendees_notify_btn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_name_tv))
                .perform(ViewActions.typeText("Announcement Name1"));
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_desc_et))
                .perform(ViewActions.typeText("Announcement Message1"));
        Espresso.onView(withText("Cancel"))
                .perform(ViewActions.click());
        Espresso.pressBack();
        Espresso.pressBack();

        Intents.init();
        Espresso.onView(ViewMatchers.withId(R.id.bottom_announcement)).perform(ViewActions.click());
        intended(hasComponent(AnnouncementActivity.class.getName()));
        Intents.release();

        Espresso.onView(withId(R.id.announcement_rv)).check(matches(hasMinimumChildCount(0)));

    }

    /**
     * Tests to see if Announcement fragment is empty-> doesn't add to signed-up Announcements
     *
     */
   
    @Test
    public void emptyFragment() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.user_home_add_event_btn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.add_event_name_tv))
                .perform(ViewActions.typeText("Announcement Event2"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_desc_et))
                .perform(ViewActions.typeText("Description2"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_geolocation_sw))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_choose_location_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.view_map_done_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_sv))
                .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.add_event_button))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn));
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        onView(withText("Announcement Event2")).perform(click());
        onView(withId(R.id.view_event_signed_up_cb)).perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withId(R.id.view_scroll_bar)).perform(ViewActions.swipeUp());
        onView(withId(R.id.view_event_see_signed_up_attendees_btn)).perform(ViewActions.click());
        onView(withId(R.id.view_attendees_notify_btn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_name_tv))
                .perform(ViewActions.typeText(""));
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_desc_et))
                .perform(ViewActions.typeText(""));
        Espresso.onView(withText("Send"))
                .perform(ViewActions.click());

        Espresso.pressBack();
        Espresso.pressBack();

        Intents.init();
        Espresso.onView(ViewMatchers.withId(R.id.bottom_announcement)).perform(ViewActions.click());
        intended(hasComponent(AnnouncementActivity.class.getName()));
        Intents.release();
        Espresso.onView(withId(R.id.announcement_rv)).check(matches(hasMinimumChildCount(0)));

    }




    /**
     * Tests to see if Announcement added to signed-up users announcement
     *
     */
    @Test
    public void addAnnouncementFirebase() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.user_home_add_event_btn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.add_event_name_tv))
                .perform(ViewActions.typeText("Announcement Event3"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_desc_et))
                .perform(ViewActions.typeText("Description3"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_geolocation_sw))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_choose_location_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.view_map_done_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_sv))
                .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.add_event_button))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn));
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        onView(withText("Announcement Event3")).perform(click());
        onView(withId(R.id.view_event_signed_up_cb)).perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withId(R.id.view_scroll_bar)).perform(ViewActions.swipeUp());
        onView(withId(R.id.view_event_see_signed_up_attendees_btn)).perform(ViewActions.click());
        onView(withId(R.id.view_attendees_notify_btn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_name_tv))
                .perform(ViewActions.typeText("Announcement Name3"));
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_desc_et))
                .perform(ViewActions.typeText("Announcement Message3"));
        Espresso.onView(withText("Send"))
                .perform(ViewActions.click());
        Espresso.pressBack();
        Espresso.pressBack();

        Intents.init();
        Espresso.onView(ViewMatchers.withId(R.id.bottom_announcement)).perform(ViewActions.click());
        intended(hasComponent(AnnouncementActivity.class.getName()));
        Intents.release();
        Espresso.onView(ViewMatchers.withId(R.id.announcementDescriptionText))
                .check(matches(withText("Announcement Message3")));

    }

    /**
     * Tests to see if multiples Announcements added to signed-up users announcement
     *
     */
    @Test
    public void multipleAnnouncements() {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.user_home_add_event_btn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.add_event_name_tv))
                .perform(ViewActions.typeText("Announcement Event4"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_desc_et))
                .perform(ViewActions.typeText("Description4"));
        Espresso.onView(ViewMatchers.withId(R.id.add_event_geolocation_sw))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_choose_location_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.view_map_done_btn))
                .perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_event_sv))
                .perform(ViewActions.swipeUp());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.add_event_button))
                .perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.user_home_organizing_btn));
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        onView(withText("Announcement Event4")).perform(click());
        onView(withId(R.id.view_event_signed_up_cb)).perform(ViewActions.click());
//        Espresso.onView(ViewMatchers.withId(R.id.view_scroll_bar)).perform(ViewActions.swipeUp());
        onView(withId(R.id.view_event_see_signed_up_attendees_btn)).perform(ViewActions.click());
        onView(withId(R.id.view_attendees_notify_btn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_name_tv))
                .perform(ViewActions.typeText("Announcement Name4"));
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_desc_et))
                .perform(ViewActions.typeText("Announcement Message4"));
        Espresso.onView(withText("Send"))
                .perform(ViewActions.click());
        onView(withId(R.id.view_attendees_notify_btn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_name_tv))
                .perform(ViewActions.typeText("Announcement Name4 pt 2"));
        Espresso.onView(ViewMatchers.withId(R.id.add_announcement_desc_et))
                .perform(ViewActions.typeText("Announcement Message4 pt 2"));
        Espresso.onView(withText("Send"))
                .perform(ViewActions.click());
        Espresso.pressBack();
        Espresso.pressBack();

        Intents.init();
        Espresso.onView(ViewMatchers.withId(R.id.bottom_announcement)).perform(ViewActions.click());
        intended(hasComponent(AnnouncementActivity.class.getName()));
        Intents.release();

        Espresso.onView(ViewMatchers.withId(R.id.announcement_rv))
                .check(matches(hasDescendant(withText("Announcement Message4"))));
        Espresso.onView(ViewMatchers.withId(R.id.announcement_rv))
                .check(matches(hasDescendant(withText("Announcement Message4 pt 2"))));

    }



}



