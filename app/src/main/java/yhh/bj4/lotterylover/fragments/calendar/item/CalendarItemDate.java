package yhh.bj4.lotterylover.fragments.calendar.item;

import java.util.Date;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class CalendarItemDate extends CalendarItem {

    private final Date mDate;

    public CalendarItemDate(Date date) {
        mDate = date;
    }

    public Date getDate() {
        return mDate;
    }

    @Override
    public boolean isClickable() {
        return true;
    }
}
