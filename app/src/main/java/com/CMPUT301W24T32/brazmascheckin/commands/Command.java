package com.CMPUT301W24T32.brazmascheckin.commands;

import com.CMPUT301W24T32.brazmascheckin.models.Event;

public interface Command {
    Event execute();
}
