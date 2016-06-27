package yhh.bj4.lotterylover;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.provider.AppSettings;
import yhh.bj4.lotterylover.services.RetrieveDataService;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class Utilities {
    public static final boolean DEBUG = true;
    public static final long SECOND = 1000;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final int QUERY_LIMIT = 500;
    private static final String TAG = "Utilities";
    private static final SparseArray<Integer> sColorAttrsArray = new SparseArray<>();
    private static Calendar sCalendar = Calendar.getInstance();
    private static Map<Integer, List<Integer>> sPlusAndLastDigitMap = new HashMap<>();
    private static Map<Integer, List<Integer>> sLastDigitMap = new HashMap<>();

    static {
        sCalendar.set(Calendar.HOUR_OF_DAY, 0);
        sCalendar.set(Calendar.MINUTE, 0);
        sCalendar.set(Calendar.SECOND, 0);
        sCalendar.set(Calendar.MILLISECOND, 0);
    }

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

    private Utilities() {
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
        if (context == null) return Color.rgb(0xff, 0x8a, 0x53);
        return getColorAttribute(context, android.R.attr.colorPrimary, context.getResources().getColor(R.color.colorPrimary));
    }

    public static int getPrimaryDarkColor(Context context) {
        if (context == null) return Color.rgb(0xb5, 0x61, 0x3b);
        return getColorAttribute(context, android.R.attr.colorPrimaryDark, context.getResources().getColor(R.color.colorPrimaryDark));
    }

    public static int getPrimaryLightColor(Context context) {
        if (context == null) return Color.rgb(0xff, 0xb1, 0x8d);
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

    public static float getDigitSizeScale(int index) {
        switch (index) {
            case LotteryLover.DIGIT_SCALE_SIZE_TINY:
                return LotteryLover.VALUE_DIGIT_SCALE_SIZE_TINY;
            case LotteryLover.DIGIT_SCALE_SIZE_SMALL:
                return LotteryLover.VALUE_DIGIT_SCALE_SIZE_SMALL;
            case LotteryLover.DIGIT_SCALE_SIZE_NORMAL:
                return LotteryLover.VALUE_DIGIT_SCALE_SIZE_NORMAL;
            case LotteryLover.DIGIT_SCALE_SIZE_LARGE:
                return LotteryLover.VALUE_DIGIT_SCALE_SIZE_LARGE;
            case LotteryLover.DIGIT_SCALE_SIZE_HUGE:
                return LotteryLover.VALUE_DIGIT_SCALE_SIZE_HUGE;
        }
        return LotteryLover.VALUE_DIGIT_SCALE_SIZE_NORMAL;
    }

    public static boolean areAllLtoItemsAreInit(Context context) {
        return AppSettings.get(context, LotteryLover.KEY_INIT_LTO, false) &&
                AppSettings.get(context, LotteryLover.KEY_INIT_LTO2C, false) &&
                AppSettings.get(context, LotteryLover.KEY_INIT_LTO7C, false) &&
                AppSettings.get(context, LotteryLover.KEY_INIT_LTO_BIG, false) &&
                AppSettings.get(context, LotteryLover.KEY_INIT_LTO_DOF, false) &&
                AppSettings.get(context, LotteryLover.KEY_INIT_LTO_HK, false);
    }

    public static void updateAllLtoData(Context context, String reason) {
        Log.d(TAG, "updateAllLtoData reason: " + reason);
        if (Utilities.areAllLtoItemsAreInit(context)) {
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO2C);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO7C);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_BIG);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_DOF);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_HK);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_539);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_POW);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_MM);
        }
    }

    public static String getListStringByType(int listType) {
        switch (listType) {
            case LotteryLover.LIST_TYPE_OVERALL:
                return "Overall";
            case LotteryLover.LIST_TYPE_NUMERIC:
                return "Numeric";
            case LotteryLover.LIST_TYPE_LAST_DIGIT:
                return "Last digit";
            case LotteryLover.LIST_TYPE_PLUS_AND_MINUS:
                return "Plus and minus";
            case LotteryLover.LIST_TYPE_PLUS_TOGETHER:
                return "Plus digits";
            default:
                throw new RuntimeException("unexpected list type");
        }
    }

    @Nullable
    public static String getProcessName(Context context, int pID) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : am.getRunningAppProcesses()) {
            if (runningAppProcessInfo.pid != pID) continue;
            if (!TextUtils.isEmpty(runningAppProcessInfo.processName) && runningAppProcessInfo.processName.startsWith("yhh.bj4.lotterylover")) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }
}
