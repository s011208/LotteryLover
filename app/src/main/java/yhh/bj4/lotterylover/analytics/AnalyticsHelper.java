package yhh.bj4.lotterylover.analytics;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yenhsunhuang on 2016/6/29.
 */
public class AnalyticsHelper {
    private static AnalyticsHelper sInstance;

    public synchronized static AnalyticsHelper getHelper(Context context) {
        if (sInstance == null) sInstance = new AnalyticsHelper(context);
        return sInstance;
    }

    private final Context mContext;
    private final List<Analytics> mAnalyticsList = new ArrayList<>();


    private AnalyticsHelper(Context context) {
        mContext = context.getApplicationContext();
        mAnalyticsList.add(new FirebaseAnalyticsHelper(mContext));
    }

    public void logEvent(String key, Bundle data) {
        for (Analytics ana : mAnalyticsList) {
            ana.logEvent(key, data);
        }
    }
}
