package yhh.bj4.lotterylover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yhh.bj4.lotterylover.firebase.FirebaseDatabaseHelper;
import yhh.bj4.lotterylover.fragments.MainTableFragment;
import yhh.bj4.lotterylover.parser.LotteryParser;
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto.LtoParser;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto2c.Lto2CParser;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.lto7c.Lto7CParser;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHKParser;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltobig.LtoBigParser;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;
import yhh.bj4.lotterylover.parser.ltodof.LtoDofParser;
import yhh.bj4.lotterylover.provider.AppSettings;
import yhh.bj4.lotterylover.settings.SettingsActivity;
import yhh.bj4.lotterylover.views.listtype.ListTypeAdapter;

public class ViewAllActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainTableFragment.Callback {

    private static final String TAG = "ViewAllActivity";
    private static final boolean DEBUG = Utilities.DEBUG;

    private static final int REQUEST_SETTINGS = 1000;

    private Spinner mActionBarSpinner;
    private RecyclerView mListTypeView;
    private MainTableFragment mMainTableFragment;
    private int mListType = LotteryLover.LIST_TYPE_OVERALL;
    private int mLtoType = LotteryLover.LTO_TYPE_LTO;

    private final ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            if (selfChange) return;
            if (DEBUG) {
                Log.d(TAG, "onChange, uri: " + uri);
            }
            boolean updateList = false;
            if (Lto.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO;
            } else if (Lto2C.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO2C;
            } else if (Lto7C.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO7C;
            } else if (LtoBig.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_BIG;
            } else if (LtoDof.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_DOF;
            } else if (LtoHK.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_HK;
            }
            if (updateList) {
                mMainTableFragment.updateAllList();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseCrash.log("ViewAllActivity onCreate " + this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        restoreSavedInstanceState(savedInstanceState);
        initActionBar(savedInstanceState);
        initViewComponents(savedInstanceState);

        mMainTableFragment = (MainTableFragment) getSupportFragmentManager().findFragmentByTag(MainTableFragment.class.getSimpleName());
        if (mMainTableFragment == null) {
            mMainTableFragment = new MainTableFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mMainTableFragment, MainTableFragment.class.getSimpleName()).commitAllowingStateLoss();

        registerObserver();
//        queryPage0();
//        for (int i=1; i<1500; ++i) {
//            add(i);
//        }
    }

    private void add(int seq) {
        ArrayList<Integer> normal = new ArrayList<>();
        normal.add(3);
        normal.add(6);
        normal.add(8);
        normal.add(30);
        normal.add(33);
        normal.add(36);

        ArrayList<Integer> special = new ArrayList<>();
        special.add(3);

        Lto lto = new Lto(seq, 321000, normal, special, "memo", "extra");
        DatabaseReference db = FirebaseDatabaseHelper.getFirebaseDatabase().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(String.valueOf(lto.getSequence()), lto.toMap());
        db.child(FirebaseDatabaseHelper.CHILD_LOTTERY_DATA).child(Lto.class.getSimpleName()).updateChildren(childUpdates);
    }

    private void registerObserver() {
        getContentResolver().registerContentObserver(Lto.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(Lto2C.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(Lto7C.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(LtoBig.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(LtoDof.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(LtoHK.DATA_URI, true, mContentObserver);
    }

    public void unregisterObserver() {
        getContentResolver().unregisterContentObserver(mContentObserver);
    }

    @Override
    protected void onResume() {
        FirebaseCrash.log("ViewAllActivity onResume " + this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        FirebaseCrash.log("ViewAllActivity onPause " + this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        FirebaseCrash.log("ViewAllActivity onDestroy " + this);
        unregisterObserver();
        super.onDestroy();
    }

    private void queryPage0() {
        new Lto2CParser(this, 0, new LotteryParser.Callback() {
            @Override
            public void onStart(int page) {

            }

            @Override
            public void onFinish(int page, int[] results) {

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new Lto7CParser(this, 0, new LotteryParser.Callback() {
            @Override
            public void onStart(int page) {

            }

            @Override
            public void onFinish(int page, int[] results) {

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new LtoBigParser(this, 0, new LotteryParser.Callback() {
            @Override
            public void onStart(int page) {

            }

            @Override
            public void onFinish(int page, int[] results) {

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new LtoDofParser(this, 0, new LotteryParser.Callback() {
            @Override
            public void onStart(int page) {

            }

            @Override
            public void onFinish(int page, int[] results) {

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new LtoHKParser(this, 0, new LotteryParser.Callback() {
            @Override
            public void onStart(int page) {

            }

            @Override
            public void onFinish(int page, int[] results) {

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new LtoParser(this, 0, new LotteryParser.Callback() {
            @Override
            public void onStart(int page) {

            }

            @Override
            public void onFinish(int page, int[] results) {

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mListType = AppSettings.get(ViewAllActivity.this, LotteryLover.KEY_LIST_TYPE, mListType);
            mLtoType = AppSettings.get(ViewAllActivity.this, LotteryLover.KEY_LTO_TYPE, mLtoType);
        } else {
            mListType = savedInstanceState.getInt(LotteryLover.KEY_LIST_TYPE, mListType);
            mLtoType = savedInstanceState.getInt(LotteryLover.KEY_LTO_TYPE, mLtoType);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LotteryLover.KEY_LIST_TYPE, mListType);
        outState.putInt(LotteryLover.KEY_LTO_TYPE, mLtoType);
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
            mActionBarSpinner.setSelection(mLtoType);
            mActionBarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mLtoType = position;
                    AppSettings.put(ViewAllActivity.this, LotteryLover.KEY_LTO_TYPE, mLtoType);
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
                AppSettings.put(ViewAllActivity.this, LotteryLover.KEY_LIST_TYPE, mListType);
                mMainTableFragment.setListType(mListType);
            }
        }, mListType));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SETTINGS) {
            if (data == null || resultCode != Activity.RESULT_OK) return;
            ArrayList<String> changedItemList = data.getStringArrayListExtra(SettingsActivity.CHANGED_LIST_KEY);
            if (DEBUG) {
                for (String s : changedItemList) {
                    Log.d(TAG, "changedItemList:" + s);
                }
            }
            if (changedItemList.contains(LotteryLover.KEY_DIGIT_SCALE_SIZE)) {
                if (mMainTableFragment != null) {
                    mMainTableFragment.updateDigitScaleSize();
                }
            }
            if (changedItemList.contains(LotteryLover.KEY_ORDER) ||
                    changedItemList.contains(LotteryLover.KEY_DISPLAY_ROWS)) {
                if (mMainTableFragment != null) {
                    mMainTableFragment.updateAllList();
                }
            }
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ViewAllActivity.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
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
