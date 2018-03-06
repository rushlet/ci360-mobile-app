package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;

/**
 * Created by rushlet on 06/03/2018.
 */

public class OverallFavourite {
    public static String id;
    public static String name;
    public static String dateTriggered;
    public static String completionDate;
    public static int completed;

    public OverallFavourite(String id, String name, String date, String completedBy, int completed) {
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
}

