package com.CMPUT301W24T32.brazmascheckin.models;

import java.util.ArrayList;

public class Attendee {
    private String firstName;
    private String lastName;
    private ArrayList<Event> signedUpEvents;

    //TODO: add image profilePicture

    public Attendee(String firstName, String lastName, ArrayList<Event> signedUpEvents){
        this.firstName = firstName;
        this.lastName = lastName;
        this.signedUpEvents = signedUpEvents;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArrayList<Event> getSignedUpEvents() {
        return signedUpEvents;
    }

    public void setSignedUpEvents(ArrayList<Event> signedUpEvents) {
        this.signedUpEvents = signedUpEvents;
    }

    public void signUpEvent(Event e){
        if (!signedUpEvents.contains(e)) {
            signedUpEvents.add(e);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    public void unSignUpEvent(Event e){
        if (signedUpEvents.contains(e)) {
            signedUpEvents.remove(e);
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    // TODO: public void setProfilePicture(Image picture)
    // TODO: public void checkIn(QrScanner qr)

}
