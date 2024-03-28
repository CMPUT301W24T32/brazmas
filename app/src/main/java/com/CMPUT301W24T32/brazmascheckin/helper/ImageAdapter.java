package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.CMPUT301W24T32.brazmascheckin.R;
import java.util.List;
import java.util.Map;

public class ImageAdapter extends BaseAdapter {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private List<Pair<String, String>> imageUrlsWithType; // Update to hold pairs of URL and type
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ImageAdapter(List<Pair<String, String>> imageUrlsWithType, Context context) {
        this.imageUrlsWithType = imageUrlsWithType;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageUrlsWithType.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrlsWithType.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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

    static class ViewHolder {
        ImageView gridImage;
        TextView gridCaption;
    }

}