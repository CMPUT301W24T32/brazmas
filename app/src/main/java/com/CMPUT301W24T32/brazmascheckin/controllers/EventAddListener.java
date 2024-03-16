package com.CMPUT301W24T32.brazmascheckin.controllers;

public interface EventAddListener {
    void onEventAddSuccess();
    void onEventAddFailure(Exception e);
}
