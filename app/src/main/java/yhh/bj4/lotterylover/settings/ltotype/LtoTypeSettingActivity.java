package yhh.bj4.lotterylover.settings.ltotype;

import android.app.Fragment;
import android.os.Bundle;

import yhh.bj4.lotterylover.settings.BaseSettingsActivity;

/**
 * Created by yenhsunhuang on 2016/6/27.
 */
public class LtoTypeSettingActivity extends BaseSettingsActivity {

    @Override
    public Fragment getContainerFragment() {
        return new LtoTypeSettingFragment();
    }

    @Override
    public String getActivityName() {
        return LtoTypeSettingActivity.class.getSimpleName();
    }

    @Override
    protected void initViewComponents(Bundle savedInstanceState) {

    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {

    }
}
