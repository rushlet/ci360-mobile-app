package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

/**
 * Created by rushlet on 27/11/2017.
 */

public class Challenge {
    public static String id;
    public static String name;
    public static String dateTriggered;
    public static String completionDate;
    public static int completed;
    public static JSONArray favourites = new JSONArray();

    public Challenge(String id, String name, String date, String completedBy, int completed) {
        this.id = id;
        this.name = name;
        this.dateTriggered = date;
        this.completionDate = completedBy;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateTriggered() {
        if (dateTriggered == null) {
            dateTriggered = "not set";
        }
        return dateTriggered;
    }

    public String getCompletionDate() {
        if (completionDate == null) {
            completionDate = "not set";
        }
        return completionDate;
    }

    public void setDateTriggered(String date) {
        dateTriggered = date;
    }

    public void setDateForCompletion(String date) {
        completionDate = date;
    }

    public static void addToFavourites(Cursor cursor) throws JSONException {
        JSONObject favourite = new JSONObject();
        favourite.put("photoID", cursor.getString(0));
        favourite.put("photoURL", cursor.getString(4));
        favourite.put("challenge", cursor.getString(1));
        favourite.put("uploadDate", cursor.getString(3));
        favourites.put(favourite);
    }

    public static JSONArray allFavourites() {
        return favourites;
    }
}
