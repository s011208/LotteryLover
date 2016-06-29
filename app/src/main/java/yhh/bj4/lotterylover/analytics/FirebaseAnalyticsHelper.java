package yhh.bj4.lotterylover.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by User on 2016/6/25.
 */
public class FirebaseAnalyticsHelper extends Analytics {

    private FirebaseAnalytics mInstance;

    public FirebaseAnalyticsHelper(Context context) {
        super(context);
        mInstance = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void logEvent(String key, Bundle data) {
        mInstance.logEvent(key, data);
    }

    @Override
    public void logEvent(String cat, String act, String lab) {
        Bundle data = new Bundle();
        data.putString(Analytics.ACTION, act);
        data.putString(Analytics.LABEL, lab);
        logEvent(Analytics.CATEGORY + "_" + cat, data);
    }
}
