package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.CMPUT301W24T32.brazmascheckin.R;

import java.util.List;

public class QRCodeSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    List<Bitmap> qrcodeImageList;

    public QRCodeSpinnerAdapter(Context context, List<Bitmap> qrcodeImages, List<String> qrcodeIDs) {
        super(context, R.layout.drop_down_item, qrcodeIDs);
        this.context = context;
        this.qrcodeImageList = qrcodeImages;

    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.drop_down_item, parent, false);
            holder = new ViewHolder();
            holder.textView = row.findViewById(R.id.text);
            holder.imageView = row.findViewById(R.id.img);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        String fileId = getItem(position);
        Bitmap qrCode = qrcodeImageList.get(position);

        if (fileId != null && qrCode != null) {
            holder.textView.setText(fileId);
            holder.imageView.setImageBitmap(qrCode);
        }

        return row;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    /*public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.drop_down_item,parent,false);
        ImageView qrcodeImage = row.findViewById(R.id.img);
        TextView qrcodeID = row.findViewById(R.id.text);
        qrcodeImage.setImageBitmap(qrcodeImageList.get(position));
        qrcodeID.setText(qrcodeIDList.get(position));
        return row;
    }*/

    /*@Override
    public void add(Bitmap image) {
        qrcodeImageList.add(image);
        //qrcodeIDList.add(eventID);
        // Assuming qrcodeIDList is always of the same size as qrcodeImageList
        // and corresponding IDs are added in the same order as images
        qrcodeIDList.add(String.valueOf(qrcodeImageList.size() - 1)); // Add a dummy ID for now
        notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }*/
}
