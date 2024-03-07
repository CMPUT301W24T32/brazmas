package com.CMPUT301W24T32.brazmascheckin.helper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Announcement;


import java.util.ArrayList;

public class AnnouncementRecyclerViewAdapter extends RecyclerView.Adapter<AnnouncementRecyclerViewAdapter.AnnouncementViewHolder> {

    /**
     * List of variables
     */
    private ArrayList<Announcement> announcements;
    private Context context;
    private OnItemClickListener onItemClickListener;

    /**
     * Constructor for this adapter
     * @param context
     * @param announcements
     */
    public AnnouncementRecyclerViewAdapter(Context context, ArrayList<Announcement> announcements) {
        this.context = context;
        this.announcements = announcements;
    }

    /**
     * Creates a new instance for ViewHolder
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return the announcement view
     */
    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_view_card, parent, false);
        return new AnnouncementViewHolder(view);
    }

    /**
     * Binds data to views
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.bind(announcement);
    }

    /**
     * Returns count of announcements
     * @return number of announcements
     */
    @Override
    public int getItemCount() {
        return announcements.size();
    }

    /**
     * Makes announcement clickable
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Interface for clickListener
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * Class to represent each announcement
     */
    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        private TextView announcementTitle;
        private TextView announcementDescription;
        // Add more TextViews or views for other announcement details if needed

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            announcementTitle = itemView.findViewById(R.id.announcement_title_tv);
            announcementDescription = itemView.findViewById(R.id.announcementDescriptionText);
            // Set the click listener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        /**
         * Binds the info to the announcement
         * @param announcement
         */
        public void bind(Announcement announcement) {
            announcementTitle.setText(announcement.getName());
            announcementDescription.setText(announcement.getDescription());
        }
    }
}
