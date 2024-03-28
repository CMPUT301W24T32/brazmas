package com.CMPUT301W24T32.brazmascheckin;

import com.CMPUT301W24T32.brazmascheckin.models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserTest {

    private User user;

    @Before
    public void setup() {
        ArrayList<String> signedUpEvents = new ArrayList<>();
        ArrayList<String> organizedEvents = new ArrayList<>();
//        user = new User("John", "Doe", signedUpEvents, "123", "profilePicture.jpg", organizedEvents, null);
    }

    @Test
    public void testGetFirstName() {
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testSetFirstName() {
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Doe", user.getLastName());
    }

    @Test
    public void testSetLastName() {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    public void testGetSignedUpEvents() {
        assertEquals(0, user.getSignedUpEvents().size());
    }

    @Test
    public void testSignUpEvent() {
        user.signUpEvent("event1");
        assertEquals(1, user.getSignedUpEvents().size());
        assertEquals("event1", user.getSignedUpEvents().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSignUpEvent_alreadySignedUp() {
        user.signUpEvent("event1");
        user.signUpEvent("event1");
    }

    @Test
    public void testUnSignUpEvent() {
        user.signUpEvent("event1");
        user.unSignUpEvent("event1");
        assertEquals(0, user.getSignedUpEvents().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnSignUpEvent_notSignedUp() {
        user.unSignUpEvent("event1");
    }

    @Test
    public void testGetID() {
        assertEquals("123", user.getID());
    }

    @Test
    public void testSetID() {
        user.setID("456");
        assertEquals("456", user.getID());
    }

    @Test
    public void testGetProfilePicture() {
        assertEquals("profilePicture.jpg", user.getProfilePicture());
    }

    @Test
    public void testSetProfilePicture() {
        user.setProfilePicture("newProfilePicture.jpg");
        assertEquals("newProfilePicture.jpg", user.getProfilePicture());
    }

    @Test
    public void testCreateEvent() {
        user.createEvent("event1");
        assertEquals(1, user.getOrganizedEvents().size());
        assertEquals("event1", user.getOrganizedEvents().get(0));
    }

    @Test
    public void testDeleteEvent() {
        user.createEvent("event1");
        user.deleteEvent("event1");
        assertEquals(0, user.getOrganizedEvents().size());
    }

    @Test
    public void testSetEvent() {
        ArrayList<String> events = new ArrayList<>();
        events.add("event1");
        events.add("event2");
        user.setEvent(events);
        assertEquals(2, user.getOrganizedEvents().size());
        assertEquals("event1", user.getOrganizedEvents().get(0));
        assertEquals("event2", user.getOrganizedEvents().get(1));
    }

    @Test
    public void testGetOrganizedEvents() {
        assertEquals(0, user.getOrganizedEvents().size());
    }
}
