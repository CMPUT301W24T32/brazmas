package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;

import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.firestore.CollectionReference;

public class AdminController {
    private Context context;
    private CollectionReference adminsRef;

    public AdminController(Context context) {
        this.context = context;
        adminsRef = FirestoreDB.getAdminsRef();
    }

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
