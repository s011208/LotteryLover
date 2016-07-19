package yhh.bj4.lotterylover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.google.firebase.crash.FirebaseCrash;

import yhh.bj4.lotterylover.provider.AppSettings;

/**
 * Created by yenhsunhuang on 2016/6/28.
 */
public abstract class BaseActivity extends AppCompatActivity {
    static final String TAG = "BaseActivity";
    static final boolean DEBUG = Utilities.DEBUG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrash.log(getActivityName() + " onCreate " + this);
        Utilities.setActivityOrientation(this);
        setContentView(getContentViewResource());
        restoreSavedInstanceState(savedInstanceState);

        initActionBar(savedInstanceState);
        initViewComponents(savedInstanceState);

        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    public abstract int getContentViewResource();

    public abstract String getActivityName();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseCrash.log(getActivityName() + " onResume " + this);
        if (AppSettings.get(BaseActivity.this, LotteryLover.KEY_KEEP_SCREEN_ON, false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
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
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
        }
    }
}
