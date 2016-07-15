package yhh.bj4.lotterylover.fragments.analyze.list34;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/14.
 */
public class List34ItemsLoader extends AsyncTask<Void, Void, List34Result> {
    private static final String TAG = "List34ItemsLoader";
    private static final boolean DEBUG = Utilities.DEBUG;

    private final WeakReference<Context> mContext;

    private final int mLotteryType;

    private final List<LotteryItem> mItems = new ArrayList<>();

    private final WeakReference<Callback> mCallback;

    public interface Callback {
        void onLoadList34Finished(List34Result result);
    }

    public List34ItemsLoader(Context context, int ltoType, Callback cb, List<LotteryItem> items) {
        mContext = new WeakReference<>(context);
        mLotteryType = ltoType;
        mCallback = new WeakReference<>(cb);
        mItems.addAll(items);
    }

    @Override
    protected List34Result doInBackground(Void... params) {
        if (mItems.isEmpty()) return null;
        final Context context = mContext.get();
        if (context == null) return null;
        final Callback callback = mCallback.get();
        if (callback == null) return null;
        List34Result result = new List34Result(context, mLotteryType, mItems);
        return result;
    }

    @Override
    protected void onPostExecute(List34Result result) {
        super.onPostExecute(result);
        Callback callback = mCallback.get();
        if (callback == null) return;
        callback.onLoadList34Finished(result);
    }
}
