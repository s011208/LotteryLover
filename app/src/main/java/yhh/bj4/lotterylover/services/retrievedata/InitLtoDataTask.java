package yhh.bj4.lotterylover.services.retrievedata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;
import yhh.bj4.lotterylover.provider.AppSettings;

/**
 * init from firebase
 * Created by yenhsunhuang on 2016/6/23.
 */
public class InitLtoDataTask implements Runnable {
    private static final String TAG = "InitLtoDataTask";
    private static final boolean DEBUG = Utilities.DEBUG;

    private final int mLtoType;

    private final Uri mProviderUri;

    private Context mContext;

    public InitLtoDataTask(int ltoType, Context context) {
        mLtoType = ltoType;
        mProviderUri = LotteryItem.getLtoTypeUri(mLtoType);
        mContext = context;
    }

    @Override
    public void run() {
        if (DEBUG) {
            Log.i(TAG, "InitLtoDataTask with type: " + LotteryItem.getSimpleClassName(mLtoType)
                    + ", pid: " + Process.myPid() + ", tid: " + Process.myTid());
        }
        Context context = mContext;
        if (context == null) return;
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
                .child(LotteryItem.getSimpleClassName(mLtoType)).orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (DEBUG) {
                    Log.i(TAG, "onDataChange");
                }
                final List<Map<String, String>> values = new ArrayList<>();
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    values.add((Map<String, String>) s.getValue());
                }
                if (values != null && providerDataCount < values.size()) {
                    // sync from firebase directly
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            writeFirebaseIntoProvider(values);
                        }
                    }).start();
                }

                FirebaseDatabaseHelper.getFirebaseDatabase().getReference().child(FirebaseDatabaseHelper.CHILD_LOTTERY_DATA)
                        .child(LotteryItem.getSimpleClassName(mLtoType)).removeEventListener(this);

                Context context = mContext;
                if (context == null) return;
                String key = null;
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
                FirebaseDatabaseHelper.getFirebaseDatabase().getReference().child(FirebaseDatabaseHelper.CHILD_LOTTERY_DATA)
                        .child(LotteryItem.getSimpleClassName(mLtoType)).removeEventListener(this);
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
            }
        }
        ContentValues[] cvs = new ContentValues[providerItemList.size()];
        for (int i = 0; i < cvs.length; ++i) {
            cvs[i] = providerItemList.get(i).toContentValues();
        }
        int result = context.getContentResolver().bulkInsert(mProviderUri, cvs);
        if (DEBUG)
            Log.d(TAG, "insert " + LotteryItem.getSimpleClassName(mLtoType) + " count from firebase: " + result);
    }

    public void release() {
        mContext = null;
    }
}
