package com.CMPUT301W24T32.brazmascheckin.controllers;

import com.CMPUT301W24T32.brazmascheckin.models.User;

public interface UserGetListener {
    void onUserGetSuccess(User user);
    void onUserGetFailure(Exception e);
}
