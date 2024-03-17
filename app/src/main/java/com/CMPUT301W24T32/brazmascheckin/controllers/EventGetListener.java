package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.CMPUT301W24T32.brazmascheckin.models.Event;

/**
 * Listener interface for handling the success and failure of retrieving events.
 */
public interface EventGetListener {
    /**
     * Called when an event is successfully retrieved.
     *
     * @param event The retrieved event.
     */
    void onEventGetSuccess(Event event);
    /**
     * Called when an error occurs while retrieving an event.
     *
     * @param e The exception representing the error.
     */
    void onEventGetFailure(Exception e);
}
