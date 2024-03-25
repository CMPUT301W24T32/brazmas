package com.CMPUT301W24T32.brazmascheckin.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301W24T32.brazmascheckin.R;
import com.CMPUT301W24T32.brazmascheckin.helper.ImageAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FullScreen extends AppCompatActivity {

    private FileOutputStream outputStream;
    private BitmapDrawable drawable;
    private Bitmap bitmap;
    private File fire, dir;

    private static int REQUEST_CODE = 100;
    ImageView imageView;
    Button button, share;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        imageView = findViewById(R.id.image_view_i);
        button = findViewById(R.id.button_i);
        share = findViewById(R.id.share_i);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");

        // Create an empty list of image URLs
        List<String> imageUrlList = new ArrayList<>();

        // Create an instance of ImageAdapter and pass the context and an empty list of image URLs
        ImageAdapter imageAdapter = new ImageAdapter(imageUrlList, this);

        /**
         * need to implement the button click stuff
         */
    }
}
