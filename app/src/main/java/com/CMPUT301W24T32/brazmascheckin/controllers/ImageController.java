package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;
import android.net.Uri;

import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing image-related operations in Firebase Storage.
 */
public class ImageController {

    private StorageReference posterReference;
    private StorageReference qrCodeReference;
    private StorageReference profilePictureReference;
    private StorageReference shareqrCodeReference;
    private StorageReference defaultProfilePictureReference;
    private static final String POSTER_PATH = "posters";
    private static final String QR_CODE_PATH = "qr_codes";
    private static final String PROFILE_PICTURE_PATH = "profile_pictures";
    private static final String SHARE_QR_CODE_PATH = "ShareQRCodes";
    public static final String PROFILE_PICTURE = "PROFILE_PICTURE";
    public static final String EVENT_POSTER = "EVENT_POSTER";
    public static final String QR_CODE ="QR_CODE";
    public static final String SHARE_QR_CODE = "SHARE_QR_CODE";
    public static final String DEFAULT_PROFILE_PICTURE_PATH = "default_profile_pictures";

    /**
     * Constructs a new instance of the Image Controller.
     *
     * @param storage Dependency Injection of FirebaseStorage
     */
    public ImageController(FirebaseStorage storage) {
        posterReference = FirestoreDB.getStorageReference(storage, POSTER_PATH);
        qrCodeReference = FirestoreDB.getStorageReference(storage, QR_CODE_PATH);
        profilePictureReference = FirestoreDB.getStorageReference(storage, PROFILE_PICTURE_PATH);
        shareqrCodeReference = FirestoreDB.getStorageReference(storage, SHARE_QR_CODE_PATH);
        defaultProfilePictureReference = FirestoreDB.getStorageReference(storage, DEFAULT_PROFILE_PICTURE_PATH);
    }

