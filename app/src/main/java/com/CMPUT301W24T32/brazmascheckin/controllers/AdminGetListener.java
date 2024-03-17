package com.CMPUT301W24T32.brazmascheckin.controllers;


//TODO: CITE CHATGPT FOR COMMENTS
/**
 * Callback interface for handling the success and failure of retrieving admin data.
 */
public interface AdminGetListener {
    /**
     * Called when admin data is successfully retrieved.
     */
    void onAdminGetSuccess();

    /**
     * Called when an error occurs while retrieving admin data.
     *
     * @param e The exception representing the error.
     */
    void onAdminGetFailure(Exception e);
}
