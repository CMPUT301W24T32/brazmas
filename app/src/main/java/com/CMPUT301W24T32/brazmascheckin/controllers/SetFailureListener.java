package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the failure of set operations.
 */
public interface SetFailureListener {

    /**
     * Called when an error occurs during a set operation.
     *
     * @param e the exception representing the error.
     */
    void onSetFailure(Exception e);
}
