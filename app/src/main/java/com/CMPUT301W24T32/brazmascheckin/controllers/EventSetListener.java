package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the success and failure of updating events.
 */
public interface EventSetListener {

    /**
     * Called when an event is successfully updated.
     */
    void onEventSetSuccess();

    /**
     * Called when an error occurs while updating an event.
     *
     * @param e The exception representing the error.
     */
    void onEventSetFailure(Exception e);
}
