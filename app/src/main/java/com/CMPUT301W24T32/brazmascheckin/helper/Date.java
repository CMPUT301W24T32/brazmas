package com.CMPUT301W24T32.brazmascheckin.helper;

/**
 * This class is a representation of the date in a usable format to be displayed
 */
public class Date {

    //TODO: may need to change to support multiple languages
    //TODO: need to add time
    private static final String[] MONTH_CONVERSION = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December"};
    private int day;
    private int month;
    private int year;

    // Default constructor with no arguments (required by Firestore)
    public Date() {
    }

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPrettyDate() {
        return MONTH_CONVERSION[getMonth()] + " " + getDay() + ", " + getYear();
    }
}

