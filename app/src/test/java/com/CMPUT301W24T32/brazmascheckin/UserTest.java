package com.CMPUT301W24T32.brazmascheckin;

import com.CMPUT301W24T32.brazmascheckin.models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        ArrayList<String> signedUpEvents = new ArrayList<>();
        ArrayList<String> organizedEvents = new ArrayList<>();
        ArrayList<String> checkedInEvents = new ArrayList<>();
        user = new User("John", "Doe", signedUpEvents, "1", organizedEvents, true, 0, "profilePicture", "defaultProfilePicture", checkedInEvents);
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
        assertTrue(user.getSignedUpEvents().isEmpty());
    }

    @Test
    public void testSetSignedUpEvents() {
        ArrayList<String> events = new ArrayList<>();
        events.add("event1");
        user.setSignedUpEvents(events);
        assertFalse(user.getSignedUpEvents().isEmpty());
    }

    @Test
    public void testSignUpEvent() {
        user.signUpEvent("event1");
        assertTrue(user.getSignedUpEvents().contains("event1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSignUpEventTwice() {
        user.signUpEvent("event1");
        user.signUpEvent("event1");
    }

    @Test
    public void testUnSignUpEvent() {
        user.signUpEvent("event1");
        user.unSignUpEvent("event1");
        assertTrue(user.getSignedUpEvents().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnSignUpEventNotSignedUp() {
        user.unSignUpEvent("event1");
    }

    @Test
    public void testGetID() {
        assertEquals("1", user.getID());
    }

    @Test
    public void testSetID() {
        user.setID("2");
        assertEquals("2", user.getID());
    }

    @Test
    public void testGetProfilePicture() {
        assertEquals("profilePicture", user.getProfilePicture());
    }

    @Test
    public void testSetProfilePicture() {
        user.setProfilePicture("newProfilePicture");
        assertEquals("newProfilePicture", user.getProfilePicture());
    }

    @Test
    public void testCreateEvent() {
        user.createEvent("event1");
        assertTrue(user.getOrganizedEvents().contains("event1"));
    }

    @Test
    public void testDeleteEvent() {
        user.createEvent("event1");
        user.deleteEvent("event1");
        assertTrue(user.getOrganizedEvents().isEmpty());
    }

    @Test
    public void testSetEvent() {
        ArrayList<String> events = new ArrayList<>();
        events.add("event1");
        user.setEvent(events);
        assertFalse(user.getOrganizedEvents().isEmpty());
    }

    @Test
    public void testGetOrganizedEvents() {
        assertTrue(user.getOrganizedEvents().isEmpty());
    }

    @Test
    public void testIsGeoLocationEnabled() {
        assertTrue(user.isGeoLocationEnabled());
    }

    @Test
    public void testSetGeoLocationEnabled() {
        user.setGeoLocationEnabled(false);
        assertFalse(user.isGeoLocationEnabled());
    }

    @Test
    public void testGetDefaultProfilePicture() {
        assertEquals("defaultProfilePicture", user.getDefaultProfilePicture());
    }

    @Test
    public void testSetDefaultProfilePicture() {
        user.setDefaultProfilePicture("newDefaultProfilePicture");
        assertEquals("newDefaultProfilePicture", user.getDefaultProfilePicture());
    }

    @Test
    public void testGetLastAnnouncementCheck() {
        assertEquals(0, user.getLastAnnouncementCheck());
    }

    @Test
    public void testSetLastAnnouncementCheck() {
        user.setLastAnnouncementCheck(1);
        assertEquals(1, user.getLastAnnouncementCheck());
    }

    @Test
    public void testCheckIn() {
        user.checkIn("event1");
        assertTrue(user.getCheckInEvents().contains("event1"));
    }
}