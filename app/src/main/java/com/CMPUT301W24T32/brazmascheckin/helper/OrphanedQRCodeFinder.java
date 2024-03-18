package com.CMPUT301W24T32.brazmascheckin.helper;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;

import java.util.ArrayList;
import java.util.List;

public class OrphanedQRCodeFinder {

    private ImageController imageController;
    private EventController eventController;

    public OrphanedQRCodeFinder(ImageController imageController, EventController eventController) {
        this.imageController = imageController;
        this.eventController = eventController;
    }

    public List<String> findAndProcessOrphanedQRCodes() {

        //get all QR code id's from Firebase storage
        List<String> allQRCodeFileIDs = imageController.getAllQRCodeFileIDs();

        //get all event QR codes from Firestore
        List<String> eventQRCodeIDs = eventController.getAllEventQRCodeIDs();

        //find orphaned QR codes
        List<String> orphanedQRCodeFileIDs = new ArrayList<>();
        for (String qrCodeFileID : allQRCodeFileIDs) {
            if (!eventQRCodeIDs.contains(qrCodeFileID)) {
                orphanedQRCodeFileIDs.add(qrCodeFileID);
            }
        }
        return orphanedQRCodeFileIDs;
    }
}
