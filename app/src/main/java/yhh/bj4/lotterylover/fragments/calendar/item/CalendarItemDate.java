package yhh.bj4.lotterylover.fragments.calendar.item;

import java.util.Date;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class CalendarItemDate extends CalendarItem {

    private final Date mDate;

    private final boolean mHasDrawing;

    public CalendarItemDate(Date date, boolean hasDrawing) {
        mDate = date;
        mHasDrawing = hasDrawing;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean hasDrawing() {
        return mHasDrawing;
    }

    @Override
    public boolean isClickable() {
        return true;
    }
}
