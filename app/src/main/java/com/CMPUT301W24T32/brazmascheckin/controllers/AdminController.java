package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;

import com.CMPUT301W24T32.brazmascheckin.models.Admin;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * Controller responsible for managing interactions with the Firestore Database for admin-related operations.
 */
public class AdminController {
    private CollectionReference adminsRef;

    /**
     * Constructs a new instance of the Admin Controller.
     *
     * @param database Dependency Injection for FirebaseFirestore database
     */
    public AdminController(FirebaseFirestore database) {
        this.adminsRef = FirestoreDB.getAdminsRef(database);
    }

    /**
     * Retrieves a document from the Admin collection.
     *
     * @param ID             the ID of the document to retrieve.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void getAdmin(String ID, GetSuccessListener<Admin> successListener,
                         GetFailureListener failureListener) {
        adminsRef.document(ID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot != null && documentSnapshot.exists()) {
                        if(successListener != null) {
                            successListener.onSuccess(null);
                        }
                    } else {
                        if(failureListener != null) {
                            failureListener.onFailure(null);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onFailure(e);
                    }
                });
    }

    /**
     * To add an admin.
     * @param admin
     * @param successListener
     * @param failureListener
     */
    public void addAdmin(Admin admin, AddSuccessListener<String> successListener,
                         AddFailureListener failureListener) {
        adminsRef.add(admin)
                .addOnSuccessListener(documentReference -> {
                    if (documentReference != null) {
                        String ID = documentReference.getId();
                        if (successListener != null) {
                            successListener.onAddSuccess(ID);
                        }
                    } else if (failureListener != null) {
                        failureListener.onAddFailure(null);
                    }
                })
                .addOnFailureListener(e -> {
                    if (failureListener != null) {
                        failureListener.onAddFailure(e);
                    }
                });
    }

    /**
     * Sets an admin in the Firestore Database.
     * @param admin
     * @param successListener
     * @param failureListener
     */
    public void setAdmin(Admin admin, SetSuccessListener successListener,
                         SetFailureListener failureListener) {
        String ID = admin.getID();
        adminsRef.document(ID).set(admin)
                .addOnSuccessListener(temp -> {
                    if (successListener != null) {
                        successListener.onSetSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    if (failureListener != null) {
                        failureListener.onSetFailure(e);
                    }
                });
    }

    public ListenerRegistration addSnapshotListener(SnapshotListener listener) {
        return adminsRef.addSnapshotListener((value, error) -> {
            if(error != null) {
                listener.onError(error);
            }
            ArrayList<Admin> admins = new ArrayList<>();
            for(QueryDocumentSnapshot doc : value) {
                Admin admin = doc.toObject(Admin.class);
                admins.add(admin);
            }
            listener.snapshotListenerCallback(admins);
        });
    }
}
