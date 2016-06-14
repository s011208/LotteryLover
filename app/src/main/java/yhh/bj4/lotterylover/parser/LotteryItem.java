package yhh.bj4.lotterylover.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public abstract class LotteryItem {
    private static final String TAG = "LotteryItem";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final String COLUMN_SEQUENCE = "seq";
    public static final String COLUMN_DRAWING_DATE_TIME = "drawing_dt";
    public static final String COLUMN_NORMAL_NUMBERS = "nn";
    public static final String COLUMN_SPECIAL_NUMBERS = "sn";
    public static final String COLUMN_MEMO = "memo";
    public static final String COLUMN_EXTRA = "extra";

    public static final String COMMAND_CREATE_TABLE(String lotteryItemTableName) {
        return "CREATE TABLE IF NOT EXISTS " + lotteryItemTableName + " ("
                + COLUMN_SEQUENCE + " INTEGER PRIMARY KEY,"
                + COLUMN_DRAWING_DATE_TIME + " INTEGER NOT NULL,"
                + COLUMN_NORMAL_NUMBERS + " TEXT NOT NULL,"
                + COLUMN_SPECIAL_NUMBERS + " TEXT,"
                + COLUMN_MEMO + " TEXT,"
                + COLUMN_EXTRA + " TEXT,"
                + ")";
    }

    private long mSequence;
    private long mDrawingDateTime;
    private final List<Integer> mNormalNumbers = new ArrayList<>();
    private final List<Integer> mSpecialNumbers = new ArrayList<>();
    private String mMemo;
    private String mExtraMessage;

    public LotteryItem(long seq, long dateTime, List<Integer> normalNumbers, List<Integer> specialNumbers) {
        this(seq, dateTime, normalNumbers, specialNumbers, "");
    }

    public LotteryItem(long seq, long dateTime, List<Integer> normalNumbers, List<Integer> specialNumbers, String memo) {
        this(seq, dateTime, normalNumbers, specialNumbers, memo, "");
    }

    public LotteryItem(long seq, long dateTime, List<Integer> normalNumbers, List<Integer> specialNumbers, String memo, String extra) {
        mSequence = seq;
        mDrawingDateTime = dateTime;
        mNormalNumbers.addAll(normalNumbers);
        mSpecialNumbers.addAll(specialNumbers);
        mMemo = memo;
        mExtraMessage = extra;
    }

    public int getTotalNumbers() {
        return getNormalNumbersCount() + getSpecialNumbersCount();
    }

    public abstract int getNormalNumbersCount();

    public abstract int getSpecialNumbersCount();

    public long getSequence() {
        return mSequence;
    }

    public long getDrawingDateTime() {
        return mDrawingDateTime;
    }

    public List<Integer> getNormalNumbers() {
        return mNormalNumbers;
    }

    public List<Integer> getSpecialNumbers() {
        return mSpecialNumbers;
    }

    public String getMemo() {
        return mMemo;
    }

    public String getExtraMessage() {
        return mExtraMessage;
    }

    public static List<Integer> fromJsonToList(String rawJson) {
        List<Integer> rtn = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(rawJson);
            for (int i = 0; i < array.length(); ++i) {
                rtn.add(array.getInt(i));
            }
        } catch (JSONException e) {
            if (DEBUG) {
                Log.w(TAG, "unexpected exception", e);
            }
        }
        return rtn;
    }

    public static String fromListToJson(List<Integer> number) {
        JSONArray json = new JSONArray();
        Collections.sort(number);
        for (Integer i : number) {
            json.put(i);
        }
        return json.toString();
    }

    @Override
    public String toString() {
        return "seq: " + mSequence + ", time: " + mDrawingDateTime + ", memo: " + mMemo + ", extra: " + mExtraMessage;
    }
}
