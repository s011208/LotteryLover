package yhh.bj4.lotterylover;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;

import yhh.bj4.lotterylover.analytics.Analytics;
import yhh.bj4.lotterylover.analytics.AnalyticsHelper;
import yhh.bj4.lotterylover.firebase.RemoteConfigHelper;
import yhh.bj4.lotterylover.fragments.MainTableFragment;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.parser.LtoList3.LtoList3;
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto539.Lto539;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltoMM.LtoMM;
import yhh.bj4.lotterylover.parser.ltoapow.LtoAuPow;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;
import yhh.bj4.lotterylover.parser.ltoem.LtoEm;
import yhh.bj4.lotterylover.parser.ltolist4.LtoList4;
import yhh.bj4.lotterylover.parser.ltopow.LtoPow;
import yhh.bj4.lotterylover.provider.AppSettings;
import yhh.bj4.lotterylover.settings.main.MainSettingsActivity;
import yhh.bj4.lotterylover.views.listtype.ListTypeAdapter;

public class ViewAllActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainTableFragment.Callback {

    private static final String TAG = "ViewAllActivity";
    private static final boolean DEBUG = Utilities.DEBUG;

    private static final int REQUEST_SETTINGS = 1000;
    private static final String KEY_CURRENT_NAV_ITEM_ID = "key_current_nav_item_id";

