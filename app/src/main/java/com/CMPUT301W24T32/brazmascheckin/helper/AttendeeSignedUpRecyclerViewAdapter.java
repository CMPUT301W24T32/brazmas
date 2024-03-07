package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.User;

import java.util.ArrayList;

/**
 * This class is the adapter for the RecyclerView with signed-up attendees.
 * This class is responsible for populating the recycler view with attendee information.
 */
public class AttendeeSignedUpRecyclerViewAdapter extends RecyclerView.Adapter<
        AttendeeSignedUpRecyclerViewAdapter.SignedUpAttendeeViewHolder> {
    private ArrayList<User> users;
    private Context context;

    /**
     * Constrcuts a new AttendeeSignedUpRecyclerViewAdapter
     * @param context the context in which the adapter is being used
     * @param users the list of signed-up attendees
     */
    public AttendeeSignedUpRecyclerViewAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    /**
     * This method is called when a recycler view requires a view holder to
     * represent the data
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public SignedUpAttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendee_signed_up_view_card,
                parent, false);
        return new SignedUpAttendeeViewHolder(view);
    }

    /**
     * This method is called to display an item at a specific position in the recycler view
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull SignedUpAttendeeViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    /**
     * Getter for the number of items being represented in the recycler view
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * ViewHolder class to hold data about signed up attendees
     */
    public class SignedUpAttendeeViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView ID;

        /**
         * Constructs a SignedUpAttendeeViewHolder
         * @param itemView the view that represents the item
         */
        public SignedUpAttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.attendee_signed_up_view_card_name_tv);
            ID = itemView.findViewById(R.id.attendee_signed_up_view_card_id_tv);
        }

        /**
         * This method binds the information to the view
         * @param user user information
         */
        public void bind(User user) {
            String combinedName = user.getFirstName() + " " + user.getLastName();
            name.setText(combinedName);
            ID.setText(user.getID());
        }
    }
}
