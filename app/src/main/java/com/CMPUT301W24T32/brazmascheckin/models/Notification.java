package com.CMPUT301W24T32.brazmascheckin.models;

import java.util.Date;

public class Notification {

    String name;
    String description;
    Date date;

    public void sendNotification(){

    }
    /**
     * This constructor...
     *
     *
     */
    public Notification() {
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





}
