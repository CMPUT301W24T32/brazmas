package com.CMPUT301W24T32.brazmascheckin.controllers;

public interface EventDeleteListener {
    void onEventDeleteSuccess();

    void onEventDeleteFailure(Exception e);
}
