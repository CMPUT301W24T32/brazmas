package com.CMPUT301W24T32.brazmascheckin.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public void findAndProcessOrphanedQRCodes(GetSuccessListener<List<Bitmap>> successListener) {

        //get all QR code id's from Firebase storage
        imageController.getAllQRCodeFileIDs(allQRCodeFileIDs -> {
            eventController.getAllEventQRCodeIDs(eventQRCodeIDs -> {
                processOrphanedQRCodes(allQRCodeFileIDs, eventQRCodeIDs, successListener);
            });
        });
    }

    private void processOrphanedQRCodes(List<String> allQRCodeFileIDs, List<String> eventQRCodeIDs,
                                        GetSuccessListener<List<Bitmap>> successListener) {
        List<String> orphanedQRCodeFileIDs = new ArrayList<>();
        List<Bitmap> orphanedQRCodeImages = new ArrayList<>();
        for (String qrCodeFileID : allQRCodeFileIDs) {
            if (!eventQRCodeIDs.contains(qrCodeFileID)) {
                orphanedQRCodeFileIDs.add(qrCodeFileID);
            }
        }
        for (String fileID : orphanedQRCodeFileIDs) {
            imageController.getImage("QR_CODE", fileID, bytes -> {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                orphanedQRCodeImages.add(bitmap);
            }, e -> {
                Log.e("failure", "processOrphanedQRCodes: "+ e.getMessage());
            });
        }
        if(successListener != null) {
            successListener.onSuccess(orphanedQRCodeImages);
        }
    }
}
