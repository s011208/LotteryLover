package yhh.bj4.lotterylover;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import yhh.bj4.lotterylover.fragments.MainTableFragment;
import yhh.bj4.lotterylover.views.listtype.ListTypeAdapter;

public class ViewAllActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainTableFragment.Callback {

    private static final String TAG = "ViewAllActivity";
    private static final boolean DEBUG = Utilities.DEBUG;

    private Spinner mActionBarSpinner;
    private RecyclerView mListTypeView;
    private MainTableFragment mMainTableFragment;
    private int mListType = LotteryLover.LIST_TYPE_OVERALL;
    private int mLtoType = LotteryLover.LTO_TYPE_LTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        initActionBar(savedInstanceState);
        initViewComponents(savedInstanceState);

        mMainTableFragment = (MainTableFragment) getSupportFragmentManager().findFragmentByTag(MainTableFragment.class.getSimpleName());
        if (mMainTableFragment == null) {
            mMainTableFragment = new MainTableFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mMainTableFragment, MainTableFragment.class.getSimpleName()).commitAllowingStateLoss();
    }

    private void initViewComponents(Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initActionBar(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);

            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT);

            mActionBarSpinner = (Spinner) ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_action_bar_spinner, null);
            mActionBarSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.action_bar_spinner_lto_type)));
            mActionBarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mLtoType = position;
                    mMainTableFragment.setLtoType(mLtoType);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            getSupportActionBar().setCustomView(mActionBarSpinner, params);
            getSupportActionBar().setElevation(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mListTypeView = (RecyclerView) findViewById(R.id.list_type_recyclerview);
        mListTypeView.setAdapter(new ListTypeAdapter(this, new ListTypeAdapter.Callback() {
            @Override
            public void onListTypeChanged(int type) {
                mListType = type;
                mMainTableFragment.setListType(mListType);
            }
        }));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public int getLtoType() {
        return mLtoType;
    }

    @Override
    public int getListType() {
        return mListType;
    }
}
