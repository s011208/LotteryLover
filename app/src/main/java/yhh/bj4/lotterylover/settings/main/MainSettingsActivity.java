package yhh.bj4.lotterylover.settings.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.settings.BaseSettingsActivity;

/**
 * Created by User on 2016/6/21.
 */
public class MainSettingsActivity extends BaseSettingsActivity implements MainSettingsFragment.Callback {
    public static final String CHANGED_LIST_KEY = "clk";

    private final List<String> mChangedItemList = new ArrayList<>();

    @Override
    public Fragment getContainerFragment() {
        return new MainSettingsFragment();
    }

    @Override
    public String getActivityName() {
        return MainSettingsActivity.class.getSimpleName();
    }

    @Override
    public void onBackPressed() {
        if (mChangedItemList.isEmpty()) {
            super.onBackPressed();
        } else {
            Intent data = new Intent();
            data.putStringArrayListExtra(CHANGED_LIST_KEY, (ArrayList<String>) mChangedItemList);
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onItemChanged(String key) {
        if (key == null || mChangedItemList.contains(key)) return;
        mChangedItemList.add(key);
        if (LotteryLover.KEY_FORCE_RELOAD.equals(key)) {
            onBackPressed();
        }
    }
}
