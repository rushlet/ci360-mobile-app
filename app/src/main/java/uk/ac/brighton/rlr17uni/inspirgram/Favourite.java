package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;

public class Favourite extends Activity {
    private static int favourite = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        final ArrayList<Image> images = getIntent().getParcelableArrayListExtra("image_uris");

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, images));
//
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Drawable highlight = getResources().getDrawable( R.drawable.highlight);
//                favourite = position;
//                v.setBackground(highlight);
//                Toast.makeText(Favourite.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
//                //add border to selected image (and not any other image)
//                //enable next button
//            }
//        });
    }


}