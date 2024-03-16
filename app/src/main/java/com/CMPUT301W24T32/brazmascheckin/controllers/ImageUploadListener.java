package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public interface ImageUploadListener {
    void onImageUploadSuccess(UploadTask.TaskSnapshot taskSnapshot, StorageReference imageReference);

    void onImageUploadFailure(Exception e);
}
