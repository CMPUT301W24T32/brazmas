package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the success and failure of adding events.
 */
public interface EventAddListener {
    /**
     * Called when an event is successfully added.
     *
     * @param ID the ID of the added event.
     */
    void onEventAddSuccess(String ID);

    /**
     * Called when an error occurs while adding an event.
     *
     * @param e The exception representing the error.
     */
    void onEventAddFailure(Exception e);
}
