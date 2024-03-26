package com.CMPUT301W24T32.brazmascheckin.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.models.User;
import com.CMPUT301W24T32.brazmascheckin.views.ViewAttendeesActivity;

import java.util.ArrayList;

public class AttendeeRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<User> users;
    private ArrayList<Integer> userCheckIns;
    private Context context;

    private int mode;

    public AttendeeRecyclerViewAdapter(Context context, ArrayList<User> users, int mode) {
        this.context = context;
        this.users = users;
        this.mode = mode;
    }

    public AttendeeRecyclerViewAdapter(Context context, ArrayList<User> users, ArrayList<Integer>
                                       checkIns, int mode) {
        this.context = context;
        this.users = users;
        this.userCheckIns = checkIns;
        this.mode = mode;
    }

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

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mode;
    }

    public static class SignUpViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView idTextView;
        private TextView checkInTextView;
        public SignUpViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.attendee_view_card_name_tv);
            idTextView = itemView.findViewById(R.id.attendee_view_card_id_tv);
            checkInTextView = itemView.findViewById(R.id.attendee_view_card_check_ins_tv);

            checkInTextView.setVisibility(View.GONE);

        }

        public void bind(User user) {
            String combinedName = user.getFirstName() + " " + user.getLastName();
            nameTextView.setText(combinedName);
            idTextView.setText(user.getID());
        }
    }

    public static class CheckInViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView idTextView;
        private TextView checkInTextView;

        public CheckInViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.attendee_view_card_name_tv);
            idTextView = itemView.findViewById(R.id.attendee_view_card_id_tv);
            checkInTextView = itemView.findViewById(R.id.attendee_view_card_check_ins_tv);
        }

        @SuppressLint("SetTextI18n")
        public void bind(User user, int checkInCount) {
            String combinedName = user.getFirstName() + " " + user.getLastName();
            nameTextView.setText(combinedName);
            idTextView.setText(user.getID());
            checkInTextView.setText("Check Ins: " + checkInCount);
        }
    }


}
