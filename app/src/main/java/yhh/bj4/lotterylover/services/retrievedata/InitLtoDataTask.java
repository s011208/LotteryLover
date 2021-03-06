package yhh.bj4.lotterylover.services.retrievedata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.firebase.FirebaseDatabaseHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.parser.LtoList3.LtoList3;
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto539.Lto539;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltoJ6.LtoJ6;
import yhh.bj4.lotterylover.parser.ltoMM.LtoMM;
import yhh.bj4.lotterylover.parser.ltoToTo.LtoToTo;
import yhh.bj4.lotterylover.parser.ltoapow.LtoAuPow;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;
import yhh.bj4.lotterylover.parser.ltoem.LtoEm;
import yhh.bj4.lotterylover.parser.ltolist4.LtoList4;
import yhh.bj4.lotterylover.parser.ltopow.LtoPow;
import yhh.bj4.lotterylover.provider.AppSettings;

/**
 * init from firebase
 * Created by yenhsunhuang on 2016/6/23.
 */
public class InitLtoDataTask implements Runnable {
    private static final String TAG = "InitLtoDataTask";
    private static final boolean DEBUG = Utilities.DEBUG;

    public interface Callback {
        void onDataChangeFinish(int ltoType);
    }

    private final int mLtoType;

    private final Uri mProviderUri;

    private Context mContext;

    private Callback mCallback;

    public InitLtoDataTask(int ltoType, Context context, Callback cb) {
        mLtoType = ltoType;
        mProviderUri = LotteryItem.getLtoTypeUri(mLtoType);
        mContext = context;
        mCallback = cb;
    }

    @Override
    public void run() {
        if (DEBUG) {
            Log.i(TAG, "InitLtoDataTask with type: " + LotteryItem.getSimpleClassName(mLtoType)
                    + ", pid: " + Process.myPid() + ", tid: " + Process.myTid());
        }
        Context context = mContext;
        if (context == null) {
            mCallback.onDataChangeFinish(mLtoType);
            return;
        }
        // check whether sync old providerData from firebase.
        final int providerDataCount;
        Cursor providerData = context.getContentResolver().query(LotteryItem.getLtoTypeUri(mLtoType), new String[]{LotteryItem.COLUMN_SEQUENCE}, null, null, null);
        if (providerData != null) {
            providerDataCount = providerData.getCount();
            providerData.close();
        } else {
            providerDataCount = 0;
        }
        if (DEBUG) {
            Log.d(TAG, "provider providerData count: " + providerDataCount);
        }
        FirebaseDatabaseHelper.getFirebaseDatabase().getReference().child(FirebaseDatabaseHelper.CHILD_LOTTERY_DATA)
                .child(LotteryItem.getSimpleClassName(mLtoType)).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (DEBUG) {
                    Log.i(TAG, "onDataChange, mLtoType: " + mLtoType);
                }
                final List<Map<String, String>> values = new ArrayList<>();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    values.add((Map<String, String>) s.getValue());
                }
                if (values != null) {
                    if (providerDataCount < values.size()) {
                        // sync from firebase directly
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                writeFirebaseIntoProvider(values);
                                mCallback.onDataChangeFinish(mLtoType);
                                return null;
                            }
                        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    } else {
                        mCallback.onDataChangeFinish(mLtoType);
                    }
                }

                Context context = mContext;
                if (context == null) {
                    mCallback.onDataChangeFinish(mLtoType);
                    return;
                }
                String key;
                switch (mLtoType) {
                    case LotteryLover.LTO_TYPE_LTO:
                        key = LotteryLover.KEY_INIT_LTO;
                        break;
                    case LotteryLover.LTO_TYPE_LTO2C:
                        key = LotteryLover.KEY_INIT_LTO2C;
                        break;
                    case LotteryLover.LTO_TYPE_LTO7C:
                        key = LotteryLover.KEY_INIT_LTO7C;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_BIG:
                        key = LotteryLover.KEY_INIT_LTO_BIG;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_DOF:
                        key = LotteryLover.KEY_INIT_LTO_DOF;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_HK:
                        key = LotteryLover.KEY_INIT_LTO_HK;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_539:
                        key = LotteryLover.KEY_INIT_LTO_539;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_POW:
                        key = LotteryLover.KEY_INIT_LTO_POW;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_MM:
                        key = LotteryLover.KEY_INIT_LTO_MM;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_J6:
                        key = LotteryLover.KEY_INIT_LTO_J6;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_TOTO:
                        key = LotteryLover.KEY_INIT_LTO_TOTO;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_AU_POW:
                        key = LotteryLover.KEY_INIT_LTO_AU_POW;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_EM:
                        key = LotteryLover.KEY_INIT_LTO_EM;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_LIST3:
                        key = LotteryLover.KEY_INIT_LTO_LIST3;
                        break;
                    case LotteryLover.LTO_TYPE_LTO_LIST4:
                        key = LotteryLover.KEY_INIT_LTO_LIST4;
                        break;
                    default:
                        throw new RuntimeException("wrong lto type");
                }
                if (key != null) {
                    AppSettings.put(context, key, true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (DEBUG) {
                    Log.w(TAG, "onCancelled", databaseError.toException());
                }
                mCallback.onDataChangeFinish(mLtoType);
            }
        });
    }

    private void writeFirebaseIntoProvider(List<Map<String, String>> firebaseData) {
        Context context = mContext;
        if (context == null) return;
        List<LotteryItem> providerItemList = new ArrayList<>();
        for (Map<String, String> map : firebaseData) {
            if (map == null) {
                continue;
            }
            switch (mLtoType) {
                case LotteryLover.LTO_TYPE_LTO:
                    providerItemList.add(new Lto(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO2C:
                    providerItemList.add(new Lto2C(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO7C:
                    providerItemList.add(new Lto7C(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_BIG:
                    providerItemList.add(new LtoBig(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_DOF:
                    providerItemList.add(new LtoDof(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_HK:
                    providerItemList.add(new LtoHK(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_539:
                    providerItemList.add(new Lto539(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_POW:
                    providerItemList.add(new LtoPow(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_MM:
                    providerItemList.add(new LtoMM(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_J6:
                    providerItemList.add(new LtoJ6(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_TOTO:
                    providerItemList.add(new LtoToTo(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_AU_POW:
                    providerItemList.add(new LtoAuPow(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_EM:
                    providerItemList.add(new LtoEm(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_LIST3:
                    providerItemList.add(new LtoList3(map));
                    break;
                case LotteryLover.LTO_TYPE_LTO_LIST4:
                    providerItemList.add(new LtoList4(map));
                    break;
                default:
                    throw new RuntimeException("unexpected type");
            }
        }
        ContentValues[] cvs = new ContentValues[providerItemList.size()];
        for (int i = 0; i < cvs.length; ++i) {
            cvs[i] = providerItemList.get(i).toContentValues();
        }
        int result = context.getContentResolver().bulkInsert(mProviderUri, cvs);
        if (DEBUG)
            Log.d(TAG, "insert " + LotteryItem.getSimpleClassName(mLtoType) + " count from firebase: " + result + ", firebaseData size: " + firebaseData.size());
    }

    public void release() {
        mContext = null;
    }
}
