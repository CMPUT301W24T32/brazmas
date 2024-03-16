package com.CMPUT301W24T32.brazmascheckin.controllers;

public interface ImageGetListener {
    void onImageGetSuccess(byte[] bytes);
    void onImageGetFailure(Exception e);
}
