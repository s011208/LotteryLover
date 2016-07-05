package yhh.bj4.lotterylover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.crash.FirebaseCrash;

import yhh.bj4.lotterylover.firebase.RemoteConfigHelper;

/**
 * Created by yenhsunhuang on 2016/6/28.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private static final boolean DEBUG = Utilities.DEBUG;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrash.log(getActivityName() + " onCreate " + this);
        Utilities.setActivityOrientation(this);
        setContentView(getContentViewResource());
        restoreSavedInstanceState(savedInstanceState);

        initActionBar(savedInstanceState);
        initViewComponents(savedInstanceState);
    }

    public abstract int getContentViewResource();

    public abstract String getActivityName();

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseCrash.log(getActivityName() + " onResume " + this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseCrash.log(getActivityName() + " onPause " + this);
    }

    protected abstract void initActionBar(Bundle savedInstanceState);

    protected abstract void initViewComponents(Bundle savedInstanceState);

    protected abstract void restoreSavedInstanceState(Bundle savedInstanceState);

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
