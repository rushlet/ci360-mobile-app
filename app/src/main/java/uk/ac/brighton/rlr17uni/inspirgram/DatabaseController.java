package uk.ac.brighton.rlr17uni.inspirgram;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;

/**
 * Created by rushlet on 27/11/2017.
 */

public class DatabaseController extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inspirgram.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "databasecontroller";

    private static final String TABLE_CHALLENGES = "challenges";
    private static final String COLUMN_ID = "challenge_id";
    private static final String COLUMN_NAME = "challenge_name";
    private static final String COLUMN_TRIGGERED = "triggered";
    private static final String COLUMN_COMPLETE = "complete";
    private static final String COLUMN_COMPLETE_DATE = "completion_date";
    private static final String COLUMN_MAIN_CHALLENGE = "main_challenge";
    private static final String COLUMN_BURST = "burst";
    private static final String COLUMN_WEEK = "week";

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_CHALLENGES
            + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "
            + COLUMN_TRIGGERED + " boolean, "
            + COLUMN_COMPLETE + " boolean, "
            + COLUMN_COMPLETE_DATE + " date, "
            + COLUMN_MAIN_CHALLENGE + " boolean, "
            + COLUMN_BURST + " boolean, "
            + COLUMN_WEEK + " integer"
            + " )";
    private static final String DATABASE_DELETE = "DROP TABLE IF EXISTS " + TABLE_CHALLENGES;

    public DatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "database controller super thing called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create db
        Log.i(TAG, "database create: " + DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
        // add entries
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DELETE);
        onCreate(db);
    }
}
