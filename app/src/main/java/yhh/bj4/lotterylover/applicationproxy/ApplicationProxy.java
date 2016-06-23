package yhh.bj4.lotterylover.applicationproxy;

import android.content.Context;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public abstract class ApplicationProxy {
    private final Context mContext;

    public ApplicationProxy(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract void onCreate();

    public abstract void onTerminate();
}
