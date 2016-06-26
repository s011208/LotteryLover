package yhh.bj4.lotterylover.views.table.main;

import android.os.AsyncTask;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
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
    private final ArrayList<LotteryItem> mLotteryItems = new ArrayList<>();
    private final ArrayList<MainTableItem> mResults = new ArrayList<>();
    private final int mValue, mQueryOrder;
    private final Callback mCallback;
    private final boolean mCombineSpecialNumber;

    public UpdatePlusAndMinusHelper(ArrayList<LotteryItem> items, int value, boolean combineSpecialNumber, ArrayList<MainTableItem> data, int order,
                                    Callback cb) {
        mLotteryItems.addAll(items);
        mValue = value;
        mResults.addAll(data);
        mCallback = cb;
        mQueryOrder = order;
        mCombineSpecialNumber = combineSpecialNumber;
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
                    if (counter == 0) {
                        ++counter;
                        continue;
                    }
                    // compare with previous
                    compareItem = mLotteryItems.get(counter - 1);
                }
                if (compareItem == null) continue;
                tempNormalCompareItem.addAll(compareItem.getNormalNumbers());
                if (parameters[3] == -1) {
                    if (mCombineSpecialNumber) {
                        tempNormalCompareItem.addAll(compareItem.getSpecialNumbers());
                        List<Integer> combinedList = new ArrayList<>();
                        combinedList.addAll(currentItem.getNormalNumbers());
                        combinedList.addAll(currentItem.getSpecialNumbers());
                        Collections.sort(combinedList);
                        for (int i = 0; i < combinedList.size(); ++i) {
                            int normalNumber = combinedList.get(i);
                            final int v = (normalNumber + mValue) % parameters[2];
                            if (tempNormalCompareItem.contains(v)) {
                                item.addHitIndexOfNormal(i);
                                Integer indexValue = normalCount.get(i);
                                if (indexValue == null) {
                                    normalCount.put(i, 1);
                                } else {
                                    normalCount.put(i, indexValue + 1);
                                }
                            }
                        }
                    } else {
                        tempNormalCompareItem.addAll(compareItem.getSpecialNumbers());
                        for (int i = 0; i < currentItem.getNormalNumbers().size(); ++i) {
                            int normalNumber = currentItem.getNormalNumbers().get(i);
                            final int v = (normalNumber + mValue) % parameters[2];
                            if (tempNormalCompareItem.contains(v)) {
                                item.addHitIndexOfNormal(i);
                                Integer indexValue = normalCount.get(i);
                                if (indexValue == null) {
                                    normalCount.put(i, 1);
                                } else {
                                    normalCount.put(i, indexValue + 1);
                                }
                            }
                        }
                        for (int i = 0; i < currentItem.getSpecialNumbers().size(); ++i) {
                            int specialNumber = currentItem.getSpecialNumbers().get(i);
                            final int v = (specialNumber + mValue) % parameters[2];
                            if (tempNormalCompareItem.contains(v)) {
                                item.addHitIndexOfSpecial(i);
                                Integer indexValue = specialCount.get(i);
                                if (indexValue == null) {
                                    specialCount.put(i, 1);
                                } else {
                                    specialCount.put(i, indexValue + 1);
                                }
                            }
                        }
                    }
                } else {
                    tempSpecialCompareItem.addAll(compareItem.getSpecialNumbers());
                    for (int i = 0; i < currentItem.getNormalNumbers().size(); ++i) {
                        int normalNumber = currentItem.getNormalNumbers().get(i);
                        final int v = (normalNumber + mValue) % parameters[2];
                        if (tempNormalCompareItem.contains(v)) {
                            item.addHitIndexOfNormal(i);
                            Integer indexValue = normalCount.get(i);
                            if (indexValue == null) {
                                normalCount.put(i, 1);
                            } else {
                                normalCount.put(i, indexValue + 1);
                            }
                        }
                    }
                    for (int i = 0; i < currentItem.getSpecialNumbers().size(); ++i) {
                        int specialNumber = currentItem.getSpecialNumbers().get(i);
                        final int v = (specialNumber + mValue) % parameters[2];
                        if (tempSpecialCompareItem.contains(v)) {
                            item.addHitIndexOfSpecial(i);
                            Integer indexValue = specialCount.get(i);
                            if (indexValue == null) {
                                specialCount.put(i, 1);
                            } else {
                                specialCount.put(i, indexValue + 1);
                            }
                        }
                    }
                }
                ++counter;
            } else {
                // collect data for monthly data
                if (parameters[3] == -1 && mCombineSpecialNumber) {
                    mainTableItem.clearNormalNumber();
                    mainTableItem.clearSpecialNumber();
                    for (int i = 0; i < parameters[0] + parameters[1]; ++i) {
                        mainTableItem.addNormalNumber(i, normalCount.get(i) == null ? 0 : normalCount.get(i));
                    }
                } else {
                    for (int i = 0; i < parameters[0]; ++i) {
                        mainTableItem.setNormalNumber(i, normalCount.get(i) == null ? 0 : normalCount.get(i));
                    }

                    for (int i = 0; i < parameters[1]; ++i) {
                        mainTableItem.setSpecialNumber(i, specialCount.get(i) == null ? 0 : specialCount.get(i));
                    }
                }
                // reset
                normalCount.clear();
                specialCount.clear();
            }
            tempNormalCompareItem.clear();
            tempSpecialCompareItem.clear();
            item.clearCacheAndMakeNewSpannableString();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallback == null) return;
        mCallback.onFinished();
    }

    public interface Callback {
        void onFinished();
    }
}
