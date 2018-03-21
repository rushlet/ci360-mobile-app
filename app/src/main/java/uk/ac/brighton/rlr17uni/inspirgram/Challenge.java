package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

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

    public static void clearFavourites() {
        favourites = new JSONArray();
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

    public static void scheduleNotifications(Context context) {
        // called when date triggered and complete set
        long dayInMilli = 1000 * 60 * 60 * 24;
        //schedule reminder
        createNotification(context, 1, (2*dayInMilli));
        //schedule final
        createNotification(context, 2, (5*dayInMilli));
        //schedule new
        createNotification(context, 3, (6*dayInMilli));
    }

    private static void createNotification(Context mContext, int id, long time) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra("NOTIFICATION_ID", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();

        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() + time, pendingIntent);
    }
}
