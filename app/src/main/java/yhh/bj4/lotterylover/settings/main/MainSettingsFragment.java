package yhh.bj4.lotterylover.settings.main;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.provider.AppSettings;

/**
 * Created by User on 2016/6/21.
 */
public class MainSettingsFragment extends PreferenceFragment {

    private static final String SETTINGS_APPEARANCE_DIGIT_SCALE_SIZE = "settings_appearance_digit_scale_size";

    private static final String SETTINGS_DISPLAY_ORDER = "settings_display_order";
    private static final String SETTINGS_DISPLAY_ROW_COUNT = "settings_display_row_count";
    private static final String SETTINGS_ABOUT_APP_VERSION = "settings_about_app_version";
    private static final String SETTINGS_ABOUT_CONTACT_ME = "settings_about_contact_me";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_settings);

        Preference pref;

        pref = findPreference(SETTINGS_APPEARANCE_DIGIT_SCALE_SIZE);
        if (pref != null) {
            pref.setSummary(getActivity().getResources().getStringArray(R.array.settings_appearance_digit_size_list)[AppSettings.get(getActivity(), LotteryLover.KEY_DIGIT_SCALE_SIZE, LotteryLover.DIGIT_SCALE_SIZE_NORMAL)]);
        }

        pref = findPreference(SETTINGS_DISPLAY_ORDER);
        if (pref != null) {
            pref.setSummary(getActivity().getResources().getStringArray(R.array.settings_display_order)[AppSettings.get(getActivity(), LotteryLover.KEY_ORDER, LotteryLover.ORDER_BY_ASC)]);
        }

        pref = findPreference(SETTINGS_DISPLAY_ROW_COUNT);
        if (pref != null) {
            pref.setSummary(getActivity().getResources().getStringArray(R.array.settings_display_rows)[AppSettings.get(getActivity(), LotteryLover.KEY_DISPLAY_ROWS, LotteryLover.DISPLAY_ROWS_100)]);
        }

        pref = findPreference(SETTINGS_ABOUT_APP_VERSION);
        if (pref != null) {
            try {
                pref.setSummary(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
            }
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
                    .setItems(R.array.settings_appearance_digit_size_list, new DialogInterface.OnClickListener() {
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
        } else if (SETTINGS_DISPLAY_ORDER.equals(key)) {
            final int selectedOrder = AppSettings.get(getActivity(), LotteryLover.KEY_ORDER, LotteryLover.ORDER_BY_ASC);
            final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.settings_display_order)
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setItems(R.array.settings_display_order, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == selectedOrder) return;
                            dialog.dismiss();
                            preference.setSummary(getActivity().getResources().getStringArray(R.array.settings_display_order)[which]);
                            AppSettings.put(getActivity(), LotteryLover.KEY_ORDER, which);
                            if (getActivity() instanceof Callback) {
                                ((Callback) getActivity()).onItemChanged(LotteryLover.KEY_ORDER);
                            }
                        }
                    }).create();
            dialog.show();
        } else if (SETTINGS_DISPLAY_ROW_COUNT.equals(key)) {
            final int selectedRowCount = AppSettings.get(getActivity(), LotteryLover.KEY_DISPLAY_ROWS, LotteryLover.DISPLAY_ROWS_100);
            final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.settings_display_row_count)
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setItems(R.array.settings_display_rows, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == selectedRowCount) return;
                            dialog.dismiss();
                            preference.setSummary(getActivity().getResources().getStringArray(R.array.settings_display_rows)[which]);
                            AppSettings.put(getActivity(), LotteryLover.KEY_DISPLAY_ROWS, which);
                            if (getActivity() instanceof Callback) {
                                ((Callback) getActivity()).onItemChanged(LotteryLover.KEY_DISPLAY_ROWS);
                            }
                        }
                    }).create();
            dialog.show();
        } else if (SETTINGS_ABOUT_CONTACT_ME.equals(key)) {
            try {
                String emailAddress = "s011208@gmail.com";
                String emailSubject = "Hi, I'd like to contact with you";
                String emailMessage = "";
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailMessage);
                startActivity(emailIntent);
                return true;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "unexpected error", Toast.LENGTH_LONG).show();
            }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public interface Callback {
        void onItemChanged(String itemKey);
    }
}
