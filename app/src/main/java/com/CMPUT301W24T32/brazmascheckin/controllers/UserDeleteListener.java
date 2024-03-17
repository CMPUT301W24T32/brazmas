package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the success and failure of deleting a user from the database.
 */
public interface UserDeleteListener {

    /**
     * Called when the user deletion is successful.
     */
    void onUserDeleteSuccess();

    /**
     * Called when an error occurs during user deletion.
     */
    void onUserDeleteFailure();
}
