package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;


import androidx.annotation.Nullable;

import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Controller responsible for managing CRUD interactions between the View and the database.
 */
public class UserController {
    private Context context;
    private CollectionReference usersRef;

    /**
     * Constructs a new instance of the UserController.
     *
     * @param context the context of the view where the controller is instantiated.
     */
    public UserController(Context context) {
        this.context = context;
        usersRef = FirestoreDB.getUsersRef();
    }


    /**
     * Sets a user in the Firestore Database.
     *
     * @param user     the user to be set.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
    public void setUser(User user, UserSetListener listener) {
        String ID = user.getID();
        if(listener != null) {
            usersRef.document(ID).set(user)
                    .addOnSuccessListener(temp -> listener.onUserSetSuccess())
                    .addOnFailureListener(temp -> listener.onUserSetFailure());
        } else {
            usersRef.document(ID).set(user);
        }
    }

    /**
     * Retrieves a user from the Firestore Database.
     *
     * @param ID       the ID of the user to be retrieved.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
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

    /**
     * Deletes a user from the Firestore Database.
     *
     * @param user     the user to be deleted.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
    public void deleteUser(User user, UserDeleteListener listener) {
        String ID = user.getID();

        if(listener != null) {
            usersRef.document(ID).delete()
                    .addOnSuccessListener(temp -> listener.onUserDeleteSuccess())
                    .addOnFailureListener(temp -> listener.onUserDeleteFailure());
        } else {
            usersRef.document(ID).delete();
        }
    }

    /**
     * Adds a snapshot listener to the users collection in the Firestore Database.
     *
     * @param listener a listener to handle snapshot data and errors.
     * @return the registration object for the listener.
     */
    public ListenerRegistration addSnapshotListener(SnapshotListener listener) {
        return usersRef.addSnapshotListener((value, error) -> {
            if(error != null) {
                listener.onError(error);
            }
            ArrayList<User> users = new ArrayList<>();
            for(QueryDocumentSnapshot doc : value) {
                User user = doc.toObject(User.class);
                users.add(user);
            }
            listener.snapshotListenerCallback(new ArrayList());
        });
    }

}
