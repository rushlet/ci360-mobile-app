package uk.ac.brighton.rlr17uni.inspirgram;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InspirationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InspirationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InspirationFragment extends Fragment {

    public InspirationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment InspirationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InspirationFragment newInstance() {
        InspirationFragment fragment = new InspirationFragment();
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
        View rootview =  inflater.inflate(R.layout.fragment_inspiration, container, false);
        String tag = "shadow";
        String baseURL = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
        String key =  "60dd9c77c36b4684185bb29b88d99c4c";
        String url = baseURL + "&api_key=" + key +"&tags=" + tag +"&per_page=10&format=json&nojsoncallback=1";
        final TextView mTextView = (TextView) rootview.findViewById(R.id.text);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            JSONObject photos = obj.getJSONObject("photos");
                            JSONArray photoArray = photos.getJSONArray("photo");
                            String[] photoURLs = new String[10];
                            String urls = "";
                            for(int i = 0; i < photoArray.length(); i++) {
                                JSONObject photo = photoArray.getJSONObject(i);
                                String url = "https://farm" + photo.getString("farm") + ".staticflickr.com/" + photo.getString("server") + "/"+photo.getString("id") + "_" + photo.getString("secret") + ".jpg";
                                photoURLs[i] = url;
                                urls = urls + url + ", ";
                            }
                            mTextView.setText("Response is: "+ urls);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mTextView.setText("Response is: "+ response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return rootview;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
