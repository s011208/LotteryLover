package yhh.bj4.lotterylover.fragments.calendar;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.fragments.calendar.item.TodayLotteryItem;
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
import yhh.bj4.lotterylover.provider.LotteryProvider;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class RetrieveTodayLotteryTask extends AsyncTask<Void, Void, ArrayList<TodayLotteryItem>> {
    private final int mYear, mMonth, mDay;
    private final WeakReference<Context> mContext;
    private final WeakReference<Callback> mCallback;

    public interface Callback {
        void onFinish(ArrayList<TodayLotteryItem> items);
    }

    public RetrieveTodayLotteryTask(Context context, Callback cb, int y, int m, int d) {
        mContext = new WeakReference<>(context);
        mYear = y;
        mMonth = m;
        mDay = d;
        mCallback = new WeakReference<>(cb);
    }

    @Override
    protected ArrayList<TodayLotteryItem> doInBackground(Void... params) {
        ArrayList<TodayLotteryItem> rtn = new ArrayList<>();
        final Context context = mContext.get();
        if (context == null) return rtn;
        Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth, mDay, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

        final long startTime = c.getTimeInMillis();
        c.add(Calendar.DAY_OF_MONTH, 1);

        final long endTime = c.getTimeInMillis();

        int ltoCount = 15;
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO, startTime, endTime), LotteryLover.LTO_TYPE_LTO));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO2C, startTime, endTime), LotteryLover.LTO_TYPE_LTO2C));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO7C, startTime, endTime), LotteryLover.LTO_TYPE_LTO7C));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_BIG, startTime, endTime), LotteryLover.LTO_TYPE_LTO_BIG));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_DOF, startTime, endTime), LotteryLover.LTO_TYPE_LTO_DOF));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_HK, startTime, endTime), LotteryLover.LTO_TYPE_LTO_HK));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_539, startTime, endTime), LotteryLover.LTO_TYPE_LTO_539));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_POW, startTime, endTime), LotteryLover.LTO_TYPE_LTO_POW));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_MM, startTime, endTime), LotteryLover.LTO_TYPE_LTO_MM));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_J6, startTime, endTime), LotteryLover.LTO_TYPE_LTO_J6));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_TOTO, startTime, endTime), LotteryLover.LTO_TYPE_LTO_TOTO));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_AU_POW, startTime, endTime), LotteryLover.LTO_TYPE_LTO_AU_POW));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_EM, startTime, endTime), LotteryLover.LTO_TYPE_LTO_EM));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_LIST3, startTime, endTime), LotteryLover.LTO_TYPE_LTO_LIST3));
        rtn.add(new TodayLotteryItem(getItemFromCursor(context, LotteryLover.LTO_TYPE_LTO_LIST4, startTime, endTime), LotteryLover.LTO_TYPE_LTO_LIST4));

        Iterator<TodayLotteryItem> items = rtn.iterator();
        while (items.hasNext()) {
            TodayLotteryItem item = items.next();
            if (item.getLotteryItem() == null) {
                items.remove();
            }
        }

        // integrity check
        Cursor tableNameData = context.getContentResolver().query(LotteryProvider.QUERY_ALL_LTO_TABLE_NAME, null, null, null, null);
        if (tableNameData != null) {
            try {
                int tableNameDataCount = tableNameData.getCount();
                if (tableNameDataCount != ltoCount) {
                    throw new RuntimeException("integrity check failed, tableNameDataCount: " + tableNameDataCount
                            + ", ltoCount: " + ltoCount);
                }
            } finally {
                tableNameData.close();
            }
        }


        return rtn;
    }

    private LotteryItem getItemFromCursor(Context context, int ltoType, long startTime, long endTime) {
        LotteryItem rtn = null;
        Cursor ltoCursor = null;
        int indexOfSeq, indexOfDrawingTime, indexOfNormalNumber, indexOfSpecialNumber, indexOfMemo, indexOfExtra;

        final String whereStat = LotteryItem.COLUMN_DRAWING_DATE_TIME + ">=" + startTime + " and " + LotteryItem.COLUMN_DRAWING_DATE_TIME
                + "<" + endTime;
        switch (ltoType) {
            case LotteryLover.LTO_TYPE_LTO:
                ltoCursor = context.getContentResolver().query(Lto.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new Lto(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO2C:
                ltoCursor = context.getContentResolver().query(Lto2C.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new Lto2C(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO7C:
                ltoCursor = context.getContentResolver().query(Lto7C.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new Lto7C(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_BIG:
                ltoCursor = context.getContentResolver().query(LtoBig.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoBig(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_DOF:
                ltoCursor = context.getContentResolver().query(LtoDof.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoDof(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_HK:
                ltoCursor = context.getContentResolver().query(LtoHK.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoHK(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_539:
                ltoCursor = context.getContentResolver().query(Lto539.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new Lto539(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_POW:
                ltoCursor = context.getContentResolver().query(LtoPow.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoPow(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_MM:
                ltoCursor = context.getContentResolver().query(LtoMM.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoMM(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_J6:
                ltoCursor = context.getContentResolver().query(LtoJ6.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoJ6(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_TOTO:
                ltoCursor = context.getContentResolver().query(LtoToTo.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoToTo(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_AU_POW:
                ltoCursor = context.getContentResolver().query(LtoAuPow.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoAuPow(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_EM:
                ltoCursor = context.getContentResolver().query(LtoEm.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoEm(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_LIST3:
                ltoCursor = context.getContentResolver().query(LtoList3.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoList3(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
            case LotteryLover.LTO_TYPE_LTO_LIST4:
                ltoCursor = context.getContentResolver().query(LtoList4.DATA_URI, null, whereStat, null, null);
                if (ltoCursor == null) break;
                if (ltoCursor.getCount() == 0) break;
                indexOfSeq = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SEQUENCE);
                indexOfDrawingTime = ltoCursor.getColumnIndex(LotteryItem.COLUMN_DRAWING_DATE_TIME);
                indexOfNormalNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_NORMAL_NUMBERS);
                indexOfSpecialNumber = ltoCursor.getColumnIndex(LotteryItem.COLUMN_SPECIAL_NUMBERS);
                indexOfMemo = ltoCursor.getColumnIndex(LotteryItem.COLUMN_MEMO);
                indexOfExtra = ltoCursor.getColumnIndex(LotteryItem.COLUMN_EXTRA);
                ltoCursor.moveToNext();
                rtn = new LtoList4(ltoCursor.getLong(indexOfSeq),
                        ltoCursor.getLong(indexOfDrawingTime),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfNormalNumber)),
                        LotteryItem.fromJsonToList(ltoCursor.getString(indexOfSpecialNumber)),
                        ltoCursor.getString(indexOfMemo),
                        ltoCursor.getString(indexOfExtra));
                break;
        }
        if (ltoCursor != null) {
            ltoCursor.close();
        }
        return rtn;
    }

    @Override
    protected void onPostExecute(ArrayList<TodayLotteryItem> lotteryItems) {
        super.onPostExecute(lotteryItems);
        Callback cb = mCallback.get();
        if (cb == null) return;
        cb.onFinish(lotteryItems);
    }
}
