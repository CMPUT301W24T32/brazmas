package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.google.firebase.storage.UploadTask;

public interface ImageUploadListener {
    void onImageUploadSuccess(UploadTask.TaskSnapshot taskSnapshot);

    void onImageUploadFailure(Exception e);
}
