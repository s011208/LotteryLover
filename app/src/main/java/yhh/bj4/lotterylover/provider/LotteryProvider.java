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
import yhh.bj4.lotterylover.parser.lto539.Lto539;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltoJ6.LtoJ6;
import yhh.bj4.lotterylover.parser.ltoMM.LtoMM;
import yhh.bj4.lotterylover.parser.ltoToTo.LtoToTo;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;
import yhh.bj4.lotterylover.parser.ltopow.LtoPow;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class LotteryProvider extends ContentProvider {
    private static final String TAG = "LotteryProvider";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final int TRUE = 1;
    public static final int FALSE = 0;

    private static final String AUTHORITY = "yhh.bj4.lotterylover.lottery_provider";
    private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int APP_SETTINGS_MATCHER = 0;
    private static final int LTO_MATCHER = 1;
    private static final int LTO_BIG_MATCHER = 2;
    private static final int LTO_HK_MATCHER = 3;
    private static final int LTO_DOF_MATCHER = 4;
    private static final int LTO_2C_MATCHER = 5;
    private static final int LTO_7C_MATCHER = 6;
    private static final int LTO_539_MATCHER = 7;
    private static final int LTO_POW_MATCHER = 8;
    private static final int LTO_MM_MATCHER = 9;
    private static final int LTO_J6_MATCHER = 10;
    private static final int LTO_TOTO_MATCHER = 11;

    public static final String PARAMETER_NOTIFY = "notify";

    static {
        sMatcher.addURI(AUTHORITY, AppSettings.TABLE_NAME, APP_SETTINGS_MATCHER);
        sMatcher.addURI(AUTHORITY, Lto.TABLE_NAME, LTO_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoBig.TABLE_NAME, LTO_BIG_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoHK.TABLE_NAME, LTO_HK_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoDof.TABLE_NAME, LTO_DOF_MATCHER);
        sMatcher.addURI(AUTHORITY, Lto2C.TABLE_NAME, LTO_2C_MATCHER);
        sMatcher.addURI(AUTHORITY, Lto7C.TABLE_NAME, LTO_7C_MATCHER);
        sMatcher.addURI(AUTHORITY, Lto539.TABLE_NAME, LTO_539_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoPow.TABLE_NAME, LTO_POW_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoMM.TABLE_NAME, LTO_MM_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoJ6.TABLE_NAME, LTO_J6_MATCHER);
        sMatcher.addURI(AUTHORITY, LtoToTo.TABLE_NAME, LTO_TOTO_MATCHER);
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
//        clearTable(Lto.DATA_URI);
//        clearTable(Lto2C.DATA_URI);
//        clearTable(Lto7C.DATA_URI);
//        clearTable(LtoBig.DATA_URI);
//        clearTable(LtoDof.DATA_URI);
//        clearTable(LtoHK.DATA_URI);
//        clearTable(AppSettings.DATA_URI);
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
            case LTO_539_MATCHER:
                rtn = mDatabase.query(Lto539.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LTO_POW_MATCHER:
                rtn = mDatabase.query(LtoPow.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LTO_MM_MATCHER:
                rtn = mDatabase.query(LtoMM.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LTO_J6_MATCHER:
                rtn = mDatabase.query(LtoJ6.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LTO_TOTO_MATCHER:
                rtn = mDatabase.query(LtoToTo.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
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
            case LTO_539_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(Lto539.TABLE_NAME, null, values));
                break;
            case LTO_POW_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(LtoPow.TABLE_NAME, null, values));
                break;
            case LTO_MM_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(LtoMM.TABLE_NAME, null, values));
                break;
            case LTO_J6_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(LtoJ6.TABLE_NAME, null, values));
                break;
            case LTO_TOTO_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(LtoToTo.TABLE_NAME, null, values));
                break;
            case APP_SETTINGS_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.replace(AppSettings.TABLE_NAME, null, values));
                break;
            default:
                throw new RuntimeException("unexpected insert uri: " + uri);
        }
        sendNotify(uri);
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
            case LTO_539_MATCHER:
                rtn = mDatabase.delete(Lto539.TABLE_NAME, selection, selectionArgs);
                break;
            case LTO_POW_MATCHER:
                rtn = mDatabase.delete(LtoPow.TABLE_NAME, selection, selectionArgs);
                break;
            case LTO_MM_MATCHER:
                rtn = mDatabase.delete(LtoMM.TABLE_NAME, selection, selectionArgs);
                break;
            case LTO_J6_MATCHER:
                rtn = mDatabase.delete(LtoJ6.TABLE_NAME, selection, selectionArgs);
                break;
            case LTO_TOTO_MATCHER:
                rtn = mDatabase.delete(LtoToTo.TABLE_NAME, selection, selectionArgs);
                break;
            case APP_SETTINGS_MATCHER:
                rtn = mDatabase.delete(AppSettings.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new RuntimeException("unexpected delete uri: " + uri);
        }
        sendNotify(uri);
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
            case LTO_539_MATCHER:
                rtn = mDatabase.update(Lto539.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LTO_POW_MATCHER:
                rtn = mDatabase.update(LtoPow.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LTO_MM_MATCHER:
                rtn = mDatabase.update(LtoMM.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LTO_J6_MATCHER:
                rtn = mDatabase.update(LtoJ6.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LTO_TOTO_MATCHER:
                rtn = mDatabase.update(LtoToTo.TABLE_NAME, values, selection, selectionArgs);
                break;
            case APP_SETTINGS_MATCHER:
                mDatabase.replace(AppSettings.TABLE_NAME, null, values);
                rtn = 1;
                break;
            default:
                throw new RuntimeException("unexpected update uri: " + uri);
        }
        sendNotify(uri);
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
            case LTO_539_MATCHER:
                rtn = bulkInsert(Lto539.TABLE_NAME, values);
                break;
            case LTO_POW_MATCHER:
                rtn = bulkInsert(LtoPow.TABLE_NAME, values);
                break;
            case LTO_MM_MATCHER:
                rtn = bulkInsert(LtoMM.TABLE_NAME, values);
                break;
            case LTO_J6_MATCHER:
                rtn = bulkInsert(LtoJ6.TABLE_NAME, values);
                break;
            case LTO_TOTO_MATCHER:
                rtn = bulkInsert(LtoToTo.TABLE_NAME, values);
                break;
            case APP_SETTINGS_MATCHER:
                rtn = bulkInsert(AppSettings.TABLE_NAME, values);
                break;
            default:
                throw new RuntimeException("unexpected bulkInsert uri: " + uri);
        }
        if (rtn > 0) {
            sendNotify(uri);
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

    private void sendNotify(Uri uri) {
        String notify = uri.getQueryParameter(PARAMETER_NOTIFY);
        if (notify == null || "true".equals(notify)) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
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

    private void clearTable(Uri uri) {
        delete(uri, null, null);
    }
}
