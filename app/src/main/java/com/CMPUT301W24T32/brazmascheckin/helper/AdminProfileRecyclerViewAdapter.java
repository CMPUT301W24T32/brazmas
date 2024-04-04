package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.models.User;

import java.util.ArrayList;
import com.bumptech.glide.Glide;  // just like browse images for admin
import com.google.firebase.storage.FirebaseStorage;

/**
 * This class is the adapter for the RecyclerView of user profiles.
 * This class is responsible for populating the recycler view with user profile information.
 */
public class AdminProfileRecyclerViewAdapter extends RecyclerView.Adapter<AdminProfileRecyclerViewAdapter.AdminProfileViewHolder> {

    private ArrayList<User> users;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private ImageController imageController = new ImageController(FirebaseStorage.getInstance());

    /**
     * This method constructs a new AdminProfileRecyclerViewAdapter.
     * @param context the context in which the adapter is being used.
     * @param users the list of all users.
     */
    public AdminProfileRecyclerViewAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    /**
     * This method is used when a recycler view requires a view holder to represent an item.
     * @param parent the ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType the view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public AdminProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.administrator_view_profile, parent, false);
        return new AdminProfileViewHolder(view);
    }

    /**
     * This method displays an items at a specific position in the Recycler View.
     * @param holder the ViewHolder which needs to be updated to include contents of item at the given position.
     * @param position the position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AdminProfileViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    /**
     * This method returns the number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    /**
     * This method sets an itemClickListener for handling click events on the RecyclerView items.
     * @param onItemClickListener the click listener.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * This method is the interface for the item click listener.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * This ViewHolder class holds data about an user and their profile.
     */
    public class AdminProfileViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView profilePicture;

        /**
         * This method constructs a AdminProfileViewHolder.
         * @param itemView the view that represents the user.
         */
        public AdminProfileViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializes the views
            name = itemView.findViewById(R.id.admin_view_profile_name_tv_admin);
            profilePicture = itemView.findViewById(R.id.profile_picture_admin);

            // set the click listener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // notify the registered click listener about the recently clicked profile
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        /**
         * This method binds the information to the view.
         * @param user the user information.
         */
        public void bind(User user) {
            Log.d("AdminProfileAdapter", "Binding user: " + user.getFirstName() + " " + user.getLastName());

            String firstName = user.getFirstName() != null ? user.getFirstName() : "John";
            String lastName = user.getLastName() != null ? user.getLastName() : "Doe";
            String fullName = firstName + " " + lastName;

            name.setText(fullName);

            // Load image using Glide
            if (user.getProfilePicture() != null) {
                displayImage(user.getProfilePicture());
            }
            else {
                displayDefaultImage(user.getDefaultProfilePicture());
            }
        }

        /**
         * This method retrieves the profile picture image from the database and displays it in the view.
         * @param profilePicID the ID of the image in the database
         */
        private void displayImage(String profilePicID) {
            // while image is loading
            profilePicture.setImageResource(R.drawable.admin_profile_24);

            if (profilePicID != null) {
                imageController.getImage(ImageController.PROFILE_PICTURE, profilePicID, bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profilePicture.setImageBitmap(bitmap);
                }, null);
            }
        }

        /**
         * This method retrieves the default profile picture image from the database and displays it in the view.
         * @param profilePicID the ID of the image in the database
         */
        private void displayDefaultImage(String profilePicID){
            // while image is loading
            profilePicture.setImageResource(R.drawable.admin_profile_24);

            if (profilePicID != null) {
                imageController.getImage(ImageController.DEFAULT_PROFILE_PICTURE_PATH, profilePicID, bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profilePicture.setImageBitmap(bitmap);
                }, null);
            }
        }
    }
}
