package yhh.bj4.lotterylover.views.table.main;

import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.views.table.main.item.MainTableItem;
import yhh.bj4.lotterylover.views.table.main.item.TypePlusAndMinus;

/**
 * Created by yenhsunhuang on 2016/6/22.
 */
public class UpdatePlusAndMinusHelper extends AsyncTask<Void, Void, Void> {
    private static final boolean DEBUG = Utilities.DEBUG;
    private static final String TAG = "PlusAndMinusHelper";

    public interface Callback {
        void onFinished();
    }

    private final ArrayList<LotteryItem> mLotteryItems = new ArrayList<>();
    private final ArrayList<MainTableItem> mResults = new ArrayList<>();
    private final int mValue, mQueryOrder;
    private final Callback mCallback;

    public UpdatePlusAndMinusHelper(ArrayList<LotteryItem> items, int value, ArrayList<MainTableItem> data, int order,
                                    Callback cb) {
        mLotteryItems.addAll(items);
        mValue = value;
        mResults.addAll(data);
        mCallback = cb;
        mQueryOrder = order;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mLotteryItems.isEmpty() || mResults.isEmpty()) return null;
        int counter = 0;
        SparseArray<Integer> normalCount = new SparseArray<>();
        SparseArray<Integer> specialCount = new SparseArray<>();
        List<Integer> tempNormalCompareItem = new ArrayList<>();
        List<Integer> tempSpecialCompareItem = new ArrayList<>();
        int[] parameters = MainTableItem.initParameters(mLotteryItems.get(0));
        if (DEBUG)
            Log.d(TAG, "mResults size: " + mResults.size() + ", mLotteryItems size: " + mLotteryItems.size());
        for (MainTableItem mainTableItem : mResults) {
            if (DEBUG) {
                Log.d(TAG, "mainTableItem.getItemType(): " + mainTableItem.getItemType());
            }
            TypePlusAndMinus item = (TypePlusAndMinus) mainTableItem;
            if (mainTableItem.getItemType() == MainTableItem.ITEM_TYPE_CONTENT) {
                LotteryItem currentItem = mLotteryItems.get(counter++);
                if (mQueryOrder == LotteryLover.ORDER_BY_ASC) {
                    if (counter >= mLotteryItems.size() - 1) continue;
                    // compare with next
                    LotteryItem compareItem = mLotteryItems.get(counter + 1);
                    tempNormalCompareItem.addAll(compareItem.getNormalNumbers());
                    if (parameters[3] == -1) {
                        tempNormalCompareItem.addAll(compareItem.getSpecialNumbers());
                    } else {
                        tempSpecialCompareItem.addAll(compareItem.getSpecialNumbers());
                    }
                    for (int i = 0; i < currentItem.getNormalNumbers().size(); ++i) {
                        int v = currentItem.getNormalNumbers().get(i);
                        if (tempNormalCompareItem.contains((v + mValue) % parameters[2])) {
                            item.addHitIndexOfNormal(i);
                        }
                    }

                } else {
                    if (counter == 0) continue;
                    // compare with previous
                    LotteryItem compareItem = mLotteryItems.get(counter - 1);
                }
            } else {
                // collect data for monthly data

                // reset
                normalCount.clear();
                specialCount.clear();
            }
            tempNormalCompareItem.clear();
            tempSpecialCompareItem.clear();
            ((TypePlusAndMinus) mainTableItem).clearCacheAndMakeNewSpannableString();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallback == null) return;
        mCallback.onFinished();
    }
}
