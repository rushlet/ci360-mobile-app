package uk.ac.brighton.rlr17uni.inspirgram;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    ArrayList<Image> SELECTED_IMAGES_ARRAY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Fragment fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, fragment, "home_fragment");
        ft.commit();
    }

//    http://blog.teamtreehouse.com/add-navigation-drawer-android
    private void addDrawerItems() {
        String[] pagesArray = { "Home", "Upload", "Inspire Me", "Timeline" };
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pagesArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch(position) {
                    case 0:
                        Fragment homeFragment = new HomeFragment();
                        ft.replace(R.id.fragment_placeholder, homeFragment);
                        ft.commit();
                        mActivityTitle = getResources().getString(R.string.app_name);
                        break;
                    case 1:
                        openGallery();
                        break;
                    case 2:
                        Fragment inspirationFragment = new InspirationFragment();
                        ft.replace(R.id.fragment_placeholder, inspirationFragment);
                        ft.commit();
                        mActivityTitle = getResources().getString(R.string.menu_inspire);
                        break;
                    case 3:
                        Fragment timelineFragment = new TimelineFragment();
                        ft.replace(R.id.fragment_placeholder, timelineFragment);
                        ft.commit();
                        mActivityTitle = getResources().getString(R.string.menu_timeline);
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
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //    https://github.com/darsh2/MultipleImageSelect
    public void openGallery() {
        Intent intent = new Intent(getBaseContext(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Intent openFavourites = new Intent(getBaseContext(), Favourite.class);
            Bundle b=new Bundle();
            SELECTED_IMAGES_ARRAY = images;
            b.putParcelableArrayList("image_uris", SELECTED_IMAGES_ARRAY);
            openFavourites.putExtras(b);
            startActivity(openFavourites);
        }
    }
}

