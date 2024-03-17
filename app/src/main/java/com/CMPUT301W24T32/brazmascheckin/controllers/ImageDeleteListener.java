package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the success and failure of deleting images.
 */
public interface ImageDeleteListener {

    /**
     * Called when an image is successfully deleted.
     */
    void onImageDeleteSuccess();

    /**
     * Called when an error occurs while deleting an image.
     */
    void onImageDeleteFailure();
}
