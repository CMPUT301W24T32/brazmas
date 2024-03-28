package com.CMPUT301W24T32.brazmascheckin.models;


import com.CMPUT301W24T32.brazmascheckin.helper.Date;

import java.io.Serializable;

/**
 * Represents an announcement related to a specific event.
 * This class encapsulates information about an announcement, including its name,
 * description, date, and associated event ID. It also provides methods for sending
 * notifications related to the announcement.
 */
public class Announcement implements Serializable {

    private String name;
    private String description;
    private Date date;

    private String eventID; //not needed field
    private long timeCreated;



    public void sendNotification(){

    }
    /**
     * This constructor is for creating an Announcement object.
     * @param name The name of announcement.
     * @param description The description of announcement.
     * @param date The date of the announcement.
     * @ eventID The ID of the event related to the announcement.
     */
    public Announcement(String name, String description, Date date, String eventID, long timeCreated) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.eventID = eventID;
        this.timeCreated = timeCreated;
    }

    /**
     * The default constructor.
     */
    public Announcement() {

    }

    /**
     * To get the name of announcement.
     * @return the name of the announcement.
     */
    public String getName() {
        return name;
    }

    /**
     * To set the name of announcement.
     * @param name the name that will be set of announcement.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * To get the description of announcement.
     * @return The description of announcement.
     */
    public String getDescription() {
        return description;
    }

    /**
     * To set the description of announcement.
     * @param description what the announcement's description will be set to.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * To get the date of the announcement.
     * @return The date of announcement.
     */
    public Date getDate() {
        return date;
    }

    /**
     * To set the date of announcement.
     * @param date the date which announcement will be set to.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * To get the event ID of announcement.
     * @return The event ID of announcement.
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * To set the event ID of announcement.
     * @param eventID The event ID that will be set for announcement.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public long getTimeCreated() {return timeCreated;}

    public void setTimeCreated(long timeCreated) {this.timeCreated = timeCreated;}
}
