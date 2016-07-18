package yhh.bj4.lotterylover.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.helpers.RetrieveLotteryItemDataHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.parser.LotteryParser;
import yhh.bj4.lotterylover.provider.AppSettings;
import yhh.bj4.lotterylover.services.retrievedata.InitLtoDataTask;

/**
 * Created by yenhsunhuang on 2016/6/23.
 */
public class RetrieveDataService extends Service {
    private static final String TAG = "RetrieveDataService";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final String INTENT_REASON = "intent_reason";
    public static final String INTENT_REQUEST_LTO_TYPE = "rlt";

    private HandlerThread mHandlerThread = new HandlerThread("RetrieveDataService");

    {
        mHandlerThread.start();
    }

    private Handler mHandler = new Handler(mHandlerThread.getLooper());

    private final Map<Integer, InitLtoDataTask> mRunnableTaskMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        checkAndInitLtoData();

        updateRegularly();
    }

    private void updateRegularly() {
        ConnectivityManager cm =
                (ConnectivityManager) RetrieveDataService.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utilities.updateAllLtoData(RetrieveDataService.this, "regularly update");
                updateRegularly();
            }
        }, activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ? Utilities.HOUR : Utilities.HOUR * 3);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getStringExtra(INTENT_REASON) != null) {
                Log.d(TAG, "onStartCommand reason: " + intent.getStringExtra(INTENT_REASON));
            }
            if (intent.getIntExtra(INTENT_REQUEST_LTO_TYPE, -1) != -1) {
                Log.d(TAG, "onStartCommand request to update lto: " + intent.getIntExtra(INTENT_REQUEST_LTO_TYPE, -1));
                handleLtoUpdate(intent.getIntExtra(INTENT_REQUEST_LTO_TYPE, -1), 0);
            }
        } else {
            Log.d(TAG, "unknown reason");
        }
        return Service.START_STICKY_COMPATIBILITY;
    }

    private void handleLtoUpdate(final int requestLtoType, final int queryPage) {
        if (requestLtoType == -1) return;
        Cursor dataCursor = RetrieveLotteryItemDataHelper.getDataCursor(RetrieveDataService.this, requestLtoType);
        boolean isDataCursorEmpty = false;
        if (dataCursor != null) {
            isDataCursorEmpty = dataCursor.getCount() == 0;
            dataCursor.close();
            Log.w(TAG, "isDataCursorEmpty: " + isDataCursorEmpty);
        }

        if (isDataCursorEmpty) {
            AppSettings.put(RetrieveDataService.this, LotteryLover.KEY_LTO_UPDATE_TIME(
                    LotteryItem.getSimpleClassName(requestLtoType)), 0l);

        }

        if (!isExpired(requestLtoType) && !isDataCursorEmpty) {
            Log.w(TAG, "requestLtoType: " + requestLtoType + ", not expired");
            return;
        }
        Log.d(TAG, "handleLtoUpdate, lto: " + requestLtoType + ", page: " + queryPage);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                LotteryParser.getParserFromType(RetrieveDataService.this, requestLtoType, queryPage, new LotteryParser.Callback() {
                    @Override
                    public void onStart(int page) {

                    }

                    @Override
                    public void onFinish(int page, int[] results) {
                        if (results[0] == Activity.RESULT_CANCELED) {
                            AppSettings.put(RetrieveDataService.this, LotteryLover.KEY_LTO_UPDATE_TIME(
                                    LotteryItem.getSimpleClassName(requestLtoType)), Calendar.getInstance().getTimeInMillis());
                            return;
                        }
                        // check data integrity
                        if (requestLtoType == LotteryLover.LTO_TYPE_LTO_LIST3 ||
                                requestLtoType == LotteryLover.LTO_TYPE_LTO_LIST4) {
                            Cursor data = getContentResolver().query(LotteryItem.getLtoTypeUri(requestLtoType), new String[]{
                                    LotteryItem.COLUMN_SEQUENCE
                            }, null, null, LotteryItem.COLUMN_SEQUENCE + " asc limit 1");
                            if (data == null) return;
                            try {
                                data.moveToNext();
                                if (requestLtoType == LotteryLover.LTO_TYPE_LTO_LIST3) {
                                    if (data.getLong(0) == 1134921600000l) return;
                                } else if (requestLtoType == LotteryLover.LTO_TYPE_LTO_LIST4) {
                                    if (data.getLong(0) == 1049644800000l) return;
                                }
                                handleLtoUpdate(requestLtoType, page + 1);
                            } finally {
                                data.close();
                            }
                        } else {
                            Cursor data = getContentResolver().query(LotteryItem.getLtoTypeUri(requestLtoType), new String[]{
                                    LotteryItem.COLUMN_SEQUENCE
                            }, null, null, LotteryItem.COLUMN_SEQUENCE + " asc");
                            if (data == null) return;
                            try {
                                long previous = 0;
                                while (data.moveToNext()) {
                                    long seq = data.getLong(0);
                                    if (previous == 0) {
                                        if (seq != 1) {
                                            handleLtoUpdate(requestLtoType, page + 1);
                                            return;
                                        }
                                        previous = seq;
                                        continue;
                                    }
                                    if (seq - previous != 1) {
                                        if (isWorkAround(requestLtoType, seq, previous)) {
                                            previous = seq;
                                            continue;
                                        }
                                        handleLtoUpdate(requestLtoType, page + 1);
                                        return;
                                    }
                                    previous = seq;
                                }
                            } finally {
                                data.close();
                            }
                        }
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    private boolean isExpired(int type) {
        long lastUpdateTime = AppSettings.get(RetrieveDataService.this, LotteryLover.KEY_LTO_UPDATE_TIME(LotteryItem.getSimpleClassName(type)), 0l);
        Calendar now = Calendar.getInstance();
        return Math.abs(lastUpdateTime - now.getTimeInMillis()) >= Utilities.HOUR * 2;
    }

    private boolean isWorkAround(int type, long seq, long pre) {
        // lto workaround
        if (type == LotteryLover.LTO_TYPE_LTO) {
            if (seq == 155 && pre == 144) return true;
            if (seq == 445 && pre == 443) return true;
        }
        return false;
    }

    private void checkAndInitLtoData() {
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
                if (needToInit(LotteryLover.LTO_TYPE_LTO_539)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_539);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_POW)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_POW);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_MM)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_MM);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_J6)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_J6);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_TOTO)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_TOTO);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_AU_POW)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_AU_POW);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_EM)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_EM);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_LIST3)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_LIST3);
                }
                if (needToInit(LotteryLover.LTO_TYPE_LTO_LIST4)) {
                    updateLtoList.add(LotteryLover.LTO_TYPE_LTO_LIST4);
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
            try {
                if (data.getCount() == 0) {
                    AppSettings.put(RetrieveDataService.this, LotteryLover.KEY_LTO_UPDATE_TIME(
                            LotteryItem.getSimpleClassName(type)), 0l);
                    return true;
                }
            } finally {
                data.close();
            }
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

    public static void startServiceAndUpdate(Context context, int requestLtoType) {
        Intent startIntent = new Intent(context, RetrieveDataService.class);
        startIntent.putExtra(RetrieveDataService.INTENT_REQUEST_LTO_TYPE, requestLtoType);
        context.startService(startIntent);
    }

    public static void startService(Context context, String reason) {
        Intent startIntent = new Intent(context, RetrieveDataService.class);
        startIntent.putExtra(RetrieveDataService.INTENT_REASON, reason);
        context.startService(startIntent);
    }
}
