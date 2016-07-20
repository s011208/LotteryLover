package yhh.bj4.lotterylover.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import yhh.bj4.lotterylover.BuildConfig;
import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.provider.AppSettings;

/**
 * Created by yenhsunhuang on 2016/6/29.
 */
public class RemoteConfigHelper {
    private static final String TAG = "RemoteConfigHelper";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final String KEY_SHOW_MONTHLY_DATA_ALWAYS = "show_monthly_data_only_always";
    public static final String KEY_IS_READ_FROM_CONFIG = "is_read_config";
    public static final String KEY_CONFIG_VERSION = "config_version";
    public static final String KEY_SHOW_ADS = "show_ads";
    public static final String KEY_SET_TABLE_BACKGROUND_FROM_WEB = "set_table_background_from_web";
    public static final String KEY_ALLOW_USER_UPDATE_LTO_LIST = "allow_user_update_lto_data";
    public static final String KEY_SYNC_FROM_FIREBASE = "sync_from_firebase";

    private static RemoteConfigHelper sRemoteConfigHelper;

    public synchronized static RemoteConfigHelper getInstance(Context context) {
        if (sRemoteConfigHelper == null) sRemoteConfigHelper = new RemoteConfigHelper(context);
        return sRemoteConfigHelper;
    }

    private final Context mContext;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private RemoteConfigHelper(Context context) {
        if (DEBUG) {
            Log.d(TAG, "RemoteConfigHelper create");
        }
        mContext = context.getApplicationContext();
    }

    public void fetch() {
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_settings);
        firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build());
        firebaseRemoteConfig.fetch(firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled() ? 0 : Utilities.HOUR)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.w(TAG, "onComplete");
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.w(TAG, "onSuccess");
                        firebaseRemoteConfig.activateFetched();
                        updateValues();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "failed to fetch remote configs", e);
                    }
                });
    }

    private void updateValues() {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        final long remoteConfigVersion = firebaseRemoteConfig.getLong(KEY_CONFIG_VERSION);
        Log.i(TAG, "remote config version: " + remoteConfigVersion);
        if (firebaseRemoteConfig.getBoolean(KEY_IS_READ_FROM_CONFIG)) {
            if (firebaseRemoteConfig.getLong(KEY_CONFIG_VERSION) > AppSettings.get(mContext, LotteryLover.KEY_REMOTE_CONFIG_VERSION, 0) ||
                    firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
                AppSettings.put(mContext, LotteryLover.KEY_REMOTE_CONFIG_VERSION, remoteConfigVersion);

                final boolean showMonthlyDataAlways = firebaseRemoteConfig.getBoolean(KEY_SHOW_MONTHLY_DATA_ALWAYS);
                if (AppSettings.get(mContext, LotteryLover.KEY_SHOW_MONTHLY_DATA_ALWAYS, true) != showMonthlyDataAlways) {
                    AppSettings.put(mContext, LotteryLover.KEY_SHOW_MONTHLY_DATA_ALWAYS, showMonthlyDataAlways);
                }
                final boolean showAds = firebaseRemoteConfig.getBoolean(KEY_SHOW_ADS);
                if (AppSettings.get(mContext, LotteryLover.KEY_SHOW_ADS, false) != showAds) {
                    AppSettings.put(mContext, LotteryLover.KEY_SHOW_ADS, showAds);
                }
                final boolean setTableBackground = firebaseRemoteConfig.getBoolean(KEY_SET_TABLE_BACKGROUND_FROM_WEB);
                if (AppSettings.get(mContext, LotteryLover.KEY_SET_TABLE_BACKGROUND_FROM_WEB, false) != setTableBackground) {
                    AppSettings.put(mContext, LotteryLover.KEY_SET_TABLE_BACKGROUND_FROM_WEB, setTableBackground);
                }
                final boolean allowUserUpdateLtoTable = firebaseRemoteConfig.getBoolean(KEY_ALLOW_USER_UPDATE_LTO_LIST);
                if (AppSettings.get(mContext, LotteryLover.KEY_ALLOW_USER_UPDATE_LTO_LIST, true) != allowUserUpdateLtoTable) {
                    AppSettings.put(mContext, LotteryLover.KEY_ALLOW_USER_UPDATE_LTO_LIST, allowUserUpdateLtoTable);
                }
                final boolean syncFromFirebase = firebaseRemoteConfig.getBoolean(KEY_SYNC_FROM_FIREBASE);
                if (AppSettings.get(mContext, LotteryLover.KEY_SYNC_FROM_FIREBASE, true) != syncFromFirebase) {
                    AppSettings.put(mContext, LotteryLover.KEY_SYNC_FROM_FIREBASE, syncFromFirebase);
                }
                Log.d(TAG, "KEY_SHOW_MONTHLY_DATA_ALWAYS: " + showMonthlyDataAlways);
                Log.d(TAG, "read from fetch config");
                Log.d(TAG, "KEY_CONFIG_VERSION: " + firebaseRemoteConfig.getLong(KEY_CONFIG_VERSION));
                Log.d(TAG, "KEY_SHOW_ADS: " + showAds);
                Log.d(TAG, "KEY_SET_TABLE_BACKGROUND_FROM_WEB: " + setTableBackground);
                Log.d(TAG, "KEY_ALLOW_USER_UPDATE_LTO_LIST: " + allowUserUpdateLtoTable);
                Log.d(TAG, "KEY_SYNC_FROM_FIREBASE: " + syncFromFirebase);
            } else {
                Log.d(TAG, "old remote config version, ignore, version: " + remoteConfigVersion);
            }
        } else {
            Log.d(TAG, "cannot read from fetch config");
        }
    }
}
