package com.CMPUT301W24T32.brazmascheckin;

import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateTest {

    private Date date;

    @Before
    public void setUp() {
        date = new Date(15, 4, 2023);
    }

    @Test
    public void testGetDay() {
        assertEquals(15, date.getDay());
    }

    @Test
    public void testSetDay() {
        date.setDay(20);
        assertEquals(20, date.getDay());
    }

    @Test
    public void testGetMonth() {
        assertEquals(4, date.getMonth());
    }

    @Test
    public void testSetMonth() {
        date.setMonth(5);
        assertEquals(5, date.getMonth());
    }

    @Test
    public void testGetYear() {
        assertEquals(2023, date.getYear());
    }

    @Test
    public void testSetYear() {
        date.setYear(2024);
        assertEquals(2024, date.getYear());
    }
}
