package yhh.bj4.lotterylover.applicationproxy;

import android.content.Context;

import yhh.bj4.lotterylover.analytics.FirebaseAnalyticsHelper;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class MainApplicationProxy extends ApplicationProxy {
    public MainApplicationProxy(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        FirebaseAnalyticsHelper.init(getContext().getApplicationContext());
    }

    @Override
    public void onTerminate() {

    }
}
