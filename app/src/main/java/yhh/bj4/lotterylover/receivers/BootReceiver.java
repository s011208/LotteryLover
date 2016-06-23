package yhh.bj4.lotterylover.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import yhh.bj4.lotterylover.services.RetrieveDataService;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RetrieveDataService.startService(context, "BootReceiver");
    }
}
