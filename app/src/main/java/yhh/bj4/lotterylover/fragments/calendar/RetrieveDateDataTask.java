package yhh.bj4.lotterylover.fragments.calendar;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.fragments.calendar.item.CalendarItem;
import yhh.bj4.lotterylover.fragments.calendar.item.CalendarItemDate;
import yhh.bj4.lotterylover.fragments.calendar.item.CalendarItemWeekDay;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.provider.LotteryProvider;
import yhh.bj4.lotterylover.settings.calendar.ShowDrawingTip;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class RetrieveDateDataTask extends AsyncTask<Void, Void, List<CalendarItem>> {

    public interface Callback {
        void onFinish(List<CalendarItem> items);
    }

    final WeakReference<Callback> mCallback;
    final int mYear, mMonth;
    final Context mContext;

    RetrieveDateDataTask(int y, int m, Context context, Callback cb) {
        mYear = y;
        mMonth = m;
        mContext = context;
        mCallback = new WeakReference<>(cb);
    }

    @Override
    protected List<CalendarItem> doInBackground(Void... params) {
        List<CalendarItem> rtn = new ArrayList<>();
        for (String weekDay : CalendarUtility.getShortWeekdayList()) {
            rtn.add(new CalendarItemWeekDay(weekDay));
        }

        Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        long startTime = c.getTimeInMillis();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        long endTime = c.getTimeInMillis() + Utilities.DAY;
        Cursor dateCursor = mContext.getContentResolver().query(LotteryProvider.QUERY_LTO_DRAWING_DATE, null,
                LotteryItem.COLUMN_DRAWING_DATE_TIME + ">=" + startTime + " and " +
                        LotteryItem.COLUMN_DRAWING_DATE_TIME + "<" + endTime
                , null, null);

        List<Long> drawingDateList = new ArrayList<>();
        List<Integer> showLtoTipsList = ShowDrawingTip.getCheckedLtoType(mContext);

        if (dateCursor != null) {
            try {
                while (dateCursor.moveToNext()) {
                    final int ltoType = dateCursor.getInt(0);
                    if (!showLtoTipsList.contains(ltoType)) continue;
                    drawingDateList.add(dateCursor.getLong(1));
                }
            } finally {
                dateCursor.close();
            }
        }
        for (Date date : CalendarUtility.getAllDateAtYearAndMonth(mYear, mMonth)) {
            rtn.add(new CalendarItemDate(date, drawingDateList.contains(date.getTime())));
        }

        return rtn;
    }

    @Override
    protected void onPostExecute(List<CalendarItem> list) {
        super.onPostExecute(list);
        Callback cb = mCallback.get();
        if (cb == null) return;
        cb.onFinish(list);
    }
}
