package uk.ac.brighton.rlr17uni.inspirgram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private static final String DATABASE_CREATE_CHALLENGE_TABLE = "CREATE TABLE " + TABLE_CHALLENGES
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
    private static final String DATABASE_DELETE_CHALLENGE_TABLE = "DROP TABLE IF EXISTS " + TABLE_CHALLENGES;

    private static final String TABLE_PHOTOS = "photos";
    private static final String PHOTOS_COLUMN_ID = "photo_id";
    private static final String PHOTOS_COLUMN_NAME = "challenge_name";
    private static final String UPLOAD_DATE = "upload_date";
    private static final String IMG_PATH = "text";
    private static final String FAVOURITE = "favourite";

    private static final String DATABASE_CREATE_PHOTO_TABLE = "CREATE TABLE " + TABLE_PHOTOS
            + " ("
            + PHOTOS_COLUMN_ID + " text, "
            + PHOTOS_COLUMN_NAME + " text, "
            + COLUMN_ID + " text, "
            + UPLOAD_DATE + " date, "
            + IMG_PATH + " string, "
            + FAVOURITE + " boolean"
            + " )";
    private static final String DATABASE_DELETE_PHOTO_TABLE = "DROP TABLE IF EXISTS " + TABLE_PHOTOS;

    public DatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQLiteDatabase db = this.getWritableDatabase();
        checkDatabase(context, DATABASE_NAME);
        createChallenge("challenge01", "SHADOW");
        createChallenge("challenge02", "SPOOKY");
        createChallenge("challenge03", "SHIMMER");
        createChallenge("challenge04", "SILENCE");
        createChallenge("challenge05", "TRANQUIL");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create db
        Log.i(TAG, "db on create called");
        db.execSQL(DATABASE_CREATE_CHALLENGE_TABLE);
        db.execSQL(DATABASE_CREATE_PHOTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DELETE_CHALLENGE_TABLE);
        db.execSQL(DATABASE_DELETE_PHOTO_TABLE);
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

    public long uploadPhotos(int position, Uri imgPath, Boolean favourite) {
        SQLiteDatabase db = this.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_PHOTOS);
        String id = "photo"+(count+1);
        String challengeId = Challenge.id;
        String challenge = Challenge.name;
        String name = challenge + position;
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String imagePath = imgPath.toString();

        ContentValues values = new ContentValues();
        values.put(PHOTOS_COLUMN_ID, id);
        values.put(PHOTOS_COLUMN_NAME, name);
        values.put(COLUMN_ID, challengeId);
        values.put(UPLOAD_DATE, date);
        values.put(IMG_PATH, imagePath);
        values.put(FAVOURITE, favourite);
        return db.insert(TABLE_PHOTOS, null, values);
    }

    public long getNumberOfPhotosUploaded() {
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_PHOTOS);
    }

//    public long getFavouritePhotos () {
//
//    }

    // set next challenge - get first entry (or randomly select?) where complete is false or empty

    // mark challenge as complete

    // get burst challenge

    //
}
