package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by rushlet on 27/11/2017.
 */

public class Challenge {
    public static String id;
    public static String name;

    public Challenge(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
