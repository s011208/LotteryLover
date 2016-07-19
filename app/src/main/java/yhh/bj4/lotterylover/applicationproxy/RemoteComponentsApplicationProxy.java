package yhh.bj4.lotterylover.applicationproxy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.services.RetrieveDataService;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class RemoteComponentsApplicationProxy extends ApplicationProxy {
    public RemoteComponentsApplicationProxy(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        RetrieveDataService.startService(getContext(), "Application start");
    }

    @Override
    public void onTerminate() {

    }
}
