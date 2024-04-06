package com.CMPUT301W24T32.brazmascheckin;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;


public class AnnouncementTest {

    @Test
    public void testAnnouncementConstructor() {
        Announcement announcement = new Announcement("Test Name", "Test Description", "Test Event ID", System.currentTimeMillis());
        assertNotNull(announcement);
    }

    @Test
    public void testGetName() {
        Announcement announcement = new Announcement("Test Name", "Test Description", "Test Event ID", System.currentTimeMillis());
        assertEquals("Test Name", announcement.getName());
    }

    @Test
    public void testSetName() {
        Announcement announcement = new Announcement("Test Name", "Test Description", "Test Event ID", System.currentTimeMillis());
        announcement.setName("New Test Name");
        assertEquals("New Test Name", announcement.getName());
    }

    @Test
    public void testGetDescription() {
        Announcement announcement = new Announcement("Test Name", "Test Description", "Test Event ID", System.currentTimeMillis());
        assertEquals("Test Description", announcement.getDescription());
    }

    @Test
    public void testSetDescription() {
        Announcement announcement = new Announcement("Test Name", "Test Description", "Test Event ID", System.currentTimeMillis());
        announcement.setDescription("New Test Description");
        assertEquals("New Test Description", announcement.getDescription());
    }

    @Test
    public void testGetEventID() {
        Announcement announcement = new Announcement("Test Name", "Test Description", "Test Event ID", System.currentTimeMillis());
        assertEquals("Test Event ID", announcement.getEventID());
    }

    @Test
    public void testSetEventID() {
        Announcement announcement = new Announcement("Test Name", "Test Description", "Test Event ID", System.currentTimeMillis());
        announcement.setEventID("New Test Event ID");
        assertEquals("New Test Event ID", announcement.getEventID());
    }

    @Test
    public void testGetTimeCreated() {
        long currentTime = System.currentTimeMillis();
        Announcement announcement = new Announcement("Test Name", "Test Description", "Test Event ID", currentTime);
        assertEquals(currentTime, announcement.getTimeCreated());
    }

    @Test
    public void testSetTimeCreated() {
        long currentTime = System.currentTimeMillis();
        Announcement announcement = new Announcement("Test Name", "Test Description", "Test Event ID", currentTime);
        long newTime = currentTime + 1000;
        announcement.setTimeCreated(newTime);
        assertEquals(newTime, announcement.getTimeCreated());
    }
}