package yhh.bj4.lotterylover.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class RetrieveDataService extends Service {
    private static final String TAG = "RetrieveDataService";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final String INTENT_REASON = "intent_reason";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static final void startService(Context context, String reason) {
        Intent startIntent = new Intent(context, RetrieveDataService.class);
        startIntent.putExtra(RetrieveDataService.INTENT_REASON, reason);
        context.startService(startIntent);
    }
}
