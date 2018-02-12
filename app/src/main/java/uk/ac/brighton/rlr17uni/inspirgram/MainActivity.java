package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.logo;
import static android.R.attr.path;

public class MainActivity extends AppCompatActivity implements Parcelable {

    private static final int PICK_IMAGE = 1;
    private static final String TAG = "MainActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    ArrayList<Image> SELECTED_IMAGES_ARRAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseController databasecontroller =  new DatabaseController(this);
        Challenge currentChallenge = databasecontroller.getChallenge("challenge01");
        TextView text = (TextView) findViewById(R.id.challengeText);
        text.setText(currentChallenge.getName());
    }

    //    https://github.com/darsh2/MultipleImageSelect
    public void openGallery(View view) {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Intent openFavourites = new Intent(this, Favourite.class);
            Bundle b=new Bundle();
            SELECTED_IMAGES_ARRAY = images;
            b.putParcelableArrayList("image_uris", SELECTED_IMAGES_ARRAY);
            openFavourites.putExtras(b);
            startActivity(openFavourites);
        }
    }

    // using android parcelable code generator

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.SELECTED_IMAGES_ARRAY);
    }

    public MainActivity() {
    }

    protected MainActivity(Parcel in) {
        this.SELECTED_IMAGES_ARRAY = in.createTypedArrayList(Image.CREATOR);
    }

    public static final Creator<MainActivity> CREATOR = new Creator<MainActivity>() {
        @Override
        public MainActivity createFromParcel(Parcel source) {
            return new MainActivity(source);
        }

        @Override
        public MainActivity[] newArray(int size) {
            return new MainActivity[size];
        }
    };
}
