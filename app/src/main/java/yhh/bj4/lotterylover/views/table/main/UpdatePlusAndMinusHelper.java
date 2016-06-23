package yhh.bj4.lotterylover.views.table.main;

import android.os.AsyncTask;
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
        for (MainTableItem mainTableItem : mResults) {
            TypePlusAndMinus item = (TypePlusAndMinus) mainTableItem;
            LotteryItem currentItem, compareItem;
            if (mainTableItem.getItemType() == MainTableItem.ITEM_TYPE_CONTENT) {
                item.cleanHitResult();
                currentItem = mLotteryItems.get(counter);
                if (mQueryOrder == LotteryLover.ORDER_BY_ASC) {
                    if (counter >= mLotteryItems.size() - 1) continue;
                    // compare with next
                    compareItem = mLotteryItems.get(counter + 1);
                } else {
                    if (counter == 0) continue;
                    // compare with previous
                    compareItem = mLotteryItems.get(counter - 1);
                }
                if (compareItem == null) continue;
                tempNormalCompareItem.addAll(compareItem.getNormalNumbers());
                if (parameters[3] == -1) {
                    tempNormalCompareItem.addAll(compareItem.getSpecialNumbers());
                    for (int i = 0; i < currentItem.getNormalNumbers().size(); ++i) {
                        int normalNumber = currentItem.getNormalNumbers().get(i);
                        final int v = (normalNumber + mValue) % parameters[2];
                        if (tempNormalCompareItem.contains(v)) {
                            item.addHitIndexOfNormal(i);
                        }
                    }
                    for (int i = 0; i < currentItem.getSpecialNumbers().size(); ++i) {
                        int specialNumber = currentItem.getSpecialNumbers().get(i);
                        final int v = (specialNumber + mValue) % parameters[2];
                        if (tempNormalCompareItem.contains(v)) {
                            item.addHitIndexOfSpecial(i);
                        }
                    }
                } else {
                    tempSpecialCompareItem.addAll(compareItem.getSpecialNumbers());
                    for (int i = 0; i < currentItem.getNormalNumbers().size(); ++i) {
                        int normalNumber = currentItem.getNormalNumbers().get(i);
                        final int v = (normalNumber + mValue) % parameters[2];
                        if (tempNormalCompareItem.contains(v)) {
                            item.addHitIndexOfNormal(i);
                        }
                    }
                    for (int i = 0; i < currentItem.getSpecialNumbers().size(); ++i) {
                        int specialNumber = currentItem.getSpecialNumbers().get(i);
                        final int v = (specialNumber + mValue) % parameters[2];
                        if (tempSpecialCompareItem.contains(v)) {
                            item.addHitIndexOfSpecial(i);
                        }
                    }
                }
                ++counter;
            } else {
                // collect data for monthly data

                // reset
                normalCount.clear();
                specialCount.clear();
            }
            tempNormalCompareItem.clear();
            tempSpecialCompareItem.clear();
            compareItem = null;
            currentItem = null;
            item.clearCacheAndMakeNewSpannableString();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallback == null) return;
        mCallback.onFinished();
    }
}