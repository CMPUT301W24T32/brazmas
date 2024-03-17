package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.CMPUT301W24T32.brazmascheckin.models.Event;

public interface EventGetListener {
    void onEventGetSuccess(Event event);
    void onEventGetFailure(Exception e);
}
