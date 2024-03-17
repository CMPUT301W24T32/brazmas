package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the success of retrieving objects from the database.
 *
 * @param <T> the type of object being retrieved.
 */
public interface GetSuccessListener<T>{
    /**
     * Called when the object retrieval is successful.
     *
     * @param object the retrieved object.
     */
    void onSuccess(T object);
}
