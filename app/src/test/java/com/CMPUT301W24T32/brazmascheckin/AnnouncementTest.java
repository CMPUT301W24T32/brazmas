package com.CMPUT301W24T32.brazmascheckin;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.CMPUT301W24T32.brazmascheckin.models.Announcement;

import java.util.Date;

public class AnnouncementTest {
    private Announcement announcement;
    private Date date;

    @Before
    public void setUp() {
        date = new Date();
        announcement = new Announcement("Test Announcement", "This is a test announcement", date, "event1");
    }

    @Test
    public void testGetName() {
        assertEquals("Test Announcement", announcement.getName());
    }

    @Test
    public void testSetName() {
        announcement.setName("New Test Announcement");
        assertEquals("New Test Announcement", announcement.getName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("This is a test announcement", announcement.getDescription());
    }

    @Test
    public void testSetDescription() {
        announcement.setDescription("This is a new test announcement");
        assertEquals("This is a new test announcement", announcement.getDescription());
    }

    @Test
    public void testGetDate() {
        assertEquals(date, announcement.getDate());
    }

    @Test
    public void testSetDate() {
        Date newDate = new Date();
        announcement.setDate(newDate);
        assertEquals(newDate, announcement.getDate());
    }

    @Test
    public void testGetEventID() {
        assertEquals("event1", announcement.getEventID());
    }

    @Test
    public void testSetEventID() {
        announcement.setEventID("event2");
        assertEquals("event2", announcement.getEventID());
    }

}

