package com.CMPUT301W24T32.brazmascheckin.helper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Notification;


import java.util.ArrayList;

public class AnnouncementRecyclerViewAdapter extends RecyclerView.Adapter<AnnouncementRecyclerViewAdapter.AnnouncementViewHolder> {

    private ArrayList<Notification> announcements;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public AnnouncementRecyclerViewAdapter(Context context, ArrayList<Notification> announcements) {
        this.context = context;
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcement_view_card, parent, false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Notification announcement = announcements.get(position);
        holder.bind(announcement);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    // Set the click listener
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        private TextView announcementTitle;
        // Add more TextViews or views for other announcement details if needed

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            announcementTitle = itemView.findViewById(R.id.announcementTitle); // Replace with your actual TextView ID
            // Initialize other TextViews or views for other announcement details

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

        public void bind(Notification announcement) {
            announcementTitle.setText(announcement.getName());
            // Set other TextViews or views with corresponding announcement details
        }
    }
}
