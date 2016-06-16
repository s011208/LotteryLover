package yhh.bj4.lotterylover.helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class RetrieveLotteryItemDataHelper extends AsyncTask<Void, Void, List<LotteryItem>> {
    private static final boolean DEBUG = Utilities.DEBUG;
    private static final String TAG = "RetrieveLotteryItemDataHelper";

    public interface Callback {
        void onFinished(List<LotteryItem> data);
    }

    private final WeakReference<Context> mContext;
    private final Callback mCallback;
    private final int mLtoType;

    public RetrieveLotteryItemDataHelper(Context context, Callback cb, int ltoType) {
        mContext = new WeakReference<>(context);
        mCallback = cb;
        mLtoType = ltoType;
    }

    @Override
    protected List<LotteryItem> doInBackground(Void... params) {
        final Context context = mContext.get();
        final ArrayList<LotteryItem> rtn = new ArrayList<>();
        if (context == null) return rtn;
        Cursor cursor = getDataCursor(context);
        rtn.addAll(getDataFromCursor(cursor));
        if (DEBUG)
            Log.d(TAG, "doInBackground, rtn size: " + rtn.size());
        return rtn;
    }

    private ArrayList<LotteryItem> getDataFromCursor(Cursor cursor) {
        final ArrayList<LotteryItem> rtn = new ArrayList<>();
        if (cursor == null) return rtn;
        if (DEBUG)
            Log.d(TAG, "getDataFromCursor, cursor count: " + cursor.getCount());
        try {
            // basic columns
            final int indexOfSeq = cursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
            final int indexOfDrawingTime = cursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
            final int indexOfNormalNumber = cursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
            final int indexOfSpecialNumber = cursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
            final int indexOfMemo = cursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
            final int indexOfExtra = cursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);

            if (mLtoType == LotteryLover.LTO_TYPE_LTO) {
                while (cursor.moveToNext()) {
                    rtn.add(new Lto(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (mLtoType == LotteryLover.LTO_TYPE_LTO2C) {
                while (cursor.moveToNext()) {
                    rtn.add(new Lto2C(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (mLtoType == LotteryLover.LTO_TYPE_LTO7C) {
                while (cursor.moveToNext()) {
                    rtn.add(new Lto7C(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (mLtoType == LotteryLover.LTO_TYPE_LTO_BIG) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoBig(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (mLtoType == LotteryLover.LTO_TYPE_LTO_DOF) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoDof(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (mLtoType == LotteryLover.LTO_TYPE_LTO_HK) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoHK(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            }
        } finally {
            cursor.close();
        }
        return rtn;
    }

    private Cursor getDataCursor(Context context) {
        Uri queryUri = null;
        switch (mLtoType) {
            case LotteryLover.LTO_TYPE_LTO:
                queryUri = Lto.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO2C:
                queryUri = Lto2C.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO7C:
                queryUri = Lto7C.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_BIG:
                queryUri = LtoBig.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_DOF:
                queryUri = LtoDof.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_HK:
                queryUri = LtoHK.DATA_URI;
                break;
        }
        return context.getContentResolver().query(queryUri, null, null, null, LotteryItem.COLUMN_DRAWING_DATE_TIME);
    }

    @Override
    protected void onPostExecute(List<LotteryItem> lotteryItems) {
        super.onPostExecute(lotteryItems);
        if (mCallback == null) return;
        mCallback.onFinished(lotteryItems);
    }
}
