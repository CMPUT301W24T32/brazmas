package com.CMPUT301W24T32.brazmascheckin.models;

import java.util.Date;

public class Announcement {

    private String name;
    private String description;
    private Date date;
    private String eventID;

    public void sendNotification(){

    }
    /**
     * This constructor...
     *
     *
     */
    public Announcement(String name, String description, Date date, String eventID) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.eventID = eventID;
    }

    public Announcement() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
