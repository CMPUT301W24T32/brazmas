package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.google.firebase.firestore.DocumentReference;

public interface EventAddListener {
    void onEventAddSuccess(String ID);
    void onEventAddFailure(Exception e);
}
