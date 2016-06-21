package yhh.bj4.lotterylover;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.util.Log;
import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class Utilities {
    public static final boolean DEBUG = true;
    private static final String TAG = "Utilities";

    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;

    public static final int QUERY_LIMIT = 500;

    private static Calendar sCalendar = Calendar.getInstance();

    static {
        sCalendar.set(Calendar.HOUR_OF_DAY, 0);
        sCalendar.set(Calendar.MINUTE, 0);
        sCalendar.set(Calendar.SECOND, 0);
        sCalendar.set(Calendar.MILLISECOND, 0);
    }

    public static long convertStringDateToLong(String date) {
        String[] splitString = date.split("[^0-9]+");
        int year = Integer.valueOf(splitString[0]);
        int month = Integer.valueOf(splitString[1]) - 1;
        int day = Integer.valueOf(splitString[2]);
        sCalendar.set(year, month, day);
        return sCalendar.getTimeInMillis();
    }

    public static List<Integer> convertStringNumberToList(String stringNumber) {
        List<Integer> rtn = new ArrayList<>();
        String[] splitString = stringNumber.split("[^0-9]+");
        for (String str : splitString) {
            rtn.add(Integer.valueOf(str));
        }
        return rtn;
    }

    private static final SparseArray<Integer> sColorAttrsArray = new SparseArray<>();

    public static int getColorAttribute(Context context, int key, int defaultColor) {
        if (sColorAttrsArray.get(key) != null) {
            return sColorAttrsArray.get(key);
        }
        TypedArray a = null;
        try {
            int[] attr = new int[]{key};
            int indexOfAttrBackgroundColor = 0;
            a = context.obtainStyledAttributes(attr);
            sColorAttrsArray.put(key, a.getColor(indexOfAttrBackgroundColor, defaultColor));
        } catch (Exception e) {
            sColorAttrsArray.put(key, defaultColor);
            if (DEBUG) Log.w(TAG, "unexpected exception", e);
        } finally {
            if (a != null) a.recycle();
        }
        return sColorAttrsArray.get(key);
    }

    public static int getWindowBackgroundColor(Context context) {
        return getColorAttribute(context, android.R.attr.windowBackground, Color.WHITE);
    }

    public static int getPrimaryColor(Context context) {
        if (context == null) return Color.rgb(0xff, 0x88, 0x00);
        return getColorAttribute(context, android.R.attr.colorPrimary, context.getResources().getColor(R.color.colorPrimary));
    }

    public static int getPrimaryDarkColor(Context context) {
        if (context == null) return Color.rgb(0xff, 0x55, 0x11);
        return getColorAttribute(context, android.R.attr.colorPrimaryDark, context.getResources().getColor(R.color.colorPrimaryDark));
    }

    public static int getPrimaryLightColor(Context context) {
        if (context == null) return Color.rgb(0xff, 0xbb, 0x77);
        return context.getResources().getColor(R.color.colorPrimaryLight);
    }

    public static String getDateTimeYMDString(long time) {
        return new SimpleDateFormat("yyyy/MM/dd").format(time);

    }

    public static String getLotteryNumberString(int num) {
        return String.format("%02d", num);
    }

    public static String getLotterySequenceString(long num) {
        return String.format("%05d", num);
    }

    /**
     * @param lotteryItems
     * @return pair.first = normal, pair.second = special
     */
    public static Pair<ArrayList<Integer>, ArrayList<Integer>> collectLotteryItemsData(ArrayList<LotteryItem> lotteryItems) {
        if (lotteryItems == null || lotteryItems.isEmpty()) {
            return new Pair<>(new ArrayList<Integer>(), new ArrayList<Integer>());
        }
        // key = index, value = count
        Map<Integer, Integer> normalMap = new HashMap<>();
        Map<Integer, Integer> specialMap = new HashMap<>();
        for (LotteryItem item : lotteryItems) {
            for (Integer itemValue : item.getNormalNumbers()) {
                Integer value = normalMap.get(itemValue);
                if (value == null) {
                    normalMap.put(itemValue, 1);
                } else {
                    normalMap.put(itemValue, value + 1);
                }
            }
            for (Integer itemValue : item.getSpecialNumbers()) {
                Integer value = specialMap.get(itemValue);
                if (value == null) {
                    specialMap.put(itemValue, 1);
                } else {
                    specialMap.put(itemValue, value + 1);
                }
            }
        }
        final int maximumOfNormal = LotteryItem.getMaximumNormalNumber(lotteryItems.get(0));
        final int maximumOfSpecial = LotteryItem.getMaximumSpecialNumber(lotteryItems.get(0));
        ArrayList<Integer> resultOfNormal = new ArrayList<>();
        ArrayList<Integer> resultOfSpecial = new ArrayList<>();
        // ignore 0
        for (int i = 1; i < maximumOfNormal + 1; ++i) {
            Integer v = normalMap.get(i);
            resultOfNormal.add(v == null ? 0 : v);
        }
        for (int i = 1; i < (maximumOfSpecial == -1 ? maximumOfNormal : maximumOfSpecial) + 1; ++i) {
            Integer v = specialMap.get(i);
            resultOfSpecial.add(v == null ? 0 : v);
        }
        return new Pair<>(resultOfNormal, resultOfSpecial);
    }

    public static int addDigits(int num) {
        return num == 0 ? 0 : (num % 9 == 0 ? 9 : (num % 9));
    }

    public static int getLastDigit(int num) {
        return num % 10;
    }

    public static int addDigitsOnce(int num) {
        return num / 10 + num % 10;
    }

    private static Map<Integer, List<Integer>> sPlusAndLastDigitMap = new HashMap<>();

    static {
        for (int i = 0; i < 100; ++i) {
            int digit = Utilities.getLastDigit(Utilities.addDigitsOnce(i));
            List<Integer> listData = sPlusAndLastDigitMap.get(digit);
            if (listData == null) {
                listData = new ArrayList<>();
            }
            listData.add(i);
            sPlusAndLastDigitMap.put(i, listData);
        }
    }

    /**
     * key = new index, value = old index
     *
     * @param maximumIndex
     * @return
     */
    public static Map<Integer, Integer> getPlusAndLastDigitMap(int maximumIndex) {
        Map<Integer, Integer> rtn = new HashMap<>();
        int index = 0;
        for (int i = 0; i < 10; ++i) {
            List<Integer> data = sPlusAndLastDigitMap.get(i);
            for (Integer value : data) {
                if (value <= maximumIndex) {
                    rtn.put(index++, value);
                }
            }
        }
        return rtn;
    }

    private static Map<Integer, List<Integer>> sLastDigitMap = new HashMap<>();

    static {
        for (int i = 0; i < 100; ++i) {
            int digit = Utilities.getLastDigit(i);
            List<Integer> listData = sLastDigitMap.get(digit);
            if (listData == null) {
                listData = new ArrayList<>();
            }
            listData.add(i);
            sLastDigitMap.put(i, listData);
        }
    }

    /**
     * key = new index, value = old index
     *
     * @param maximumIndex
     * @return
     */
    public static Map<Integer, Integer> getLastDigitMap(int maximumIndex) {
        Map<Integer, Integer> rtn = new HashMap<>();
        int index = 0;
        for (int i = 0; i < 10; ++i) {
            List<Integer> data = sLastDigitMap.get(i);
            for (Integer value : data) {
                if (value <= maximumIndex) {
                    rtn.put(index++, value);
                }
            }
        }
        return rtn;
    }

    private Utilities() {
    }
}
