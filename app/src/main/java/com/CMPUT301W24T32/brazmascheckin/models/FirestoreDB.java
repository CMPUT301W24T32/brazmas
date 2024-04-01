package com.CMPUT301W24T32.brazmascheckin.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * The FirestoreDB class provides access to Firebase database instances and collection references through static methods.
 * It includes methods for obtaining the Firestore database instance, collection references for events and users,
 * as well as methods for interacting with Firebase Storage and deleting events.
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
    public static CollectionReference getEventsRef(FirebaseFirestore database) {
        return database.collection("events");
    }

    /**
     * This method provides a reference to the "users" collection
     * @return a CollectionReference for the "users" collection
     */
    public static CollectionReference getUsersRef(FirebaseFirestore database) {
        return database.collection("users");
    }

    /**
     * This method gets a reference from the administrator collection in the database.
     * @return the CollectionReference for the admin collection.
     */
    public static CollectionReference getAdminsRef(FirebaseFirestore database) {
        return database.collection("admins");
    }

    /**
     * This method gets an instance of Firebase Storage.
     * @return The FirebaseStorage instance.
     */
    public static FirebaseStorage getStorageInstance() {
        return FirebaseStorage.getInstance();
    }

    /**
     * This method gets a StorageReference for the specific path in Firebase Storage.
     * @param ref The specific path within Firebase Storage.
     * @return The StorageReference for the specified path.
     */
    public static StorageReference getStorageReference(FirebaseStorage storage, String ref) {
        return storage.getReference(ref);
    }

    /**
     * This method gets a DatabaseReference for the specified path in Firebase Realtime Database.
     * @param ref The path within the Firebase Realtime Database.
     * @return The DatabaseReference for the specified path.
     */
    public static DatabaseReference getDatabaseReference(String ref) {
        return FirebaseDatabase.getInstance().getReference(ref);
    }

}

