package com.CMPUT301W24T32.brazmascheckin.models;

import java.util.ArrayList;


/**
 * The User class is a representation of the Attendee entity.
 * It includes information such as first name, last name, signed-up events, user ID, profile picture reference,
 * and a list of organized events.
 */
public class User {
    private String firstName;
    private String lastName;
    private ArrayList<String> signedUpEvents;
    private ArrayList<String> checkInEvents;
    private String ID;
    private String profilePicture;
    private String defaultProfilePicture;
    private ArrayList<String> organizedEvents;
    private long lastAnnouncementCheck;
    private boolean geoLocationEnabled = false;





    /** Constructs a new instance of the User class with full information
     * @param ID Identification assigned by FirestoreDB
     * @param firstName First name of user
     * @param lastName Last name of user
     * @param signedUpEvents Events the user has signed up for
     * @param profilePicture Reference to the image
     * @param organizedEvents List of events the user is organizing
     * @param geoLocationEnabled If geolocation is enabled
     * @param checkedInEvents List of events the user has checked-in to
     */
    public User(String firstName, String lastName, ArrayList<String> signedUpEvents, String ID,
                ArrayList<String> organizedEvents, boolean geoLocationEnabled, long lastAnnouncementCheck, String profilePicture,String defaultProfilePicture,
                ArrayList<String> checkedInEvents) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.signedUpEvents = signedUpEvents;
        this.ID = ID;
        this.profilePicture = profilePicture;
        this.organizedEvents = organizedEvents;
        this.geoLocationEnabled = geoLocationEnabled;
        this.defaultProfilePicture = defaultProfilePicture;
        this.lastAnnouncementCheck = lastAnnouncementCheck;
        this.checkInEvents = checkedInEvents;
    }

    /**
     * Constructs a new instance of the User class with the mandatory information for an user.
     * @param ID Identification assigned by FirestoreDB
     * @param firstName First name of user
     * @param lastName Last name of user
     * @param signedUpEvents Events the user has signed up for
     * @param geoLocationEnabled If geolocation is enabled
     * @param checkedInEvents List of events the user has checked-in to
     */
    public User(String ID, String firstName, String lastName, ArrayList<String>
                    signedUpEvents, boolean geoLocationEnabled, ArrayList<String> checkedInEvents) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.signedUpEvents = signedUpEvents;
        this.geoLocationEnabled = geoLocationEnabled;
        this.checkInEvents = checkedInEvents;
    }

    /**
     * Constructs a new instance of the User class as required by Firestore
     */
    public User() {

    }

    /**
     *
     * @param ID
     */
    public User(String ID) {
        this.ID = ID;
    }

    /**
     * Getter for the first name of the user
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for the first name of the user
     * @param firstName new first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for the last name of the user
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for the last name of the user
     * @param lastName new last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * This method provides the list of events the user has signed-up to attend
     * @return the list of events the user has signed-up to event
     */
    public ArrayList<String> getSignedUpEvents() {
       if(signedUpEvents != null) {
           return signedUpEvents;
       } else {
           return new ArrayList<String>();
       }
    }

    /**
     * Setter for the signed-up events list
     * @param signedUpEvents List of events the user has signed-up to attend
     */
    public void setSignedUpEvents(ArrayList<String> signedUpEvents) {
        this.signedUpEvents = signedUpEvents;
    }

    /**
     * This method "signs-up" the user to the event
     * @param e the reference ID of the event
     */
    public void signUpEvent(String e){
        if (!signedUpEvents.contains(e)) {
            signedUpEvents.add(e);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * This method "un-signs-up" the user from an event
     * @param e the reference ID of the event
     */
    public void unSignUpEvent(String e){
        if(signedUpEvents != null) {
            if (signedUpEvents.contains(e)) {
                signedUpEvents.remove(e);
            }
            else{
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Getter for the ID of the user
     * @return the identification string assigned by FirestoreDB
     */
    public String getID() {
        return ID;
    }

    /**
     * Setter for the ID of the event
     * @param ID the identification string assigned by FirestoreDB
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Getter for the user's profile picture
     * @return reference ID for the image containing the profile picture
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * Setter for the user's profile picture
     * @param profilePicture reference ID for the image containing the profile picture
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * This method adds an event to the list of the user's created events
     * @param event the reference ID of the created event
     */
    public void createEvent(String event) {
        organizedEvents.add(event);
    }

    /**
     * This method deletes an event from the list of the user's created events
     * @param event the reference ID of the event to delete
     */
    public void deleteEvent(String event) {
        organizedEvents.remove(event);
    }

    /**
     * Setter for the list of created events
     * @param organizedEvents new list of created events
     */
    public void setEvent(ArrayList<String> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }

    /**
     * Getter for the list of created events
     * @return the list of events organized by the user
     */
    public ArrayList<String> getOrganizedEvents() {
        if(organizedEvents != null) {
            return organizedEvents;
        } else {
            return new ArrayList<>();
        }

    }

    /**
     * This method checks if geolocation is enabled for user.
     * @return True if geolocation is enabled, false otherwise.
     */
    public boolean isGeoLocationEnabled() {
        return geoLocationEnabled;
    }

    /**
     * This method sets whether geolocation is enabled for the user.
     * @param geoLocationEnabled True to enable geolocation, false to disable it.
     */
    public void setGeoLocationEnabled(boolean geoLocationEnabled) {
        this.geoLocationEnabled = geoLocationEnabled;
    }

    /**
     * Retrieves the default profile picture for the user.
     * @return The default profile picture for the user.
     */
    public String getDefaultProfilePicture() {
        return defaultProfilePicture;
    }

    /**
     * Sets the default profile picture for the user.
     * @param defaultProfilePicture The default profile picture for the user.
     */
    public void setDefaultProfilePicture(String defaultProfilePicture) {
        this.defaultProfilePicture = defaultProfilePicture;
    }

    /**
     * Retrieves the timestamp of the user's last announcement check.
     * @return The timestamp of the user's last announcement check.
     */
    public long getLastAnnouncementCheck() {
        return lastAnnouncementCheck;
    }

    /**
     * Sets the timestamp of the user's last announcement check.
     * @param lastAnnouncementCheck The timestamp of the user's last announcement check.
     */
    public void setLastAnnouncementCheck(long lastAnnouncementCheck) {
        this.lastAnnouncementCheck = lastAnnouncementCheck;
    }

    /**
     * Retrieves the list of events the user has checked into.
     * @return The list of events the user has checked into.
     */
    public ArrayList<String> getCheckInEvents() {
        return checkInEvents;
    }

    /**
     * Sets the list of events the user has checked into.
     * @param checkInEvents The list of events the user has checked into.
     */
    public void setCheckInEvents(ArrayList<String> checkInEvents) {
        this.checkInEvents = checkInEvents;
    }

    /**
     * Sets the list of events the user has organized.
     * @param organizedEvents The list of events the user has organized.n
     */
    public void setOrganizedEvents(ArrayList<String> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }

    /**
     * Adds an event to the list of events the user has checked into.
     * @param eventID The ID of the event to be added to the list of check-ins.
     */
    public void checkIn(String eventID) {
        if(eventID != null) {
            checkInEvents.add(eventID);
        }
    }

}
