package yhh.bj4.lotterylover;

import android.app.Application;

import yhh.bj4.lotterylover.services.RetrieveDataService;

/**
 * Created by User on 2016/6/15.
 */
public class LotteryLoverApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrieveDataService.startService(this, "Application start");
    }
}
