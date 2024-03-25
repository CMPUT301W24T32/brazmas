package com.CMPUT301W24T32.brazmascheckin.controllers;

import java.util.ArrayList;

/**
 * Listener interface for handling snapshot data and errors from Firestore queries.
 *
 * @param <T> the type of data contained in the snapshot.
 */
public interface SnapshotListener<T> {

    /**
     * Called when snapshot data is available.
     *
     * @param values the list of snapshot values.
     */
    void snapshotListenerCallback(ArrayList<T> values);

    /**
     * Called when an error occurs during snapshot retrieval.
     *
     * @param e The exception representing the error.
     */
    void onError(Exception e);
}
