package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.DeleteFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.DeleteSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.helper.ImageAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdministratorBrowseImages extends AppCompatActivity {
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private List<String> imageUrls;
    private ImageController imageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_image);

        // Initialize variables
        gridView = findViewById(R.id.gridView);
        imageUrls = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageUrls, this);
        gridView.setAdapter(imageAdapter);

        imageController = new ImageController(this);

        // Fetch images from Firebase Storage
        fetchImagesFromFirebase();

        // set click listener for gridView items
        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showConfirmationDialog(position);
            }
        });
    }

    private void fetchImagesFromFirebase() {
        // Get reference to the "posters" folder in Firebase Storage
        StorageReference postersRef = FirestoreDB.getStorageReference("posters");

        postersRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                // Get download URL for each item
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    // testing
                    Log.d("ImageURL", "Image URL: " + uri.toString());


                    // Add URL to imageUrls list
                    imageUrls.add(uri.toString());
                    // Notify adapter of data change
                    imageAdapter.notifyDataSetChanged();
                }).addOnFailureListener(exception -> {
                    // Handle any errors
                    Log.e("ImageURL", "Failed to get image URL" + exception.getMessage());
                });
            }
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Log.e("FetchImages", "Failed to fetch images: " + exception.getMessage());
        });
    }

    private void deleteImage(int position, String image_type) {
        image_type = "EVENT_POSTER";
        imageController.deleteImage(image_type, imageUrls.get(position), new DeleteSuccessListener() {
            @Override
            public void onDeleteSuccess() {
                // profile deleted successfully
                Toast.makeText(AdministratorBrowseImages.this, "Event poster deleted", Toast.LENGTH_SHORT).show();
                imageUrls.remove(position);
                imageAdapter.notifyDataSetChanged();
            }
        }, new DeleteFailureListener() {
            @Override
            public void onDeleteFailure(Exception e) {
                // failed to delete image
                Toast.makeText(AdministratorBrowseImages.this, "Failed to delete event poster", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this image?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed, delete the profile
                deleteImage(position, "EVENT_POSTER");
            }
        });
        builder.setNegativeButton("No", null); // Do nothing if user cancels
        builder.show();
    }
}