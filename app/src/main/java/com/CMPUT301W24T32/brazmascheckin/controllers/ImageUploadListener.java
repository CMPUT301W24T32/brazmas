package com.CMPUT301W24T32.brazmascheckin.controllers;

import android.net.Uri;

/**
 * Listener interface for handling the success and failure of uploading images.
 */
public interface ImageUploadListener {

    /**
     * Called when an image is successfully uploaded.
     *
     * @param uri the URI of the uploaded image.
     */
    void onImageUploadSuccess(Uri uri);

    /**
     * Called when an error occurs while uploading an image.
     *
     * @param e The exception representing the error.
     */
    void onImageUploadFailure(Exception e);
}
