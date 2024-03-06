package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.Attendee;

import java.util.ArrayList;

/**
 *
 */
public class AttendeeRecyclerViewAdapter extends RecyclerView.Adapter<AttendeeRecyclerViewAdapter.AttendeeViewHolder> {

    private ArrayList<Attendee> attendees;
    private Context context;
    private OnItemClickListener listener;


    /**
     *
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    /**
     *
     * @param context
     * @param attendees
     * @param listener
     */
    public AttendeeRecyclerViewAdapter(Context context, ArrayList<Attendee> attendees,
                                       OnItemClickListener listener) {
        this.context = context;
        this.attendees = attendees;
        this.listener = listener;
    }

    /**
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public AttendeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attendee_view_card, parent, false);
        return new AttendeeViewHolder(view);
    }

    /**
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AttendeeViewHolder holder, int position) {
        Attendee attendee = attendees.get(position);
        holder.bind(attendee);
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return attendees.size();
    }

    /**
     *
     */
    public class AttendeeViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView ID;

        /**
         *
         * @param itemView
         */
        public AttendeeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.attendee_view_card_name_tv);
            ID = itemView.findViewById(R.id.attendee_view_card_id_tv);

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
         *
         * @param attendee
         */
        public void bind(Attendee attendee) {
            String combinedName = attendee.getFirstName() + " " + attendee.getLastName();
            name.setText(combinedName);
            ID.setText(attendee.getID());
        }
    }
}
