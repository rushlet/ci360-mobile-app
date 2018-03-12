package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.models.Image;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Favourite extends Activity {
    private static int favourite = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        final Activity activity = this;
        final Context context = this;

        final String TAG = "isFavourite";

        final DatabaseController databasecontroller =  new DatabaseController(this);

        final ArrayList<Image> images = getIntent().getParcelableArrayListExtra("image_uris");

        final GridView gridview = findViewById(R.id.favourite__gridview);
        gridview.setAdapter(new ImageAdapter(this, images));

        final ImageView favouriteImage = findViewById(R.id.favourite__selectedImage);

        final Button nextButton = findViewById(R.id.favourite__nextButton);
        final Button overallButton = findViewById(R.id.favourite__overallButton);
        nextButton.setVisibility(View.VISIBLE);
        overallButton.setVisibility(View.GONE);

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
                    }
                    Toast.makeText(Favourite.this, "Photos uploaded", Toast.LENGTH_SHORT).show();

                    //select overall favourite
                    final JSONArray favourites =  databasecontroller.checkMultipleFavourites();
                    if (favourites.length() > 1) {
                        // clear current favourite
                        favouriteImage.setImageResource(android.R.color.transparent);
                        nextButton.setVisibility(View.GONE);
                        overallButton.setVisibility(View.VISIBLE);
                        // update text on screen
                        TextView title = findViewById(R.id.favourite__selectText);
                        title.setText("Choose overall favourite");
                        // repopulate current grid
                        gridview.setAdapter(new FavouriteImageAdapter(context, favourites));
                        // add new click listener
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                favourite = position;
                                try {
                                    Uri uri = Uri.parse(favourites.getJSONArray(position).get(0).toString());
                                    Picasso.with(Favourite.this)
                                            .load(uri)
                                            .placeholder(R.drawable.wallpaper)
                                            .error(R.drawable.image_placeholder)
                                            .fit()
                                            .centerCrop()
                                            .into(favouriteImage);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        // update click listener on button
                        overallButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                boolean isOverallFavourite;
                                if (favourite == -1) {
                                    Toast.makeText(Favourite.this, "Pick a favourite",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    for (int i = 0; i < favourites.length(); i++) {
                                        if (i == favourite) {
                                            isOverallFavourite = true;
                                        } else {
                                            isOverallFavourite = false;
                                        }
                                        Log.i(TAG, "" + isOverallFavourite);
                                        try {
                                            JSONArray entry = favourites.getJSONArray(i);
                                            databasecontroller.updateFavourite(entry, isOverallFavourite);
                                            // check there is now only 1 favourite and redirect to home page.
                                            final JSONArray updatedFavourites =  databasecontroller.checkMultipleFavourites();
                                            int numberOfFavourites = updatedFavourites.length();
                                            Toast.makeText(Favourite.this, "Photos uploaded", Toast.LENGTH_SHORT).show();
                                            if (numberOfFavourites == 1) {
                                                activity.finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(Favourite.this, "Photos uploaded", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                }
            }
        });
    }


}