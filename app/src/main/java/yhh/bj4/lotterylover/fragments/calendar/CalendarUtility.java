package yhh.bj4.lotterylover.fragments.calendar;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yenhsunhuang on 2016/7/4.
 */
public class CalendarUtility {
    public static final int SIZE_OF_DATE = 42;

    private static final List<String> sShortWeekdayList = new ArrayList<>();

    static {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        sShortWeekdayList.addAll(Arrays.asList(dateFormatSymbols.getShortWeekdays()));
        // dateFormatSymbols.getShortWeekdays provides {"", "sun", "mon"...}
        sShortWeekdayList.remove(0);
    }

    public static List<String> getShortWeekdayList() {
        return new ArrayList<>(sShortWeekdayList);
    }

    /**
     * get all date data which will be display on calendar view
     *
     * @param y year
     * @param m month
     * @return all date data start with Sunday
     */
    public static List<Date> getAllDateAtYearAndMonth(int y, int m) {
        return getAllDateAtYearAndMonth(y, m, true);
    }

    private static List<Date> getAllDateAtYearAndMonth(final int y, final int m, final boolean fillUpSpace) {
        final List<Date> rtn = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        clearCalendarOffset(calendar);
        final int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (fillUpSpace) {
            // filling up previous
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            final int dayOfWeekOfFirstDay = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeekOfFirstDay != Calendar.SUNDAY) {
                int previousYear = y;
                int previousMonth = m - 1;
                if (previousMonth < Calendar.JANUARY) {
                    previousMonth = Calendar.DECEMBER;
                    --previousYear;
                }
                List<Date> previousDates = getAllDateAtYearAndMonth(previousYear, previousMonth, false);
                final int startIndex = previousDates.size() - dayOfWeekOfFirstDay + 1;
                final int endIndex = previousDates.size();
                rtn.addAll(previousDates.subList(startIndex, endIndex));
            }
        }

        for (int i = 0; i < dayOfMonth; ++i) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            rtn.add(new Date(calendar.getTimeInMillis()));
        }

        if (fillUpSpace) {
            // filling up following
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            int nextYear = y;
            int nextMonth = m + 1;
            if (nextMonth > Calendar.DECEMBER) {
                nextMonth = Calendar.JANUARY;
                ++nextYear;
            }
            // we keep all data size to SIZE_OF_DATE
            List<Date> nextDates = getAllDateAtYearAndMonth(nextYear, nextMonth, false);
            for (int i = 0; i < nextDates.size() && rtn.size() < SIZE_OF_DATE; ++i) {
                rtn.add(nextDates.get(i));
            }
        }
        return rtn;
    }

    /**
     * keep year, month & day of calendar
     *
     * @param c calendar to be clear
     */
    public static void clearCalendarOffset(Calendar c) {
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
    }

    private CalendarUtility() {
    }
}
