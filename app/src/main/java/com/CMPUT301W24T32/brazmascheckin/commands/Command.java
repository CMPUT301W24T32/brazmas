package com.CMPUT301W24T32.brazmascheckin.commands;


/**
 * The Command interface represents a generic command that can be executed.
 * Concrete implementations of this interface define specific actions to be performed.
 */

public interface Command {
    /**
     * Executes the command.
     */
    void execute();
}
