package yhh.bj4.lotterylover.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by yenhsunhuang on 2016/6/29.
 */
public class GoogleAnalyticsHelper extends Analytics {
    private GoogleAnalytics mAnalytics;
    private Tracker mTracker;

    public GoogleAnalyticsHelper(Context context) {
        super(context);
        mAnalytics = GoogleAnalytics.getInstance(context);
        mTracker = mAnalytics.newTracker("UA-45176399-8");
        mTracker.enableAdvertisingIdCollection(true);
        mTracker.enableAutoActivityTracking(true);
        mTracker.enableExceptionReporting(true);
    }

    @Override
    public void logEvent(String key, Bundle data) {

    }

    @Override
    public void logEvent(String cat, String act, String lab) {
        mTracker.send(new HitBuilders.EventBuilder().setCategory(cat)
                .setAction(act).setLabel(lab).build());
    }
}
