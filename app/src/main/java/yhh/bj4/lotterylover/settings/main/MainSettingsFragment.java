package yhh.bj4.lotterylover.settings.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.provider.AppSettings;

/**
 * Created by User on 2016/6/21.
 */
public class MainSettingsFragment extends PreferenceFragment {

    private static final String SETTINGS_APPEARANCE_DIGIT_SCALE_SIZE = "settings_appearance_digit_scale_size";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_settings);

        Preference digitSize = findPreference(SETTINGS_APPEARANCE_DIGIT_SCALE_SIZE);
        if (digitSize != null) {
            digitSize.setSummary(getActivity().getResources().getStringArray(R.array.settings_appearance_digit_size_list)[AppSettings.get(getActivity(), LotteryLover.KEY_DIGIT_SCALE_SIZE, LotteryLover.DIGIT_SCALE_SIZE_NORMAL)]);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, final Preference preference) {
        final String key = preference.getKey();
        if (SETTINGS_APPEARANCE_DIGIT_SCALE_SIZE.equals(key)) {
            final int selectedDigitSize = AppSettings.get(getActivity(), LotteryLover.KEY_DIGIT_SCALE_SIZE, LotteryLover.DIGIT_SCALE_SIZE_NORMAL);
            final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.settings_appearance_digit_scale_size_title)
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setSingleChoiceItems(R.array.settings_appearance_digit_size_list, selectedDigitSize, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == selectedDigitSize) return;
                            dialog.dismiss();
                            preference.setSummary(getActivity().getResources().getStringArray(R.array.settings_appearance_digit_size_list)[which]);
                            AppSettings.put(getActivity(), LotteryLover.KEY_DIGIT_SCALE_SIZE, which);
                            if (getActivity() instanceof Callback) {
                                ((Callback) getActivity()).onItemChanged(LotteryLover.KEY_DIGIT_SCALE_SIZE);
                            }
                        }
                    }).create();
            dialog.show();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public interface Callback {
        void onItemChanged(String itemKey);
    }
}
