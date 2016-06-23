package yhh.bj4.lotterylover.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.services.retrievedata.InitLtoDataTask;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class RetrieveDataService extends Service {
    private static final String TAG = "RetrieveDataService";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final String INTENT_REASON = "intent_reason";

    private HandlerThread mHandlerThread = new HandlerThread("RetrieveDataService");

    {
        mHandlerThread.start();
    }

    private Handler mHandler = new Handler(mHandlerThread.getLooper());

    private final Map<Integer, InitLtoDataTask> mRunnableTaskMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) {
            if (intent != null && intent.getStringExtra(INTENT_REASON) != null) {
                checkAndStartQuery();
                Log.d(TAG, "reason: " + intent.getStringExtra(INTENT_REASON));
            } else {
                Log.d(TAG, "unknown reason");
            }
        }
        return Service.START_STICKY_COMPATIBILITY;
    }

    private void checkAndStartQuery() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Integer> updateLtoList = new ArrayList<>();
                if (needToInit(LotteryLover.LTO_TYPE_LTO)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO2C)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO2C);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO7C)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO7C);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_BIG)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_BIG);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_DOF)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_DOF);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_HK)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_HK);
                }
                for (Integer ltoType : updateLtoList) {
                    mHandler.post(new InitLtoDataTask(ltoType, RetrieveDataService.this));
                }
            }
        });
    }

    private boolean needToInit(int type) {
        Cursor data;
        data = getContentResolver().query(LotteryItem.getLtoTypeUri(type), null, null, null, null);
        if (data != null) {
            if (data.getCount() == 0) {
                return true;
            }
            data.close();
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        releaseRunnableTask();
        super.onDestroy();
    }

    private void releaseRunnableTask() {
        Iterator<Integer> keyIterator = mRunnableTaskMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            Integer key = keyIterator.next();
            if (mRunnableTaskMap.get(key) != null) {
                mRunnableTaskMap.get(key).release();
                mRunnableTaskMap.remove(key);
            }
        }
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
