package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.ImageAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdministratorBrowseImages extends AppCompatActivity {
    GridView gridView;
    ImageAdapter imageAdapter;
    List<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_image);

        // Initialize variables
        gridView = findViewById(R.id.gridView);
        imageUrls = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageUrls, this);
        gridView.setAdapter(imageAdapter);

        // Fetch images from Firebase Storage
        fetchImagesFromFirebase();
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
}