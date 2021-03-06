package yhh.bj4.lotterylover.parser;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.Exclude;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
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
 * Created by yenhsunhuang on 2016/6/14.
 */
public abstract class LotteryItem {
    public static final int TOTAL_LTO_TYPE_COUNT = 15;


    public static final String COLUMN_SEQUENCE = "seq";
    public static final String COLUMN_DRAWING_DATE_TIME = "drawing_dt";
    public static final String COLUMN_NORMAL_NUMBERS = "nn";
    public static final String COLUMN_SPECIAL_NUMBERS = "sn";
    public static final String COLUMN_MEMO = "memo";
    public static final String COLUMN_EXTRA = "extra";
    private static final String TAG = "LotteryItem";
    private static final boolean DEBUG = Utilities.DEBUG;
    private final List<Integer> mNormalNumbers = new ArrayList<>();
    private final List<Integer> mSpecialNumbers = new ArrayList<>();
    private long mSequence;
    private long mDrawingDateTime;
    private String mMemo;
    private String mExtraMessage;

    public LotteryItem() {
    }

    public LotteryItem(Map<String, String> map) {
        this(Long.valueOf(map.get(COLUMN_SEQUENCE)), Long.valueOf(map.get(COLUMN_DRAWING_DATE_TIME))
                , fromJsonToList(map.get(COLUMN_NORMAL_NUMBERS)), fromJsonToList(map.get(COLUMN_SPECIAL_NUMBERS)),
                map.get(COLUMN_MEMO), map.get(COLUMN_EXTRA));
    }

    public LotteryItem(long seq, long dateTime, List<Integer> normalNumbers, List<Integer> specialNumbers, String memo, String extra) {
        mSequence = seq;
        mDrawingDateTime = dateTime;
        mNormalNumbers.addAll(normalNumbers);
        mSpecialNumbers.addAll(specialNumbers);
        mMemo = memo;
        mExtraMessage = extra;
    }

    public static String COMMAND_CREATE_TABLE(String lotteryItemTableName) {
        return "CREATE TABLE IF NOT EXISTS " + lotteryItemTableName + " ("
                + COLUMN_SEQUENCE + " INTEGER NOT NULL,"
                + COLUMN_DRAWING_DATE_TIME + " INTEGER PRIMARY KEY,"
                + COLUMN_NORMAL_NUMBERS + " TEXT NOT NULL,"
                + COLUMN_SPECIAL_NUMBERS + " TEXT,"
                + COLUMN_MEMO + " TEXT,"
                + COLUMN_EXTRA + " TEXT"
                + ")";
    }

    public static int getNormalNumbersCount() {
        throw new RuntimeException("must implement getNormalNumbersCount");
    }

    public static int getSpecialNumbersCount() {
        throw new RuntimeException("must implement getSpecialNumbersCount");
    }

    public static int getMaximumNormalNumber() {
        throw new RuntimeException("must implement getMaximumNormalNumber");
    }

    public static int getMaximumSpecialNumber() {
        throw new RuntimeException("must implement getMaximumSpecialNumber");
    }

    public static int getNormalNumbersCount(LotteryItem item) {
        if (item instanceof Lto) {
            return Lto.getNormalNumbersCount();
        } else if (item instanceof Lto2C) {
            return Lto2C.getNormalNumbersCount();
        } else if (item instanceof Lto7C) {
            return Lto7C.getNormalNumbersCount();
        } else if (item instanceof LtoBig) {
            return LtoBig.getNormalNumbersCount();
        } else if (item instanceof LtoDof) {
            return LtoDof.getNormalNumbersCount();
        } else if (item instanceof LtoHK) {
            return LtoHK.getNormalNumbersCount();
        } else if (item instanceof Lto539) {
            return Lto539.getNormalNumbersCount();
        } else if (item instanceof LtoPow) {
            return LtoPow.getNormalNumbersCount();
        } else if (item instanceof LtoMM) {
            return LtoMM.getNormalNumbersCount();
        } else if (item instanceof LtoJ6) {
            return LtoJ6.getNormalNumbersCount();
        } else if (item instanceof LtoToTo) {
            return LtoToTo.getNormalNumbersCount();
        } else if (item instanceof LtoAuPow) {
            return LtoAuPow.getNormalNumbersCount();
        } else if (item instanceof LtoEm) {
            return LtoEm.getNormalNumbersCount();
        } else if (item instanceof LtoList3) {
            return LtoList3.getNormalNumbersCount();
        } else if (item instanceof LtoList4) {
            return LtoList4.getNormalNumbersCount();
        } else {
            throw new RuntimeException("must provide valid item");
        }
    }

