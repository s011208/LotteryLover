package yhh.bj4.lotterylover.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/7/22.
 */
public class SharedPrefHelper {
    private static SharedPrefHelper sInstance;

    public synchronized static SharedPrefHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPrefHelper(context);
        }
        return sInstance;
    }

    private static final String SHARED_PREF_FILE = "spf_";

    // keys
    private static final String KEY_AUTO_SCALE_SIZE = "auto_scale_size";

    private final Context mContext;

    private final SharedPreferences mPrefs;

    private SharedPrefHelper(Context context) {
        mContext = context.getApplicationContext();
        mPrefs = mContext.getSharedPreferences(SHARED_PREF_FILE + Utilities.getProcessName(mContext, android.os.Process.myPid()), Context.MODE_PRIVATE);
    }

    public SharedPreferences getPrefs() {
        return mPrefs;
    }

    public String getKey(String key) {
        return key + "_" + mContext.getResources().getConfiguration().orientation;
    }

    public String getAutoScaleKeyByLtoType(int ltoType) {
        return getKey(KEY_AUTO_SCALE_SIZE + "_" + ltoType);
    }
}
