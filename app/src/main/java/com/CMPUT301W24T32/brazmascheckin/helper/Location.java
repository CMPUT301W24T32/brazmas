package com.CMPUT301W24T32.brazmascheckin.helper;

import java.io.Serializable;

public class Location implements Serializable {
    private double latitude;
    private double longitude;

    /**
     * Constructs a new Location object as required by Firebase.
     */
    public Location() {

    }

    /**
     * Constructs a new Location object with the specified latitude and longitude.
     *
     * @param latitude  the latitude coordinate of the location
     * @param longitude the longitude coordinate of the location
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude coordinate of the location.
     *
     * @return the latitude coordinate
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude coordinate of the location.
     *
     * @param latitude the latitude coordinate to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude coordinate of the location.
     *
     * @return the longitude coordinate
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude coordinate of the location.
     *
     * @param longitude the longitude coordinate to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
