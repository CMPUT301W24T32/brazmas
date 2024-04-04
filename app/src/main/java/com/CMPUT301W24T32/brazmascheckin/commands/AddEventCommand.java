package com.CMPUT301W24T32.brazmascheckin.commands;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.controllers.AddFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.DeviceID;
import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import com.CMPUT301W24T32.brazmascheckin.helper.QRCodeGenerator;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;
import java.util.HashMap;

public class AddEventCommand implements Command{
    private String ID;
    private EventController eventController;
    private ImageController imageController;
    private UserController userController;
    private String title;
    private String desc;
    private int limit;
    private Date date;

    private Uri imageURI;
    private boolean geoLocationEnabled;
    private String qrCode;
    private boolean generateShareQRCode;
    private Location location;
    private String organizer;
    private Context context;

    public AddEventCommand(EventController eventController, ImageController imageController,
                           UserController userController,
                           String ID, String title, String desc, int limit, Date date,
                           boolean geoLocationEnabled, String qrCode,
                           Uri imageURI, boolean generateShareQRCode, String organizer,
                           Location location,
                           Context context) {
        this.ID = ID;
        this.eventController = eventController;
        this.imageController = imageController;
        this.userController = userController;
        this.title = title;
        this.desc = desc;
        this.limit = limit;
        this.date = date;
        this.geoLocationEnabled = geoLocationEnabled;
        this.qrCode = qrCode;
        this.imageURI = imageURI;
        this.generateShareQRCode = generateShareQRCode;
        this.organizer = organizer;
        this.location = location;
        this.context = context;
    }

    @Override
    public void execute() {
        Event event = createEvent();
        if(ID != null && qrCode != null) {
            reuseEvent(event);
        } else {
            addEvent(event);
        }

    }

    private void addEvent(Event event) {
        eventController.addEvent(event, ID -> {
            event.setID(ID);
            if(generateShareQRCode) {
                generateShareQRCode(event);
            }
            if(event.getQRCode() == null) {
                Bitmap bitmap = QRCodeGenerator.generateQRCode(ID);
                byte[] imageData = QRCodeGenerator.getQRCodeByteArray(bitmap);
                String fileID = event.getID() + "-QRCODE";
                imageController.uploadQRCode("CHECK-IN", fileID, imageData, uri -> {
                    event.setQRCode(fileID);
                    eventController.setEvent(event, null,
                            e -> Toast.makeText(context, "Unable to store QR Code", Toast.LENGTH_SHORT).show());
                }, e -> {

                });
            }

            userController.getUser(organizer, user -> {
                user.createEvent(ID);
                userController.setUser(user, null, null);
            }, e -> {
                Toast.makeText(context, "Unable to organize event", Toast.LENGTH_SHORT).show();
            });

        }, e -> Toast.makeText(context, "Unable to add event", Toast.LENGTH_SHORT).show());
    }

    private void reuseEvent(Event event) {
        if(generateShareQRCode) {
            generateShareQRCode(event);
        }
        eventController.setEvent(event, () -> {
            userController.getUser(organizer, user -> {
                user.createEvent(event.getID());
                userController.setUser(user, null, null);
            }, e -> {
                Toast.makeText(context, "Unable to organize event", Toast.LENGTH_SHORT).show();
            });
        }, e -> {
            Toast.makeText(context, "Unable to add event", Toast.LENGTH_SHORT).show();
        });
    }

    private Event createEvent() {
        Event event = new Event(
                ID, title, date, desc, new HashMap<>(), new ArrayList<>(),
                limit, uploadFile(), qrCode, null, organizer, geoLocationEnabled,
                location, new HashMap<>(), new ArrayList<>()
        );


//        if(generateShareQRCode) {
//            generateShareQRCode(event);
//        }

        return event;
    }

    private String uploadFile() {
        String fileID = String.valueOf(System.currentTimeMillis());

        if(imageURI != null) {
            imageController.uploadImage(ImageController.EVENT_POSTER, fileID, imageURI,
                    object -> {
                        Toast.makeText(context, "Image uploaded!", Toast.LENGTH_SHORT).show();
                    }, e-> {

                    });
        } else {
            fileID = "defaultPoster.png";
        }
        return fileID;
    }

    /**
     * Method to generate a promotional QR Code for an event
     * @param event event for which promo qr code is being generated
     */

    private void generateShareQRCode(Event event) {
        Bitmap qrCodeBitmap = QRCodeGenerator.generateQRCode(event.getID()+"-SHARE-QRCODE");
        byte[] imageData = QRCodeGenerator.getQRCodeByteArray(qrCodeBitmap);
        String fileID = event.getID()+"-SHARE-QRCODE"; // TODO: constant
        event.setShareQRCode(fileID);
        imageController.uploadQRCode("SHARE",fileID, imageData, uri -> { // TODO: constant
            String QRCodeURI = uri.toString();
        }, e -> {
            Toast.makeText(context, "Unable to store share QR code", Toast.LENGTH_SHORT).show();
        });
    }
}
