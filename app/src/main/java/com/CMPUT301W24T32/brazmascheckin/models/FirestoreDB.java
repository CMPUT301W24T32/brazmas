package com.CMPUT301W24T32.brazmascheckin.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
     * This method provides a reference to the "users" collection
     * @return a CollectionReference for the "users" collection
     */
    public static CollectionReference getUsersRef() {
        return FirebaseFirestore.getInstance().collection("users");
    }


    public static void updateEvent(Event updatedEvent) {
        getEventsRef().document(updatedEvent.getID()).set(updatedEvent)
                .addOnSuccessListener(unused -> {

                })
                .addOnFailureListener(e -> {

                });
    }

    public static void updateUser(Attendee user) {
        //TODO: need to add ID to Attendee
//        getUsersRef().document(user.getID()).set(user)
//                .addOnSuccessListener(unused -> {
//
//                })
//                .addOnFailureListener(e -> {
//
//                });
    }

    //TODO: include static methods to create/update/destroy database entry attributes
}
