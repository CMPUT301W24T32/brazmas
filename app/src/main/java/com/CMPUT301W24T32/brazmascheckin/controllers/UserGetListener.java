package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.CMPUT301W24T32.brazmascheckin.models.User;

/**
 * Listener interface for handling the success and failure of retrieving a user from the database.
 */
public interface UserGetListener {

    /**
     * Called when the user retrieval is successful.
     *
     * @param user the retrieved user.
     */
    void onUserGetSuccess(User user);

    /**
     * Called when an error occurs during user retrieval.
     *
     * @param e the exception representing the error.
     */
    void onUserGetFailure(Exception e);
}

