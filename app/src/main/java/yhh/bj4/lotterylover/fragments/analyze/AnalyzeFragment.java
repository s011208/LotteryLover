package yhh.bj4.lotterylover.fragments.analyze;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/7/12.
 */
public class AnalyzeFragment extends PreferenceFragment {
    private static final String TAG = "AnalyzeFragment";
    private static final boolean DEBUG = Utilities.DEBUG;

    private static final String ALL_PERIOD_TOP_5 = "all_period_top_five";
    private static final String ALL_PERIOD_LAST_5 = "all_period_last_five";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.analyze_fragment);
    }
}
