package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public interface UserListener {
    void onSuccess(DocumentSnapshot documentSnapshot);
    void onError(Exception e);
}
