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
 * Adapter for the checked in attendees recycler view.
 */
public class AttendeeCheckedInRecyclerViewAdapter extends RecyclerView.Adapter<AttendeeCheckedInRecyclerViewAdapter.AttendeeViewHolder> {

    private ArrayList<User> users;
    private ArrayList<Integer> userCheckIns;
    private Context context;
    private OnItemClickListener listener;


    /**
     * This method is an interface for an item click listener.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    /**
     * Constructs a new AttendeeCheckedInRecyclerViewAdapter
     * @param context the context in which the adapter is being used
     * @param users the list of checked-in attendees
     * @param listener the on item clicked listener
     */
    public AttendeeCheckedInRecyclerViewAdapter(Context context, ArrayList<User> users,
                                                ArrayList<Integer> userCheckIns,
                                                OnItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.userCheckIns = userCheckIns;
        this.listener = listener;
    }

    /**
     * This method is called when a recycler view requires a view holder to
     * represent the data (the checked-in attendee).
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return The checked in attendee view holder.
     */
    @NonNull
    @Override
    public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendee_view_card, parent, false);
        return new AttendeeViewHolder(view);
    }

    /**
     * This method is called to display an item at a specific position in the recycler view.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AttendeeViewHolder holder, int position) {
        User user = users.get(position);
        int checkInCount = userCheckIns.get(position);
        holder.bind(user, checkInCount);
    }

    /**
     * Getter for the number of items being represented in the recycler view
     * @return the number of items.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * This ViewHolder class displays the individual attendees checked in items in the RecyclerView.
     */
    public class AttendeeViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView ID;
        private TextView checkIn;

        /**
         * This method is a constructor.
         * @param itemView Represents the individual item in the RecyclerView
         */
        public AttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.attendee_view_card_name_tv);
            ID = itemView.findViewById(R.id.attendee_view_card_id_tv);
            checkIn = itemView.findViewById(R.id.attendee_view_card_check_ins_tv);

            itemView.setOnClickListener(view -> {
                if(listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(view -> {
                if(listener != null) {
                    listener.onItemLongClick(getAdapterPosition());
                }
                return false;
            });
        }

        /**
         * This method binds data from provided User to views within AttendeeViewHolder.
         * @param user The User object contains data to be displayed.
         * @param checkInCount The count of check-ins associated with user.
         */
        public void bind(User user, int checkInCount) {
            String combinedName = user.getFirstName() + " " + user.getLastName();
            name.setText(combinedName);
            ID.setText(user.getID());
            checkIn.setText("Check Ins: " + checkInCount);
        }
    }
}
