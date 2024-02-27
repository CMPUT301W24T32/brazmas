package com.CMPUT301W24T32.brazmascheckin.models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class provides access to Firebase database instances and collection references
 * through static methods.
 */
public class FirestoreDB {

    /**
     * This method provides an instance of the Firestore database.
     * @return the FirebaseFirestore database instance
     */
    public static FirebaseFirestore getDatabaseInstance() {
        return FirebaseFirestore.getInstance();
    }

    /**
     * This method provides a reference to the "events" collection
     * @return a CollectionReference for the "events" collection
     */
    public static CollectionReference getEventsRef() {
        return FirebaseFirestore.getInstance().collection("events");
    }

    /**
     * This method provides a reference to the "attendees" collection
     * @return a CollectionReference for the "attendees" collection
     */
    public static CollectionReference getAttendeesRef() {
        return FirebaseFirestore.getInstance().collection("attendees");
    }

    //TODO: determine data flow for check-in/sign up between Attendee and Event

    //TODO: include static methods to create/update/destroy database entry attributes
}