    private MainTableFragment mMainTableFragment;
    private LinearLayout mLoadingProgressbar;
    private int mListType = LotteryLover.LIST_TYPE_OVERALL;
    private int mLtoType = LotteryLover.LTO_TYPE_LTO;
    private RecyclerView mListTypeView;
    private ListTypeAdapter mListTypeAdapter;
    private AdView mAdView;
    private int mCurrentNavItemId = R.id.nav_list;

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
            } else if (Lto539.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_539;
            } else if (LtoPow.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_POW;
            } else if (LtoMM.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_MM;
            } else if (LtoMM.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_J6;
            } else if (LtoMM.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_TOTO;
            } else if (LtoAuPow.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_AU_POW;
            } else if (LtoEm.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_EM;
            } else if (LtoList3.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_LIST3;
            } else if (LtoList4.DATA_URI.equals(uri)) {
                updateList = mLtoType == LotteryLover.LTO_TYPE_LTO_LIST4;
            } else if (uri.toString().startsWith(AppSettings.DATA_URI.toString())) {
                if (Utilities.areAllLtoItemsAreInit(ViewAllActivity.this)) {
                    if (!isMainTableAvailable()) {
                        mLoadingProgressbar.setVisibility(View.INVISIBLE);
                        if (mCurrentNavItemId == R.id.nav_list) {
                            Utilities.updateAllLtoData(ViewAllActivity.this, "just finish loading");
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mMainTableFragment, MainTableFragment.class.getSimpleName()).commitAllowingStateLoss();
                        }
                    }
                }
                if (LotteryLover.KEY_SHOW_MONTHLY_DATA_ALWAYS.equals(uri.getLastPathSegment())) {
                    invalidateOptionsMenu();
                }
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

        if (Utilities.areAllLtoItemsAreInit(this)) {
            mLoadingProgressbar.setVisibility(View.INVISIBLE);
            if (mCurrentNavItemId == R.id.nav_list) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mMainTableFragment, MainTableFragment.class.getSimpleName()).commitAllowingStateLoss();
            }
        }

        registerObserver();
        initAds();
    }

    private void initAds() {
        mAdView = (AdView) findViewById(R.id.adView);
        if (mAdView == null) return;
        if (Utilities.isEnableAds(ViewAllActivity.this) == false) {
            mAdView.setVisibility(View.GONE);
            return;
        }
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6361389364792908/9700783026");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private boolean isMainTableAvailable() {
        return getSupportFragmentManager().findFragmentByTag(MainTableFragment.class.getSimpleName()) != null;
    }

    private void registerObserver() {
        getContentResolver().registerContentObserver(Lto.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(Lto2C.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(Lto7C.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(LtoBig.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(LtoDof.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(LtoHK.DATA_URI, true, mContentObserver);
        getContentResolver().registerContentObserver(AppSettings.DATA_URI, true, mContentObserver);
    }

    public void unregisterObserver() {
        getContentResolver().unregisterContentObserver(mContentObserver);
    }

    @Override
    protected void onResume() {
        FirebaseCrash.log("ViewAllActivity onResume " + this);
        RemoteConfigHelper.getInstance(this).fetch();
        super.onResume();
        if (mAdView != null && Utilities.isEnableAds(ViewAllActivity.this)) {
            mAdView.resume();
        }
    }

    @Override
    protected void onPause() {
        FirebaseCrash.log("ViewAllActivity onPause " + this);
        super.onPause();
        if (mAdView != null && Utilities.isEnableAds(ViewAllActivity.this)) {
            mAdView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        FirebaseCrash.log("ViewAllActivity onDestroy " + this);
        unregisterObserver();
        super.onDestroy();
        if (mAdView != null && Utilities.isEnableAds(ViewAllActivity.this)) {
            mAdView.destroy();
        }
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mListType = AppSettings.get(ViewAllActivity.this, LotteryLover.KEY_LIST_TYPE, mListType);
            mLtoType = AppSettings.get(ViewAllActivity.this, LotteryLover.KEY_LTO_TYPE, mLtoType);
            mCurrentNavItemId = R.id.nav_list;
        } else {
            mListType = savedInstanceState.getInt(LotteryLover.KEY_LIST_TYPE, mListType);
            mLtoType = savedInstanceState.getInt(LotteryLover.KEY_LTO_TYPE, mLtoType);
            mCurrentNavItemId = savedInstanceState.getInt(KEY_CURRENT_NAV_ITEM_ID, R.id.nav_list);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LotteryLover.KEY_LIST_TYPE, mListType);
        outState.putInt(LotteryLover.KEY_LTO_TYPE, mLtoType);
        outState.putInt(KEY_CURRENT_NAV_ITEM_ID, mCurrentNavItemId);
    }

    private void initViewComponents(Bundle savedInstanceState) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(mCurrentNavItemId);
        mLoadingProgressbar = (LinearLayout) findViewById(R.id.loading_progressbar);
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

            Spinner actionBarSpinner = (Spinner) ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_action_bar_spinner, null);
            actionBarSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.action_bar_spinner_lto_type)));
            actionBarSpinner.setSelection(mLtoType);
            actionBarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mLtoType = position;
                    if (mLtoType != AppSettings.get(ViewAllActivity.this, LotteryLover.KEY_LTO_TYPE, LotteryLover.LTO_TYPE_LTO)) {
                        AppSettings.put(ViewAllActivity.this, LotteryLover.KEY_LTO_TYPE, mLtoType);
                    }
                    if (isMainTableAvailable()) {
                        mMainTableFragment.setLtoType(mLtoType);
                        mListTypeAdapter.setLtoType(mLtoType);
                        Bundle data = new Bundle();
                        data.putString(Analytics.KEY_LTO_TYPE, LotteryItem.getSimpleClassName(mLtoType));
                        data.putString(Analytics.KEY_LIST_TYPE, Utilities.getListStringByType(mListType));
                        AnalyticsHelper.getHelper(ViewAllActivity.this).logEvent(Analytics.EVENT_TABLE_INFORMATION, data);
                        AnalyticsHelper.getHelper(ViewAllActivity.this).logEvent(Analytics.EVENT_TABLE_INFORMATION, LotteryItem.getSimpleClassName(mLtoType), Utilities.getListStringByType(mListType));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            getSupportActionBar().setCustomView(actionBarSpinner, params);
            getSupportActionBar().setElevation(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mListTypeView = (RecyclerView) findViewById(R.id.list_type_recyclerview);
        mListTypeAdapter = new ListTypeAdapter(this, new ListTypeAdapter.Callback() {
            @Override
            public void onListTypeChanged(int type) {
                mListType = type;
                AppSettings.put(ViewAllActivity.this, LotteryLover.KEY_LIST_TYPE, mListType);
                if (isMainTableAvailable()) {
                    mMainTableFragment.setListType(mListType);
                    Bundle data = new Bundle();
                    data.putString(Analytics.KEY_LTO_TYPE, LotteryItem.getSimpleClassName(mLtoType));
                    data.putString(Analytics.KEY_LIST_TYPE, Utilities.getListStringByType(mListType));
                    AnalyticsHelper.getHelper(ViewAllActivity.this).logEvent(Analytics.EVENT_TABLE_INFORMATION, data);
                    AnalyticsHelper.getHelper(ViewAllActivity.this).logEvent(Analytics.EVENT_TABLE_INFORMATION, LotteryItem.getSimpleClassName(mLtoType), Utilities.getListStringByType(mListType));
                }
            }
        }, mListType, mLtoType);
        mListTypeView.setAdapter(mListTypeAdapter);
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
            ArrayList<String> changedItemList = data.getStringArrayListExtra(MainSettingsActivity.CHANGED_LIST_KEY);
            if (DEBUG) {
                for (String s : changedItemList) {
                    Log.d(TAG, "changedItemList:" + s);
                }
            }
            if (changedItemList.contains(LotteryLover.KEY_DIGIT_SCALE_SIZE)) {
                if (mMainTableFragment != null) {
                    if (isMainTableAvailable()) {
                        mMainTableFragment.updateDigitScaleSize();
                    }
                }
            }
            if (changedItemList.contains(LotteryLover.KEY_ORDER) ||
                    changedItemList.contains(LotteryLover.KEY_DISPLAY_ROWS)) {
                if (mMainTableFragment != null) {
                    if (isMainTableAvailable()) {
                        mMainTableFragment.updateAllList();
                    }
                }
            }
            if (changedItemList.contains(LotteryLover.KEY_COMBINE_SPECIAL)) {
                if (mListType == LotteryLover.LIST_TYPE_PLUS_AND_MINUS ||
                        mListType == LotteryLover.LIST_TYPE_OVERALL) {
                    if (isMainTableAvailable()) {
                        mMainTableFragment.updateAllList();
                    }
                }
            }
            if (changedItemList.contains(LotteryLover.KEY_DISPLAY_ORIENTATION)) {
                Utilities.setActivityOrientation(this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_all, menu);
        MenuItem item = menu.findItem(R.id.action_show_subtotal_only);
        item.setChecked(AppSettings.get(ViewAllActivity.this, LotteryLover.KEY_SHOW_SUB_TOTAL_ONLY, false));
        item.setIcon(item.isChecked() ? R.drawable.ic_format_list_bulleted_black_24dp : R.drawable.ic_format_list_bulleted_white_24dp);
        item.setShowAsAction(AppSettings.get(ViewAllActivity.this, LotteryLover.KEY_SHOW_MONTHLY_DATA_ALWAYS, true) ? MenuItem.SHOW_AS_ACTION_IF_ROOM : MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AnalyticsHelper.getHelper(this).logEvent(Analytics.EVENT_SETTINGS_BUTTON, new Bundle());
            AnalyticsHelper.getHelper(ViewAllActivity.this).logEvent(Analytics.EVENT_SETTINGS_BUTTON, null, null);
            Intent intent = new Intent(ViewAllActivity.this, MainSettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS);
            return true;
        } else if (id == R.id.action_align_top) {
            if (mMainTableFragment != null) {
                mMainTableFragment.scrollToTop();
            }
            Bundle data = new Bundle();
            data.putString(Analytics.KEY_LTO_TYPE, LotteryItem.getSimpleClassName(mLtoType));
            data.putString(Analytics.KEY_LIST_TYPE, Utilities.getListStringByType(mListType));
            AnalyticsHelper.getHelper(this).logEvent(Analytics.EVENT_SCROLL_TO_TOP, data);
            AnalyticsHelper.getHelper(this).logEvent(Analytics.EVENT_SCROLL_TO_TOP, LotteryItem.getSimpleClassName(mLtoType), Utilities.getListStringByType(mListType));
            return true;
        } else if (id == R.id.action_align_bottom) {
            if (mMainTableFragment != null) {
                mMainTableFragment.scrollToBottom();
            }
            Bundle data = new Bundle();
            data.putString(Analytics.KEY_LTO_TYPE, LotteryItem.getSimpleClassName(mLtoType));
            data.putString(Analytics.KEY_LIST_TYPE, Utilities.getListStringByType(mListType));
            AnalyticsHelper.getHelper(this).logEvent(Analytics.EVENT_SCROLL_TO_BOTTOM, data);
            AnalyticsHelper.getHelper(this).logEvent(Analytics.EVENT_SCROLL_TO_BOTTOM, LotteryItem.getSimpleClassName(mLtoType), Utilities.getListStringByType(mListType));
            return true;
        } else if (id == R.id.action_show_subtotal_only) {
            final boolean newValue = !item.isChecked();
            item.setChecked(newValue);
            item.setIcon(newValue ? R.drawable.ic_format_list_bulleted_black_24dp : R.drawable.ic_format_list_bulleted_white_24dp);
            AppSettings.put(ViewAllActivity.this, LotteryLover.KEY_SHOW_SUB_TOTAL_ONLY, newValue);
            if (mMainTableFragment != null) {
                // TODO block overall content list if can
                mMainTableFragment.setIsShowSubTotalOnly(newValue);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();
        mCurrentNavItemId = id;
        if (id == R.id.nav_list) {
            if (Utilities.areAllLtoItemsAreInit(this)) {
                mLoadingProgressbar.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mMainTableFragment, MainTableFragment.class.getSimpleName()).commitAllowingStateLoss();
            }
        } else if (id == R.id.nav_calendar) {

        } else if (id == R.id.nav_analyze) {

        } else if (id == R.id.nav_rating_us) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.d(TAG, "Cannot find activity", e);
                Toast.makeText(ViewAllActivity.this, R.string.view_all_activity_toast_cannot_find_store, Toast.LENGTH_LONG).show();
            }
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

    @Override
    public boolean isShowSubTotalOnly() {
        return AppSettings.get(ViewAllActivity.this, LotteryLover.KEY_SHOW_SUB_TOTAL_ONLY, false);
    }

    @Override
    public void onStartUpdate() {
        if (mLoadingProgressbar == null) return;
        mLoadingProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinishUpdate() {
        if (mLoadingProgressbar == null) return;
        mLoadingProgressbar.setVisibility(View.INVISIBLE);
    }
}
