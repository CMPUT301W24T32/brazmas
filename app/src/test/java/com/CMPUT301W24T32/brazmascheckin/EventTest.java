package com.CMPUT301W24T32.brazmascheckin;

import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.commands.AddEventCommand;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Link: https://chat.mistral.ai/
 *
 * Date: 2024-03-07
 *
 * Use: Creating unit testing
 */
public class EventTest {
    private Event event;
    private Location eventLocation;

    @Before
    public void setUp() {
        HashMap<String, Integer> checkIns = new HashMap<>();
        ArrayList<String> signUps = new ArrayList<>();
        ArrayList<Announcement> announcements = new ArrayList<>();
        Location eventLocation = new Location(0.0, 0.0);
        HashMap<String, Location> userLocationPairs = new HashMap<>();


        event = new Event("1", "Test Event", "Test Description", new HashMap<>(), new ArrayList<>(),
                "organizer", true, new HashMap<>());

        event.setAnnouncements(announcements);
    }

    @Test
    public void testGetID() {
        assertEquals("1", event.getID());
    }

    @Test
    public void testSetID() {
        event.setID("2");
        assertEquals("2", event.getID());
    }

    @Test
    public void testGetName() {
        assertEquals("Test Event", event.getName());
    }

    @Test
    public void testSetName() {
        event.setName("New Test Event");
        assertEquals("New Test Event", event.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test Description", event.getDescription());
    }

    @Test
    public void testSetDescription() {
        event.setDescription("New Test Description");
        assertEquals("New Test Description", event.getDescription());
    }

    @Test
    public void testCheckIn() {
        event.checkIn("attendee", new Location(0.0, 0.0));
        assertTrue(event.getCheckIns().containsKey("attendee"));
    }

    @Test
    public void testHelperKeys() {
        event.checkIn("attendee", new Location(0.0, 0.0));
        assertTrue(event.helperKeys().contains("attendee"));
    }

    @Test
    public void testGetCheckIns() {
        event.checkIn("attendee", new Location(0.0, 0.0));
        assertTrue(event.getCheckIns().containsKey("attendee"));
    }

    @Test
    public void testSetCheckIns() {
        HashMap<String, Integer> newCheckIns = new HashMap<>();
        newCheckIns.put("newAttendee", 1);
        event.setCheckIns(newCheckIns);
        assertTrue(event.getCheckIns().containsKey("newAttendee"));
    }

    @Test
    public void testHelperCount() {
        event.checkIn("attendee", new Location(0.0, 0.0));
        assertEquals(1, event.helperCount());
    }

    @Test
    public void testGetSignUps() {
        event.signUp("attendee");
        assertTrue(event.getSignUps().contains("attendee"));
    }

    @Test
    public void testSignUp() {
        assertTrue(event.signUp("attendee"));
    }

    @Test
    public void testUnSignUp() {
        event.signUp("attendee");
        assertTrue(event.unSignUp("attendee"));
    }

    @Test
    public void testSetSignUps() {
        ArrayList<String> newSignUps = new ArrayList<>();
        newSignUps.add("newAttendee");
        event.setSignUps(newSignUps);
        assertTrue(event.getSignUps().contains("newAttendee"));
    }

    @Test
    public void testSetAttendeeLimit() {
        event.setAttendeeLimit(200);
        assertEquals(200, event.getAttendeeLimit());
    }

    @Test
    public void testSetPoster() {
        event.setPoster("newPosterID");
        assertEquals("newPosterID", event.getPoster());
    }

    @Test
    public void testSetQRCode() {
        event.setQRCode("newQRCodeID");
        assertEquals("newQRCodeID", event.getQRCode());
    }

    @Test
    public void testSetShareQRCode() {
        event.setShareQRCode("newShareQRCodeID");
        assertEquals("newShareQRCodeID", event.getShareQRCode());
    }

    @Test
    public void testGetOrganizer() {
        assertEquals("organizer", event.getOrganizer());
    }

    @Test
    public void testSetOrganizer() {
        event.setOrganizer("newOrganizer");
        assertEquals("newOrganizer", event.getOrganizer());
    }

    @Test
    public void testGetGeoLocationEnabled() {
        assertTrue(event.getGeoLocationEnabled());
    }

    @Test
    public void testSetGeoLocationEnabled() {
        event.setGeoLocationEnabled(false);
        assertFalse(event.getGeoLocationEnabled());
    }

    @Test
    public void testGetUserLocationPairs() {
        event.checkIn("attendee", new Location(0.0, 0.0));
        assertTrue(event.getUserLocationPairs().containsKey("attendee"));
    }

    @Test
    public void testSetUserLocationPairs() {
        HashMap<String, Location> newUserLocationPairs = new HashMap<>();
        newUserLocationPairs.put("newAttendee", new Location(0.0, 0.0));
        event.setUserLocationPairs(newUserLocationPairs);
        assertTrue(event.getUserLocationPairs().containsKey("newAttendee"));
    }

    @Test
    public void testGetNextMilestone() {
        assertEquals(1, event.getNextMilestone());
    }

    @Test
    public void testSetNextMilestone() {
        event.setNextMilestone(2);
        assertEquals(2, event.getNextMilestone());
    }

    @Test
    public void testGetAnnouncements() {
        assertEquals(new ArrayList<Announcement>(), event.getAnnouncements());
    }


}