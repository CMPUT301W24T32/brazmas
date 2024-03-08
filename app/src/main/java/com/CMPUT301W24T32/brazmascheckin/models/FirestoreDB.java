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
    public static CollectionReference getEventsRef() {
        return getDatabaseInstance().collection("events");
    }

    /**
     * This method provides a reference to the "users" collection
     * @return a CollectionReference for the "users" collection
     */
    public static CollectionReference getUsersRef() {
        return getDatabaseInstance().collection("users");
    }

    /**
     * This method gets a reference from the administrator collection in the database.
     * @return the CollectionReference for the admin collection.
     */
    public static CollectionReference getAdminsRef() {
        return getDatabaseInstance().collection("admins");
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
    public static StorageReference getStorageReference(String ref) {
        return getStorageInstance().getReference(ref);
    }

    /**
     * This method gets a DatabaseReference for the specified path in Firebase Realtime Database.
     * @param ref The path within the Firebase Realtime Database.
     * @return The DatabaseReference for the specified path.
     */
    public static DatabaseReference getDatabaseReference(String ref) {
        return FirebaseDatabase.getInstance().getReference(ref);
    }

    /**
     * This method deletes an event along with its associated images from the database.
     * @param eventID The ID of the event document in the "events" collection.
     * @param posterID The ID of the poster image stored in Firebase Storage.
     * @param qrCodeID The ID of the QR code image stored in Firebase Storage.
     */
    public static void deleteEvent(String eventID, String posterID, String qrCodeID) {
        // Delete the event document from the "events" collection
        getEventsRef().document(eventID)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Event document deleted successfully
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occurred while deleting the event document
                });

        // Delete the poster image from the storage
        deleteImageFromStorage(posterID);

        // Delete the QR code image from the storage
        deleteImageFromStorage(qrCodeID);
    }

    /**
     * This method deletes an image from Firebase Storage.
     * @param imageID The ID of the image to be deleted.
     */
    private static void deleteImageFromStorage(String imageID) {
        if (imageID != null) {
            StorageReference storage = getStorageReference("uploads");
            StorageReference imageRef = storage.child(imageID);

            imageRef.delete()
                    .addOnSuccessListener(aVoid -> {
                        // Image deleted successfully
                    })
                    .addOnFailureListener(exception -> {
                        // Handle any errors that occurred while deleting the image
                    });
        }
    }
}

