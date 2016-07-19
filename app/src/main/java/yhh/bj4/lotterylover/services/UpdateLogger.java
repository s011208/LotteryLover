package yhh.bj4.lotterylover.services;

import android.net.Uri;

import yhh.bj4.lotterylover.provider.LotteryProvider;

/**
 * Created by yenhsunhuang on 2016/7/18.
 */
public class UpdateLogger {
    public static final String TABLE_NAME = "update_logger";
    public static final Uri URI = LotteryProvider.getUri(TABLE_NAME);

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_REASON = "reason";
    public static final String COLUMN_TYPE = "type";
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_INFO = 1;

    public static final String COMMAND_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + COLUMN_TIME + " INTEGER NOT NULL,"
            + COLUMN_TYPE + " INTEGER NOT NULL DEFAULT " + TYPE_NORMAL + ","
            + COLUMN_REASON + " TEXT NOT NULL"
            + ")";
}
