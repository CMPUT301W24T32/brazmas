package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.ListenerRegistration;

public class EventController {
    private CollectionReference eventsRef;
    private Context context;

    public EventController(Context context) {
        this.context = context;
        eventsRef = FirestoreDB.getEventsRef();
    }

    public void addEvent(Event event, EventAddListener listener) {
        eventsRef.add(event)
                .addOnSuccessListener(listener::onEventAddSuccess)
                .addOnFailureListener(listener::onEventAddFailure);
    }

    /*
    Significance: can still have nested
       private void interaction() {
        eventController.interaction(new InteractionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
     */

    public void setEvent(Event event, EventSetListener listener) {
        String ID = event.getID();
        eventsRef.document(ID).set(event)
                .addOnSuccessListener(temp -> listener.onEventSetSuccess())
                .addOnFailureListener(listener::onEventSetFailure);
    }

    public void deleteEvent(String ID, EventDeleteListener listener) {
        eventsRef.document(ID).delete()
                .addOnSuccessListener(unused -> {
                    listener.onEventDeleteSuccess();;
                })
                .addOnFailureListener(listener::onEventDeleteFailure);
    }

    public ListenerRegistration addSnapshotListener(SnapshotListener listener) {
        return eventsRef.addSnapshotListener((value, error) -> {
            if(error != null) {
                listener.onError(error);
            }
            listener.snapshotListenerCallback(value);
        });
    }

}
