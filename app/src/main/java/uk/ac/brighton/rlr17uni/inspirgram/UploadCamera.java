package uk.ac.brighton.rlr17uni.inspirgram;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UploadCamera extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_camera);
        Context mContext = this;
        String filepath = getIntent().getStringExtra("image");

        ImageView image = (ImageView) findViewById(R.id.upload_imageView);
        Picasso.with(mContext)
                .load(Uri.parse(filepath))
                .placeholder(R.drawable.wallpaper)
                .error(R.drawable.image_placeholder)
                .fit()
                .centerCrop()
                .into(image);
    }

    // add onclick to buttons

//    databasecontroller.uploadPhotos(i, uri, isFavourite);
}
