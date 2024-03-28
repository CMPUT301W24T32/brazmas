package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.DialogInterface;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class AdministratorBrowseImages extends AppCompatActivity {
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private List<Pair<String, String>> imageUrlsWithType; // Change imageUrls to hold pairs of URL and type
    private ImageController imageController;
    private List<String> allFileIds; // Declare a list to hold all file IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_image);

        // bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_image_bnv);
        bottomNavigationView.setSelectedItemId(R.id.admin_image);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            /**
             * This method determines something from navigation bar has been selected or not.
             * @param menuItem The selected item
             * @return True if selected, false otherwise.
             */
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == (R.id.admin_event)){
                    startActivity(new Intent(getApplicationContext(), AdministratorHome.class));
                    overridePendingTransition(0,0);
                    return true;
                }

                if (id == (R.id.admin_profile)){
                    startActivity(new Intent(getApplicationContext(), AdministratorBrowseProfiles.class));
                    overridePendingTransition(0,0);
                    return true;
                }

                if (id == (R.id.admin_image)){
                    return true;
                }

                return false;

            }
        });

        gridView = findViewById(R.id.gridView);
        imageUrlsWithType = new ArrayList<>(); // Initialize the list for URLs and types
        imageAdapter = new ImageAdapter(imageUrlsWithType, this); // Update adapter
        gridView.setAdapter(imageAdapter);

        imageController = new ImageController(this);

        fetchImagesFromFirebase();

        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showConfirmationDialog(position); // Only pass position
            }
        });
    }

    private void fetchImagesFromFirebase() {
        fetchImagesFromFolder("posters", "EVENT_POSTER"); // Fetch posters
        fetchImagesFromFolder("profile_pictures", "PROFILE_PICTURE"); // Fetch profile pictures
    }

    private void fetchImagesFromFolder(String folderName, String imageType) {
        StorageReference folderRef = FirestoreDB.getStorageReference(folderName);

        folderRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Add URL and its type to the list
                    imageUrlsWithType.add(new Pair<>(uri.toString(), imageType));
                    if (allFileIds == null) {
                        allFileIds = new ArrayList<>(); // Initialize allFileIds if null
                    }
                    allFileIds.add(item.getName()); // Add file ID to the list of all file IDs
                    imageAdapter.notifyDataSetChanged();
                }).addOnFailureListener(exception -> {
                    Log.e("ImageURL", "Failed to get image URL: " + exception.getMessage());
                });
            }
        }).addOnFailureListener(exception -> {
            Log.e("FetchImages", "Failed to fetch images: " + exception.getMessage());
        });
    }

    private void deleteImage(int position) {
        // Get the image type from the list using the position
        String imageType = imageUrlsWithType.get(position).second;
        String fileID = allFileIds.get(position);
        imageController.deleteImage(imageType, fileID, new DeleteSuccessListener() {
            @Override
            public void onDeleteSuccess() {
                Toast.makeText(AdministratorBrowseImages.this, "Image deleted", Toast.LENGTH_SHORT).show();
                imageUrlsWithType.remove(position);
                imageAdapter.notifyDataSetChanged();
            }
        }, new DeleteFailureListener() {
            @Override
            public void onDeleteFailure(Exception e) {
                Toast.makeText(AdministratorBrowseImages.this, "Failed to delete image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this image?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImage(position);
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}

