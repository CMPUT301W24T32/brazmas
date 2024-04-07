package com.CMPUT301W24T32.brazmascheckin.helper;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for finding and processing orphaned QR codes.
 * An orphaned QR code is a QR code stored in Firebase Storage that is not associated with any event.
 */
public class OrphanedQRCodeFinder {

    private ImageController imageController;
    private EventController eventController;

    /**
     * Constructs a new OrphanedQRCodeFinder with the specified ImageController and EventController.
     *
     * @param imageController Controller for image-related operations.
     * @param eventController Controller for event-related operations.
     */
    public OrphanedQRCodeFinder(ImageController imageController, EventController eventController) {
        this.imageController = imageController;
        this.eventController = eventController;
    }

    /**
     * Finds and processes orphaned QR codes by comparing all QR code file IDs in Firebase Storage
     * with the QR code IDs associated with events.
     *
     * @param successListener Listener to handle success callbacks with a list of orphaned QR code file IDs.
     */
    public void findAndProcessOrphanedQRCodes(GetSuccessListener<List<String>> successListener) {

        //get all QR code id's from Firebase storage
        imageController.getAllQRCodeFileIDs(allQRCodeFileIDs -> {
            eventController.getAllEventQRCodeIDs(eventQRCodeIDs -> {
                processOrphanedQRCodes(allQRCodeFileIDs, eventQRCodeIDs, successListener);
            });
        });
    }

    /**
     * Processes orphaned QR codes by identifying those that are not associated with any event.
     *
     * @param allQRCodeFileIDs  List of all QR code file IDs in Firebase Storage.
     * @param eventQRCodeIDs    List of QR code IDs associated with events.
     * @param successListener   Listener to handle success callbacks with a list of orphaned QR code file IDs.
     */
    private void processOrphanedQRCodes(List<String> allQRCodeFileIDs, List<String> eventQRCodeIDs,
                                        GetSuccessListener<List<String>> successListener) {
        List<String> orphanedQRCodeFileIDs = new ArrayList<>();
        for (String qrCodeFileID : allQRCodeFileIDs) {
            if (!eventQRCodeIDs.contains(qrCodeFileID)) {
                orphanedQRCodeFileIDs.add(qrCodeFileID);
            }
        }
        if(successListener != null) {
            successListener.onSuccess(orphanedQRCodeFileIDs);
        }
    }
}