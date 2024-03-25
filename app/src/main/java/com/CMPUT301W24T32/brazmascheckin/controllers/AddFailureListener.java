package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Interface for handling failure events during addition operations.
 */
public interface AddFailureListener {
    /**
     * Callback method invoked when a failure occurs during addition.
     *
     * @param e the exception indicating the cause of the failure.
     */
    void onAddFailure(Exception e);
}
