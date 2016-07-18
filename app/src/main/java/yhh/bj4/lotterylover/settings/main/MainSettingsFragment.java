package yhh.bj4.lotterylover.settings.main;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.analytics.Analytics;
import yhh.bj4.lotterylover.analytics.AnalyticsHelper;
import yhh.bj4.lotterylover.provider.AppSettings;
import yhh.bj4.lotterylover.services.UpdateLogger;
import yhh.bj4.lotterylover.settings.ltotype.LtoTypeSettingActivity;

/**
 * Created by User on 2016/6/21.
 */
public class MainSettingsFragment extends PreferenceFragment {

    private static final String SETTINGS_APPEARANCE_DIGIT_SCALE_SIZE = "settings_appearance_digit_scale_size";

    private static final String SETTINGS_DISPLAY_ORDER = "settings_display_order";
    private static final String SETTINGS_DISPLAY_ROW_COUNT = "settings_display_row_count";
    private static final String SETTINGS_ABOUT_APP_VERSION = "settings_about_app_version";
    private static final String SETTINGS_ABOUT_CONTACT_ME = "settings_about_contact_me";
    private static final String SETTINGS_DISPLAY_COMBINE_SPECIAL_NUMBER = "settings_display_combine_special_number";
    private static final String SETTINGS_OTHERS_LTO_LIST = "settings_others_lto_type_selector_title";
    private static final String SETTINGS_DISPLAY_SCREEN_ORIENTATION = "settings_display_orientation";

