package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the success and failure of deleting events.
 */
public interface EventDeleteListener {
    /**
     * Called when an event is successfully deleted.
     */
    void onEventDeleteSuccess();

    /**
     * Called when an error occurs while deleting an event.
     *
     * @param e The exception representing the error.
     */
    void onEventDeleteFailure(Exception e);
}
