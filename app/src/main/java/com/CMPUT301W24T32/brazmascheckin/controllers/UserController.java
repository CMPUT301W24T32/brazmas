package com.CMPUT301W24T32.brazmascheckin.controllers;

import androidx.annotation.NonNull;

import com.CMPUT301W24T32.brazmascheckin.models.Attendee;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.Organizer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserController {
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    public UserController() {
        usersRef = FirestoreDB.getEventsRef();
    }

    public void setUser(String deviceID, Organizer user, UserListener listener) {
        usersRef.document(deviceID).set(user);
//                .addOnSuccessListener(unused -> {listener.onSuccess();})
//                .addOnFailureListener(e -> {listener.onError(e);});
    }

    public void getUser(String deviceID, UserListener listener) {
        usersRef.document(deviceID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        listener.onSuccess(documentSnapshot);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e);
                    }
                });
    }
}
