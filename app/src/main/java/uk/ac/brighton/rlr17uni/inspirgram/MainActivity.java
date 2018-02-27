package uk.ac.brighton.rlr17uni.inspirgram;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;

import static uk.ac.brighton.rlr17uni.inspirgram.R.id.default_activity_button;
import static uk.ac.brighton.rlr17uni.inspirgram.R.id.parent;

public class MainActivity extends AppCompatActivity implements Parcelable {

    private static final int PICK_IMAGE = 1;
    private static final String TAG = "MainActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    ArrayList<Image> SELECTED_IMAGES_ARRAY;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseController databasecontroller =  new DatabaseController(this);
        Challenge currentChallenge = databasecontroller.getChallenge("challenge01");
        TextView text = (TextView) findViewById(R.id.challengeText);
        text.setText(currentChallenge.getName());
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {
        String[] pagesArray = { "Home", "Inspire Me", "Timeline" };
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pagesArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "id: "+id + " position: " + position, Toast.LENGTH_SHORT).show();
                switch(position) {
                    case 0:
                        setContentView(R.layout.activity_main);
                        break;
                    case 1:
                        Intent inspirationIntent = new Intent(view.getContext(), InspirationFeed.class);
                        startActivity(inspirationIntent);
                        break;
                    case 2:
                        Intent timelineIntent = new Intent(view.getContext(), Timeline.class);
                        startActivity(timelineIntent);
                        break;
                    default:
                        break;
                }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.menu);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

