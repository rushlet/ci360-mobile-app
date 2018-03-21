package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;

public class HomeFragment extends Fragment implements Parcelable{
    private static final int PICK_IMAGE = 1;
    private static final String TAG = "Home Frag";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int DAILY_REMINDER_REQUEST_CODE = 001;
    static final String NOTIFICATION_ID = "notification_id";
    ArrayList<Image> SELECTED_IMAGES_ARRAY;
    private FragmentManager supportFragmentManager;
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

//        ImageButton camera = (ImageButton) rootView.findViewById(R.id.imageButton_camera);
//        camera.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view) {
//                openCamera();
//            }
//        });
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
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Intent openFavourites = new Intent(getActivity(), Favourite.class);
            Bundle b=new Bundle();
            SELECTED_IMAGES_ARRAY = images;
            b.putParcelableArrayList("image_uris", SELECTED_IMAGES_ARRAY);
            openFavourites.putExtras(b);
            startActivity(openFavourites);
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
