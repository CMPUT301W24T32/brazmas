package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the failure of retrieving objects from the database.
 */
public interface GetFailureListener {

        /**
         * Called when an error occurs during object retrieval.
         *
         * @param e the exception representing the error.
         */
        void onFailure(Exception e);

}
