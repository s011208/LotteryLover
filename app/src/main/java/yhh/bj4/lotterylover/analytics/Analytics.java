package yhh.bj4.lotterylover.analytics;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by yenhsunhuang on 2016/6/29.
 */
public abstract class Analytics {
    public static final String EVENT_TABLE_INFORMATION = "Table_information";
    public static final String EVENT_SCROLL_TO_TOP = "Scroll_to_top";
    public static final String EVENT_SCROLL_TO_BOTTOM = "Scroll_to_bottom";
    public static final String EVENT_SETTINGS = "settings";
    public static final String EVENT_SETTINGS_BUTTON = "settings_button";

    public static final String KEY_LIST_TYPE = "List_type";
    public static final String KEY_LTO_TYPE = "Lto_type";
    public static final String KEY_SETTINGS_NAME = "Settings_name";
    public static final String KEY_SETTINGS_VALUE = "Settings_value";

    public static final String CATEGORY = "Category";
    public static final String ACTION = "Action";
    public static final String LABEL = "Label";

    public Analytics(Context context) {
    }

    public abstract void logEvent(String key, Bundle data);

    public abstract void logEvent(String cat, String act, String lab);
}
