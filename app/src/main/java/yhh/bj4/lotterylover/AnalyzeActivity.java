package yhh.bj4.lotterylover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import yhh.bj4.lotterylover.fragments.analyze.AnalyzeFragment;

/**
 * Created by yenhsunhuang on 2016/7/12.
 */
public class AnalyzeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int mDrawerSelectedItemId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(AnalyzeFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new AnalyzeFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, AnalyzeFragment.class.getSimpleName()).commitAllowingStateLoss();
    }

    @Override
    public int getContentViewResource() {
        return R.layout.activity_analyze;
    }

    @Override
    public String getActivityName() {
        return AnalyzeActivity.class.getSimpleName();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setCheckedItem(R.id.nav_analyze);
        }
    }

    @Override
    protected void initActionBar(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.analyze_activity_title);
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
                    startActivity(new Intent(AnalyzeActivity.this, MainTableActivity.class));
                } else if (mDrawerSelectedItemId == R.id.nav_calendar) {
                    startActivity(new Intent(AnalyzeActivity.this, CalendarActivity.class));
                } else if (mDrawerSelectedItemId == R.id.nav_rating_us) {
                    Utilities.startRatingUsAction(AnalyzeActivity.this);
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
        if (mDrawerSelectedItemId == R.id.nav_analyze) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
