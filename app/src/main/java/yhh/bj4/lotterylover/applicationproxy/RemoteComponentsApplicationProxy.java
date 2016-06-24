package yhh.bj4.lotterylover.applicationproxy;

import android.content.Context;

import yhh.bj4.lotterylover.LotteryLover;
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
        Utilities.updateAllLtoData(getContext(), "app start");
    }

    @Override
    public void onTerminate() {

    }
}
