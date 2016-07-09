package yhh.bj4.lotterylover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import yhh.bj4.lotterylover.analytics.Analytics;
import yhh.bj4.lotterylover.analytics.AnalyticsHelper;
import yhh.bj4.lotterylover.fragments.calendar.CalendarFragment;
import yhh.bj4.lotterylover.settings.calendar.CalendarSettingsActivity;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class CalendarActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_SETTINGS = 1000;

    private int mDrawerSelectedItemId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment(), CalendarFragment.class.getSimpleName()).commitAllowingStateLoss();

    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setCheckedItem(R.id.nav_calendar);
        }
    }

    @Override
    public int getContentViewResource() {
        return R.layout.activity_calendar;
    }

    @Override
    public String getActivityName() {
        return CalendarActivity.class.getSimpleName();
    }

    @Override
    protected void initActionBar(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.calender_activity_title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mDrawerSelectedItemId == R.id.nav_list) {
                    onBackPressed();
                } else if (mDrawerSelectedItemId == R.id.nav_analyze) {
                } else if (mDrawerSelectedItemId == R.id.nav_rating_us) {
                    Utilities.startRatingUsAction(CalendarActivity.this);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void initViewComponents(Bundle savedInstanceState) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerSelectedItemId = item.getItemId();
        if (mDrawerSelectedItemId == R.id.nav_calendar) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AnalyticsHelper.getHelper(this).logEvent(Analytics.EVENT_SETTINGS_BUTTON, new Bundle());
            AnalyticsHelper.getHelper(CalendarActivity.this).logEvent(Analytics.EVENT_SETTINGS_BUTTON, null, null);
            Intent intent = new Intent(CalendarActivity.this, CalendarSettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
