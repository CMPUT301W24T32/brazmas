package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.content.Context;
import android.net.Uri;



import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;


import com.google.firebase.storage.StorageReference;

public class ImageController {

    private StorageReference posterReference;
    private StorageReference qrCodeReference;
    private StorageReference profilePictureReference;
    private Context context;
    private static final String POSTER_PATH = "posters";
    private static final String QR_CODE_PATH = "qr_codes";
    private static final String PROFILE_PICTURE_PATH = "profile_pictures";

    public ImageController(Context context) {
        this.context = context;
        posterReference = FirestoreDB.getStorageReference(POSTER_PATH);
        qrCodeReference = FirestoreDB.getStorageReference(QR_CODE_PATH);
        profilePictureReference = FirestoreDB.getStorageReference(PROFILE_PICTURE_PATH);
    }

    public void uploadEventPoster(String fileID, Uri imageURI, ImageUploadListener listener) {
        StorageReference fileReference = posterReference.child(fileID);
        fileReference.putFile(imageURI)
                .addOnSuccessListener(listener::onImageUploadSuccess)
                .addOnFailureListener(listener::onImageUploadFailure);
    }

    public void uploadQRCode(String fileID, byte[] imageData, ImageUploadListener listener) {
        StorageReference fileReference = qrCodeReference.child(fileID);
        fileReference.putBytes(imageData)
                .addOnSuccessListener(listener::onImageUploadSuccess)
                .addOnFailureListener(listener::onImageUploadFailure);
    }

    public void uploadProfilePicture(String fileID, Uri imageURI, ImageUploadListener listener) {
        StorageReference fileReference = profilePictureReference.child(fileID);
        fileReference.putFile(imageURI)
                .addOnSuccessListener(listener::onImageUploadSuccess)
                .addOnFailureListener(listener::onImageUploadFailure);
    }

    public void getEventPoster(String fileID, ImageGetListener listener) {
        StorageReference imageReference = posterReference.child(fileID);
        imageReference.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(listener::onImageGetSuccess)
                .addOnFailureListener(listener::onImageGetFailure);
    }

    public void getQRCode(String fileID, ImageGetListener listener) {
        StorageReference imageReference = qrCodeReference.child(fileID);
        imageReference.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(listener::onImageGetSuccess)
                .addOnFailureListener(listener::onImageGetFailure);
    }

    public void getProfilePicture(String fileID, ImageGetListener listener) {
        StorageReference imageReference = profilePictureReference.child(fileID);
        imageReference.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(listener::onImageGetSuccess)
                .addOnFailureListener(listener::onImageGetFailure);
    }

    public void deleteEventPoster(String fileID, ImageDeleteListener listener) {
        StorageReference imageReference = posterReference.child(fileID);
        imageReference.delete()
                .addOnSuccessListener(unused -> listener.onImageDeleteSuccess())
                .addOnFailureListener(unused -> listener.onImageDeleteFailure());
    }

    public void deleteQRCode(String fileID, ImageDeleteListener listener) {
        StorageReference imageReference = qrCodeReference.child(fileID);
        imageReference.delete()
                .addOnSuccessListener(unused -> listener.onImageDeleteSuccess())
                .addOnFailureListener(unused -> listener.onImageDeleteFailure());
    }

    public void deleteProfilePicture(String fileID, ImageDeleteListener listener) {
        StorageReference imageReference = profilePictureReference.child(fileID);
        imageReference.delete()
                .addOnSuccessListener(unused -> listener.onImageDeleteSuccess())
                .addOnFailureListener(unused -> listener.onImageDeleteFailure());
    }
}
