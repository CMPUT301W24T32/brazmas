package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing interactions with events in the Firestore Database.
 */
public class EventController {
    private CollectionReference eventsRef;

    /**
     * Constructs a new instance of the Event Controller.
     * @param database Dependency Injection of FirebaseFirestore database
     */
    public EventController(FirebaseFirestore database) {
        this.eventsRef = FirestoreDB.getEventsRef(database);
    }

    /**
     * Adds an event to the Firestore Database.
     * @param event           the event to be added.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void addEvent(Event event, AddSuccessListener<String> successListener,
                         AddFailureListener failureListener) {
        eventsRef.add(event)
                .addOnSuccessListener(documentReference -> {
                    if(documentReference != null) {
                        String ID = documentReference.getId();
                        if(successListener != null) {
                            successListener.onAddSuccess(ID);
                        }
                    } else if (failureListener != null) {
                        failureListener.onAddFailure(null);
                    }
                })
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onAddFailure(e);
                    }
                });
    }


    /**
     * Updates an existing event in the Firestore Database.
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

    /**
     * Retrieves the QR cods IDs of all events from the Firestore Database.
     * @param listener a listener to handle success callbacks for the operation.
     */
    public void getAllEventQRCodeIDs(GetSuccessListener<List<String>> listener) {

        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> qrCodeIDs = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Event event = documentSnapshot.toObject(Event.class);
                if (event != null) {
                    String qrCodeID = event.getQRCode();
                    if (qrCodeID != null && !qrCodeID.isEmpty()) {
                        qrCodeIDs.add(qrCodeID);
                    }
                }
            }
            if (listener != null) {
                listener.onSuccess(qrCodeIDs);
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }

    /**
     * Retrieves all events from the Firestore Database that have geo-location enabled.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void getAllEvents(GetSuccessListener<List<Event>> successListener, GetFailureListener
                             failureListener) {
        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<Event> events = new ArrayList<>();
            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Event event = documentSnapshot.toObject(Event.class);
                if(event != null && event.getGeoLocationEnabled()) {
                    events.add(event);
                }
            }
            if(successListener != null) {
                successListener.onSuccess(events);
            }
        })
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onFailure(e);
                    }
                });
    }

    /**
     * Retrieves all events from the Firestore Database.
     * @param successListener successListener a listener to handle success callbacks for the operation.
     * @param failureListener successListener a listener to handle failure callbacks for the operation.
     */
    public void getAllEventsReg(GetSuccessListener<List<Event>> successListener, GetFailureListener
            failureListener) {
        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Event> events = new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Event event = documentSnapshot.toObject(Event.class);
                        if(event != null) {
                            events.add(event);
                        }
                    }
                    if(successListener != null) {
                        successListener.onSuccess(events);
                    }
                })
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onFailure(e);
                    }
                });
    }
}
