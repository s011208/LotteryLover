package yhh.bj4.lotterylover.settings.calendar;

import android.app.Fragment;

import yhh.bj4.lotterylover.settings.BaseSettingsActivity;

/**
 * Created by yenhsunhuang on 2016/7/9.
 */
public class CalendarSettingsActivity extends BaseSettingsActivity {
    @Override
    public Fragment getContainerFragment() {
        return new CalendarSettingsFragment();
    }

    @Override
    public String getActivityName() {
        return CalendarSettingsActivity.class.getSimpleName();
    }
}
