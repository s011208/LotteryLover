package yhh.bj4.lotterylover.views.table.main;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.views.table.main.item.MainTableItem;
import yhh.bj4.lotterylover.views.table.main.item.TypeCombinedList;
import yhh.bj4.lotterylover.views.table.main.item.TypeLastDigit;
import yhh.bj4.lotterylover.views.table.main.item.TypeNumeric;
import yhh.bj4.lotterylover.views.table.main.item.TypeOverall;
import yhh.bj4.lotterylover.views.table.main.item.TypePlusAndMinus;
import yhh.bj4.lotterylover.views.table.main.item.TypePlusTogether;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class AdapterDataGenerator extends AsyncTask<Void, Void, ArrayList<MainTableItem>> {
    private static final String TAG = "AdapterDataGenerator";
    private static final boolean DEBUG = false;
    private static final int TABLE_OFFSET = 1;
    private final int mListType;
    private final ArrayList<LotteryItem> mLotteryData = new ArrayList<>();
    private final Callback mCallback;
    private final int mWindowBackgroundColor;
    private final boolean mCombineSpecialNumber;
    private final boolean mShowSequence;
    private int mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber, mMaximumSpecialNumber;
    public AdapterDataGenerator(int lto, int list, boolean combineSpecialNumber, int color, List<LotteryItem> data, Callback cb, boolean showSequence) {
        mListType = list;
        mLotteryData.addAll(data);
        mCallback = cb;
        mWindowBackgroundColor = color;
        mCombineSpecialNumber = combineSpecialNumber;
        mShowSequence = showSequence;
    }

    @Override
    protected ArrayList<MainTableItem> doInBackground(Void... params) {
        final ArrayList<MainTableItem> rtn = new ArrayList<>();
        if (mLotteryData == null || mLotteryData.isEmpty()) return rtn;
        initParameters();
        final int maximumDigitOfSum = Utilities.getMaximumDigitLengthOfSum(mLotteryData, mMaximumSpecialNumber);

        switch (mListType) {
            case LotteryLover.LIST_TYPE_OVERALL:
                rtn.addAll(initListTypeOverall());
                break;
            case LotteryLover.LIST_TYPE_NUMERIC:
                rtn.addAll(initListTypeNumeric(maximumDigitOfSum));
                break;
            case LotteryLover.LIST_TYPE_PLUS_TOGETHER:
                rtn.addAll(initListTypePlusTogether(maximumDigitOfSum));
                break;
            case LotteryLover.LIST_TYPE_LAST_DIGIT:
                rtn.addAll(initListTypeLastDigit(maximumDigitOfSum));
                break;
            case LotteryLover.LIST_TYPE_PLUS_AND_MINUS:
                rtn.addAll(initListTypePlusAndMinus());
                break;
            case LotteryLover.LIST_TYPE_COMBINE_LIST:
                rtn.addAll(initListTypeCombine());
                break;
        }
        return rtn;
    }

    // for list 4 & list 3 only
    private ArrayList<MainTableItem> initListTypeCombine() {
        ArrayList<MainTableItem> rtn = new ArrayList<>();
        for (LotteryItem item : mLotteryData) {
            MainTableItem mainTableItem = new TypeCombinedList(MainTableAdapter.TYPE_COMBINE_LIST,
                    item.getSequence(), item.getDrawingDateTime(),
                    item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                    , mMaximumSpecialNumber);
            mainTableItem.setWindowBackgroundColor(mWindowBackgroundColor);
            if (item.getNormalNumbers().size() == 3) {
                mainTableItem.addNormalNumber(0, -1);
                for (int i = 0; i < 3; ++i) {
                    mainTableItem.addNormalNumber(i + 1, item.getNormalNumbers().get(i));
                }
            } else {
                for (int i = 0; i < 4; ++i) {
                    mainTableItem.addNormalNumber(i, item.getNormalNumbers().get(i));
                }
            }
            mainTableItem.makeSpannableString();
            mainTableItem.setShowSequence(mShowSequence);
            rtn.add(mainTableItem);
        }
        return rtn;
    }

    private ArrayList<MainTableItem> initListTypePlusAndMinus() {
        ArrayList<MainTableItem> rtn = new ArrayList<>();
        // content data
        Calendar currentItemCalendar = Calendar.getInstance(), previousItemCalendar = null;
        for (LotteryItem item : mLotteryData) {
            currentItemCalendar.setTimeInMillis(item.getDrawingDateTime());
            if (previousItemCalendar != null) {
                if (currentItemCalendar.get(Calendar.MONTH) != previousItemCalendar.get(Calendar.MONTH)) {
                    // add new empty month data
                    MainTableItem monthlyItem = new TypePlusAndMinus(MainTableAdapter.TYPE_PLUS_AND_MINUS,
                            item.getSequence(), previousItemCalendar.getTimeInMillis(),
                            item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                            , mMaximumSpecialNumber);
                    ((TypePlusAndMinus) monthlyItem).setCombineSpecialNumber(mCombineSpecialNumber);
                    monthlyItem.setItemType(MainTableItem.ITEM_TYPE_SUB_TOTAL);
                    monthlyItem.setWindowBackgroundColor(Utilities.getPrimaryLightColor(null));
                    monthlyItem.setShowSequence(mShowSequence);
                    for (int i = 0; i < mNormalNumberCount; ++i) {
                        monthlyItem.addNormalNumber(i, 0);
                    }
                    for (int i = 0; i < mSpecialNumberCount; ++i) {
                        monthlyItem.addSpecialNumber(i, 0);
                    }
                    rtn.add(monthlyItem);
                }
                previousItemCalendar.setTimeInMillis(item.getDrawingDateTime());
            } else {
                previousItemCalendar = Calendar.getInstance();
                previousItemCalendar.setTimeInMillis(item.getDrawingDateTime());
            }
            MainTableItem mainTableItem = new TypePlusAndMinus(MainTableAdapter.TYPE_PLUS_AND_MINUS,
                    item.getSequence(), item.getDrawingDateTime(),
                    item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                    , mMaximumSpecialNumber);
            ((TypePlusAndMinus) mainTableItem).setCombineSpecialNumber(mCombineSpecialNumber);
            mainTableItem.setItemType(MainTableItem.ITEM_TYPE_CONTENT);
            mainTableItem.setShowSequence(mShowSequence);
            for (int i = 0; i < mNormalNumberCount; ++i) {
                mainTableItem.addNormalNumber(i, item.getNormalNumbers().get(i));
            }
            for (int i = 0; i < mSpecialNumberCount; ++i) {
                mainTableItem.addSpecialNumber(i, item.getSpecialNumbers().get(i));
            }
            mainTableItem.makeSpannableString();
            rtn.add(mainTableItem);
        }

        // monthly data

        return rtn;
    }

    private ArrayList<MainTableItem> initListTypeLastDigit(int maximumDigitOfSum) {
        ArrayList<MainTableItem> rtn = new ArrayList<>();
        // add content data
        for (LotteryItem item : mLotteryData) {
            MainTableItem mainTableItem = new TypeLastDigit(MainTableAdapter.TYPE_LAST_DIGIT,
                    item.getSequence(), item.getDrawingDateTime(),
                    item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                    , mMaximumSpecialNumber);
            mainTableItem.setDigitLength(maximumDigitOfSum);
            mainTableItem.setShowSequence(mShowSequence);
            mainTableItem.setWindowBackgroundColor(mWindowBackgroundColor);
            mainTableItem.setItemType(MainTableItem.ITEM_TYPE_CONTENT);
            Map<Integer, Integer> newNormalIndex = Utilities.getLastDigitMap(mMaximumNormalNumber);

            for (int k = TABLE_OFFSET; k < mMaximumNormalNumber + TABLE_OFFSET; ++k) {
                boolean isFind = false;
                int valueOfIndex = newNormalIndex.get(k);
                for (int i = 0; i < mNormalNumberCount; ++i) {
                    if (item.getNormalNumbers().get(i) == valueOfIndex) {
                        isFind = true;
                        // TODO found item can be removed
                        break;
                    }
                }
                if (isFind) {
                    mainTableItem.addNormalNumber(k - TABLE_OFFSET, valueOfIndex);
                } else {
                    mainTableItem.addNormalNumber(k - TABLE_OFFSET, -1);
                }
            }

            if (mMaximumSpecialNumber == -1) {
                for (int i = 0; i < mSpecialNumberCount; ++i) {
                    mainTableItem.addSpecialNumber(i, item.getSpecialNumbers().get(i));
                }
            } else {
                newNormalIndex = Utilities.getLastDigitMap(mMaximumSpecialNumber);
                for (int k = TABLE_OFFSET; k < mMaximumSpecialNumber + TABLE_OFFSET; ++k) {
                    boolean isFind = false;
                    int valueOfIndex = newNormalIndex.get(k);
                    for (int i = 0; i < mSpecialNumberCount; ++i) {
                        if (item.getSpecialNumbers().get(i) == valueOfIndex) {
                            isFind = true;
                            // TODO found item can be removed
                            break;
                        }
                    }
                    if (isFind) {
                        mainTableItem.addSpecialNumber(k - TABLE_OFFSET, valueOfIndex);
                    } else {
                        mainTableItem.addSpecialNumber(k - TABLE_OFFSET, -1);
                    }
                }
            }

            mainTableItem.makeSpannableString();
            rtn.add(mainTableItem);
        }

        // add monthly data
        Calendar previous = null, current = Calendar.getInstance();
        ArrayList<LotteryItem> tempItems = new ArrayList<>();
        int itemAdded = 0;
        for (int i = 0; i < mLotteryData.size(); ++i) {
            LotteryItem item = mLotteryData.get(i);
            current.setTimeInMillis(item.getDrawingDateTime());
            if (previous == null) {
                tempItems.add(item);
            } else {
                if (previous.get(Calendar.MONTH) == current.get(Calendar.MONTH) && i != mLotteryData.size() - 1) {
                    tempItems.add(item);
                } else {
                    LotteryItem tempItem = tempItems.get(tempItems.size() - 1);
                    if (i == mLotteryData.size() - 1) {
                        tempItems.add(item);
                        tempItem = item;
                        itemAdded++; // insert into the last item of list
                    }

                    Pair<ArrayList<Integer>, ArrayList<Integer>> combinedResult = Utilities.collectLotteryItemsData(tempItems);

                    MainTableItem mainTableItem = new TypeLastDigit(MainTableAdapter.TYPE_LAST_DIGIT,
                            tempItem.getSequence(), tempItem.getDrawingDateTime(),
                            tempItem.getMemo(), tempItem.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                            , mMaximumSpecialNumber);
                    mainTableItem.setDigitLength(maximumDigitOfSum);
                    mainTableItem.setShowSequence(mShowSequence);
                    mainTableItem.setWindowBackgroundColor(Utilities.getPrimaryLightColor(null));
                    mainTableItem.setItemType(MainTableItem.ITEM_TYPE_SUB_TOTAL);

                    if (mMaximumSpecialNumber == -1) {
                        // combine results
                        if (DEBUG) {
                            // special & normal list should have the same list size
                            Log.d(TAG, "normal: " + combinedResult.first.size() + ", special: " + combinedResult.second.size());
                        }
                        for (int indexOfList = 0; indexOfList < combinedResult.first.size(); ++indexOfList) {
                            combinedResult.first.set(indexOfList, combinedResult.first.get(indexOfList) + combinedResult.second.get(indexOfList));
                        }
                    }
                    if (DEBUG) {
                        Log.d(TAG, "mMaximumNormalNumber: " + mMaximumNormalNumber + ", combinedResult.first: " + combinedResult.first.size());
                    }
                    List<Integer> tempData = new ArrayList<>();
                    for (int k = 0; k < mMaximumNormalNumber; ++k) {
                        tempData.add(k, combinedResult.first.get(k));
                    }

                    Map<Integer, Integer> tempMap = Utilities.getLastDigitMap(mMaximumNormalNumber);
                    for (int in = 1; in < mMaximumNormalNumber + 1; ++in) {
                        int newKey = in - 1;
                        int oldKey = tempMap.get(in) - 1;
                        mainTableItem.addNormalNumber(newKey, combinedResult.first.get(oldKey));
                    }

                    if (mMaximumSpecialNumber != -1) {
                        tempData.clear();
                        for (int k = 0; k < mMaximumSpecialNumber; ++k) {
                            tempData.add(k, combinedResult.second.get(k));
                        }

                        tempMap = Utilities.getLastDigitMap(mMaximumSpecialNumber);
                        for (int in = 1; in < mMaximumSpecialNumber + 1; ++in) {
                            int newKey = in - 1;
                            int oldKey = tempMap.get(in) - 1;
                            mainTableItem.addSpecialNumber(newKey, combinedResult.second.get(oldKey));
                        }
                    }

                    tempItems.clear();
                    tempItems.add(item);
                    rtn.add(i + itemAdded++, mainTableItem);
                }
            }
            if (previous == null) {
                previous = Calendar.getInstance();
            }
            previous.setTimeInMillis(current.getTimeInMillis());
        }
        return rtn;
    }

    private ArrayList<MainTableItem> initListTypePlusTogether(int maximumDigitOfSum) {
        ArrayList<MainTableItem> rtn = new ArrayList<>();
        // add content data
        for (LotteryItem item : mLotteryData) {
            MainTableItem mainTableItem = new TypePlusTogether(MainTableAdapter.TYPE_PLUS_TOGETHER,
                    item.getSequence(), item.getDrawingDateTime(),
                    item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                    , mMaximumSpecialNumber);
            mainTableItem.setDigitLength(maximumDigitOfSum);
            mainTableItem.setWindowBackgroundColor(mWindowBackgroundColor);
            mainTableItem.setItemType(MainTableItem.ITEM_TYPE_CONTENT);
            mainTableItem.setShowSequence(mShowSequence);
            Map<Integer, Integer> newNormalIndex = Utilities.getPlusAndLastDigitMap(mMaximumNormalNumber);

            for (int k = TABLE_OFFSET; k < mMaximumNormalNumber + TABLE_OFFSET; ++k) {
                boolean isFind = false;
                int valueOfIndex = newNormalIndex.get(k);
                for (int i = 0; i < mNormalNumberCount; ++i) {
                    if (item.getNormalNumbers().get(i) == valueOfIndex) {
                        isFind = true;
                        // TODO found item can be removed
                        break;
                    }
                }
                if (isFind) {
                    mainTableItem.addNormalNumber(k - TABLE_OFFSET, valueOfIndex);
                } else {
                    mainTableItem.addNormalNumber(k - TABLE_OFFSET, -1);
                }
            }

            if (mMaximumSpecialNumber == -1) {
                for (int i = 0; i < mSpecialNumberCount; ++i) {
                    mainTableItem.addSpecialNumber(i, item.getSpecialNumbers().get(i));
                }
            } else {
                newNormalIndex = Utilities.getPlusAndLastDigitMap(mMaximumSpecialNumber);
                for (int k = TABLE_OFFSET; k < mMaximumSpecialNumber + TABLE_OFFSET; ++k) {
                    boolean isFind = false;
                    int valueOfIndex = newNormalIndex.get(k);
                    for (int i = 0; i < mSpecialNumberCount; ++i) {
                        if (item.getSpecialNumbers().get(i) == valueOfIndex) {
                            isFind = true;
                            // TODO found item can be removed
                            break;
                        }
                    }
                    if (isFind) {
                        mainTableItem.addSpecialNumber(k - TABLE_OFFSET, valueOfIndex);
                    } else {
                        mainTableItem.addSpecialNumber(k - TABLE_OFFSET, -1);
                    }
                }
            }

            mainTableItem.makeSpannableString();
            rtn.add(mainTableItem);
        }

        // add monthly data
        Calendar previous = null, current = Calendar.getInstance();
        ArrayList<LotteryItem> tempItems = new ArrayList<>();
        int itemAdded = 0;
        for (int i = 0; i < mLotteryData.size(); ++i) {
            LotteryItem item = mLotteryData.get(i);
            current.setTimeInMillis(item.getDrawingDateTime());
            if (previous == null) {
                tempItems.add(item);
            } else {
                if (previous.get(Calendar.MONTH) == current.get(Calendar.MONTH) && i != mLotteryData.size() - 1) {
                    tempItems.add(item);
                } else {
                    LotteryItem tempItem = tempItems.get(tempItems.size() - 1);
                    if (i == mLotteryData.size() - 1) {
                        tempItems.add(item);
                        tempItem = item;
                        itemAdded++; // insert into the last item of list
                    }

                    Pair<ArrayList<Integer>, ArrayList<Integer>> combinedResult = Utilities.collectLotteryItemsData(tempItems);

                    MainTableItem mainTableItem = new TypePlusTogether(MainTableAdapter.TYPE_PLUS_TOGETHER,
                            tempItem.getSequence(), tempItem.getDrawingDateTime(),
                            tempItem.getMemo(), tempItem.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                            , mMaximumSpecialNumber);
                    mainTableItem.setDigitLength(maximumDigitOfSum);
                    mainTableItem.setShowSequence(mShowSequence);
                    mainTableItem.setWindowBackgroundColor(Utilities.getPrimaryLightColor(null));
                    mainTableItem.setItemType(MainTableItem.ITEM_TYPE_SUB_TOTAL);

                    if (mMaximumSpecialNumber == -1) {
                        // combine results
                        if (DEBUG) {
                            // special & normal list should have the same list size
                            Log.d(TAG, "normal: " + combinedResult.first.size() + ", special: " + combinedResult.second.size());
                        }
                        for (int indexOfList = 0; indexOfList < combinedResult.first.size(); ++indexOfList) {
                            combinedResult.first.set(indexOfList, combinedResult.first.get(indexOfList) + combinedResult.second.get(indexOfList));
                        }
                    }
                    if (DEBUG) {
                        Log.d(TAG, "mMaximumNormalNumber: " + mMaximumNormalNumber + ", combinedResult.first: " + combinedResult.first.size());
                    }
                    List<Integer> tempData = new ArrayList<>();
                    for (int k = 0; k < mMaximumNormalNumber; ++k) {
                        tempData.add(k, combinedResult.first.get(k));
                    }

                    Map<Integer, Integer> tempMap = Utilities.getPlusAndLastDigitMap(mMaximumNormalNumber);
                    for (int in = 1; in < mMaximumNormalNumber + 1; ++in) {
                        int newKey = in - 1;
                        int oldKey = tempMap.get(in) - 1;
                        mainTableItem.addNormalNumber(newKey, combinedResult.first.get(oldKey));
                    }

                    if (mMaximumSpecialNumber != -1) {
                        tempData.clear();
                        for (int k = 0; k < mMaximumSpecialNumber; ++k) {
                            tempData.add(k, combinedResult.second.get(k));
                        }

                        tempMap = Utilities.getPlusAndLastDigitMap(mMaximumSpecialNumber);
                        for (int in = 1; in < mMaximumSpecialNumber + 1; ++in) {
                            int newKey = in - 1;
                            int oldKey = tempMap.get(in) - 1;
                            mainTableItem.addSpecialNumber(newKey, combinedResult.second.get(oldKey));
                        }
                    }

                    tempItems.clear();
                    tempItems.add(item);
                    rtn.add(i + itemAdded++, mainTableItem);
                }
            }
            if (previous == null) {
                previous = Calendar.getInstance();
            }
            previous.setTimeInMillis(current.getTimeInMillis());
        }
        return rtn;
    }

    private ArrayList<MainTableItem> initListTypeNumeric(int maximumDigitOfSum) {
        ArrayList<MainTableItem> rtn = new ArrayList<>();
        // add content data
        for (LotteryItem item : mLotteryData) {
            MainTableItem mainTableItem = new TypeNumeric(MainTableAdapter.TYPE_NUMERIC,
                    item.getSequence(), item.getDrawingDateTime(),
                    item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                    , mMaximumSpecialNumber);
            mainTableItem.setDigitLength(maximumDigitOfSum);
            mainTableItem.setWindowBackgroundColor(mWindowBackgroundColor);
            mainTableItem.setShowSequence(mShowSequence);
            mainTableItem.setItemType(MainTableItem.ITEM_TYPE_CONTENT);
            for (int k = TABLE_OFFSET; k < mMaximumNormalNumber + TABLE_OFFSET; ++k) {
                boolean isFind = false;
                for (int i = 0; i < mNormalNumberCount; ++i) {
                    if (item.getNormalNumbers().get(i) == k) {
                        isFind = true;
                        // TODO found item can be removed
                        break;
                    }
                }
                if (isFind) {
                    mainTableItem.addNormalNumber(k - TABLE_OFFSET, k);
                } else {
                    mainTableItem.addNormalNumber(k - TABLE_OFFSET, -1);
                }
            }

            if (mMaximumSpecialNumber == -1) {
                for (int i = 0; i < mSpecialNumberCount; ++i) {
                    mainTableItem.addSpecialNumber(i, item.getSpecialNumbers().get(i));
                }
            } else {
                for (int k = TABLE_OFFSET; k < mMaximumSpecialNumber + TABLE_OFFSET; ++k) {
                    boolean isFind = false;
                    for (int i = 0; i < mSpecialNumberCount; ++i) {
                        if (item.getSpecialNumbers().get(i) == k) {
                            isFind = true;
                            // TODO found item can be removed
                            break;
                        }
                    }
                    if (isFind) {
                        mainTableItem.addSpecialNumber(k - TABLE_OFFSET, k);
                    } else {
                        mainTableItem.addSpecialNumber(k - TABLE_OFFSET, -1);
                    }
                }
            }

            mainTableItem.makeSpannableString();
            rtn.add(mainTableItem);
        }

        // add monthly data
        Calendar previous = null, current = Calendar.getInstance();
        ArrayList<LotteryItem> tempItems = new ArrayList<>();
        int itemAdded = 0;
        for (int i = 0; i < mLotteryData.size(); ++i) {
            LotteryItem item = mLotteryData.get(i);
            current.setTimeInMillis(item.getDrawingDateTime());
            if (previous == null) {
                tempItems.add(item);
            } else {
                if (previous.get(Calendar.MONTH) == current.get(Calendar.MONTH) && i != mLotteryData.size() - 1) {
                    tempItems.add(item);
                } else {
                    LotteryItem tempItem = tempItems.get(tempItems.size() - 1);
                    if (i == mLotteryData.size() - 1) {
                        tempItems.add(item);
                        tempItem = item;
                        itemAdded++; // insert into the last item of list
                    }

                    Pair<ArrayList<Integer>, ArrayList<Integer>> combinedResult = Utilities.collectLotteryItemsData(tempItems);

                    MainTableItem mainTableItem = new TypeNumeric(MainTableAdapter.TYPE_NUMERIC,
                            tempItem.getSequence(), tempItem.getDrawingDateTime(),
                            tempItem.getMemo(), tempItem.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                            , mMaximumSpecialNumber);
                    mainTableItem.setShowSequence(mShowSequence);
                    mainTableItem.setDigitLength(maximumDigitOfSum);
                    mainTableItem.setWindowBackgroundColor(Utilities.getPrimaryLightColor(null));
                    mainTableItem.setItemType(MainTableItem.ITEM_TYPE_SUB_TOTAL);

                    if (mMaximumSpecialNumber == -1) {
                        // combine results
                        if (DEBUG) {
                            // special & normal list should have the same list size
                            Log.d(TAG, "normal: " + combinedResult.first.size() + ", special: " + combinedResult.second.size());
                        }
                        for (int indexOfList = 0; indexOfList < combinedResult.first.size(); ++indexOfList) {
                            combinedResult.first.set(indexOfList, combinedResult.first.get(indexOfList) + combinedResult.second.get(indexOfList));
                        }
                    }
                    if (DEBUG) {
                        Log.d(TAG, "mMaximumNormalNumber: " + mMaximumNormalNumber + ", combinedResult.first: " + combinedResult.first.size());
                    }
                    for (int k = 0; k < mMaximumNormalNumber; ++k) {
                        mainTableItem.addNormalNumber(k, combinedResult.first.get(k));
                    }

                    if (mMaximumSpecialNumber != -1) {
                        for (int k = 0; k < mMaximumSpecialNumber; ++k) {
                            mainTableItem.addSpecialNumber(k, combinedResult.second.get(k));
                        }
                    }

                    tempItems.clear();
                    tempItems.add(item);
                    rtn.add(i + itemAdded++, mainTableItem);
                }
            }
            if (previous == null) {
                previous = Calendar.getInstance();
            }
            previous.setTimeInMillis(current.getTimeInMillis());
        }
        return rtn;
    }

    private ArrayList<MainTableItem> initListTypeOverall() {
        ArrayList<MainTableItem> rtn = new ArrayList<>();
        try {
            for (LotteryItem item : mLotteryData) {
                MainTableItem mainTableItem = new TypeOverall(MainTableAdapter.TYPE_OVER_ALL_CONTENT,
                        item.getSequence(), item.getDrawingDateTime(),
                        item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                        , mMaximumSpecialNumber);
                ((TypeOverall) mainTableItem).setCombineSpecialNumber(mCombineSpecialNumber);
                for (int i = 0; i < mNormalNumberCount; ++i) {
                    mainTableItem.addNormalNumber(i, item.getNormalNumbers().get(i));
                }
                for (int i = 0; i < mSpecialNumberCount; ++i) {
                    mainTableItem.addSpecialNumber(i, item.getSpecialNumbers().get(i));
                }
                mainTableItem.setShowSequence(mShowSequence);
                mainTableItem.makeSpannableString();
                rtn.add(mainTableItem);
            }
        } catch (Exception e) {
            Log.w(TAG, "mListType: " + mListType, e);
            throw new RuntimeException(e);
        }
        return rtn;
    }

    private void initParameters() {
        int[] parameters = MainTableItem.initParameters(mLotteryData.get(0));
        mNormalNumberCount = parameters[0];
        mSpecialNumberCount = parameters[1];
        mMaximumNormalNumber = parameters[2];
        mMaximumSpecialNumber = parameters[3];
    }

    @Override
    protected void onPostExecute(ArrayList<MainTableItem> mainTableItems) {
        super.onPostExecute(mainTableItems);
        if (mCallback == null) return;
        mCallback.onFinished(mainTableItems);
    }

    public interface Callback {
        void onFinished(ArrayList<MainTableItem> data);
    }
}
