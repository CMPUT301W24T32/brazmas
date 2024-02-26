package com.CMPUT301W24T32.brazmascheckin.models;

import com.CMPUT301W24T32.brazmascheckin.helper.Date;

/**
 * representation of Event
 */
public class Event {

    String name;
    Date date;
    String description;
    //TODO: add Image image
    //TODO: add Image QRCode
    //TODO: add Image shareQRCode
    //TODO: add List<Attendee> checkedInAttendees
    //TODO: add geolocation/event map

    public Event(String name, Date date, String description) {
        this.name = name;
        this.date = date;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
