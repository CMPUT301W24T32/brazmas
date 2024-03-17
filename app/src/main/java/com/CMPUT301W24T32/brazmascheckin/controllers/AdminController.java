package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;

import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.firestore.CollectionReference;

/**
 * Controller responsible for managing interactions with the Firestore Database for admin-related operations.
 */
public class AdminController {
    private Context context;
    private CollectionReference adminsRef;

    /**
     * Constructs a new instance of the Admin Controller.
     *
     * @param context the context of the view where the controller is instantiated.
     */
    public AdminController(Context context) {
        this.context = context;
        adminsRef = FirestoreDB.getAdminsRef();
    }

    /**
     * Retrieves a document from the Admin collection.
     *
     * @param ID       the ID of the document to retrieve.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
    public void getAdmin(String ID, AdminGetListener listener) {
        adminsRef.document(ID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot != null && documentSnapshot.exists()) {
                        listener.onAdminGetSuccess();
                    } else {
                        listener.onAdminGetFailure(null);
                    }
                })
                .addOnFailureListener(listener::onAdminGetFailure);
    }
}
