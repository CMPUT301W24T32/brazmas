package com.CMPUT301W24T32.brazmascheckin.controllers;

/**
 * Listener interface for handling the success and failure of retrieving images.
 */
public interface ImageGetListener {

    /**
     * Called when an image is successfully retrieved.
     *
     * @param bytes the byte array representing the retrieved image.
     */
    void onImageGetSuccess(byte[] bytes);

    /**
     * Called when an error occurs while retrieving an image.
     *
     * @param e The exception representing the error.
     */
    void onImageGetFailure(Exception e);
}
