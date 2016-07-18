package yhh.bj4.lotterylover;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        if (context == null) return Color.rgb(0xff, 0xb0, 0x1c);
        return getColorAttribute(context, android.R.attr.colorPrimary, context.getResources().getColor(R.color.colorPrimary));
    }

    public static int getPrimaryDarkColor(Context context) {
        if (context == null) return Color.rgb(0x80, 0x53, 0x00);
        return getColorAttribute(context, android.R.attr.colorPrimaryDark, context.getResources().getColor(R.color.colorPrimaryDark));
    }

    public static int getPrimaryLightColor(Context context) {
        if (context == null) return Color.rgb(0xff, 0xcf, 0x78);
        return context.getResources().getColor(R.color.colorPrimaryLight);
    }

    public static String getDateTimeYMDString(long time) {
        return new SimpleDateFormat("yyyy/MM/dd").format(time);

    }

    public static String getLotteryNumberString(int num, int digitLength) {
        return String.format("%0" + digitLength + "d", num);
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
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_J6);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_TOTO);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_AU_POW);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_EM);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_LIST3);
            RetrieveDataService.startServiceAndUpdate(context, LotteryLover.LTO_TYPE_LTO_LIST4);
        }
    }

    public static void clearAllLtoTables(Context context) {
        context.getContentResolver().delete(Lto.DATA_URI, null, null);
        context.getContentResolver().delete(LtoBig.DATA_URI, null, null);
        context.getContentResolver().delete(LtoHK.DATA_URI, null, null);
        context.getContentResolver().delete(LtoDof.DATA_URI, null, null);
        context.getContentResolver().delete(Lto2C.DATA_URI, null, null);
        context.getContentResolver().delete(Lto7C.DATA_URI, null, null);
        context.getContentResolver().delete(Lto539.DATA_URI, null, null);
        context.getContentResolver().delete(LtoPow.DATA_URI, null, null);
        context.getContentResolver().delete(LtoMM.DATA_URI, null, null);
        context.getContentResolver().delete(LtoJ6.DATA_URI, null, null);
        context.getContentResolver().delete(LtoToTo.DATA_URI, null, null);
        context.getContentResolver().delete(LtoAuPow.DATA_URI, null, null);
        context.getContentResolver().delete(LtoEm.DATA_URI, null, null);
        context.getContentResolver().delete(LtoList4.DATA_URI, null, null);
        context.getContentResolver().delete(LtoList3.DATA_URI, null, null);
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
            case LotteryLover.LIST_TYPE_COMBINE_LIST:
                return "Combine list";
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

    private static String ODD = "", PLURAL = "";

    public static void initVariables(Context context) {
        ODD = context.getResources().getString(R.string.odd);
        PLURAL = context.getResources().getString(R.string.plural);
    }

    public static String oddString() {
        return ODD;
    }

    public static String pluralString() {
        return PLURAL;
    }

    public static void setActivityOrientation(Activity activity) {
        if (activity == null) return;
        int orientationSetting = AppSettings.get(activity, LotteryLover.KEY_DISPLAY_ORIENTATION, LotteryLover.VALUE_BY_DEVICE);
        if (orientationSetting == LotteryLover.VALUE_BY_DEVICE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else if (orientationSetting == LotteryLover.VALUE_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (orientationSetting == LotteryLover.VALUE_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public static boolean isEnableAds(Context context) {
        return AppSettings.get(context, LotteryLover.KEY_SHOW_ADS, false);
    }

    public static boolean isEnableToLoadTableBackgroundFromWeb(Context context) {
        return AppSettings.get(context, LotteryLover.KEY_SET_TABLE_BACKGROUND_FROM_WEB, false);
    }

    public static int getMaximumDigitLengthOfSum(ArrayList<LotteryItem> lotteryData, final int maximumSpecialNumber) {
        int largestNumber = 0;
        Map<Integer, Integer> sumMap = new HashMap<>();
        for (LotteryItem item : lotteryData) {
            for (Integer value : item.getNormalNumbers()) {
                Integer mapSum = sumMap.get(value);
                if (mapSum == null) {
                    sumMap.put(value, 1);
                } else {
                    sumMap.put(value, mapSum + 1);
                }
            }
        }
        Iterator<Integer> mapIterator;
        if (maximumSpecialNumber == -1) {
            mapIterator = sumMap.keySet().iterator();
            while (mapIterator.hasNext()) {
                final int key = mapIterator.next();
                largestNumber = Math.max(sumMap.get(key), largestNumber);
            }

            sumMap = new HashMap<>();
        }

        for (LotteryItem item : lotteryData) {
            for (Integer value : item.getSpecialNumbers()) {
                Integer mapSum = sumMap.get(value);
                if (mapSum == null) {
                    sumMap.put(value, 1);
                } else {
                    sumMap.put(value, mapSum + 1);
                }
            }
        }
        mapIterator = sumMap.keySet().iterator();
        while (mapIterator.hasNext()) {
            final int key = mapIterator.next();
            largestNumber = Math.max(sumMap.get(key), largestNumber);
        }

        int rtn = 0;
        while (largestNumber != 0) {
            largestNumber /= 10;
            ++rtn;
        }

        if (rtn < 2) {
            rtn = 2;
        }
        return rtn;
    }

    public static void startRatingUsAction(Activity activity) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "Cannot find activity", e);
            Toast.makeText(activity, R.string.view_all_activity_toast_cannot_find_store, Toast.LENGTH_LONG).show();
        }
    }
}
