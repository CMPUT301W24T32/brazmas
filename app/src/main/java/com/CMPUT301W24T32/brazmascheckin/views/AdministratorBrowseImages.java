package com.CMPUT301W24T32.brazmascheckin.views;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be the page to browse images for admin.
 */
public class AdministratorBrowseImages extends AppCompatActivity {

    private GridView gridView;
    private List<String> imageIDs;  // list of image IDS from events and profiles
    private ImageController imageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_image);

        gridView = findViewById(R.id.grid_view);
        imageController = new ImageController(this);

        // Fetch event posters
        getAllEvents();
    }

    private void getAllEvents() {
        EventController eventController = new EventController(this);
        eventController.addSnapshotListener(new SnapshotListener<Event>() {
            @Override
            public void snapshotListenerCallback(ArrayList<Event> events) {
                imageIDs = new ArrayList<>();
                for (Event event : events) {
                    // Add event poster IDs to the list
                    imageIDs.add(event.getPoster());
                }
                // Once all event posters are fetched, load them into the grid view
                loadImages();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(AdministratorBrowseImages.this, "Unable to connect to the database", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadImages() {
        // Loop through image IDs and load each image into the grid view
        for (String imageID : imageIDs) {
            imageController.getImage(ImageController.EVENT_POSTER, imageID, bytes -> {
                // Load the image into the grid view
                // Here, you can directly manipulate the grid view to add images
                // For example, you can create an ImageView, set its bitmap, and add it to the grid view
            }, e -> {
                // Handle failure
                Toast.makeText(AdministratorBrowseImages.this, "Unable to load image", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
