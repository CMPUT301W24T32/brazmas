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

    public static CollectionReference getAdminsRef() {
        return getDatabaseInstance().collection("admins");
    }

    public static FirebaseStorage getStorageInstance() {
        return FirebaseStorage.getInstance();
    }
    public static StorageReference getStorageReference(String ref) {
        return getStorageInstance().getReference(ref);
    }

    public static DatabaseReference getDatabaseReference(String ref) {
        return FirebaseDatabase.getInstance().getReference(ref);
    }

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
