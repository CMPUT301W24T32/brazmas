package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.Organizer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class EventController {
    private CollectionReference eventsRef;

    //TODO: If the callback function is not executed in the environment it was created in, no controller
    public EventController() {
        eventsRef = FirestoreDB.getEventsRef();
    }

    public void addEvent(Event event, OnSuccessListener<DocumentReference> successListener,
                         OnFailureListener failureListener) {
        eventsRef.add(event)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void setEvent(Event event, OnSuccessListener<Void> successListener,
                         OnFailureListener failureListener) {
        String ID = event.getID();
        eventsRef.document(ID).set(event)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void deleteEvent(Event event, OnSuccessListener<Void> successListener,
                            OnFailureListener failureListener) {
        String ID = event.getID();
        eventsRef.document(ID).delete()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void addListener(EventListener<QuerySnapshot> eventListener) {
        eventsRef.addSnapshotListener(eventListener);
    }
}
