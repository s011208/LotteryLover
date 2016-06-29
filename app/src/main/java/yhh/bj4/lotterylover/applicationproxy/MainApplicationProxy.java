package yhh.bj4.lotterylover.applicationproxy;

import android.content.Context;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.analytics.AnalyticsHelper;
import yhh.bj4.lotterylover.remoteconfig.RemoteConfigHelper;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class MainApplicationProxy extends ApplicationProxy {
    public MainApplicationProxy(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        Utilities.initVariables(getContext());
        AnalyticsHelper.getHelper(getContext());
    }

    @Override
    public void onTerminate() {

    }
}