    public static int getSpecialNumbersCount(LotteryItem item) {
        if (item instanceof Lto) {
            return Lto.getSpecialNumbersCount();
        } else if (item instanceof Lto2C) {
            return Lto2C.getSpecialNumbersCount();
        } else if (item instanceof Lto7C) {
            return Lto7C.getSpecialNumbersCount();
        } else if (item instanceof LtoBig) {
            return LtoBig.getSpecialNumbersCount();
        } else if (item instanceof LtoDof) {
            return LtoDof.getSpecialNumbersCount();
        } else if (item instanceof LtoHK) {
            return LtoHK.getSpecialNumbersCount();
        } else if (item instanceof Lto539) {
            return Lto539.getSpecialNumbersCount();
        } else if (item instanceof LtoPow) {
            return LtoPow.getSpecialNumbersCount();
        } else if (item instanceof LtoMM) {
            return LtoMM.getSpecialNumbersCount();
        } else if (item instanceof LtoJ6) {
            return LtoJ6.getSpecialNumbersCount();
        } else if (item instanceof LtoToTo) {
            return LtoToTo.getSpecialNumbersCount();
        } else if (item instanceof LtoAuPow) {
            return LtoAuPow.getSpecialNumbersCount();
        } else if (item instanceof LtoEm) {
            return LtoEm.getSpecialNumbersCount();
        } else if (item instanceof LtoList3) {
            return LtoList3.getSpecialNumbersCount();
        } else if (item instanceof LtoList4) {
            return LtoList4.getSpecialNumbersCount();
        } else {
            throw new RuntimeException("must provide valid item");
        }
    }

    public static int getMaximumNormalNumber(LotteryItem item) {
        if (item instanceof Lto) {
            return Lto.getMaximumNormalNumber();
        } else if (item instanceof Lto2C) {
            return Lto2C.getMaximumNormalNumber();
        } else if (item instanceof Lto7C) {
            return Lto7C.getMaximumNormalNumber();
        } else if (item instanceof LtoBig) {
            return LtoBig.getMaximumNormalNumber();
        } else if (item instanceof LtoDof) {
            return LtoDof.getMaximumNormalNumber();
        } else if (item instanceof LtoHK) {
            return LtoHK.getMaximumNormalNumber();
        } else if (item instanceof Lto539) {
            return Lto539.getMaximumNormalNumber();
        } else if (item instanceof LtoPow) {
            return LtoPow.getMaximumNormalNumber();
        } else if (item instanceof LtoMM) {
            return LtoMM.getMaximumNormalNumber();
        } else if (item instanceof LtoJ6) {
            return LtoJ6.getMaximumNormalNumber();
        } else if (item instanceof LtoToTo) {
            return LtoToTo.getMaximumNormalNumber();
        } else if (item instanceof LtoAuPow) {
            return LtoAuPow.getMaximumNormalNumber();
        } else if (item instanceof LtoEm) {
            return LtoEm.getMaximumNormalNumber();
        } else if (item instanceof LtoList3) {
            return LtoList3.getMaximumNormalNumber();
        } else if (item instanceof LtoList4) {
            return LtoList4.getMaximumNormalNumber();
        } else {
            throw new RuntimeException("must provide valid item");
        }
    }

    public static int getMaximumSpecialNumber(int type) {
        if (LotteryLover.LTO_TYPE_LTO == type) {
            return Lto.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO2C == type) {
            return Lto2C.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO7C == type) {
            return Lto7C.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_BIG == type) {
            return LtoBig.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_DOF == type) {
            return LtoDof.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_HK == type) {
            return LtoHK.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_539 == type) {
            return Lto539.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_POW == type) {
            return LtoPow.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_MM == type) {
            return LtoMM.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_J6 == type) {
            return LtoJ6.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_TOTO == type) {
            return LtoToTo.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_AU_POW == type) {
            return LtoAuPow.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_EM == type) {
            return LtoEm.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_LIST3 == type) {
            return LtoList3.getMaximumSpecialNumber();
        } else if (LotteryLover.LTO_TYPE_LTO_LIST4 == type) {
            return LtoList4.getMaximumSpecialNumber();
        } else {
            throw new RuntimeException("must provide valid item");
        }
    }

    public static int getMaximumSpecialNumber(LotteryItem item) {
        if (item instanceof Lto) {
            return Lto.getMaximumSpecialNumber();
        } else if (item instanceof Lto2C) {
            return Lto2C.getMaximumSpecialNumber();
        } else if (item instanceof Lto7C) {
            return Lto7C.getMaximumSpecialNumber();
        } else if (item instanceof LtoBig) {
            return LtoBig.getMaximumSpecialNumber();
        } else if (item instanceof LtoDof) {
            return LtoDof.getMaximumSpecialNumber();
        } else if (item instanceof LtoHK) {
            return LtoHK.getMaximumSpecialNumber();
        } else if (item instanceof Lto539) {
            return Lto539.getMaximumSpecialNumber();
        } else if (item instanceof LtoPow) {
            return LtoPow.getMaximumSpecialNumber();
        } else if (item instanceof LtoMM) {
            return LtoMM.getMaximumSpecialNumber();
        } else if (item instanceof LtoJ6) {
            return LtoJ6.getMaximumSpecialNumber();
        } else if (item instanceof LtoToTo) {
            return LtoToTo.getMaximumSpecialNumber();
        } else if (item instanceof LtoAuPow) {
            return LtoAuPow.getMaximumSpecialNumber();
        } else if (item instanceof LtoEm) {
            return LtoEm.getMaximumSpecialNumber();
        } else if (item instanceof LtoList3) {
            return LtoList3.getMaximumSpecialNumber();
        } else if (item instanceof LtoList4) {
            return LtoList4.getMaximumSpecialNumber();
        } else {
            throw new RuntimeException("must provide valid item");
        }
    }

