package com.CMPUT301W24T32.brazmascheckin.commands;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.UserController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;
import com.CMPUT301W24T32.brazmascheckin.helper.Location;
import com.CMPUT301W24T32.brazmascheckin.helper.QRCodeGenerator;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Concrete command class that implements the Command interface. Provides commands to add, create,
 * and reuse events, upload posters for events, and generate QR codes for events.
 */
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

    /**
     * Constructor for add event fragment
     *
     * @param eventController event controller
     * @param imageController image controller
     * @param userController user controller
     * @param ID id of event
     * @param title name of event
     * @param desc description of event
     * @param limit attendee limit
     * @param date date of event
     * @param geoLocationEnabled if geolocation is enabled
     * @param qrCode qr code reference
     * @param imageURI image URI
     * @param generateShareQRCode if event has a share qr code
     * @param organizer organizer reference
     * @param location location of the event
     * @param context context of the activity creating an event
     */
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

    /**
     *  Execute the command to add or reuse an event.
     */
    @Override
    public Event execute() {
        Event event = createEvent();
        if(ID != null && qrCode != null) {
            reuseEvent(event);
        } else {
            addEvent(event);
        }
        Toast.makeText(context.getApplicationContext(),"Making your event...",Toast.LENGTH_LONG).show();
        try { Thread.sleep(2000);} catch (Exception ignored) {}
        return event;

    }


    /**
     *  Method to add a new event.
     * @param event : The event to be added.
     */
    private void addEvent(Event event) {
        eventController.addEvent(event, ID -> {
            event.setID(ID);
            if(generateShareQRCode) {
                generateShareQRCode(event);
            }
            if(event.getQRCode() == null) {
                String fileID = event.getID() + "-QRCODE";
                Bitmap bitmap = QRCodeGenerator.generateQRCode(fileID);
                byte[] imageData = QRCodeGenerator.getQRCodeByteArray(bitmap);
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

    /**
     * Method to reuse an existing event.
     * @param event : The event to be reused.
     */
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

    /**
     * Method to creates a new event based on the provided information.
     * @return The created event.
     */
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

    /**
     * Method to upload the event's image file.
     * @return The ID of the uploaded file.
     */
    private String uploadFile() {
        String fileID = String.valueOf(System.currentTimeMillis());

        if(imageURI != null) {
            imageController.uploadImage(ImageController.EVENT_POSTER, fileID, imageURI,
                    object -> {
                        Toast.makeText(context, "Image uploaded!", Toast.LENGTH_SHORT).show();
                    }, e-> {

                    });
        } else {
            fileID = ImageController.DEFAULT_EVENT_POSTER_FILE;
        }
        return fileID;
    }

    /**
     * Method to generate a promotional QR Code for an event
     * @param event event for which promo qr code is being generated
     */

    private void generateShareQRCode(Event event) {
        String fileID = event.getID()+"-SHARE-QRCODE"; 
        Bitmap qrCodeBitmap = QRCodeGenerator.generateQRCode(fileID);
        byte[] imageData = QRCodeGenerator.getQRCodeByteArray(qrCodeBitmap);
        event.setShareQRCode(fileID);
        imageController.uploadQRCode("SHARE", fileID, imageData, uri -> {
            Toast.makeText(context, "Share QR Code uploaded!", Toast.LENGTH_SHORT).show();
        }, e -> {
            Toast.makeText(context, "Unable to store share QR code", Toast.LENGTH_SHORT).show();
        });
    }
}
