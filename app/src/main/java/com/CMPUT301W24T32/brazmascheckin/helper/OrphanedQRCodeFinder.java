package com.CMPUT301W24T32.brazmascheckin.helper;

import android.util.Log;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
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

    public void findAndProcessOrphanedQRCodes(GetSuccessListener<List<String>> successListener) {

        //get all QR code id's from Firebase storage
        imageController.getAllQRCodeFileIDs(allQRCodeFileIDs -> {
            eventController.getAllEventQRCodeIDs(eventQRCodeIDs -> {
                processOrphanedQRCodes(allQRCodeFileIDs, eventQRCodeIDs, successListener);
            });
        });
    }

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
