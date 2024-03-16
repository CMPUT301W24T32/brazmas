package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

public class EventController {
    private CollectionReference eventsRef;
    private Context context;

    public EventController(Context context) {
        this.context = context;
        eventsRef = FirestoreDB.getEventsRef();
    }

    public void addEvent(Event event, EventAddListener listener) {
        eventsRef.add(event)
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

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

    //TODO: create snapshot listener


}
