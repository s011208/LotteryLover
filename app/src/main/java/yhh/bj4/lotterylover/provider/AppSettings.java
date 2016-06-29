package yhh.bj4.lotterylover.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by User on 2016/6/15.
 */
public class AppSettings {
    public static final String TABLE_NAME = "AppSettings";
    public static final Uri DATA_URI = LotteryProvider.getUri(TABLE_NAME);

    public static final String COLUMN_KEY = "k";
    private static final String COLUMN_VALUE = "v";

    public static final String COMMAND_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_KEY + " TEXT PRIMARY KEY,"
                    + COLUMN_VALUE + " TEXT NOT NULL"
                    + ")";

    public static boolean contains(Context context, String key) {
        Cursor cursor = context.getContentResolver().query(DATA_URI, null, COLUMN_KEY + "='" + key + "'", null, null);
        if (cursor == null) return false;
        try {
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
        }
    }

    public static int get(Context context, String key, int defaultValue) {
        Cursor c = context.getContentResolver().query(DATA_URI, new String[]{COLUMN_VALUE}, COLUMN_KEY + "='" + key + "'", null, null);
        if (c == null) return defaultValue;
        try {
            while (c.moveToNext()) {
                return c.getInt(0);
            }
        } finally {
            c.close();
        }
        return defaultValue;
    }

    public static void put(Context context, String key, int value) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_KEY, key);
        cv.put(COLUMN_VALUE, value);
        context.getContentResolver().insert(DATA_URI, cv);
    }

    public static void put(Context context, String key, boolean value) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_KEY, key);
        cv.put(COLUMN_VALUE, value ? LotteryProvider.TRUE : LotteryProvider.FALSE);
        context.getContentResolver().insert(DATA_URI, cv);
    }

    public static boolean get(Context context, String key, boolean defaultValue) {
        Cursor c = context.getContentResolver().query(DATA_URI, new String[]{COLUMN_VALUE}, COLUMN_KEY + "='" + key + "'", null, null);
        if (c == null) return defaultValue;
        try {
            while (c.moveToNext()) {
                return c.getInt(0) == LotteryProvider.TRUE;
            }
        } finally {
            c.close();
        }
        return defaultValue;
    }

    public static void put(Context context, String key, long value) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_KEY, key);
        cv.put(COLUMN_VALUE, value);
        context.getContentResolver().insert(DATA_URI, cv);
    }

    public static long get(Context context, String key, long defaultValue) {
        Cursor c = context.getContentResolver().query(DATA_URI, new String[]{COLUMN_VALUE}, COLUMN_KEY + "='" + key + "'", null, null);
        if (c == null) return defaultValue;
        try {
            while (c.moveToNext()) {
                return c.getLong(0);
            }
        } finally {
            c.close();
        }
        return defaultValue;
    }
}
