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
public class RetrieveDataService extends Service implements InitLtoDataTask.Callback {
    private static final String TAG = "RetrieveDataService";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final String INTENT_REASON = "intent_reason";
    public static final String INTENT_REQUEST_LTO_TYPE = "rlt";
    public static final String INTENT_FORCE_RELOAD = "force_reload";

    private HandlerThread mHandlerThread = new HandlerThread("RetrieveDataService");

    {
        mHandlerThread.start();
    }

    private Handler mHandler = new Handler(mHandlerThread.getLooper());

    private final Map<Integer, InitLtoDataTask> mRunnableTaskMap = new HashMap<>();

    private String mUpdateReason = "service start";

    @Override
    public void onCreate() {
        super.onCreate();
        checkAndInitLtoData();
    }

    private void updateRegularly(final String reason) {
        Utilities.updateAllLtoData(RetrieveDataService.this, reason);
        long postDelayed;
        final int updatePeriodType = AppSettings.get(RetrieveDataService.this, LotteryLover.KEY_UPDATE_PERIOD, LotteryLover.KEY_UPDATE_PERIOD_DEFAULT);
        switch (updatePeriodType) {
            case LotteryLover.KEY_UPDATE_PERIOD_DEFAULT:
                ConnectivityManager cm =
                        (ConnectivityManager) RetrieveDataService.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                postDelayed = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ? Utilities.HOUR : Utilities.HOUR * 3;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_HOUR:
                postDelayed = Utilities.HOUR;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_2_HOUR:
                postDelayed = Utilities.HOUR * 2;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_3_HOUR:
                postDelayed = Utilities.HOUR * 3;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_6_HOUR:
                postDelayed = Utilities.HOUR * 6;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_12_HOUR:
                postDelayed = Utilities.HOUR * 12;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_1_DAY:
                postDelayed = Utilities.HOUR * 24;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_NEVER:
                return;
            default:
                throw new RuntimeException("unexpected selection");
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateRegularly("regularly update");
            }
        }, postDelayed);
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
            if (intent.getStringExtra(INTENT_FORCE_RELOAD) != null) {
                mHandler.removeCallbacks(null);
                mUpdateReason = "force reload";
                Utilities.clearAllLtoTables(RetrieveDataService.this);
                checkAndInitLtoData();
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
                            Log.i(TAG, "cancel type: " + requestLtoType);
                            return;
                        }
                        // check data integrity
                        if (requestLtoType == LotteryLover.LTO_TYPE_LTO_LIST3 ||
                                requestLtoType == LotteryLover.LTO_TYPE_LTO_LIST4) {
                            Cursor data = getContentResolver().query(LotteryItem.getLtoTypeUri(requestLtoType), new String[]{
                                    LotteryItem.COLUMN_SEQUENCE
                            }, null, null, LotteryItem.COLUMN_DRAWING_DATE_TIME + " asc limit 1");
                            if (data == null) return;
                            try {
                                if (data.getCount() == 0) return;
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
                            }, null, null, LotteryItem.COLUMN_DRAWING_DATE_TIME + " asc");
                            if (data == null) return;
                            try {
                                long previous = 0;
                                while (data.moveToNext()) {
                                    long seq = data.getLong(0);
                                    if (previous == 0) {
                                        if (seq > 1) {
                                            Log.w(TAG, "1 seq: " + seq + ", previous: " + previous + ", requestLtoType: " + requestLtoType);
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
                                        Log.w(TAG, "2 seq: " + seq + ", previous: " + previous + ", requestLtoType: " + requestLtoType);
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
                }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        });
    }

    private boolean isExpired(int type) {
        long lastUpdateTime = AppSettings.get(RetrieveDataService.this, LotteryLover.KEY_LTO_UPDATE_TIME(LotteryItem.getSimpleClassName(type)), 0l);
        Calendar now = Calendar.getInstance();
        final int updatePeriodType = AppSettings.get(RetrieveDataService.this, LotteryLover.KEY_UPDATE_PERIOD, LotteryLover.KEY_UPDATE_PERIOD_DEFAULT);
        long expiredTime;
        switch (updatePeriodType) {
            case LotteryLover.KEY_UPDATE_PERIOD_DEFAULT:
                ConnectivityManager cm =
                        (ConnectivityManager) RetrieveDataService.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                expiredTime = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ? Utilities.HOUR : Utilities.HOUR * 3;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_HOUR:
                expiredTime = Utilities.HOUR;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_2_HOUR:
                expiredTime = Utilities.HOUR * 2;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_3_HOUR:
                expiredTime = Utilities.HOUR * 3;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_6_HOUR:
                expiredTime = Utilities.HOUR * 6;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_12_HOUR:
                expiredTime = Utilities.HOUR * 12;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_1_DAY:
                expiredTime = Utilities.HOUR * 24;
                break;
            case LotteryLover.KEY_UPDATE_PERIOD_NEVER:
                expiredTime = Utilities.HOUR * 24;
                break;
            default:
                throw new RuntimeException("unexpected selection");
        }
        expiredTime -= Utilities.MINUTE * 5;
        return Math.abs(lastUpdateTime - now.getTimeInMillis()) >= expiredTime;
    }

    private boolean isWorkAround(int type, long seq, long pre) {
        // lto workaround
        if (type == LotteryLover.LTO_TYPE_LTO) {
            if (seq == 155 && pre == 144) return true;
            if (seq == 445 && pre == 443) return true;
        }
        if (type == LotteryLover.LTO_TYPE_LTO_POW) {
            if (seq == 68 && pre == 66) return true;
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
                if (!AppSettings.get(RetrieveDataService.this, LotteryLover.KEY_SYNC_FROM_FIREBASE, true)) {
                    updateLtoList.clear();
                }
                if (updateLtoList.isEmpty()) {
                    updateRegularly(mUpdateReason);
                } else {
                    for (Integer ltoType : updateLtoList) {
                        mHandler.post(new InitLtoDataTask(ltoType, RetrieveDataService.this, RetrieveDataService.this));
                    }
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

    public static void startServiceAndForceReload(Context context) {
        Intent startIntent = new Intent(context, RetrieveDataService.class);
        startIntent.putExtra(RetrieveDataService.INTENT_FORCE_RELOAD, "force_reload");
        context.startService(startIntent);
    }

    @Override
    public void onDataChangeFinish(int ltoType) {
        handleLtoUpdate(ltoType, 0);
    }
}
