package com.CMPUT301W24T32.brazmascheckin.models;
/**
 * Represents an administrator of the system.
 * This class encapsulates information about an admin, including their ID.
 */
public class Admin {
    private String ID;

    /**
     * Constructs a new instance of the Admin class.
     * @param ID Identification assigned by FirestoreDB
     */
    public Admin(String ID) {
        this.ID = ID;
    }

    /**
     * Constructs a new instance of the Admin class as required by Firestore.
     */
    public Admin() {
    }

    /**
     * Getter for the ID of the admin.
     * @return the identification string assigned by FirestoreDB.
     */
    public String getID() {
        return ID;
    }

    /**
     * Setter for the ID of the admin.
     * @param ID the identification string assigned by FirestoreDB.
     */
    public void setID(String ID) {
        this.ID = ID;
    }
}
