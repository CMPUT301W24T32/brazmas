package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * This class is an adapter for displaying images in a grid view with captions.
 * Adapter for displaying images in a GridView.
 */
public class ImageAdapter extends BaseAdapter {

    /**
     * Interface definition for a callback to be invoked when an item in this AdapterView has been clicked.
     */
    public interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in the AdapterView has been clicked.
         *
         * @param position The position of the item clicked.
         */
        void onItemClick(int position);
    }

    private List<Pair<String, String>> imageUrlsWithType; // hold pairs of URL and type
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    /**
     * Sets the listener for item click events.
     * @param listener the listener to be set.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /**
     * Constructs a new ImageAdapter with the given list of image URLs and types and the provided context.
     * @param imageUrlsWithType list of pairs containing image URLs and their types.
     * @param context the context in which the adapter will be used.
     */
    public ImageAdapter(List<Pair<String, String>> imageUrlsWithType, Context context) {
        this.imageUrlsWithType = imageUrlsWithType;
        this.context = context;
    }

    /**
     * This method returns the size of imagesUrlsWithType list.
     * @return the size, integer.
     */
    @Override
    public int getCount() {
        return imageUrlsWithType.size();
    }

    /**
     * This method gets the item at specific position.
     * @param position Position of the item whose data we want within the adapter's data set.
     * @return the item to be returned
     */
    @Override
    public Object getItem(int position) {
        return imageUrlsWithType.get(position);
    }

    /**
     * This method returns the ID of the item.
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return the item's ID
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View displaying the data at the specified position in the data set.
     * @param position The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to
     * @return a view corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false);
            holder = new ViewHolder();
            holder.gridImage = convertView.findViewById(R.id.gridImage);
            holder.gridCaption = convertView.findViewById(R.id.gridCaption); // Get reference to TextView
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Load image using Glide
        Glide.with(context).load(imageUrlsWithType.get(position).first).into(holder.gridImage);

        // set caption text based on image type
        String imageType = imageUrlsWithType.get(position).second;
        if (imageType.equals("EVENT_POSTER")) {
            holder.gridCaption.setText("Event Poster");
        } else if (imageType.equals("PROFILE_PICTURE")) {
            holder.gridCaption.setText("Profile Picture");
        }

        holder.gridImage.setOnClickListener(new View.OnClickListener() {
            /**
             * This method handles the click event on the gridImage ImageView.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        return convertView;
    }

    /**
     * This method is the ViewHolder class to hold views for efficient recycling.
     */
    static class ViewHolder {
        ImageView gridImage;
        TextView gridCaption;
    }
}
