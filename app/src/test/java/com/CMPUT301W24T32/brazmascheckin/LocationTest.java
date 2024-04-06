package com.CMPUT301W24T32.brazmascheckin;

import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocationTest {

    private Location location;

    @Before
    public void setUp() {
        location = new Location(40.7128, -74.0060);
    }

    @Test
    public void testGetLatitude() {
        assertEquals(40.7128, location.getLatitude(), 0.0001);
    }

    @Test
    public void testSetLatitude() {
        location.setLatitude(40.7127);
        assertEquals(40.7127, location.getLatitude(), 0.0001);
    }

    @Test
    public void testGetLongitude() {
        assertEquals(-74.0060, location.getLongitude(), 0.0001);
    }

    @Test
    public void testSetLongitude() {
        location.setLongitude(-74.0061);
        assertEquals(-74.0061, location.getLongitude(), 0.0001);
    }
}