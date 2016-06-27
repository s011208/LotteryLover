package yhh.bj4.lotterylover.settings.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.settings.BaseSettingsActivity;

/**
 * Created by User on 2016/6/21.
 */
public class MainSettingsActivity extends BaseSettingsActivity implements MainSettingsFragment.Callback {
    public static final String CHANGED_LIST_KEY = "clk";

    private final List<String> mChangedItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainSettingsFragment()).commitAllowingStateLoss();
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
    }
}
