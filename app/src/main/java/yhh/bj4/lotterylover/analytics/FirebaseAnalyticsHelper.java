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
}
