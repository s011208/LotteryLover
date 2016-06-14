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

    private static final String COLUMN_KEY = "k";
    private static final String COLUMN_VALUE = "v";

    public static final String COMMAND_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_KEY + " TEXT PRIMARY KEY,"
                    + COLUMN_VALUE + " TEXT NOT NULL"
                    + ")";

    public static int get(Context context, String key, int defaultValue) {
        int rtn = defaultValue;
        Cursor c = context.getContentResolver().query(DATA_URI, new String[]{COLUMN_VALUE}, COLUMN_KEY + "='" + key + "'", null, null);
        if (c == null) return rtn;
        try {
            while (c.moveToNext()) {
                return c.getInt(0);
            }
        } finally {
            c.close();
        }
        return rtn;
    }

    public static void put(Context context, String key, int value) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_KEY, key);
        cv.put(COLUMN_VALUE, value);
        context.getContentResolver().insert(DATA_URI, cv);
    }
}
