package yhh.bj4.lotterylover.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.lto.Lto;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class LotteryDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ltd.db";
    private static final int VERSION = 1;

    private static final String TAG = "LotteryDatabase";
    private static final boolean DEBUG = Utilities.DEBUG;

    public LotteryDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableLto(db);
        createTableAppSettings(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTableAppSettings(SQLiteDatabase db) {
        db.execSQL(AppSettings.COMMAND_CREATE_TABLE);
    }

    private void createTableLto(SQLiteDatabase db) {
        db.execSQL(Lto.COMMAND_CREATE_TABLE(Lto.TABLE_NAME));
    }
}
