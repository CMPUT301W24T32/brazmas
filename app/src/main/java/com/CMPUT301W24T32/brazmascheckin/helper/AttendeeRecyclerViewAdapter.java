package com.CMPUT301W24T32.brazmascheckin.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.ViewAttendeesActivity;

import java.util.ArrayList;

/**
 * RecyclerView Adapter for displaying attendees in different modes (SIGN_UP_MODE or CHECK_IN_MODE).
 */
public class AttendeeRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<User> users;
    private ArrayList<Integer> userCheckIns;
    private Context context;

    private int mode;
    private ImageController imageController = new ImageController(FirestoreDB.getStorageInstance());

    /**
     * Constructor for the AttendeeRecyclerViewAdapter.
     *
     * @param context The context of the application.
     * @param users   The list of users to display.
     * @param mode    The mode of the adapter (SIGN_UP_MODE or CHECK_IN_MODE).
     */
    public AttendeeRecyclerViewAdapter(Context context, ArrayList<User> users, int mode) {
        this.context = context;
        this.users = users;
        this.mode = mode;
    }

    /**
     * Constructor for the AttendeeRecyclerViewAdapter.
     *
     * @param context    The context of the application.
     * @param users      The list of users to display.
     * @param checkIns   The list of check-in counts for each user.
     * @param mode       The mode of the adapter (SIGN_UP_MODE or CHECK_IN_MODE).
     */
    public AttendeeRecyclerViewAdapter(Context context, ArrayList<User> users, ArrayList<Integer>
                                       checkIns, int mode) {
        this.context = context;
        this.users = users;
        this.userCheckIns = checkIns;
        this.mode = mode;
    }

    /**
     * Inflates the layout for the RecyclerView item based on the view type.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.attendee_view_card, parent, false);

        switch(viewType) {
            case ViewAttendeesActivity.CHECK_IN_MODE:
                return new CheckInViewHolder(view);
            case ViewAttendeesActivity.SIGN_UP_MODE:
                return new SignUpViewHolder(view);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    /**
     * Binds data to the RecyclerView item at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        User user = users.get(position);

        switch(viewType) {
            case ViewAttendeesActivity.CHECK_IN_MODE:
                int checkInCount = userCheckIns.get(position);
                ((CheckInViewHolder) holder).bind(user, checkInCount);
                break;
            case ViewAttendeesActivity.SIGN_UP_MODE:
                ((SignUpViewHolder) holder).bind(user);
                break;
        }
    }

    /**
     * Gets the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }


    /**
     * Returns the view type of the item at the specified position for the purpose of recycling.
     *
     * @param position The position of the item within the adapter's data set.
     * @return An integer representing the view type of the item at the given position.
     */

    @Override
    public int getItemViewType(int position) {
        return mode;
    }

    /**
     * ViewHolder for displaying a user in the SIGN_UP_MODE.
     */
    public class SignUpViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView idTextView;
        private TextView checkInTextView;
        private ImageView profileImageView;
        public SignUpViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.attendee_view_card_name_tv);
            idTextView = itemView.findViewById(R.id.attendee_view_card_id_tv);
            checkInTextView = itemView.findViewById(R.id.attendee_view_card_check_ins_tv);
            profileImageView = itemView.findViewById(R.id.attendee_view_card_picture_iv);
            checkInTextView.setVisibility(View.GONE);
        }

        /**
         * Binds user data to the ViewHolder in SIGN_UP_MODE.
         *
         * @param user The user to bind.
         */
        public void bind(User user) {
            String combinedName = user.getFirstName() + " " + user.getLastName();
            nameTextView.setText(combinedName);
            idTextView.setText(user.getID());

            String profilePicture = null;
            String profileFolder;

            // image: default and custom
            if(user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                profilePicture = user.getProfilePicture();
                profileFolder = ImageController.PROFILE_PICTURE;
            } else if (user.getDefaultProfilePicture() != null && !user.getDefaultProfilePicture().isEmpty()) {
                profilePicture = user.getDefaultProfilePicture();
                profileFolder = ImageController.DEFAULT_PROFILE_PICTURE_PATH;
            } else {
                profileFolder = ImageController.DEFAULT_PROFILE_PICTURE_PATH;
                profilePicture = null;
            }

            if(profilePicture != null) {
                imageController.getImage(profileFolder, profilePicture,
                        byteArray -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            profileImageView.setImageBitmap(bitmap);
                        }, null);
            }
        }
    }

    /**
     * ViewHolder for displaying a user in the CHECK_IN_MODE.
     */
    public class CheckInViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView idTextView;
        private TextView checkInTextView;
        private ImageView profileImageView;

        public CheckInViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.attendee_view_card_name_tv);
            idTextView = itemView.findViewById(R.id.attendee_view_card_id_tv);
            checkInTextView = itemView.findViewById(R.id.attendee_view_card_check_ins_tv);
            profileImageView = itemView.findViewById(R.id.attendee_view_card_picture_iv);
        }

        /**
         * Binds user data to the ViewHolder in CHECK_IN_MODE.
         *
         * @param user          The user to bind.
         * @param checkInCount  The check-in count of the user.
         */
        @SuppressLint("SetTextI18n")
        public void bind(User user, int checkInCount) {
            String combinedName = user.getFirstName() + " " + user.getLastName();
            nameTextView.setText(combinedName);
            idTextView.setText(user.getID());
            checkInTextView.setText("Check Ins: " + checkInCount);

            // image: default and custom
            String profilePicture = null;
            String profileFolder;
            if(user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                profilePicture = user.getProfilePicture();
                profileFolder = ImageController.PROFILE_PICTURE;
            } else if (user.getDefaultProfilePicture() != null && !user.getDefaultProfilePicture().isEmpty()) {
                profilePicture = user.getDefaultProfilePicture();
                profileFolder = ImageController.DEFAULT_PROFILE_PICTURE_PATH;
            } else {
                profileFolder = ImageController.DEFAULT_PROFILE_PICTURE_PATH;
                profilePicture = null;
            }

            if(profilePicture != null) {
                imageController.getImage(profileFolder, profilePicture,
                        byteArray -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            profileImageView.setImageBitmap(bitmap);
                        }, null);
            }
        }
    }

}
