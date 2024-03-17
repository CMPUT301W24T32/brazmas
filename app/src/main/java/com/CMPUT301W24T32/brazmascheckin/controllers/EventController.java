package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

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
                    .addOnSuccessListener(documentReference -> {
                        String ID = documentReference.getId();
                        listener.onEventAddSuccess(ID);
                    })
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

    public void getEvent(String ID, EventGetListener listener) {
        eventsRef.document(ID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot != null) {
                        Event event = documentSnapshot.toObject(Event.class);
                        if(event != null) {
                            listener.onEventGetSuccess(event);
                        } else {
                            listener.onEventGetFailure(null);
                        }
                    } else {
                        listener.onEventGetFailure(null);
                    }
                })
                .addOnFailureListener(listener::onEventGetFailure);
    }

    public ListenerRegistration addSnapshotListener(SnapshotListener listener) {
        return eventsRef.addSnapshotListener((value, error) -> {
            if(error != null) {
                listener.onError(error);
            }
            ArrayList<Event> events = new ArrayList<>();
            for(QueryDocumentSnapshot doc : value) {
                Event event = doc.toObject(Event.class);
                events.add(event);
            }
            listener.snapshotListenerCallback(events);
        });
    }

    public ListenerRegistration addSingleSnapshotListener(String ID, SnapshotListener listener) {
        return eventsRef.document(ID).addSnapshotListener((value, error) -> {
            if(error != null) {
                listener.onError(error);
            }
            ArrayList<Event> events = new ArrayList<>();
            assert value != null;
            Event event = value.toObject(Event.class);
            events.add(event);
            listener.snapshotListenerCallback(events);
        });
    }

}
