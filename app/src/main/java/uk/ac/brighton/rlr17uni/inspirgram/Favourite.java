package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.darsh.multipleimageselect.models.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class Favourite extends Activity {
    private static int favourite = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        final Activity activity = this;

        final String TAG = "isFavourite";

        final DatabaseController databasecontroller =  new DatabaseController(this);

        final ArrayList<Image> images = getIntent().getParcelableArrayListExtra("image_uris");

        final GridView gridview = findViewById(R.id.favourite__gridview);
        gridview.setAdapter(new ImageAdapter(this, images));

        final ImageView favouriteImage = findViewById(R.id.favourite__selectedImage);

        final Button nextButton = findViewById(R.id.favourite__nextButton);
//
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                favourite = position;
                Uri uri = Uri.fromFile(new File(images.get(position).path));

                Picasso.with(Favourite.this)
                        .load(uri)
                        .placeholder(R.drawable.wallpaper)
                        .error(R.drawable.image_placeholder)
                        .fit()
                        .centerCrop()
                        .into(favouriteImage);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (favourite == -1) {
                    Toast.makeText(Favourite.this, "Pick a favourite",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    // upload images to db - new runnable/thread?
                    boolean isFavourite;
                    for (int i = 0; i < images.size(); i++) {
                        Uri uri = Uri.fromFile(new File(images.get(i).path));
                        if (i == favourite) {
                            isFavourite = true;
                        } else {
                            isFavourite = false;
                        }
                        Log.i(TAG, "" + isFavourite);
                        databasecontroller.uploadPhotos(i, uri, isFavourite);
                        ArrayList favourites =  databasecontroller.checkMultipleFavourites();
                        if (favourites.size() > 0) {
                            favourites.size();
                            Toast.makeText(Favourite.this, "Favourites: " + favourites.size(), Toast.LENGTH_SHORT).show();
                            // open new activity
//                            for (int k = 0; k < favourites.size(); k++) {
//                                String url = favourites.get(k).toString();
//                                Uri path = Uri.fromFile(new File(url));
                            } else {
                            Toast.makeText(Favourite.this, "Photos uploaded", Toast.LENGTH_SHORT).show();
                            activity.finish();
                        }
                    }
                }
            }
        });
    }


}