package com.CMPUT301W24T32.brazmascheckin.controllers;

public interface GetFailureListener {
    /**
     * Listener interface for handling the failure of retrieving objects from the database.
     */
    public interface FailureListener {

        /**
         * Called when an error occurs during object retrieval.
         *
         * @param e the exception representing the error.
         */
        void onFailure(Exception e);
    }
}
