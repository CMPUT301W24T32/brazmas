package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Event;

import java.util.ArrayList;

/**
 * Adapter for the event recycler view
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventViewHolder> {

    private ArrayList<Event> events;
    private Context context;
    private OnItemClickListener onItemClickListener;

    /**
     * This method is a constructor for the adapter.
     * @param context The context in which the adapter is created.
     * @param events The lists of events that will be displayed in the RecyclerView.
     */
    public EventRecyclerViewAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    /**
     * This method is called to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return Returns View with the layout resource event_view_card.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_view_card, parent, false);
        return new EventViewHolder(view);
    }

    /**
     * This method is called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }

    /**
     * This method returns the total number of events.
     * @return The total number of events.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * This method sets an itemClickListener for handling click events on the RecyclerView items.
     * @param onItemClickListener is the click listener.
     */
    // Set the click listener
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * This method is the interface for the item click listener
     */
    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * Class for displaying individual Event items in the RecyclerView.
     */
    public class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName;
        private TextView eventDescription;
        private TextView eventDate;

        /**
         * This method is a constructor.
         * @param itemView the View representing an individual event in the RecyclerView.
         */
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializes the views
            eventName = itemView.findViewById(R.id.nameText);
            eventDescription = itemView.findViewById(R.id.descriptionText);
            eventDate = itemView.findViewById(R.id.dateText);

            // Set the click listener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * Called when a view has been clicked.
                 * @param view The view that was clicked.
                 */
                @Override
                public void onClick(View view) {
                    // notify the registered click listener about the recently clicked event
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        /**
         * Binds data from the provided Event to the views within the ViewHolder.
         * @param event the event object and its data the will be displayed.
         */
        public void bind(Event event) {
            eventName.setText(event.getName());
            eventDescription.setText(event.getDescription());
            eventDate.setText(event.getDate().getPrettyDate());
        }
    }
}
