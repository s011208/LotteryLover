package yhh.bj4.lotterylover;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class Utilities {
    public static final boolean DEBUG = true;

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
        int month = Integer.valueOf(splitString[1]);
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
        try {
            int[] attr = new int[]{key};
            int indexOfAttrBackgroundColor = 0;
            TypedArray a = context.obtainStyledAttributes(attr);
            sColorAttrsArray.put(key, a.getColor(indexOfAttrBackgroundColor, defaultColor));
            a.recycle();
        } catch (Exception e) {
            sColorAttrsArray.put(key, defaultColor);
        }
        return sColorAttrsArray.get(key);
    }

    public static int getWindowBackgroundColor(Context context) {
        return getColorAttribute(context, android.R.attr.windowBackground, Color.WHITE);
    }

    public static int getPrimaryColor(Context context) {
        return getColorAttribute(context, android.R.attr.colorPrimary, Color.WHITE);
    }

    private Utilities() {
    }
}
