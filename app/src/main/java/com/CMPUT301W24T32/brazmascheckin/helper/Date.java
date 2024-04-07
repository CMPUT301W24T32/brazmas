package com.CMPUT301W24T32.brazmascheckin.helper;

import java.io.Serializable;

/**
 * This class ia a representation of the date in a usable format to be displayed
 */
public class Date implements Serializable {

    //TODO: may need to change to support multiple languages
    //TODO: need to add time
    private static final String[] MONTH_CONVERSION = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November", "December"};
    private int day;

    private int month;
    private int year;


    /**
     * empty constructor for Date
     */
    public Date() {}

    /**
     * Constructs a new instance of the Date class as required by Firebase
     */
    public Date(String date) {}

    /**
     * Constructs a new instance of the Date class with full information
     * @param day day the day
     * @param month month the month
     * @param year year the year
     */
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * This method returns a formatted version of the date in "Month Day, Year"
     * @return month, day, and year
     */
    public String getPrettyDate() {
        return MONTH_CONVERSION[getMonth()] + " " + getDay() + ", " + getYear();
    }

    /**
     * Getter for the day
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * Setter for the day
     * @param day the day to be set
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Getter for the month
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Setter for the month
     * @param month the month to be set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Getter for the year
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Setter for the year
     * @param year the year to be set
     */
    public void setYear(int year) {
        this.year = year;
    }
}

