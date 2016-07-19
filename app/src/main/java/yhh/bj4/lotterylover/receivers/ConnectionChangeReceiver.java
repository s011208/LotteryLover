package yhh.bj4.lotterylover.receivers;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;

import yhh.bj4.lotterylover.services.RetrieveDataService;
import yhh.bj4.lotterylover.services.UpdateLogger;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        if (isConnected) {
            RetrieveDataService.startService(context, "ConnectionChangeReceiver");
            ContentValues cv = new ContentValues();
            cv.put(UpdateLogger.COLUMN_TIME, Calendar.getInstance().getTimeInMillis());
            cv.put(UpdateLogger.COLUMN_REASON, "ConnectionChangeReceiver");
            cv.put(UpdateLogger.COLUMN_TYPE, UpdateLogger.TYPE_INFO);
            context.getContentResolver().insert(UpdateLogger.URI, cv);
        }
    }
}
