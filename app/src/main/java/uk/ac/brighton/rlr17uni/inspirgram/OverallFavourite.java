package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.zip.Deflater;

public class OverallFavourite extends AppCompatActivity {
    private static int favourite = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_favourite);
        final Activity activity = this;
        final Context context = this;

        final DatabaseController databasecontroller = new DatabaseController(this);
        final ImageView favouriteImage = (ImageView) findViewById(R.id.overall__selectedImage);
        final GridView gridview = (GridView) findViewById(R.id.overall__gridview);
        final JSONArray favourites =  databasecontroller.checkMultipleFavourites();
        final Button overallButton = (Button) findViewById(R.id.overall__button);

        if (favourites.length() > 1) {
            favouriteImage.setImageResource(android.R.color.transparent);
            gridview.setAdapter(new FavouriteImageAdapter(this, favourites));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    favourite = position;
                    try {
                        Uri uri = Uri.parse(favourites.getJSONArray(position).get(0).toString());
                        Picasso.with(context)
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
                        Toast.makeText(context, "Pick a favourite",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        for (int i = 0; i < favourites.length(); i++) {
                            if (i == favourite) {
                                isOverallFavourite = true;
                            } else {
                                isOverallFavourite = false;
                            }
                            try {
                                JSONArray entry = favourites.getJSONArray(i);
                                databasecontroller.updateFavourite(entry, isOverallFavourite);
                                // check there is now only 1 favourite and redirect to home page.
                                final JSONArray updatedFavourites =  databasecontroller.checkMultipleFavourites();
                                int numberOfFavourites = updatedFavourites.length();
                                Toast.makeText(context, "Photos uploaded", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "Photos uploaded", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }
}
