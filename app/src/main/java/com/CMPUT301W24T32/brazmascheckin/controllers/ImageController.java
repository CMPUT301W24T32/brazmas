package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;
import android.net.Uri;


import androidx.annotation.NonNull;

import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Controller responsible for managing image-related operations in Firebase Storage.
 */
public class ImageController {

    private StorageReference posterReference;
    private StorageReference qrCodeReference;
    private StorageReference profilePictureReference;
    private Context context;
    private static final String POSTER_PATH = "posters";
    private static final String QR_CODE_PATH = "qr_codes";
    private static final String PROFILE_PICTURE_PATH = "profile_pictures";
    public static final String PROFILE_PICTURE = "PROFILE_PICTURE";
    public static final String EVENT_POSTER = "EVENT_POSTER";

    /**
     * Constructs a new instance of the Image Controller.
     *
     * @param context the context of the view where the controller is instantiated.
     */
    public ImageController(Context context) {
        this.context = context;
        posterReference = FirestoreDB.getStorageReference(POSTER_PATH);
        qrCodeReference = FirestoreDB.getStorageReference(QR_CODE_PATH);
        profilePictureReference = FirestoreDB.getStorageReference(PROFILE_PICTURE_PATH);
    }

    public void uploadImage(String TYPE, String fileID,
                            Uri imageURI, AddSuccessListener<Uri> successListener,
                            AddFailureListener failureListener) {
        StorageReference fileReference;
        if(TYPE.equals(EVENT_POSTER)) {
            fileReference = posterReference.child(fileID);
        } else if (TYPE.equals(PROFILE_PICTURE)) {
            fileReference = profilePictureReference.child(fileID);
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

//    /**
//     * Uploads an event poster image to Firebase Storage.
//     *
//     * @param fileID   the ID of the file.
//     * @param imageURI the URI of the image file.
//     * @param listener a listener to handle success and failure callbacks for the operation.
//     */
//    public void uploadEventPoster(String fileID, Uri imageURI, ImageUploadListener listener) {
//        StorageReference fileReference = posterReference.child(fileID);
//        fileReference.putFile(imageURI)
//                .addOnSuccessListener(askSnapshot -> fileReference.getDownloadUrl()
//                        .addOnSuccessListener(listener::onImageUploadSuccess)
//                        .addOnFailureListener(listener::onImageUploadFailure))
//                .addOnFailureListener(listener::onImageUploadFailure);
//    }

    /**
     * Uploads a QR code image to Firebase Storage.
     *
     * @param fileID         the ID of the file.
     * @param imageData      the byte array of image data.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public void uploadQRCode(String fileID, byte[] imageData, AddSuccessListener<Uri> successListener,
                             AddFailureListener failureListener) {
        StorageReference fileReference = qrCodeReference.child(fileID);
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

//    /**
//     * Uploads a profile picture image to Firebase Storage.
//     *
//     * @param fileID   the ID of the file.
//     * @param imageURI the URI of the image file.
//     * @param listener a listener to handle success and failure callbacks for the operation.
//     */
//    public void uploadProfilePicture(String fileID, Uri imageURI, ImageUploadListener listener) {
//        StorageReference fileReference = profilePictureReference.child(fileID);
//        fileReference.putFile(imageURI)
//                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
//                        .addOnSuccessListener(listener::onImageUploadSuccess)
//                        .addOnFailureListener(listener::onImageUploadFailure))
//                .addOnFailureListener(listener::onImageUploadFailure);
//    }

    /**
     * Retrieves an event poster image from Firebase Storage.
     *
     * @param fileID   the ID of the file.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
    public void getEventPoster(String fileID, ImageGetListener listener) {
        StorageReference imageReference = posterReference.child(fileID);
        imageReference.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(listener::onImageGetSuccess)
                .addOnFailureListener(listener::onImageGetFailure);
    }

    /**
     * Retrieves a QR code image from Firebase Storage.
     *
     * @param fileID   the ID of the file.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
    public void getQRCode(String fileID, ImageGetListener listener) {
        StorageReference imageReference = qrCodeReference.child(fileID);
        imageReference.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(listener::onImageGetSuccess)
                .addOnFailureListener(listener::onImageGetFailure);
    }

    /**
     * Retrieves a profile picture image from Firebase Storage.
     *
     * @param fileID   the ID of the file.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
    public void getProfilePicture(String fileID, ImageGetListener listener) {
        StorageReference imageReference = profilePictureReference.child(fileID);
        imageReference.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(listener::onImageGetSuccess)
                .addOnFailureListener(listener::onImageGetFailure);
    }

    /**
     * Deletes an event poster image from Firebase Storage.
     *
     * @param fileID   the ID of the file.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
    public void deleteEventPoster(String fileID, ImageDeleteListener listener) {
        StorageReference imageReference = posterReference.child(fileID);
        imageReference.delete()
                .addOnSuccessListener(unused -> listener.onImageDeleteSuccess())
                .addOnFailureListener(unused -> listener.onImageDeleteFailure());
    }

    /**
     * Deletes a QR code image from Firebase Storage.
     *
     * @param fileID   the ID of the file.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
    public void deleteQRCode(String fileID, ImageDeleteListener listener) {
        StorageReference imageReference = qrCodeReference.child(fileID);
        imageReference.delete()
                .addOnSuccessListener(unused -> listener.onImageDeleteSuccess())
                .addOnFailureListener(unused -> listener.onImageDeleteFailure());
    }

    /**
     * Deletes a profile picture image from Firebase Storage.
     *
     * @param fileID   the ID of the file.
     * @param listener a listener to handle success and failure callbacks for the operation.
     */
    public void deleteProfilePicture(String fileID, ImageDeleteListener listener) {
        StorageReference imageReference = profilePictureReference.child(fileID);
        imageReference.delete()
                .addOnSuccessListener(unused -> listener.onImageDeleteSuccess())
                .addOnFailureListener(unused -> listener.onImageDeleteFailure());
    }
}
