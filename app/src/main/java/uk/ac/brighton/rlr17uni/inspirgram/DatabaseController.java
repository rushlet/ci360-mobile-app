package uk.ac.brighton.rlr17uni.inspirgram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;

import java.io.File;

/**
 * Created by rushlet on 27/11/2017.
 */

public class DatabaseController extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "databasecontroller";
    private Context context;

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
            + COLUMN_ID + " text, "
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
        this.context = context;
        SQLiteDatabase db = this.getWritableDatabase();
        boolean checkDatabase = checkDatabase(context, DATABASE_NAME);
//        Log.d(TAG, "check db: " + checkDatabase(context, DATABASE_NAME));
        long createChallenge = createChallenge("challenge01", "SHADOW");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create db
        Log.i(TAG, "db on create called");
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DELETE);
        onCreate(db);
    }

    private boolean checkDatabase(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public long createChallenge(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_TRIGGERED, false);
        values.put(COLUMN_COMPLETE, false);
        values.put(COLUMN_MAIN_CHALLENGE, true);
        return db.insert(TABLE_CHALLENGES, null, values);
    }

    public Challenge getChallenge(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_MAIN_CHALLENGE, COLUMN_COMPLETE, COLUMN_TRIGGERED};
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = {id};
        Cursor cursor = db.query(TABLE_CHALLENGES, columns, whereClause, whereArgs, null, null, null, null);
        cursor.moveToFirst();
        Challenge challenge = new Challenge(cursor.getString(0), cursor.getString(1));
        cursor.close();
        return challenge;
    }

    // set next challenge - get first entry (or randomly select?) where complete is false or empty

    // mark challenge as complete

    // get burst challenge

    //
}
