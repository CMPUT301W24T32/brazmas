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

    private List<Pair<String, String>> imageUrlsWithType; // Update to hold pairs of URL and type
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    /**
     * Sets the listener for item clicks.
     *
     * @param listener The listener to set.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /**
     * Constructs an ImageAdapter.
     *
     * @param imageUrlsWithType List of pairs containing image URLs and their types.
     * @param context           The context.
     */
    public ImageAdapter(List<Pair<String, String>> imageUrlsWithType, Context context) {
        this.imageUrlsWithType = imageUrlsWithType;
        this.context = context;
    }

    /**
     * Returns the number of items in the adapter's data set.
     *
     * @return The number of items in the adapter's data set.
     */

    @Override
    public int getCount() {
        return imageUrlsWithType.size();
    }

    /**
     * Returns the data item at the specified position in the adapter's data set.
     *
     * @param position The position of the item within the adapter's data set.
     * @return The data item at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return imageUrlsWithType.get(position);
    }

    /**
     * Returns the row ID associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set.
     * @return The row ID associated with the specified position.
     */

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
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
     * ViewHolder pattern for better ListView performance.
     */
    static class ViewHolder {
        ImageView gridImage;
        TextView gridCaption;
    }

}
