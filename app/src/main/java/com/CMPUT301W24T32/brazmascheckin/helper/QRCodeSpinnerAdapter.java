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

/**
 * Custom ArrayAdapter for displaying QR codes and their corresponding IDs in a Spinner.
 */
public class QRCodeSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    List<Bitmap> qrcodeImageList;

    /**
     * Constructs a new QRCodeSpinnerAdapter.
     *
     * @param context         The context of the activity or fragment.
     * @param qrcodeImages    The list of QR code images to be displayed.
     * @param qrcodeIDs       The list of corresponding QR code IDs.
     */
    public QRCodeSpinnerAdapter(Context context, List<Bitmap> qrcodeImages, List<String> qrcodeIDs) {
        super(context, R.layout.drop_down_item, qrcodeIDs);
        this.context = context;
        this.qrcodeImageList = qrcodeImages;

    }
    /**
     * Returns the custom view for the Spinner dropdown.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return            The custom view for the Spinner dropdown.
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    /**
     * Creates a custom view for the Spinner dropdown.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return            The custom view for the Spinner dropdown.
     */
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
    /**
     * ViewHolder pattern to improve performance by caching views for reuse.
     */
    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
    /**
     * Returns the custom view for the selected item in the Spinner.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return            The custom view for the selected item in the Spinner.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

}
