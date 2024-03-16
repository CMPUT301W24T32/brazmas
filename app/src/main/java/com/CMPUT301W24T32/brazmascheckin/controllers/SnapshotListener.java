package com.CMPUT301W24T32.brazmascheckin.controllers;


import com.google.firebase.firestore.QuerySnapshot;

public interface SnapshotListener {
    void snapshotListenerCallback(QuerySnapshot value);

    void onError(Exception e);
}
