package uk.ac.brighton.rlr17uni.inspirgram;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements Parcelable{
    private static final int PICK_IMAGE = 1;
    private static final String TAG = "Home Frag";
    ArrayList<Image> SELECTED_IMAGES_ARRAY;
    private FragmentManager supportFragmentManager;
    static final int REQUEST_IMAGE_CAPTURE = 3;
    private String mCurrentPhotoPath;
    private Context mContext = this.getContext();

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_home2, container, false);
        DatabaseController databasecontroller =  new DatabaseController(getContext());
        Challenge currentChallenge = databasecontroller.getChallenge();
        String dateTriggered = currentChallenge.getDateTriggered();

        if (dateTriggered == "not set") {
            databasecontroller.setChallenge(currentChallenge.getId(), currentChallenge);
            currentChallenge.scheduleNotifications(mContext);

        } else {
            String dateCompleteBy = currentChallenge.getCompletionDate();
            try {
                if (new SimpleDateFormat("dd-MM-yyyy").parse(dateCompleteBy).before(new Date())) {
                    // completed. mark as completed and set new challenge
                    databasecontroller.completeChallenge(currentChallenge.getId());
                    databasecontroller.getChallenge();
                    databasecontroller.setChallenge(currentChallenge.getId(), currentChallenge);
                    currentChallenge.scheduleNotifications(mContext);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        TextView text = (TextView) rootView.findViewById(R.id.challengeText);
        text.setText(currentChallenge.getName());
        ImageButton gallery = (ImageButton) rootView.findViewById(R.id.imageButton_gallery);
        gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        ImageButton inspiration = (ImageButton) rootView.findViewById(R.id.imageButton_inspiration);
        inspiration.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openInspiration();
            }
        });

        ImageButton camera = (ImageButton) rootView.findViewById(R.id.imageButton_camera);
        camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void openInspiration() {
        Fragment fragment = new InspirationFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, fragment, "home_fragment");
        ft.commit();
    }

    //    https://github.com/darsh2/MultipleImageSelect
    public void openGallery() {
        Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent, PICK_IMAGE);
    }

//    https://stackoverflow.com/questions/2729267/android-camera-intent
    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Inspirgram_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Intent openFavourites = new Intent(getActivity(), Favourite.class);
            Bundle b=new Bundle();
            SELECTED_IMAGES_ARRAY = images;
            b.putParcelableArrayList("image_uris", SELECTED_IMAGES_ARRAY);
            openFavourites.putExtras(b);
//            startActivity(openFavourites);
            startActivityForResult(openFavourites, PICK_IMAGE);

        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent uploadImage = new Intent(getActivity(), UploadCamera.class);
            uploadImage.putExtra("image", mCurrentPhotoPath);
//            startActivity(uploadImage);
            startActivityForResult(uploadImage, REQUEST_IMAGE_CAPTURE);
        }
    }

    // created using parcelable generator

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.SELECTED_IMAGES_ARRAY);
    }

    protected HomeFragment(Parcel in) {
        this.SELECTED_IMAGES_ARRAY = in.createTypedArrayList(Image.CREATOR);
    }

    public static final Creator<HomeFragment> CREATOR = new Creator<HomeFragment>() {
        @Override
        public HomeFragment createFromParcel(Parcel source) {
            return new HomeFragment(source);
        }

        @Override
        public HomeFragment[] newArray(int size) {
            return new HomeFragment[size];
        }
    };
}