    private static final String SETTINGS_OTHER_UPDATE_PERIOD = "settings_other_update_period";
    private static final String SETTINGS_OTHER_UPDATE_RECORD = "settings_others_update_record";

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
                Log.w("Settings", "unexpected error", e);
            }
        }

        pref = findPreference(SETTINGS_DISPLAY_COMBINE_SPECIAL_NUMBER);
        if (pref != null) {
            ((CheckBoxPreference) pref).setChecked(AppSettings.get(getActivity(), LotteryLover.KEY_COMBINE_SPECIAL, false));
        }

        pref = findPreference(SETTINGS_DISPLAY_SCREEN_ORIENTATION);
        if (pref != null) {
            pref.setSummary(getResources().getStringArray(R.array.settings_display_orientation_list)[AppSettings.get(getActivity(), LotteryLover.KEY_DISPLAY_ORIENTATION, LotteryLover.VALUE_BY_DEVICE)]);
        }

        pref = findPreference(SETTINGS_OTHER_UPDATE_PERIOD);
        if (pref != null) {
            pref.setSummary(getResources().getStringArray(R.array.settings_other_update_period)[AppSettings.get(getActivity(), LotteryLover.KEY_UPDATE_PERIOD, LotteryLover.KEY_UPDATE_PERIOD_DEFUALT)]);
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
                            final String newSummary = getActivity().getResources().getStringArray(R.array.settings_appearance_digit_size_list)[which];
                            preference.setSummary(newSummary);
                            AppSettings.put(getActivity(), LotteryLover.KEY_DIGIT_SCALE_SIZE, which);
                            if (getActivity() instanceof Callback) {
                                ((Callback) getActivity()).onItemChanged(LotteryLover.KEY_DIGIT_SCALE_SIZE);
                            }
                            Bundle data = new Bundle();
                            data.putString(Analytics.KEY_SETTINGS_NAME, "Digit scale");
                            data.putString(Analytics.KEY_SETTINGS_VALUE, newSummary);
                            AnalyticsHelper.getHelper(getActivity()).logEvent(Analytics.EVENT_SETTINGS, data);
                            AnalyticsHelper.getHelper(getActivity()).logEvent(Analytics.EVENT_SETTINGS, "Digit scale", newSummary);
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
                            final String newSummary = getActivity().getResources().getStringArray(R.array.settings_display_order)[which];
                            preference.setSummary(newSummary);
                            AppSettings.put(getActivity(), LotteryLover.KEY_ORDER, which);
                            if (getActivity() instanceof Callback) {
                                ((Callback) getActivity()).onItemChanged(LotteryLover.KEY_ORDER);
                            }
                            Bundle data = new Bundle();
                            data.putString(Analytics.KEY_SETTINGS_NAME, "Display order");
                            data.putString(Analytics.KEY_SETTINGS_VALUE, newSummary);
                            AnalyticsHelper.getHelper(getActivity()).logEvent(Analytics.EVENT_SETTINGS, data);
                            AnalyticsHelper.getHelper(getActivity()).logEvent(Analytics.EVENT_SETTINGS, "Display order", newSummary);
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
                            final String newSummary = getActivity().getResources().getStringArray(R.array.settings_display_rows)[which];
                            preference.setSummary(newSummary);
                            AppSettings.put(getActivity(), LotteryLover.KEY_DISPLAY_ROWS, which);
                            if (getActivity() instanceof Callback) {
                                ((Callback) getActivity()).onItemChanged(LotteryLover.KEY_DISPLAY_ROWS);
                            }
                            Bundle data = new Bundle();
                            data.putString(Analytics.KEY_SETTINGS_NAME, "Display rows");
                            data.putString(Analytics.KEY_SETTINGS_VALUE, newSummary);
                            AnalyticsHelper.getHelper(getActivity()).logEvent(Analytics.EVENT_SETTINGS, data);
                            AnalyticsHelper.getHelper(getActivity()).logEvent(Analytics.EVENT_SETTINGS, "Display rows", newSummary);
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
                Bundle data = new Bundle();
                data.putString(Analytics.KEY_SETTINGS_NAME, "Contact me");
                AnalyticsHelper.getHelper(getActivity()).logEvent(Analytics.EVENT_SETTINGS, data);
                AnalyticsHelper.getHelper(getActivity()).logEvent(Analytics.EVENT_SETTINGS, "Contact me", null);
                return true;
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "unexpected error", Toast.LENGTH_LONG).show();
            }
        } else if (SETTINGS_DISPLAY_COMBINE_SPECIAL_NUMBER.equals(key)) {
            AppSettings.put(getActivity(), LotteryLover.KEY_COMBINE_SPECIAL, ((CheckBoxPreference) preference).isChecked());
            if (getActivity() instanceof Callback) {
                ((Callback) getActivity()).onItemChanged(LotteryLover.KEY_COMBINE_SPECIAL);
            }
        } else if (SETTINGS_OTHERS_LTO_LIST.equals(key)) {
            Intent intent = new Intent(getActivity(), LtoTypeSettingActivity.class);
            startActivity(intent);
        } else if (SETTINGS_DISPLAY_SCREEN_ORIENTATION.equals(key)) {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.settings_display_orientation_title)
                    .setItems(R.array.settings_display_orientation_list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            preference.setSummary(getResources().getStringArray(R.array.settings_display_orientation_list)[which]);
                            AppSettings.put(getActivity(), LotteryLover.KEY_DISPLAY_ORIENTATION, which);
                            if (getActivity() instanceof Callback) {
                                ((Callback) getActivity()).onItemChanged(LotteryLover.KEY_DISPLAY_ORIENTATION);
                            }
                            Utilities.setActivityOrientation(getActivity());
                        }
                    }).show();
            return true;
        } else if (SETTINGS_OTHER_UPDATE_PERIOD.equals(key)) {
            new AlertDialog.Builder(getActivity()).setTitle(R.string.settings_others_category_title)
                    .setItems(R.array.settings_other_update_period, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            preference.setSummary(getResources().getStringArray(R.array.settings_other_update_period)[which]);
                            AppSettings.put(getActivity(), LotteryLover.KEY_UPDATE_PERIOD, which);
                        }
                    }).show();
            return true;
        } else if (SETTINGS_OTHER_UPDATE_RECORD.equals(key)) {
            Cursor itemCursor = getActivity().getContentResolver().query(UpdateLogger.URI, null, null, null, null);
            if (itemCursor == null) return true;
            CharSequence[] items = null;
            try {
                items = new CharSequence[itemCursor.getCount()];
                int index = 0;
                final int indexOfTime = itemCursor.getColumnIndex(UpdateLogger.COLUMN_TIME);
                final int indexOfReason = itemCursor.getColumnIndex(UpdateLogger.COLUMN_REASON);
                while (itemCursor.moveToNext()) {
                    String time = new SimpleDateFormat("MM/dd HH:mm").format(new Date(itemCursor.getLong(indexOfTime)));
                    String reason = itemCursor.getString(indexOfReason);
                    items[index] = time + ": " + reason;
                    ++index;
                }
            } finally {
                itemCursor.close();
            }
            if (items == null) {
                items = new CharSequence[]{""};
            }
            new AlertDialog.Builder(getActivity()).setTitle(R.string.settings_other_update_record)
                    .setItems(items, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public interface Callback {
        void onItemChanged(String itemKey);
    }
}
