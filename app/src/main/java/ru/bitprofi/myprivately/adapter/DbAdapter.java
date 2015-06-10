package ru.bitprofi.myprivately.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TEXT = "text";
    public static final String KEY_FROM = "sender";
    public static final String KEY_TO = "reciever";
    public static final String KEY_TS = "ts";

    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Privately";
    private static final String SQLITE_TABLE = "Messages";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
            KEY_ROWID + " integer PRIMARY KEY autoincrement," +
            KEY_TEXT + " TEXT," +
            KEY_FROM + " VARCHAR," +
            KEY_TO + " VARCHAR," +
            KEY_TS + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ");";

    public DbAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public boolean deleteAllMessages() {
        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public long createMessage(String text, String from, String to) {
        Log.w(TAG, "Insert message: '" + text + "'");

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TEXT, text);
        initialValues.put(KEY_FROM, from);
        initialValues.put(KEY_TO, to);
        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public Cursor fetchMesagesByCreds(String usr1, String usr2) throws SQLException {
        Cursor mCursor;
        mCursor = mDb.query(
                true,
                SQLITE_TABLE,
                null,
                KEY_FROM + " = '" + usr1 + "' and " + KEY_TO + " = " + usr2 +
                " or " +
                KEY_FROM + " = '" + usr2 + "' and " + KEY_TO + " = " + usr1,
                null, null, null, KEY_TS + " ASC", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }
}
