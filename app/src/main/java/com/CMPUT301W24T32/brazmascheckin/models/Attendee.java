package com.CMPUT301W24T32.brazmascheckin.models;

import java.util.ArrayList;

/**
 * This class is a representation of the Attendee entity.
 */
public class Attendee {
    private String firstName;
    private String lastName;
    private ArrayList<String> signedUpEvents;
    private String ID;

    private String profilePicture;


    /**
     * Constructs a new instance of the Attendee class with the full information for an attendee.
     * @param ID Identification assigned by FirestoreDB
     * @param firstName First name of attendee
     * @param lastName Last name of attendee
     * @param signedUpEvents Events the attendee has signed up for
     * @param profilePictureID Reference to the image
     */

    public Attendee(String ID, String firstName, String lastName, ArrayList<String> signedUpEvents,
                    String profilePictureID){
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.signedUpEvents = signedUpEvents;
        this.profilePicture = profilePictureID;
    }

    /**
     * Constructs a new instance of the Attendee class with the mandatory information for an attendee.
     * @param ID Identification assigned by FirestoreDB
     * @param firstName First name of attendee
     * @param lastName Last name of attendee
     * @param signedUpEvents Events the attendee has signed up for
     */
    public Attendee(String ID, String firstName, String lastName, ArrayList<String>
                    signedUpEvents) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.signedUpEvents = signedUpEvents;
    }

    /**
     * Constructs a new instance of the Attendee class as required by Firestore
     */
    public Attendee() {

    }

    /**
     * Getter for the first name of the attendee
     * @return the first name of the attendee
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for the first name of the attendee
     * @param firstName new first name of the attendee
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for the last name of the attendee
     * @return the last name of the attendee
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for the last name of the attendee
     * @param lastName new last name of the attendee
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * This method provides the list of events the attendee has signed-up to attend
     * @return the list of events the attendee has signed-up to event
     */
    public ArrayList<String> getSignedUpEvents() {
        return signedUpEvents;
    }

    /**
     * Setter for the signed-up events list
     * @param signedUpEvents List of events the attendee has signed-up to attend
     */
    public void setSignedUpEvents(ArrayList<String> signedUpEvents) {
        this.signedUpEvents = signedUpEvents;
    }

    /**
     * This method "signs-up" the attendee to the event
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
     * This method "un-signs-up" the attendee from an event
     * @param e the reference ID of the event
     */
    public void unSignUpEvent(String e){
        if (signedUpEvents.contains(e)) {
            signedUpEvents.remove(e);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Getter for the ID of the attendee
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
     * Getter for the attendee's profile picture
     * @return reference ID for the image containing the profile picture
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    /**
     * Setter for the attendee's profile picture
     * @param profilePicture reference ID for the image containing the profile picture
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    // TODO: public void setProfilePicture(Image picture)
    // TODO: public void checkIn(QrScanner qr)

}