    public static int getTotalNumbers() {
        return getNormalNumbersCount() + getSpecialNumbersCount();
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

    public static String fromListToJson(List<Integer> number, boolean sort) {
        JSONArray json = new JSONArray();
        if (sort) {
            Collections.sort(number);
        }
        for (Integer i : number) {
            json.put(i);
        }
        return json.toString();
    }

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

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SEQUENCE, mSequence);
        cv.put(COLUMN_DRAWING_DATE_TIME, mDrawingDateTime);
        cv.put(COLUMN_NORMAL_NUMBERS, fromListToJson(mNormalNumbers, sortNumbers()));
        cv.put(COLUMN_SPECIAL_NUMBERS, fromListToJson(mSpecialNumbers, sortNumbers()));
        cv.put(COLUMN_MEMO, mMemo);
        cv.put(COLUMN_EXTRA, mExtraMessage);
        return cv;
    }

    public boolean sortNumbers() {
        return true;
    }

    @Override
    public String toString() {
        return "seq: " + mSequence + ", time: " + mDrawingDateTime + ", memo: " + mMemo + ", extra: " + mExtraMessage;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put(COLUMN_SEQUENCE, String.valueOf(mSequence));
        result.put(COLUMN_DRAWING_DATE_TIME, String.valueOf(mDrawingDateTime));
        result.put(COLUMN_NORMAL_NUMBERS, fromListToJson(mNormalNumbers, sortNumbers()));
        result.put(COLUMN_SPECIAL_NUMBERS, fromListToJson(mSpecialNumbers, sortNumbers()));
        result.put(COLUMN_MEMO, mMemo);
        result.put(COLUMN_EXTRA, mExtraMessage);
        return result;
    }

    public static Uri getLtoTypeUri(int ltoType) {
        switch (ltoType) {
            case LotteryLover.LTO_TYPE_LTO:
                return Lto.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO2C:
                return Lto2C.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO7C:
                return Lto7C.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_BIG:
                return LtoBig.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_DOF:
                return LtoDof.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_HK:
                return LtoHK.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_539:
                return Lto539.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_POW:
                return LtoPow.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_MM:
                return LtoMM.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_J6:
                return LtoJ6.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_TOTO:
                return LtoToTo.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_AU_POW:
                return LtoAuPow.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_EM:
                return LtoEm.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_LIST3:
                return LtoList3.DATA_URI;
            case LotteryLover.LTO_TYPE_LTO_LIST4:
                return LtoList4.DATA_URI;
            default:
                throw new RuntimeException("wrong lto type");
        }
    }

    public static Uri getLtoTypeUriNotNotify(int ltoType) {
        switch (ltoType) {
            case LotteryLover.LTO_TYPE_LTO:
                return Uri.parse(Lto.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO2C:
                return Uri.parse(Lto2C.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO7C:
                return Uri.parse(Lto7C.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_BIG:
                return Uri.parse(LtoBig.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_DOF:
                return Uri.parse(LtoDof.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_HK:
                return Uri.parse(LtoHK.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_539:
                return Uri.parse(Lto539.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_POW:
                return Uri.parse(LtoPow.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_MM:
                return Uri.parse(LtoMM.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_J6:
                return Uri.parse(LtoJ6.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_TOTO:
                return Uri.parse(LtoToTo.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_AU_POW:
                return Uri.parse(LtoAuPow.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_EM:
                return Uri.parse(LtoEm.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_LIST3:
                return Uri.parse(LtoList3.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            case LotteryLover.LTO_TYPE_LTO_LIST4:
                return Uri.parse(LtoList4.DATA_URI.toString() + "?" + LotteryProvider.PARAMETER_NOTIFY + "=false");
            default:
                throw new RuntimeException("wrong lto type");
        }
    }

    public static String getSimpleClassName(int ltoType) {
        switch (ltoType) {
            case LotteryLover.LTO_TYPE_LTO:
                return Lto.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO2C:
                return Lto2C.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO7C:
                return Lto7C.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_BIG:
                return LtoBig.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_DOF:
                return LtoDof.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_HK:
                return LtoHK.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_539:
                return Lto539.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_POW:
                return LtoPow.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_MM:
                return LtoMM.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_J6:
                return LtoJ6.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_TOTO:
                return LtoToTo.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_AU_POW:
                return LtoAuPow.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_EM:
                return LtoEm.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_LIST3:
                return LtoList3.class.getSimpleName();
            case LotteryLover.LTO_TYPE_LTO_LIST4:
                return LtoList4.class.getSimpleName();
            default:
                throw new RuntimeException("wrong lto type");
        }
    }
}
