package yhh.bj4.lotterylover.helpers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.parser.LtoList3.LtoList3;
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto539.Lto539;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltoJ6.LtoJ6;
import yhh.bj4.lotterylover.parser.ltoMM.LtoMM;
import yhh.bj4.lotterylover.parser.ltoToTo.LtoToTo;
import yhh.bj4.lotterylover.parser.ltoapow.LtoAuPow;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;
import yhh.bj4.lotterylover.parser.ltoem.LtoEm;
import yhh.bj4.lotterylover.parser.ltolist4.LtoList4;
import yhh.bj4.lotterylover.parser.ltopow.LtoPow;
import yhh.bj4.lotterylover.provider.AppSettings;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class RetrieveLotteryItemDataHelper extends AsyncTask<Void, Void, List<LotteryItem>> {
    private static final boolean DEBUG = Utilities.DEBUG;
    private static final String TAG = "RetrieveLotteryItem";

    public interface Callback {
        void onFinished(List<LotteryItem> data);
    }

    private final WeakReference<Context> mContext;
    private final Callback mCallback;
    private final int mLtoType;
    private final int mListType;

    public RetrieveLotteryItemDataHelper(Context context, Callback cb, int ltoType, int listType) {
        mContext = new WeakReference<>(context);
        mCallback = cb;
        mLtoType = ltoType;
        mListType = listType;
    }

    @Override
    protected List<LotteryItem> doInBackground(Void... params) {
        final Context context = mContext.get();
        final ArrayList<LotteryItem> rtn = new ArrayList<>();
        if (context == null) return rtn;
        if (mListType == LotteryLover.LIST_TYPE_COMBINE_LIST) {
            if (mLtoType == LotteryLover.LTO_TYPE_LTO_LIST3) {
                rtn.addAll(getDataFromCursor(getDataCursor(context, LotteryLover.LTO_TYPE_LTO_LIST3), LotteryLover.LTO_TYPE_LTO_LIST3));
                rtn.addAll(getDataFromCursor(getDataCursor(context, LotteryLover.LTO_TYPE_LTO_LIST4), LotteryLover.LTO_TYPE_LTO_LIST4));
            } else {
                rtn.addAll(getDataFromCursor(getDataCursor(context, LotteryLover.LTO_TYPE_LTO_LIST4), LotteryLover.LTO_TYPE_LTO_LIST4));
                rtn.addAll(getDataFromCursor(getDataCursor(context, LotteryLover.LTO_TYPE_LTO_LIST3), LotteryLover.LTO_TYPE_LTO_LIST3));
            }
            int orderSettings = AppSettings.get(context, LotteryLover.KEY_ORDER, LotteryLover.ORDER_BY_ASC);
            final boolean isAsc = orderSettings == LotteryLover.ORDER_BY_ASC;
            Collections.sort(rtn, new Comparator<LotteryItem>() {
                @Override
                public int compare(LotteryItem lhs, LotteryItem rhs) {
                    if (isAsc) {
                        if (lhs.getSequence() < rhs.getSequence()) return -1;
                        else if (lhs.getSequence() > rhs.getSequence()) return 1;
                        else return 0;
                    } else {
                        if (lhs.getSequence() < rhs.getSequence()) return 1;
                        else if (lhs.getSequence() > rhs.getSequence()) return -1;
                        else return 0;
                    }
                }
            });
        } else {
            Cursor cursor = getDataCursor(context, mLtoType);
            rtn.addAll(getDataFromCursor(cursor, mLtoType));
        }
        if (DEBUG)
            Log.d(TAG, "doInBackground, rtn size: " + rtn.size());
        return rtn;
    }

    public static ArrayList<LotteryItem> getDataFromCursor(Cursor cursor, int ltoType) {
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

            if (ltoType == LotteryLover.LTO_TYPE_LTO) {
                while (cursor.moveToNext()) {
                    rtn.add(new Lto(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO2C) {
                while (cursor.moveToNext()) {
                    rtn.add(new Lto2C(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO7C) {
                while (cursor.moveToNext()) {
                    rtn.add(new Lto7C(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_BIG) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoBig(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_DOF) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoDof(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_HK) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoHK(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_539) {
                while (cursor.moveToNext()) {
                    rtn.add(new Lto539(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_POW) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoPow(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_MM) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoMM(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_J6) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoJ6(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_TOTO) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoToTo(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_AU_POW) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoAuPow(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_EM) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoEm(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_LIST3) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoList3(cursor.getLong(indexOfSeq),
                            cursor.getLong(indexOfDrawingTime),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfNormalNumber)),
                            LotteryItem.fromJsonToList(cursor.getString(indexOfSpecialNumber)),
                            cursor.getString(indexOfMemo),
                            cursor.getString(indexOfExtra)));
                }
            } else if (ltoType == LotteryLover.LTO_TYPE_LTO_LIST4) {
                while (cursor.moveToNext()) {
                    rtn.add(new LtoList4(cursor.getLong(indexOfSeq),
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

    public static Cursor getDataCursor(Context context, int ltoType) {
        Uri queryUri = null;
        switch (ltoType) {
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
            case LotteryLover.LTO_TYPE_LTO_539:
                queryUri = Lto539.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_POW:
                queryUri = LtoPow.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_MM:
                queryUri = LtoMM.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_J6:
                queryUri = LtoJ6.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_TOTO:
                queryUri = LtoToTo.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_AU_POW:
                queryUri = LtoAuPow.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_EM:
                queryUri = LtoEm.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_LIST3:
                queryUri = LtoList3.DATA_URI;
                break;
            case LotteryLover.LTO_TYPE_LTO_LIST4:
                queryUri = LtoList4.DATA_URI;
                break;
            default:
                throw new RuntimeException("unexpected type");
        }
        int orderSettings = AppSettings.get(context, LotteryLover.KEY_ORDER, LotteryLover.ORDER_BY_ASC);
        String order = orderSettings == LotteryLover.ORDER_BY_ASC ? "asc" : "desc";

        int rowSettings = AppSettings.get(context, LotteryLover.KEY_DISPLAY_ROWS, LotteryLover.DISPLAY_ROWS_100);
        String row = "";
        switch (rowSettings) {
            case LotteryLover.DISPLAY_ROWS_50:
                row = "limit " + LotteryLover.VALUE_DISPLAY_ROWS_50;
                break;
            case LotteryLover.DISPLAY_ROWS_100:
                row = "limit " + LotteryLover.VALUE_DISPLAY_ROWS_100;
                break;
            case LotteryLover.DISPLAY_ROWS_150:
                row = "limit " + LotteryLover.VALUE_DISPLAY_ROWS_150;
                break;
            case LotteryLover.DISPLAY_ROWS_200:
                row = "limit " + LotteryLover.VALUE_DISPLAY_ROWS_200;
                break;
            case LotteryLover.DISPLAY_ROWS_500:
                row = "limit " + LotteryLover.VALUE_DISPLAY_ROWS_500;
                break;
            case LotteryLover.DISPLAY_ROWS_ALL:
                row = "";
                break;
        }
        return context.getContentResolver().query(queryUri, null, null, null, LotteryItem.COLUMN_DRAWING_DATE_TIME + " " + order + " " + row);
    }

    @Override
    protected void onPostExecute(List<LotteryItem> lotteryItems) {
        super.onPostExecute(lotteryItems);
        if (mCallback == null) return;
        mCallback.onFinished(lotteryItems);
    }
}
