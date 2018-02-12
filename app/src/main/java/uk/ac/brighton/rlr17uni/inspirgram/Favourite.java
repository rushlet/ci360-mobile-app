package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        final ArrayList<Image> images = getIntent().getParcelableArrayListExtra("image_uris");

        final GridView gridview = findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, images));

        final ImageView favouriteImage = findViewById(R.id.favouriteImage);
//
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Drawable highlight = getResources().getDrawable( R.drawable.highlight);
                favourite = position;
                Uri uri = Uri.fromFile(new File(images.get(position).path));

                Picasso.with(Favourite.this)
                        .load(uri)
                        .placeholder(R.drawable.wallpaper)
                        .error(R.drawable.image_placeholder)
                        .fit()
                        .centerCrop()
                        .into(favouriteImage);

//                v.setBackground(highlight);
                Toast.makeText(Favourite.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                //add border to selected image (and not any other image)
                //enable next button
            }
        });
    }


}