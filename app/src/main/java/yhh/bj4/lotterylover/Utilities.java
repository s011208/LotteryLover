package yhh.bj4.lotterylover;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

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

    private static int sWindowBackgroundColor;
    private static boolean sHasRetrievedWindowBackgroundColor = false;

    public static int getWindowBackgroundColor(Context context) {
        if (sHasRetrievedWindowBackgroundColor) {
            return sWindowBackgroundColor;
        }
        try {
            int[] backgroundColorAttr = new int[]{android.R.attr.windowBackground};
            int indexOfAttrBackgroundColor = 0;
            TypedArray a = context.obtainStyledAttributes(backgroundColorAttr);
            sWindowBackgroundColor = a.getColor(indexOfAttrBackgroundColor, -1);
            a.recycle();
        } catch (Exception e) {
            sWindowBackgroundColor = Color.WHITE;
        }
        sHasRetrievedWindowBackgroundColor = true;
        return sWindowBackgroundColor;
    }

    private static int sPrimaryColor;
    private static boolean sHasRetrievedPrimaryColor = false;

    public static int getPrimaryColor(Context context) {
        if (sHasRetrievedPrimaryColor) {
            return sPrimaryColor;
        }
        try {
            int[] backgroundColorAttr = new int[]{android.R.attr.colorPrimary};
            int indexOfAttrBackgroundColor = 0;
            TypedArray a = context.obtainStyledAttributes(backgroundColorAttr);
            sPrimaryColor = a.getColor(indexOfAttrBackgroundColor, -1);
            a.recycle();
        } catch (Exception e) {
            sPrimaryColor = Color.WHITE;
        }
        sHasRetrievedPrimaryColor = true;
        return sPrimaryColor;
    }

    private Utilities() {
    }
}
