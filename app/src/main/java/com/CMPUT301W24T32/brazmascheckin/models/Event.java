package com.CMPUT301W24T32.brazmascheckin.models;

import com.CMPUT301W24T32.brazmascheckin.helper.Date;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * This class is a representation of the Event entity.
 */
public class Event implements Serializable {
    private String ID;

    private String name;

    //TODO: should we make continuous events? with a start date and end date?
    private Date date;
    private String description;

    private HashMap<String, Integer> checkIns;
    private ArrayList<String> signUps;

    private int attendeeLimit;
    private String poster;
    private String QRCode;
    private String shareQRCode;

    //TODO: add geolocation/event map

    /**
     * Constructs a new instance of the Event class with the full information for an event.
     * @param ID Identification assigned by FirestoreDB
     * @param name Name of the event
     * @param date Date of the event
     * @param description Description of the event
     * @param checkIns Attendees who have checked in, including number of times they have checked in
     * @param signUps Attendees who have signed up
     * @param attendeeLimit Maximum number of attendees who can check-in
     * @param posterID Reference to the image
     * @param QRCodeID Reference to the image
     * @param shareQRCodeID Reference to the image
     */
    public Event(String ID, String name, Date date, String description, HashMap<String, Integer> checkIns,
                 ArrayList<String> signUps, int attendeeLimit, String posterID, String QRCodeID,
                 String shareQRCodeID) {
        this.ID = ID;
        this.name = name;
        this.date = date;
        this.description = description;
        this.checkIns = checkIns;
        this.signUps = signUps;
        this.attendeeLimit = attendeeLimit;
        this.poster = posterID;
        this.QRCode = QRCodeID;
        this.shareQRCode = shareQRCodeID;
    }

    /**
     * Constructs a new instance of the Event class as required by Firestore
     */
    public Event() {

    }

    /**
     * Constructs a new instance of the Event class with the mandatory information
     * @param ID Identification assigned by FirestoreDB
     * @param name Name of the event
     * @param description Description of the event
     * @param checkIns Attendees who have checked in, including number of times they have checked in
     * @param signUps Attendees who have signed up
     */
    public Event(String ID, String name, String description, HashMap<String, Integer> checkIns,
                 ArrayList<String> signUps) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.checkIns = checkIns;
        this.signUps = signUps;

    }

    /**
     * Getter for the ID of the event
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
    public boolean checkIn(String attendee) {
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
    public ArrayList<String> getCheckInsKeys() {
        Set<String> keySet = checkIns.keySet();
        return new ArrayList<>(keySet);
    }

    /**
     * Getter for the checked-in attendees map of the event.
     * @return Map of the checked-in attendees in <user ID, number of times checked in>
     */
    public HashMap<String, Integer> getCheckIns() {
        return checkIns;
    }

    /**
     * Setter for the checked-in attendees map of the event.
     * @param checkIns Map of the checked-in attendees in <user ID, number of times checked in>
     */
    public void setCheckIns(HashMap<String, Integer> checkIns) {
        this.checkIns = checkIns;
    }

    /**
     * This method provides the number of attendees who have checked-into the event.
     * @return the number of attendees who have checked-into the event
     */
    public int getCheckInsCount() {
        ArrayList<String> attendees = getCheckInsKeys();
        return attendees.size();
    }

    /**
     * This method provides the list of users who have signed-up to attend the event.
     * @return the list of users who have signed-up to attend the event
     */
    public ArrayList<String> getSignUps() {
        return signUps;
    }

    /**
     * This method "signs-up" a user for an event.
     * @param attendee the user who is signing-up for the event
     * @return successful sign-up to the event
     */
    public boolean signUp(String attendee) {
        // control flow: attendee cannot sign-up for an event while they are still signed up
        if(signUps.contains(attendee)) {
            return false;
        }
        return signUps.add(attendee);
    }

    /**
     * This method "un-signs-up" a user for an event.
     * @param attendee the user who is un-signing-up for the event
     * @return successful un-sign-up for the event
     */
    public boolean unSignUp(String attendee) {
        return signUps.remove(attendee);
    }

    /**
     * Setter for the signed-up attendees list.
     * @param signUps List of attendees who have signed-up for the event
     */
    public void setSignUps(ArrayList<String> signUps) {
        this.signUps = signUps;
    }

    /**
     * Getter for the attendee limit
     * @return The maximum number of attendees allowed to check-in for the event
     */
    public int getAttendeeLimit() {
        return attendeeLimit;
    }

    /**
     * Setter for the attendee limit
     * @param attendeeLimit The maximum number of attendees allowed to check-in for the event
     */
    public void setAttendeeLimit(int attendeeLimit) {
        this.attendeeLimit = attendeeLimit;
    }

    /**
     * Getter for the event's poster
     * @return reference ID for the image containing the poster
     */
    public String getPoster() {
        return poster;
    }

    /**
     * Setter for the event's poster
     * @param poster reference ID for the image containing the poster
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * Getter for the event's check-in QR-Code
     * @return reference ID for the image containing the QR code
     */
    public String getQRCode() {
        return QRCode;
    }

    /**
     * Setter for the event's check-in QR-Code
     * @param QRCode reference ID for the image containing the QR code
     */
    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    /**
     * Setter for the event's share QR-Code
     * @return reference ID for the image containing the QR Code
     */
    public String getShareQRCode() {
        return shareQRCode;
    }

    /**
     * Setter for the event's share QR-Code
     * @param shareQRCode reference ID for the image containing the QR code
     */
    public void setShareQRCode(String shareQRCode) {
        this.shareQRCode = shareQRCode;
    }
}
