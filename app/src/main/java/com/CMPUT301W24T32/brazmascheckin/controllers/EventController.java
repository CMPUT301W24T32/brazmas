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
        if(listener != null) {
            eventsRef.add(event)
                    .addOnSuccessListener(listener::onEventAddSuccess)
                    .addOnFailureListener(listener::onEventAddFailure);
        } else {
            eventsRef.add(event);
        }
    }


    public void setEvent(Event event, EventSetListener listener) {
        String ID = event.getID();

        if(listener != null) {
            eventsRef.document(ID).set(event)
                    .addOnSuccessListener(temp -> listener.onEventSetSuccess())
                    .addOnFailureListener(listener::onEventSetFailure);
        } else {
            eventsRef.document(ID).set(event);
        }
    }

    public void deleteEvent(String ID, EventDeleteListener listener) {
        if(listener != null) {
            eventsRef.document(ID).delete()
                    .addOnSuccessListener(unused -> {
                        listener.onEventDeleteSuccess();;
                    })
                    .addOnFailureListener(listener::onEventDeleteFailure);
        } else {
            eventsRef.document(ID).delete();
        }
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
