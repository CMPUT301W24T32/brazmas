package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the failure of delete operations.
 */
public interface DeleteFailureListener {

    /**
     * Called when an error occurs during a delete operation.
     *
     * @param e the exception representing the error.
     */
    void onDeleteFailure(Exception e);
}
