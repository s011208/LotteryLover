package yhh.bj4.lotterylover.fragments.analyze;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 * Created by yenhsunhuang on 2016/7/12.
 */
public class LotteryItemsLoader extends AsyncTask<Void, Void, Void> {
    public interface Callback {

    }

    private final WeakReference<Context> mContext;

    private final WeakReference<Callback> mCallback;

    public LotteryItemsLoader(Context context, Callback cb) {
        mContext = new WeakReference<>(context);
        mCallback = new WeakReference<>(cb);
    }

    @Override
    protected Void doInBackground(Void... params) {
        final Context context = mContext.get();
        if (context == null) return null;
        final Callback callback = mCallback.get();
        if (callback == null) return null;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
