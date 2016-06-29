package yhh.bj4.lotterylover.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by User on 2016/6/25.
 */
public class FirebaseAnalyticsHelper {

    public static final String EVENT_TABLE_INFORMATION = "Table_information";
    public static final String EVENT_SCROLL_TO_TOP = "Scroll_to_top";
    public static final String EVENT_SCROLL_TO_BOTTOM = "Scroll_to_bottom";
    public static final String EVENT_SETTINGS = "settings";
    public static final String EVENT_SETTINGS_BUTTON = "settings_button";

    public static final String KEY_LIST_TYPE = "List_type";
    public static final String KEY_LTO_TYPE = "Lto_type";
    public static final String KEY_SETTINGS_NAME = "Settings_name";
    public static final String KEY_SETTINGS_VALUE = "Settings_value";

    private static FirebaseAnalytics sInstance;

    public synchronized static void init(Context context) {
        if (sInstance != null) return;
        sInstance = FirebaseAnalytics.getInstance(context.getApplicationContext());
    }

    public static void logEvent(String event, Bundle data) {
        if (sInstance == null) return;
        sInstance.logEvent(event, data);
    }
}
