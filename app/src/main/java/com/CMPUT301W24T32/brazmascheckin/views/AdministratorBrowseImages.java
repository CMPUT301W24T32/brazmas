package com.CMPUT301W24T32.brazmascheckin.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.EventController;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.controllers.SnapshotListener;
import com.CMPUT301W24T32.brazmascheckin.helper.ImageAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        List<Bitmap> bitmaps = new ArrayList<>(); // List to hold Bitmap objects
        AtomicInteger imageCounter = new AtomicInteger();  // counter to track the number of loaded images

        // Loop through image IDs and load each image into the grid view
        for (String imageID : imageIDs) {
            imageController.getImage(ImageController.EVENT_POSTER, imageID, bytes -> {
                // Convert byte array to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmaps.add(bitmap); // Add Bitmap to the list

                // increase imageCounter
                imageCounter.getAndIncrement();

                if (imageCounter.get() == imageIDs.size()) {
                    ImageAdapter adapter = new ImageAdapter(AdministratorBrowseImages.this, bitmaps);
                    gridView.setAdapter(adapter);
                }
            }, e -> {
                // Handle failure
                Toast.makeText(AdministratorBrowseImages.this, "Unable to load image", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
