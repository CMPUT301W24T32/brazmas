package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing CRUD interactions between the View and the database.
 */
public class UserController {

    private CollectionReference usersRef;

    /**
     * Constructs a new instance of the UserController.
     * @param database Dependency Injection of FirebaseFirestore database
     */
    public UserController(FirebaseFirestore database) {
        this.usersRef = FirestoreDB.getUsersRef(database);
    }


    /**
     * Sets a user in the Firestore Database.
     * @param user            the user object to be set.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void setUser(User user, SetSuccessListener successListener,
                        SetFailureListener failureListener) {
        String ID = user.getID();
        usersRef.document(ID).set(user)
                .addOnSuccessListener(temp -> {
                    if (successListener != null) {
                        successListener.onSetSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    if (failureListener != null) {
                        failureListener.onSetFailure(e);
                    }
                });
    }

    /**
     * Adds a user to the Firestore Database.
     * @param user the user object to be added.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void addUser(User user, AddSuccessListener<String> successListener,
                         AddFailureListener failureListener) {
        usersRef.add(user)
                .addOnSuccessListener(documentReference -> {
                    if (documentReference != null) {
                        String ID = documentReference.getId();
                        if (successListener != null) {
                            successListener.onAddSuccess(ID);
                        }
                    } else if (failureListener != null) {
                        failureListener.onAddFailure(null);
                    }
                })
                .addOnFailureListener(e -> {
                    if (failureListener != null) {
                        failureListener.onAddFailure(e);
                    }
                });
    }

    /**
     * Retrieves a user from the Firestore Database.
     * @param ID             the ID of the user to be retrieved.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void getUser(String ID, GetSuccessListener<User> successListener,
                        GetFailureListener failureListener) {
        usersRef.document(ID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        if(user != null && successListener != null) {
                            successListener.onSuccess(user);
                        } else if (failureListener != null) {
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
     * Deletes a user from the Firestore Database.
     * @param user            the user to be deleted.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void deleteUser(User user, DeleteSuccessListener successListener,
                           DeleteFailureListener failureListener) {
        String ID = user.getID();


        usersRef.document(ID).delete()
                .addOnSuccessListener(temp -> {
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
     * Adds a snapshot listener to the users collection in the Firestore Database.
     * @param listener a listener to handle snapshot data and errors.
     * @return the registration object for the listener.
     */
    public ListenerRegistration addSnapshotListener(SnapshotListener listener) {
        return usersRef.addSnapshotListener((value, error) -> {
            if(error != null) {
                listener.onError(error);
            }
            ArrayList<User> users = new ArrayList<>();
            for(QueryDocumentSnapshot doc : value) {
                User user = doc.toObject(User.class);
                users.add(user);
            }
            listener.snapshotListenerCallback(users);
        });
    }

    /**
     * Gets all users.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void getAllUsers(GetSuccessListener<List<User>> successListener, GetFailureListener
                            failureListener) {
        usersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<User> users = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    users.add(user);
                }
            }
            if (successListener != null) {
                successListener.onSuccess(users);
            }
        })
                .addOnFailureListener(e -> {
                    if (failureListener != null) {
                        failureListener.onFailure(e);
                    }
                });
    }
}
