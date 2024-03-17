package com.CMPUT301W24T32.brazmascheckin.controllers;


import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public interface SnapshotListener<T> {
    void snapshotListenerCallback(ArrayList<T> values);

    void onError(Exception e);
}
