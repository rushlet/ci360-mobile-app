package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import static android.R.attr.path;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    public void onActivityCreated(Bundle savedInstanceState) {
//        DatabaseController databasecontroller =  new DatabaseController(this);
//        Challenge currentChallenge = databasecontroller.getChallenge("challenge01");
//        TextView text = (TextView) findViewById(R.id.challengeText);
//        text.setText(currentChallenge.getName());
//    }

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
    }

    public void writeToFile(String input) {

    }

//    http://codetheory.in/android-pick-select-image-from-gallery-with-intents/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);


//                bitmap too large to be uploaded into a texture
//                Bitmap d= BitmapFactory.decodeFile(String.valueOf(bitmap));
//                int newHeight = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
//                Bitmap putImage = Bitmap.createScaledBitmap(d, 512, newHeight, true);
//                imageView.setImageBitmap(putImage);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
