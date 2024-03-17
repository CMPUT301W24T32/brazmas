package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the success and failure of setting a user in the database.
 */
public interface UserSetListener {

    /**
     * Called when the user setting operation is successful.
     */
    void onUserSetSuccess();

    /**
     * Called when an error occurs during user setting operation.
     */
    void onUserSetFailure();
}
