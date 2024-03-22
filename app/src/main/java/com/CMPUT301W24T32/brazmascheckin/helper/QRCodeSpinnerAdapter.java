package com.CMPUT301W24T32.brazmascheckin.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.CMPUT301W24T32.brazmascheckin.R;

import java.util.List;

public class QRCodeSpinnerAdapter extends ArrayAdapter<Bitmap> {

    Context context;
    List<Bitmap> qrcodeImageList;

    public QRCodeSpinnerAdapter(Context context, List<Bitmap> qrcodeImages) {
        super(context, R.layout.drop_down_item);
        this.context = context;
        this.qrcodeImageList = qrcodeImages;
    }

    public View GetCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.drop_down_item,parent,false);
        ImageView qrcodeImage = row.findViewById(R.id.img);
        qrcodeImage.setImageBitmap(qrcodeImageList.get(position));
        return row;
    }
}
