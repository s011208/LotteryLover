package yhh.bj4.lotterylover.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import yhh.bj4.lotterylover.BaseActivity;
import yhh.bj4.lotterylover.R;

/**
 * Created by User on 2016/6/21.
 */
public abstract class BaseSettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, getContainerFragment()).commitAllowingStateLoss();
    }

    @Override
    public int getContentViewResource() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initActionBar(Bundle savedInstanceState) {
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

    public abstract Fragment getContainerFragment();
}
