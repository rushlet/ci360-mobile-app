package uk.ac.brighton.rlr17uni.inspirgram;

        import android.content.Context;
        import android.net.Uri;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.GridView;
        import android.widget.ImageView;

        import com.darsh.multipleimageselect.models.Image;
        import com.squareup.picasso.Picasso;

        import org.json.JSONArray;
        import org.json.JSONException;

        import java.io.File;
        import java.util.ArrayList;

/**
 * Created by rushlet on 06/03/2018.
 */

public class FavouriteImageAdapter  extends BaseAdapter {
    private static final String TAG = ImageAdapter.class.getSimpleName();
    private Context mContext;
    private JSONArray mImages;

    public FavouriteImageAdapter(Context c, JSONArray images) {
        mContext = c;
        mImages = images;
    }

    public int getCount() {
        return mImages.length();
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
            imageView.setLayoutParams(new GridView.LayoutParams(336, 336));
            imageView.setId(position);
        } else {
            imageView = (ImageView) convertView;
        }

        Uri uri = null;
        try {
            uri = Uri.parse(mImages.getJSONArray(position).get(0).toString());
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
