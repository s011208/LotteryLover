package yhh.bj4.lotterylover.fragments.calendar.item;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class CalendarItemWeekDay extends CalendarItem {
    private final String mWeekDayString;

    public CalendarItemWeekDay(String weekDay) {
        mWeekDayString = weekDay;
    }

    public String getWeekDay() {
        return mWeekDayString;
    }

    @Override
    public boolean isClickable() {
        return false;
    }
}
