package com.CMPUT301W24T32.brazmascheckin.commands;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.helper.Date;

public class AddEventCommand implements Command{
    private EventController eventController;
    private ImageController imageController;
    private String title;
    private String desc;
    private int limit;
    private Date date;
    private Uri imageURI;
    private boolean geoLocationEnabled;
    private Context context;

    public AddEventCommand(EventController eventController, ImageController imageController,
                           String title, String desc, int limit, Date date,
                           String posterID, boolean geoLocationEnabled, Uri imageURI,
                           Context context) {
        this.eventController = eventController;
        this.imageController = imageController;
        this.title = title;
        this.desc = desc;
        this.limit = limit;
        this.date = date;
        this.geoLocationEnabled = geoLocationEnabled;
        this.imageURI = imageURI;
        this.context = context;
    }
    @Override
    public void execute() {

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
            fileID = null;
        }

        return fileID;
    }
}
