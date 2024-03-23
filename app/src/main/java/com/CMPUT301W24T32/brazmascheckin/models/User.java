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
    private String ID;

    private String profilePicture;
    private ArrayList<String> organizedEvents;

    private boolean geoLocationEnabled = false;




    /** Constructs a new instance of the User class with full information
     * @param ID Identification assigned by FirestoreDB
     * @param firstName First name of user
     * @param lastName Last name of user
     * @param signedUpEvents Events the user has signed up for
     * @param profilePicture Reference to the image
     * @param organizedEvents List of events the user is organizing
     * @param geoLocationEnabled If geolocation is enabled
     */
    public User(String firstName, String lastName, ArrayList<String> signedUpEvents, String ID,
                String profilePicture, ArrayList<String> organizedEvents, boolean geoLocationEnabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.signedUpEvents = signedUpEvents;
        this.ID = ID;
        this.profilePicture = profilePicture;
        this.organizedEvents = organizedEvents;
        this.geoLocationEnabled = geoLocationEnabled;
    }

    /**
     * Constructs a new instance of the User class with the mandatory information for an user.
     * @param ID Identification assigned by FirestoreDB
     * @param firstName First name of user
     * @param lastName Last name of user
     * @param signedUpEvents Events the user has signed up for
     * @param geoLocationEnabled If geolocation is enabled
     */
    public User(String ID, String firstName, String lastName, ArrayList<String>
                    signedUpEvents, boolean geoLocationEnabled) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.signedUpEvents = signedUpEvents;
        this.geoLocationEnabled = geoLocationEnabled;
    }

    /**
     * Constructs a new instance of the User class as required by Firestore
     */
    public User() {

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

    public boolean isGeoLocationEnabled() {
        return geoLocationEnabled;
    }

    public void setGeoLocationEnabled(boolean geoLocationEnabled) {
        this.geoLocationEnabled = geoLocationEnabled;
    }

    // TODO: public void setProfilePicture(Image picture)
    // TODO: public void checkIn(QrScanner qr)

}
