package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public interface ImageUploadListener {
    void onImageUploadSuccess(Uri uri);

    void onImageUploadFailure(Exception e);
}
