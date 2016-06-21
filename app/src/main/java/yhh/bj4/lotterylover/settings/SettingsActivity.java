package yhh.bj4.lotterylover.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.settings.main.MainSettingsFragment;

/**
 * Created by User on 2016/6/21.
 */
public class SettingsActivity extends AppCompatActivity implements MainSettingsFragment.Callback {
    public static final String CHANGED_LIST_KEY = "clk";

    private final List<String> mChangedItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initActionBar(savedInstanceState);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainSettingsFragment()).commitAllowingStateLoss();
    }

    private void initActionBar(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
