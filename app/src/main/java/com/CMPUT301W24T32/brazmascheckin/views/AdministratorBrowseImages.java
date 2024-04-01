package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.DialogInterface;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.AddFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.DeleteFailureListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.DeleteSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.GetSuccessListener;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.helper.ImageAdapter;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will be the activity where admin can browse images (posters, profile pic).
 */
public class AdministratorBrowseImages extends AppCompatActivity {
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private List<Pair<String, String>> imageUrlsWithType; // URL and type
    private List<String> imageType;
    private ImageController imageController;
    private List<String> allFileIds; // list to hold all file IDs

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
        allFileIds = new ArrayList<>();
        imageType = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageUrlsWithType, this); // Update adapter
        gridView.setAdapter(imageAdapter);

        imageController = new ImageController(FirestoreDB.getStorageInstance());

        fetchImagesFromController();

        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showConfirmationDialog(position); // Only pass position
            }
        });
    }

    private void fetchImagesFromController() {
        imageController.getAllPosterFileIDs(new GetSuccessListener<List<String>>() {
            @Override
            public void onSuccess(List<String> posterFileIDs) {
                for (String fileID : posterFileIDs) {
                    // Fetch poster images using ImageController
                    imageController.getImage(ImageController.EVENT_POSTER, fileID, new GetSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] imageData) {
                            // Add URL and its type to the list
                            String imageUrl = "data:image/jpeg;base64," + Base64.encodeToString(imageData, Base64.DEFAULT);
                            imageUrlsWithType.add(new Pair<>(imageUrl, ImageController.EVENT_POSTER));
                            allFileIds.add(fileID); // Add fileID to the list
                            imageType.add(ImageController.EVENT_POSTER); // Add image type to the list
                            imageAdapter.notifyDataSetChanged();
                        }
                    }, new AddFailureListener() {
                        @Override
                        public void onAddFailure(Exception e) {
                            Log.e("FetchImages", "Failed to fetch poster image: " + e.getMessage());
                        }
                    });
                }
            }
        });

        imageController.getAllProfilePicFileIDs(new GetSuccessListener<List<String>>() {
            @Override
            public void onSuccess(List<String> profilePicFileIDs) {
                for (String fileID : profilePicFileIDs) {
                    // Fetch profile pictures using ImageController
                    imageController.getImage(ImageController.PROFILE_PICTURE, fileID, new GetSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] imageData) {
                            // Add URL and its type to the list
                            String imageUrl = "data:image/jpeg;base64," + Base64.encodeToString(imageData, Base64.DEFAULT);
                            imageUrlsWithType.add(new Pair<>(imageUrl, ImageController.PROFILE_PICTURE));
                            allFileIds.add(fileID); // Add fileID to the list
                            imageType.add(ImageController.PROFILE_PICTURE); // Add image type to the list
                            imageAdapter.notifyDataSetChanged();
                        }
                    }, new AddFailureListener() {
                        @Override
                        public void onAddFailure(Exception e) {
                            Log.e("FetchImages", "Failed to fetch profile picture: " + e.getMessage());
                        }
                    });
                }
            }
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
                // Remove the image URL and type from the list
                imageUrlsWithType.remove(position);
                allFileIds.remove(position);
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
