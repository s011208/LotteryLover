package yhh.bj4.lotterylover.settings.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import yhh.bj4.lotterylover.R;

/**
 * Created by yenhsunhuang on 2016/7/9.
 */
public class CalendarSettingsFragment extends PreferenceFragment {
    private static final String SHOW_DRAWING_TIME_TIP = "show_drawing_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.calendar_settings);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        final String key = preference.getKey();
        if (SHOW_DRAWING_TIME_TIP.equals(key)) {
            Intent intent = new Intent(getActivity(), ShowDrawingTipActivity.class);
            startActivity(intent);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
