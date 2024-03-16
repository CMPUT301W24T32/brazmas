package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;


import androidx.annotation.Nullable;

import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Controller to manage CRUD interactions between the View and the database.
 */
public class UserController {
    private Context context;
    private CollectionReference usersRef;

    public UserController(Context context) {
        this.context = context;
        usersRef = FirestoreDB.getUsersRef();
    }


    public void setUser(User user, UserSetListener listener) {
        String ID = user.getID();

        usersRef.document(ID).set(user)
                .addOnSuccessListener(temp -> listener.onUserSetSuccess())
                .addOnFailureListener(temp -> listener.onUserSetFailure());
    }

    public void getUser(String ID, UserGetListener listener) {
        usersRef.document(ID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        if(user != null) {
                            listener.onUserGetSuccess(user);
                        } else {
                            listener.onUserGetFailure(null);
                        }
                    } else {
                        listener.onUserGetFailure(null);
                    }
                })
                .addOnFailureListener(listener::onUserGetFailure);
    }

    public void deleteUser(User user, UserDeleteListener listener) {
        String ID = user.getID();
        usersRef.document(ID).delete()
                .addOnSuccessListener(temp -> listener.onUserDeleteSuccess())
                .addOnFailureListener(temp -> listener.onUserDeleteFailure());
    }

    public ListenerRegistration addSnapshotListener(SnapshotListener listener) {
        return usersRef.addSnapshotListener((value, error) -> {
            if(error != null) {
                listener.onError(error);
            }
            listener.snapshotListenerCallback(value);
        });
    }

}
