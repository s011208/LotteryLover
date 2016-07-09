package yhh.bj4.lotterylover.settings.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.provider.LotteryProvider;

/**
 * Created by yenhsunhuang on 2016/7/9.
 */
public class ShowDrawingTip {
    public static final String TABLE_NAME = "show_drawing_tip";
    public static final Uri URI = LotteryProvider.getUri(TABLE_NAME);

    public static final String COLUMN_LTO_TYPE = "lto_type";
    public static final String COLUMN_IS_CHECKED = "_lto_order";

    public static final String COMMAND_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_LTO_TYPE + " TEXT PRIMARY KEY,"
                    + COLUMN_IS_CHECKED + " INTEGER DEFAULT " + LotteryProvider.TRUE
                    + ")";

    public static final List<Integer> getCheckedLtoType(Context context) {
        List<Integer> rtn = new ArrayList<>();
        if (context == null) return rtn;
        Cursor data = context.getContentResolver().query(URI, new String[]{COLUMN_LTO_TYPE}, COLUMN_IS_CHECKED + "=" + LotteryProvider.TRUE, null, null);
        if (data == null) return rtn;
        try {
            while (data.moveToNext()) {
                rtn.add(data.getInt(0));
            }
        } finally {
            data.close();
        }
        return rtn;
    }

    public static final List<Pair<Integer, Boolean>> getData(Context context) {
        List<Pair<Integer, Boolean>> rtn = new ArrayList<>();
        if (context == null) return rtn;
        Cursor data = context.getContentResolver().query(URI, null, null, null, null);
        if (data == null) return rtn;
        if (data.getCount() == 0) {
            initValues(context);
            return getData(context);
        }
        try {
            final int indexOfType = data.getColumnIndex(COLUMN_LTO_TYPE);
            final int indexOfChecked = data.getColumnIndex(COLUMN_IS_CHECKED);
            while (data.moveToNext()) {
                rtn.add(new Pair<>(data.getInt(indexOfType), data.getInt(indexOfChecked) == LotteryProvider.TRUE));
            }
        } finally {
            data.close();
        }
        return rtn;
    }

    private static void initValues(Context context) {
        if (context == null) return;
        final int ITEMS_OF_LTO = 15;
        ContentValues[] cvs = new ContentValues[ITEMS_OF_LTO];
        for (int i = 0; i < ITEMS_OF_LTO; ++i) {
            cvs[i] = new ContentValues();
            cvs[i].put(COLUMN_LTO_TYPE, i);
            cvs[i].put(COLUMN_IS_CHECKED, LotteryProvider.TRUE);
        }
        context.getContentResolver().bulkInsert(URI, cvs);
    }
}
