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

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class LotteryProvider extends ContentProvider {
    private static final String TAG = "LotteryProvider";
    private static final boolean DEBUG = Utilities.DEBUG;

    private static final String AUTHORITY = "yhh.bj4.lotterylover.lottery_provider";

    public static Uri getUri(String tableName) {
        return Uri.parse("content://" + AUTHORITY + "/" + tableName);
    }

    private static final UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int LTO_MATCHER = 0;

    static {
        sMatcher.addURI(AUTHORITY, Lto.TABLE_NAME, LTO_MATCHER);
    }

    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        mDatabase = new LotteryDatabase(getContext()).getWritableDatabase();
        if (DEBUG) {
            printDatabaseData(Lto.DATA_URI);
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor rtn = null;
        switch (sMatcher.match(uri)) {
            case LTO_MATCHER:
                rtn = mDatabase.query(Lto.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
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
        Uri rtn = null;
        switch (sMatcher.match(uri)) {
            case LTO_MATCHER:
                rtn = ContentUris.withAppendedId(uri, mDatabase.insert(Lto.TABLE_NAME, null, values));
                break;
        }
        return rtn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rtn = 0;
        switch (sMatcher.match(uri)) {
            case LTO_MATCHER:
                rtn = mDatabase.delete(Lto.TABLE_NAME, selection, selectionArgs);
                break;
        }
        return rtn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rtn = 0;
        switch (sMatcher.match(uri)) {
            case LTO_MATCHER:
                rtn = mDatabase.update(Lto.TABLE_NAME, values, selection, selectionArgs);
                break;
        }
        return rtn;
    }

    private void printDatabaseData(Uri uri) {
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
    }
}
