package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.darsh.multipleimageselect.models.Image;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class UploadCamera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_camera);
        final Context mContext = this;
        final String filepath = getIntent().getStringExtra("image");
        final DatabaseController databasecontroller =  new DatabaseController(this);

        ImageView image = (ImageView) findViewById(R.id.upload_imageView);
        Picasso.with(mContext)
                .load(Uri.parse(filepath))
                .placeholder(R.drawable.wallpaper)
                .error(R.drawable.image_placeholder)
                .fit()
                .centerCrop()
                .into(image);

        // add onclick to buttons
        Button nextButton = (Button) findViewById(R.id.upload_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databasecontroller.uploadPhotos(0, Uri.parse(filepath), true);
                Toast.makeText(UploadCamera.this, "Photos uploaded", Toast.LENGTH_SHORT).show();
                // check if multiple favourites
                if(!((Activity) mContext).isFinishing()) {
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent selectFavourite = new Intent(mContext, OverallFavourite.class);
                            Bundle b=new Bundle();
                            ArrayList images = new ArrayList();
                            images.add(Uri.parse(filepath));
                            b.putParcelableArrayList("image_uris", images);
                            selectFavourite.putExtras(b);
                            finish();
                            startActivity(selectFavourite);
                        }
                    });
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.upload_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
