package yhh.bj4.lotterylover.fragments.calendar;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import yhh.bj4.lotterylover.fragments.calendar.item.TodayLotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class RetrieveTodayLotteryTask extends AsyncTask<Void, Void, ArrayList<TodayLotteryItem>> {
    private final int mYear, mMonth, mDay;
    private final WeakReference<Context> mContext;
    private final WeakReference<Callback> mCallback;

    public interface Callback {
        void onFinish(ArrayList<TodayLotteryItem> items);
    }

    public RetrieveTodayLotteryTask(Context context, Callback cb, int y, int m, int d) {
        mContext = new WeakReference<>(context);
        mYear = y;
        mMonth = m;
        mDay = d;
        mCallback = new WeakReference<>(cb);
    }

    @Override
    protected ArrayList<TodayLotteryItem> doInBackground(Void... params) {
        ArrayList<TodayLotteryItem> rtn = new ArrayList<>();
        return rtn;
    }

    @Override
    protected void onPostExecute(ArrayList<TodayLotteryItem> lotteryItems) {
        super.onPostExecute(lotteryItems);
        Callback cb = mCallback.get();
        if (cb == null) return;
        cb.onFinish(lotteryItems);
    }
}
