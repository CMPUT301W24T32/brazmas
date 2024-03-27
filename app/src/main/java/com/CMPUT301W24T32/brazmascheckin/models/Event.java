package com.CMPUT301W24T32.brazmascheckin.models;

import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * The Event class represents an event entity with various attributes such as ID, name, date, description, etc.
 * It includes methods for handling check-ins, sign-ups, and other event-related operations.
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
    private String organizer;

    private Boolean geoLocationEnabled = false;
    private HashMap<String, Location> userLocationPairs;

    private Location eventLocation;

    private int nextMilestone;


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
     * @param organizer Reference to the user who created the event
     * @param geoLocationEnabled If geolocation is enabled
     * @param eventLocation the location of the event
     * @param userLocationPairs the list of users who have checked in, and where they have checked in
     */
    public Event(String ID, String name, Date date, String description, HashMap<String, Integer> checkIns,
                 ArrayList<String> signUps, int attendeeLimit, String posterID, String QRCodeID,
                 String shareQRCodeID, String organizer, boolean geoLocationEnabled, Location eventLocation,
                 HashMap<String, Location> userLocationPairs) {
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
        this.organizer = organizer;
        this.geoLocationEnabled = geoLocationEnabled;
        this.eventLocation = eventLocation;
        this.userLocationPairs = userLocationPairs;
        this.nextMilestone = 1;
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
     * @param organizer Reference to the user who created the event
     * @param geoLocationEnabled If geolocation is enabled
     * @param userLocationPairs the list of users who have checked in, and where they have checked in
     */
    public Event(String ID, String name, String description, HashMap<String, Integer> checkIns,
                 ArrayList<String> signUps, String organizer, boolean geoLocationEnabled,
                 HashMap<String, Location> userLocationPairs) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.checkIns = checkIns;
        this.signUps = signUps;
        this.organizer = organizer;
        this.geoLocationEnabled = geoLocationEnabled;
        this.userLocationPairs = userLocationPairs;
        this.nextMilestone = 1;
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
     */
    public void checkIn(String attendee, Location location) {
        if(attendee != null) {
            if(checkIns.containsKey(attendee)) {
                // retrieving the previous number of check-ins by the attendee
                Integer previousCheckIns = checkIns.get(attendee);

                if(previousCheckIns != null) {
                    checkIns.put(attendee, previousCheckIns + 1);
                }

            } else {
                Integer firstCheckIn = 1;
                checkIns.put(attendee, firstCheckIn);
            }

            if(geoLocationEnabled && location != null) {
                userLocationPairs.put(attendee, location);
            }
        }
    }

    /**
     * This method provides the list of attendees checked-into the event.
     * @return list of attendees checked-into the event
     */
    public ArrayList<String> helperKeys() {
        if(checkIns != null) {
            Set<String> keySet = checkIns.keySet();
            return new ArrayList<>(keySet);
        } else {
            return new ArrayList<>();
        }
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
    public int helperCount() {
        if(checkIns != null) {
            ArrayList<String> attendees = helperKeys();
            return attendees.size();
        } else {
            return -1;
        }
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

    /**
     * Getter for the reference to the user who created the event
     * @return reference to the user who created the event
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * Setter for the reference to the user who created the event
     * @param organizer the user who created the event
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    /**
     * Returns whether geolocation is enabled for the user.
     *
     * @return true if geolocation is enabled, false otherwise
     */
    public Boolean getGeoLocationEnabled() {
        return geoLocationEnabled;
    }

    /**
     * Sets whether geolocation is enabled for the user.
     *
     * @param geoLocationEnabled true to enable geolocation, false to disable it
     */
    public void setGeoLocationEnabled(Boolean geoLocationEnabled) {
        this.geoLocationEnabled = geoLocationEnabled;
    }

    /**
     * Retrieves the location of the event.
     * @return the location of the event
     */
    public Location getEventLocation() {
        return eventLocation;
    }

    /**
     * Sets the location of the event.
     * @param eventLocation the location of the event
     */
    public void setEventLocation(Location eventLocation) {
        this.eventLocation = eventLocation;
    }

    /**
     * Retrieves the map associating users to where they checked in
     * @return the map associating users to where they checked in
     */
    public HashMap<String, Location> getUserLocationPairs() {
        return userLocationPairs;
    }

    /**
     * Sets the map associating users to where they checked in
     * @param userLocationPairs the map associating users to where they checked in
     */
    public void setUserLocationPairs(HashMap<String, Location> userLocationPairs) {
        this.userLocationPairs = userLocationPairs;
    }

    public int getNextMilestone() {
        return nextMilestone;
    }

    public void setNextMilestone(int nextMilestone) {
        this.nextMilestone = nextMilestone;
    }
}
