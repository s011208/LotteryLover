package yhh.bj4.lotterylover;

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

    private Utilities() {
    }
}
