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

/**
 * Controller responsible for managing interactions with events in the Firestore Database.
 */
public class EventController {
    private CollectionReference eventsRef;
    private Context context;

    /**
     * Constructs a new instance of the Event Controller.
     *
     * @param context the context of the view where the controller is instantiated.
     */
    public EventController(Context context) {
        this.context = context;
        eventsRef = FirestoreDB.getEventsRef();
    }

    /**
     * Adds a new event to the Firestore Database.
     *
     * @param event    the event to be added.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
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


    /**
     * Updates an existing event in the Firestore Database.
     *
     * @param event           the updated event.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void setEvent(Event event, SetSuccessListener successListener,
                         SetFailureListener failureListener) {
        String ID = event.getID();

        eventsRef.document(ID).set(event)
                .addOnSuccessListener(temp -> {
                   if(successListener != null) {
                       successListener.onSetSuccess();
                   }
                })
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onSetFailure(e);
                    }
                });
    }

    /**
     * Deletes an event from the Firestore Database.
     *
     * @param ID             the ID of the event to be deleted.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void deleteEvent(String ID, DeleteSuccessListener successListener,
                            DeleteFailureListener failureListener) {


        eventsRef.document(ID).delete()
                .addOnSuccessListener(unused -> {
                    if(successListener != null) {
                        successListener.onDeleteSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onDeleteFailure(e);
                    }
                });
    }

    /**
     * Retrieves an event from the Firestore Database.
     *
     * @param ID             the ID of the event to be retrieved.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void getEvent(String ID, GetSuccessListener<Event> successListener,
                         GetFailureListener failureListener) {
        eventsRef.document(ID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot != null) {
                        Event event = documentSnapshot.toObject(Event.class);
                        if(event != null && successListener != null) {
                            successListener.onSuccess(event);
                        } else if (failureListener != null){
                            failureListener.onFailure(null);
                        }
                    } else if (failureListener != null) {
                        failureListener.onFailure(null);
                    }
                })
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onFailure(e);
                    }
                });
    }

    /**
     * Adds a snapshot listener to the events collection in the Firestore Database.
     *
     * @param listener a listener to handle snapshot data and errors.
     * @return the registration object for the listener.
     */
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

    /**
     * Adds a single document snapshot listener for a specific event in the Firestore Database.
     *
     * @param ID       the ID of the event to listen for.
     * @param listener a listener to handle snapshot data and errors.
     * @return the registration object for the listener.
     */
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
