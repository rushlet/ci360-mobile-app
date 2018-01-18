package uk.ac.brighton.rlr17uni.inspirgram;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by rushlet on 27/11/2017.
 */

public class Challenge {
    private String id;
    private String name;

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

    public void updateChallengeOnScreen (Bundle saveInstanceState){
        String challenge = this.getName();

    }


}
