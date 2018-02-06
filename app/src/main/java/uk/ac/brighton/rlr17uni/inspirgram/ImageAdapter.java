package uk.ac.brighton.rlr17uni.inspirgram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import com.darsh.multipleimageselect.models.Image;

/**
 * Created by rushlet on 05/02/2018.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Image> mImages;
    private Bitmap currentImage;
    private Uri currentURI;

    public ImageAdapter(Context c, ArrayList<Image> images) {
        mContext = c;
        mImages = images;
    }

    public int getCount() {
        return mImages.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        Uri uri = Uri.fromFile(new File(mImages.get(position).path));


        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageURI(uri);

        return imageView;
    };
}
