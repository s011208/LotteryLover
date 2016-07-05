package yhh.bj4.lotterylover.fragments.calendar;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yhh.bj4.lotterylover.fragments.calendar.item.CalendarItem;
import yhh.bj4.lotterylover.fragments.calendar.item.CalendarItemDate;
import yhh.bj4.lotterylover.fragments.calendar.item.CalendarItemWeekDay;

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
        for (Date date : CalendarUtility.getAllDateAtYearAndMonth(mYear, mMonth)) {
            rtn.add(new CalendarItemDate(date));
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
