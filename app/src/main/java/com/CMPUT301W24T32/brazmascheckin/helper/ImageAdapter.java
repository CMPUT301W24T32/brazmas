package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.CMPUT301W24T32.brazmascheckin.R;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Bitmap> mImages; // List of images to display

    public ImageAdapter(Context context, List<Bitmap> images) {
        mContext = context;
        mImages = images;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // If convertView is null, inflate the layout for each grid item
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item_image, parent, false);
            imageView = convertView.findViewById(R.id.image_view);
            convertView.setTag(imageView);
        } else {
            // Reuse the convertView if it's not null
            imageView = (ImageView) convertView.getTag();
        }

        // Set the image to the ImageView
        imageView.setImageBitmap(mImages.get(position));

        return convertView;
    }
}

