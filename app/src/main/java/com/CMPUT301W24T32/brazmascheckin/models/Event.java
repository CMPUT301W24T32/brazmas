package com.CMPUT301W24T32.brazmascheckin.models;

import com.CMPUT301W24T32.brazmascheckin.helper.Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * This class is a representation of the Event entity.
 */
public class Event {

    private String name;

    //TODO: should we make continuous events? with a start date and end date?
    private Date date;
    private String description;

    private HashMap<Attendee, Integer> checkIns;
    private ArrayList<Attendee> signUps;

    private int attendeeLimit;
    //TODO: add Image poster
    //TODO: add Image QRCode
    //TODO: add Image shareQRCode
    //TODO: add HashMap<Attendee, Integer>
    //TODO: add geolocation/event map

    /**
     * Constructs a new instance of the Event class with the mandatory information for an event.
     * @param name Name of the event
     * @param date Date of the event
     * @param description Description of the event
     */
    public Event(String name, Date date, String description) {
        this.name = name;
        this.date = date;
        this.description = description;
    }

    /**
     * Getter for the name attribute.
     * @return name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name attribute.
     * @param name new name of the event
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the date attribute.
     * @return date of the event
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the date attribute.
     * @param date new date of the event
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for the description of the event.
     * @return description of the event
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description of the event.
     * @param description new description of the event
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This method "checks-in" the attendee to the event, updating how many times they
     * have previously checked-in.
     * @param attendee the attendee who is checking into the event
     * @return successful check-in to the event
     */
    public boolean checkIn(Attendee attendee) {
        if(attendee != null) {
            if(checkIns.containsKey(attendee)) {
                // retrieving the previous number of check-ins by the attendee
                Integer previousCheckIns = checkIns.get(attendee);

                if(previousCheckIns != null) {
                    checkIns.put(attendee, previousCheckIns + 1);
                    return true;
                }

            } else {
                Integer firstCheckIn = 1;
                checkIns.put(attendee, firstCheckIn);
                return true;
            }
        }
        return false;
    }

    //TODO: how to deal with attendees leaving the event?
    /**
     * This method provides the list of attendees checked-into the event.
     * @return list of attendees checked-into the event
     */
    public ArrayList<Attendee> getCheckIns() {
        Set<Attendee> keySet = checkIns.keySet();
        return new ArrayList<>(keySet);
    }

    /**
     * This method provides the number of attendees who have checked-into the event.
     * @return the number of attendees who have checked-into the event
     */
    public int getCheckInsCount() {
        ArrayList<Attendee> attendees = getCheckIns();
        return attendees.size();
    }

    /**
     * This method provides the list of users who have signed-up to attend the event.
     * @return the list of users who have signed-up to attend the event
     */
    public ArrayList<Attendee> getSignUps() {
        return signUps;
    }

    /**
     * This method "signs-up" a user for an event.
     * @param attendee the user who is signing-up for the event
     * @return successful sign-up to the event
     */
    public boolean signUp(Attendee attendee) {
        return signUps.add(attendee);
    }

    /**
     * This method "un-signs-up" a user for an event.
     * @param attendee the user who is un-signing-up for the event
     * @return successful un-sign-up for the event
     */
    public boolean unSignUp(Attendee attendee) {
        return signUps.remove(attendee);
    }

}
