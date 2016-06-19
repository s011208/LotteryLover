package yhh.bj4.lotterylover.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class LotteryProvider extends ContentProvider {
    private static final String TAG = "LotteryProvider";
    private static final boolean DEBUG = Utilities.DEBUG;

    private static final String AUTHORITY = "yhh.bj4.lotterylover.lottery_provider";
    private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int APP_SETTINGS_MATCHER = 0;
    private static final int LTO_MATCHER = 1;
    private static final int LTO_BIG_MATCHER = 2;
    private static final int LTO_HK_MATCHER = 3;
    private static final int LTO_DOF_MATCHER = 4;
    private static final int LTO_2C_MATCHER = 5;
    private static final int LTO_7C_MATCHER = 6;

    static {
        sMatcher.addURI(AUTHORITY, Lto.TABLE_NAME, LTO_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoBig.TABLE_NAME, LTO_BIG_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoHK.TABLE_NAME, LTO_HK_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoDof.TABLE_NAME, LTO_DOF_MATCHER);
        sMatcher.addURI(AUTHORITY, Lto2C.TABLE_NAME, LTO_2C_MATCHER);
        sMatcher.addURI(AUTHORITY, Lto7C.TABLE_NAME, LTO_7C_MATCHER);
        sMatcher.addURI(AUTHORITY, AppSettings.TABLE_NAME, APP_SETTINGS_MATCHER);
    }

    private SQLiteDatabase mDatabase;

    public static Uri getUri(String tableName) {
        return Uri.parse("content://" + AUTHORITY + "/" + tableName);
    }

    @Override
    public boolean onCreate() {
        mDatabase = new LotteryDatabase(getContext()).getWritableDatabase();
//        if (DEBUG) {
//            printDatabaseData(Lto.DATA_URI);
//            printDatabaseData(AppSettings.DATA_URI);
//        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor rtn;
        switch (sMatcher.match(uri)) {
            case LTO_MATCHER:
                rtn = mDatabase.query(Lto.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LTO_BIG_MATCHER:
                rtn = mDatabase.query(LtoBig.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LTO_HK_MATCHER:
                rtn = mDatabase.query(LtoHK.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LTO_DOF_MATCHER:
                rtn = mDatabase.query(LtoDof.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LTO_2C_MATCHER:
                rtn = mDatabase.query(Lto2C.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LTO_7C_MATCHER:
                rtn = mDatabase.query(Lto7C.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case APP_SETTINGS_MATCHER:
                rtn = mDatabase.query(AppSettings.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new RuntimeException("unexpected query uri: " + uri);
        }
        return rtn;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri rtn;
        switch (sMatcher.match(uri)) {
            case LTO_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(Lto.TABLE_NAME, null, values));
                break;
            case LTO_BIG_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(LtoBig.TABLE_NAME, null, values));
                break;
            case LTO_HK_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(LtoHK.TABLE_NAME, null, values));
                break;
            case LTO_DOF_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(LtoDof.TABLE_NAME, null, values));
                break;
            case LTO_2C_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(Lto2C.TABLE_NAME, null, values));
                break;
            case LTO_7C_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(Lto7C.TABLE_NAME, null, values));
                break;
            case APP_SETTINGS_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.replace(AppSettings.TABLE_NAME, null, values));
                break;
            default:
                throw new RuntimeException("unexpected insert uri: " + uri);
        }
        return rtn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rtn;
        switch (sMatcher.match(uri)) {
            case LTO_MATCHER:
                rtn = mDatabase.delete(Lto.TABLE_NAME, selection, selectionArgs);
                break;
            case LTO_BIG_MATCHER:
                rtn = mDatabase.delete(LtoBig.TABLE_NAME, selection, selectionArgs);
                break;
            case LTO_HK_MATCHER:
                rtn = mDatabase.delete(LtoHK.TABLE_NAME, selection, selectionArgs);
                break;
            case LTO_DOF_MATCHER:
                rtn = mDatabase.delete(LtoDof.TABLE_NAME, selection, selectionArgs);
                break;
            case LTO_2C_MATCHER:
                rtn = mDatabase.delete(Lto2C.TABLE_NAME, selection, selectionArgs);
                break;
            case LTO_7C_MATCHER:
                rtn = mDatabase.delete(Lto7C.TABLE_NAME, selection, selectionArgs);
                break;
            case APP_SETTINGS_MATCHER:
                rtn = mDatabase.delete(AppSettings.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new RuntimeException("unexpected delete uri: " + uri);
        }
        return rtn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rtn;
        switch (sMatcher.match(uri)) {
            case LTO_MATCHER:
                rtn = mDatabase.update(Lto.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LTO_BIG_MATCHER:
                rtn = mDatabase.update(LtoBig.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LTO_HK_MATCHER:
                rtn = mDatabase.update(LtoHK.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LTO_DOF_MATCHER:
                rtn = mDatabase.update(LtoDof.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LTO_2C_MATCHER:
                rtn = mDatabase.update(Lto2C.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LTO_7C_MATCHER:
                rtn = mDatabase.update(Lto7C.TABLE_NAME, values, selection, selectionArgs);
                break;
            case APP_SETTINGS_MATCHER:
                mDatabase.replace(AppSettings.TABLE_NAME, null, values);
                rtn = 1;
                break;
            default:
                throw new RuntimeException("unexpected update uri: " + uri);
        }
        return rtn;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int rtn;
        switch (sMatcher.match(uri)) {
            case LTO_MATCHER:
                rtn = bulkInsert(Lto.TABLE_NAME, values);
                break;
            case LTO_BIG_MATCHER:
                rtn = bulkInsert(LtoBig.TABLE_NAME, values);
                break;
            case LTO_HK_MATCHER:
                rtn = bulkInsert(LtoHK.TABLE_NAME, values);
                break;
            case LTO_DOF_MATCHER:
                rtn = bulkInsert(LtoDof.TABLE_NAME, values);
                break;
            case LTO_2C_MATCHER:
                rtn = bulkInsert(Lto2C.TABLE_NAME, values);
                break;
            case LTO_7C_MATCHER:
                rtn = bulkInsert(Lto7C.TABLE_NAME, values);
                break;
            case APP_SETTINGS_MATCHER:
                rtn = bulkInsert(AppSettings.TABLE_NAME, values);
                break;
            default:
                throw new RuntimeException("unexpected bulkInsert uri: " + uri);
        }
        return rtn;
    }

    private int bulkInsert(String table, ContentValues[] values) {
        int rtn = 0;
        mDatabase.beginTransaction();
        try {
            for (ContentValues cv : values) {
                mDatabase.insertWithOnConflict(table, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
            }
            mDatabase.setTransactionSuccessful();
            rtn = values.length;
        } finally {
            mDatabase.endTransaction();
        }
        return rtn;
    }

    private void printDatabaseData(Uri uri) {
        try {
            Log.i(TAG, "============ start to print " + uri.getPath() + " ============");
            Cursor cursor = query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    String data = DatabaseUtils.dumpCursorToString(cursor);
                    Log.d(TAG, "data: " + data);
                } finally {
                    cursor.close();
                }
            }
            Log.i(TAG, "============ finish ============");
        } catch (Exception e) {
            Log.w(TAG, "unexpected exception occurs when printDatabaseData, uri: " + uri, e);
        }
    }
}
