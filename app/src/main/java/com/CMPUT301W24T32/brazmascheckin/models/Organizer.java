package com.CMPUT301W24T32.brazmascheckin.models;

import java.util.ArrayList;

/**
 * This class represents the Organizer entity
 */
public class Organizer extends Attendee {

    /**
     * Constructs a new instance of the Organizer class as required by Firestore
     */
    public Organizer() {

    }

    /**
     * Constructs a new instance of the Attendee class with the full information for an attendee.
     * @param ID Identification assigned by FirestoreDB
     * @param firstName First name of attendee
     * @param lastName Last name of attendee
     * @param signedUpEvents Events the attendee has signed up for
     * @param profilePictureID Reference to the image
     * @param organizedEvents Events the organizer has created
     */
    public Organizer(String ID, String firstName, String lastName, ArrayList<String> signedUpEvents,
                     String profilePictureID, ArrayList<String> organizedEvents) {
        super(ID, firstName, lastName, signedUpEvents, profilePictureID);
        this.organizedEvents = organizedEvents;
    }

    private ArrayList<String> organizedEvents;

    /**
     * This method adds an event to the list of the organizer's created events
     * @param event the reference ID of the created event
     */
    public void createEvent(String event) {
        organizedEvents.add(event);
    }

    /**
     * This method deletes an event from the list of the organizer's created events
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
     * @return the list of events organized by the organizer
     */
    public ArrayList<String> getOrganizedEvents() {
        return organizedEvents;
    }
}