    public void uploadImage(String TYPE, String fileID,
                            Uri imageURI, AddSuccessListener<Uri> successListener,
                            AddFailureListener failureListener) {
        StorageReference fileReference;
        if(TYPE.equals(EVENT_POSTER)) {
            fileReference = posterReference.child(fileID);
        } else if (TYPE.equals(PROFILE_PICTURE)) {
            fileReference = profilePictureReference.child(fileID);
        } else if (TYPE.equals(DEFAULT_PROFILE_PICTURE_PATH)){
                fileReference = defaultProfilePictureReference.child(fileID);
        } else {
            fileReference = null;
            //TODO: add proper error checking
            return;
        }

        fileReference.putFile(imageURI)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            if(successListener != null) {
                                successListener.onAddSuccess(uri);
                            }
                        }))
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onAddFailure(e);
                    }
                });

    }


    /**
     * Uploads a QR code image to Firebase Storage.
     *
     * @param fileID         the ID of the file.
     * @param imageData      the byte array of image data.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void uploadQRCode(String type, String fileID, byte[] imageData, AddSuccessListener<Uri> successListener,
                             AddFailureListener failureListener) {
        StorageReference fileReference;
        if (type == "CHECK-IN") {
            fileReference = qrCodeReference.child(fileID);
        }
        else {
            fileReference = shareqrCodeReference.child(fileID);
        }
        fileReference.putBytes(imageData)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            if(successListener != null) {
                                successListener.onAddSuccess(uri);
                            }
                        })
                        .addOnFailureListener(e -> {
                            if(failureListener != null) {
                                failureListener.onAddFailure(e);
                            }
                        }))
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onAddFailure(e);
                    }
                });
    }

    /**
     * Retrieves an image from Firebase Storage based on the specified type and file ID.
     *
     * @param TYPE            the type of image to retrieve (e.g., EVENT_POSTER, PROFILE_PICTURE, QR_CODE).
     * @param fileID          the ID of the file.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void getImage(String TYPE, String fileID, GetSuccessListener<byte[]> successListener,
                         AddFailureListener failureListener) {
        StorageReference imageReference;
        if(TYPE.equals(EVENT_POSTER)) {
            imageReference = posterReference.child(fileID);
        } else if (TYPE.equals(PROFILE_PICTURE)) {
            imageReference = profilePictureReference.child(fileID);
        } else if (TYPE.equals(QR_CODE)) {
            imageReference = qrCodeReference.child(fileID);
        } else if (TYPE.equals(SHARE_QR_CODE)) {
            imageReference = shareqrCodeReference.child(fileID);
        } else if (TYPE.equals(DEFAULT_PROFILE_PICTURE_PATH)){
            imageReference = defaultProfilePictureReference.child(fileID);
        } else {
            imageReference = null;
            return;
        }
        imageReference.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(bytes -> {
                    if(successListener != null) {
                        successListener.onSuccess(bytes);
                    }
                })
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onAddFailure(e);
                    }
                });
    }

    public void getImageURL(String TYPE, String fileID, GetSuccessListener<String> successListener,
                            AddFailureListener failureListener) {
        StorageReference imageReference;
        if(TYPE.equals(EVENT_POSTER)) {
            imageReference = posterReference.child(fileID);
        } else if (TYPE.equals(PROFILE_PICTURE)) {
            imageReference = profilePictureReference.child(fileID);
        } else if (TYPE.equals(QR_CODE)) {
            imageReference = qrCodeReference.child(fileID);
        } else if (TYPE.equals(SHARE_QR_CODE)) {
            imageReference = shareqrCodeReference.child(fileID);
        } else {
            imageReference = null;
            return;
        }
        imageReference.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    String imageURL = uri.toString();
                    if(successListener != null) {
                        successListener.onSuccess(imageURL);
                    }
                })
                .addOnFailureListener(e -> {
                    if(failureListener != null) {
                        failureListener.onAddFailure(e);
                    }
                });
    }
    /**
     * Deletes an image from Firebase Storage based on the specified type and file ID.
     *
     * @param TYPE            the type of image to delete (e.g., EVENT_POSTER, PROFILE_PICTURE, QR_CODE).
     * @param fileID          the ID of the file.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void deleteImage(String TYPE, String fileID, DeleteSuccessListener successListener,
                            DeleteFailureListener failureListener) {
        StorageReference imageReference;
        if(TYPE.equals(EVENT_POSTER)) {
            imageReference = posterReference.child(fileID);
        } else if (TYPE.equals(PROFILE_PICTURE)) {
            imageReference = profilePictureReference.child(fileID);
        } else if (TYPE.equals(QR_CODE)) {
            imageReference = qrCodeReference.child(fileID);
        } else {
            imageReference = null;
            return;
        }

        imageReference.delete()
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
     * This method retrieves the file IDs of all QR Codes stored in Firebase Storage.
     * @param listener to handle success of providing a list of file IDs.
     */
    public void getAllQRCodeFileIDs(GetSuccessListener<List<String>> listener) {

        qrCodeReference.listAll().addOnSuccessListener(listResult -> {
            List<String> qrCodeFileIDs = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                String fileID = item.getName();
                qrCodeFileIDs.add(fileID);
            }
            if (listener != null) {
                listener.onSuccess(qrCodeFileIDs);
            }
        }).addOnFailureListener(e -> {
        });
    }

    /**
     * This method retrieves the file IDs of all profile pictures stored in Firebase Storage.
     * @param listener to handle success of providing a list of file IDs.
     */
    public void getAllProfilePicFileIDs(GetSuccessListener<List<String>> listener) {
        profilePictureReference.listAll().addOnSuccessListener(listResult -> {
            List<String> profilePicFileIDs = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                String fileID = item.getName();
                profilePicFileIDs.add(fileID);
            }

            if(listener != null) {
                listener.onSuccess(profilePicFileIDs);
            }
        }).addOnFailureListener(e -> {
        });
    }

    /**
     * This method retrieves the file IDs of all event posters stored in Firebase Storage.
     * @param listener to handle success of providing a list of file IDs.
     */
    public void getAllPosterFileIDs(GetSuccessListener<List<String>> listener) {
        posterReference.listAll().addOnSuccessListener(listResult -> {
            List<String> posterFileIDs = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                String fileID = item.getName();
                posterFileIDs.add(fileID);
            }

            if (listener != null) {
                listener.onSuccess(posterFileIDs);
            }
        }).addOnFailureListener(e -> {
        });
    }

}
