package com.CMPUT301W24T32.brazmascheckin.helper;


//TODO: remove class if unneeded
/**
 * Represents a pair consisting of a user ID and their check-in location.
 */
public class UserLocationPair {
    private String userID;
    private Location userCheckInLocation;

    /**
     * Constructs a new UserLocationPair object as required by Firebase.
     */
    public UserLocationPair() {

    }

    /**
     * Constructs a new UserLocationPair object with the specified user ID and check-in location.
     *
     * @param userID             the user ID
     * @param userCheckInLocation the check-in location of the user
     */
    public UserLocationPair(String userID, Location userCheckInLocation) {
        this.userID = userID;
        this.userCheckInLocation = userCheckInLocation;
    }

    /**
     * Returns the user ID.
     *
     * @return the user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the user ID.
     *
     * @param userID the user ID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Returns the check-in location of the user.
     *
     * @return the check-in location
     */
    public Location getUserCheckInLocation() {
        return userCheckInLocation;
    }

    /**
     * Sets the check-in location of the user.
     *
     * @param userCheckInLocation the check-in location to set
     */
    public void setUserCheckInLocation(Location userCheckInLocation) {
        this.userCheckInLocation = userCheckInLocation;
    }
}
