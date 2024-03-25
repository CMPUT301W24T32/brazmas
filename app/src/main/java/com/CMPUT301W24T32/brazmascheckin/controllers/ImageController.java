
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
    public static final String QR_CODE ="QR_CODE";

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

    /**
     * Retrieves an image from Firebase Storage based on the specified type and file ID.
     *
     * @param TYPE            the type of image to retrieve (e.g., EVENT_POSTER, PROFILE_PICTURE, QR_CODE).
     * @param fileID          the ID of the file.
     * @param successListener a listener to handle success callbacks for the operation.
     * @param failureListener a listener to handle failure callbacks for the operation.
     */
    public String getImage(String TYPE, String fileID, GetSuccessListener<byte[]> successListener,
                         AddFailureListener failureListener) {
        StorageReference imageReference;
        if(TYPE.equals(EVENT_POSTER)) {
            imageReference = posterReference.child(fileID);
        } else if (TYPE.equals(PROFILE_PICTURE)) {
            imageReference = profilePictureReference.child(fileID);
        } else if (TYPE.equals(QR_CODE)) {
            imageReference = qrCodeReference.child(fileID);
        } else {
            // Handle the case where TYPE is invalid
            return null; // Or throw an exception, depending on your requirements
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

        return fileID;
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

}
