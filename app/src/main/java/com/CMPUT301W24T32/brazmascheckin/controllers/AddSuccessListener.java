package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Interface for handling successful addition operations.
 *
 * @param <T> the type of object that was successfully added.
 */
public interface AddSuccessListener<T> {
    /**
     * Callback method invoked when an object is successfully added.
     *
     * @param object the object that was successfully added.
     */
    void onAddSuccess(T object);
}

