package yhh.bj4.lotterylover.fragments.analyze;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.fragments.analyze.result.AnalyzeResult;
import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/12.
 */
public class LotteryItemsLoader extends AsyncTask<Void, Void, Void> {
    public interface Callback {
        void onFinish(AnalyzeResult result);
    }

    private final WeakReference<Context> mContext;

    private final WeakReference<Callback> mCallback;

    private final int mLotteryType;

    private final List<LotteryItem> mItems = new ArrayList<>();

    private AnalyzeResult mResult;

    public LotteryItemsLoader(Context context, Callback cb, int lotteryType, List<LotteryItem> items) {
        mContext = new WeakReference<>(context);
        mCallback = new WeakReference<>(cb);
        mLotteryType = lotteryType;
        mItems.addAll(items);
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mItems.isEmpty()) return null;
        final Context context = mContext.get();
        if (context == null) return null;
        final Callback callback = mCallback.get();
        if (callback == null) return null;

        mResult = new AnalyzeResult(mLotteryType, mItems);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Callback cb = mCallback.get();
        if (cb == null) return;
        cb.onFinish(mResult);
    }
}
