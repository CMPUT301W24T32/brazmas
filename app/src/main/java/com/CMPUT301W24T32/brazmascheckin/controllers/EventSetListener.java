package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.CMPUT301W24T32.brazmascheckin.models.Event;

public interface EventSetListener {
    void onEventSetSuccess();
    void onEventSetFailure(Exception e);
}
