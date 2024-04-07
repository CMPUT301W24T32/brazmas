package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.controllers.ImageController;
import com.CMPUT301W24T32.brazmascheckin.models.Event;
import com.CMPUT301W24T32.brazmascheckin.models.FirestoreDB;

import java.util.ArrayList;

/**
 * Adapter for the event recycler view
 */
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.EventViewHolder> {

    private ArrayList<Event> events;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private ImageController imageController = new ImageController(FirestoreDB.getStorageInstance());

    /**
     * Constructor for the adapter.
     * @param context The context in which the adapter is created.
     * @param events The lists of events that will be displayed in the RecyclerView.
     */
    public EventRecyclerViewAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    /**
     * Method is called to represent an item.
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
     * Method is called by RecyclerView to display the data at the specified position.
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
     * Method returns the total number of events.
     * @return The total number of events.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * Method sets an itemClickListener for handling click events on the RecyclerView items.
     * @param onItemClickListener is the click listener.
     */
    // Set the click listener
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Method is the interface for the item click listener
     */
    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * Class for displaying individual Event items in the RecyclerView.
     */
    public class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventNameTextView;
        private TextView eventDescriptionTextView;
        private TextView eventDateTextView;
        private ImageView eventPosterTextView;

        /**
         * Method is a constructor.
         * @param itemView the View representing an individual event in the RecyclerView.
         */
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializes the views
            eventNameTextView = itemView.findViewById(R.id.event_view_card_name_tv);
            eventDescriptionTextView = itemView.findViewById(R.id.event_view_card_desc_tv);
            eventDateTextView = itemView.findViewById(R.id.event_view_card_date_tv);
            eventPosterTextView = itemView.findViewById(R.id.event_view_card_poster_iv);

            // Set the click listener for the itemView
            itemView.setOnClickListener(view -> {
                // notify the registered click listener about the recently clicked event
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }

        /**
         * Method binds data from the provided Event to the views within the ViewHolder.
         * @param event the event object and its data the will be displayed.
         */
        public void bind(Event event) {
            eventNameTextView.setText(event.getName());
            eventDescriptionTextView.setText(event.getDescription());
            eventDateTextView.setText(event.getDate().getPrettyDate());

            String eventFolder = ImageController.DEFAULT_EVENT_POSTER;
            String poster = ImageController.DEFAULT_EVENT_POSTER_FILE;
            if(event.getPoster() != null && !event.getPoster().equals(poster)) {
                eventFolder = ImageController.EVENT_POSTER;
                poster = event.getPoster();
            }
            imageController.getImage(eventFolder, poster, byteArray -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                eventPosterTextView.setImageBitmap(bitmap);
            }, null);
        }
    }
}
