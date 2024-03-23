package com.CMPUT301W24T32.brazmascheckin;

import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;

public class EventTest {

    private Event event;

    @Before
    public void setup() {
        HashMap<String, Integer> checkIns = new HashMap<>();
        ArrayList<String> signUps = new ArrayList<>();
        event = new Event("1", "Test Event", new Date("2024-12-01"), "Test Description", checkIns, signUps, 100, "posterID", "QRCodeID", "shareQRCodeID", "organizerID",
        false, null, null);
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
//        assertTrue(event.checkIn("attendee1", null));
//        assertTrue(event.checkIn("attendee1", null));
        assertEquals(2, event.getCheckIns().get("attendee1").intValue());
    }

    @Test
    public void testHelperKeys() {
        event.checkIn("attendee1", null);
        event.checkIn("attendee2", null);
        ArrayList<String> keys = event.helperKeys();
        assertTrue(keys.contains("attendee1"));
        assertTrue(keys.contains("attendee2"));
    }

    @Test
    public void testHelperCount() {
        event.checkIn("attendee1", null);
        event.checkIn("attendee2", null);
        assertEquals(2, event.helperCount());
    }

    @Test
    public void testGetSignUps() {
        event.signUp("attendee1");
        event.signUp("attendee2");
        ArrayList<String> signUps = event.getSignUps();
        assertTrue(signUps.contains("attendee1"));
        assertTrue(signUps.contains("attendee2"));
    }

    @Test
    public void testSignUp() {
        assertTrue(event.signUp("attendee1"));
        assertTrue(event.signUp("attendee2"));
        assertEquals(2, event.getSignUps().size());
    }

    @Test
    public void testUnSignUp() {
        event.signUp("attendee1");
        event.signUp("attendee2");
        assertTrue(event.unSignUp("attendee1"));
        assertEquals(1, event.getSignUps().size());
    }

    @Test
    public void testGetAttendeeLimit() {
        assertEquals(100, event.getAttendeeLimit());
    }

    @Test
    public void testSetAttendeeLimit() {
        event.setAttendeeLimit(200);
        assertEquals(200, event.getAttendeeLimit());
    }

    @Test
    public void testGetPoster() {
        assertEquals("posterID", event.getPoster());
    }

    @Test
    public void testSetPoster() {
        event.setPoster("newPosterID");
        assertEquals("newPosterID", event.getPoster());
    }

    @Test
    public void testGetQRCode() {
        assertEquals("QRCodeID", event.getQRCode());
    }

    @Test
    public void testSetQRCode() {
        event.setQRCode("newQRCodeID");
        assertEquals("newQRCodeID", event.getQRCode());
    }

    @Test
    public void testGetShareQRCode() {
        assertEquals("shareQRCodeID", event.getShareQRCode());
    }

    @Test
    public void testSetShareQRCode() {
        event.setShareQRCode("newShareQRCodeID");
        assertEquals("newShareQRCodeID", event.getShareQRCode());
    }

    @Test
    public void testGetOrganizer() {
        assertEquals("organizerID", event.getOrganizer());
    }

    @Test
    public void testSetOrganizer() {
        event.setOrganizer("newOrganizerID");
        assertEquals("newOrganizerID", event.getOrganizer());
    }
}