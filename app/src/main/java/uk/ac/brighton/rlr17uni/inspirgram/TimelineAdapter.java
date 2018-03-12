package uk.ac.brighton.rlr17uni.inspirgram;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by rushlet on 12/03/2018.
 */

public class TimelineAdapter extends BaseAdapter {
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private Context mContext;
    private JSONArray mFavourites;

    public TimelineAdapter(Context c, JSONArray favourites) {
        mContext = c;
        mFavourites = favourites;
    }

    public int getCount() {
        return mFavourites.length();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
            imageView.setId(position);
        } else {
            imageView = (ImageView) convertView;
        }

        Uri uri = null;
        try {
            uri = Uri.parse(mFavourites.getJSONObject(position).getString("photoURL"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Picasso.with(mContext)
                .load(uri)
                .placeholder(R.drawable.wallpaper)
                .error(R.drawable.image_placeholder)
                .fit()
                .centerCrop()
                .into(imageView);

        return imageView;
    }
}
