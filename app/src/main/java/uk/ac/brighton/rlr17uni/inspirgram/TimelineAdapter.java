package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by rushlet on 12/03/2018.
 */

public class TimelineAdapter extends BaseAdapter {
    private JSONArray favourites = new JSONArray();
    private Activity context;

    public TimelineAdapter(Activity c, JSONArray favourites) throws JSONException {
        this.context = c;
        this.favourites = favourites;
    }

    public int getCount() {
        int count = favourites.length();
        return count;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.timeline_item, null, false);
        rowView.setId(position);

        TextView challengeText = (TextView) rowView.findViewById(R.id.timeline__challenge);
        TextView dateText = (TextView) rowView.findViewById(R.id.timeline__date);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.timeline__image);

        try {
            challengeText.setText(favourites.getJSONObject(position).getString("challenge"));
            dateText.setText(favourites.getJSONObject(position).getString("uploadDate"));

            Uri uri = Uri.parse(favourites.getJSONObject(position).getString("photoURL"));
            Picasso.with(context)
                    .load(uri)
                    .placeholder(R.drawable.wallpaper)
                    .error(R.drawable.image_placeholder)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;
    }
}
