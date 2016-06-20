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
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;
import yhh.bj4.lotterylover.views.table.main.item.MainTableItem;
import yhh.bj4.lotterylover.views.table.main.item.TypeNumeric;
import yhh.bj4.lotterylover.views.table.main.item.TypeOverall;
import yhh.bj4.lotterylover.views.table.main.item.TypePlusTogether;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class AdapterDataGenerator extends AsyncTask<Void, Void, ArrayList<MainTableItem>> {
    private static final String TAG = "AdapterDataGenerator";
    private static final boolean DEBUG = Utilities.DEBUG;

    public interface Callback {
        void onFinished(ArrayList<MainTableItem> data);
    }

    private static final int TABLE_OFFSET = 1;

    private final int mLtoType, mListType;
    private final ArrayList<LotteryItem> mLotteryData = new ArrayList<>();
    private final Callback mCallback;
    private int mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber, mMaximumSpecialNumber;
    private final int mWindowBackgroundColor;

    public AdapterDataGenerator(int lto, int list, int color, List<LotteryItem> data, Callback cb) {
        mListType = list;
        mLtoType = lto;
        mLotteryData.addAll(data);
        mCallback = cb;
        mWindowBackgroundColor = color;
    }

    @Override
    protected ArrayList<MainTableItem> doInBackground(Void... params) {
        final ArrayList<MainTableItem> rtn = new ArrayList<>();
        if (mLotteryData == null || mLotteryData.isEmpty()) return rtn;
        initParameters();
        switch (mListType) {
            case LotteryLover.LIST_TYPE_OVERALL:
                rtn.addAll(initListTypeOverall());
                break;
            case LotteryLover.LIST_TYPE_NUMERIC:
                rtn.addAll(initListTypeNumeric());
                break;
            case LotteryLover.LIST_TYPE_PLUS_TOGETHER:
                rtn.addAll(initListTypePlusTogether());
                break;
        }
        return rtn;
    }

    private ArrayList<MainTableItem> initListTypePlusTogether() {
        ArrayList<MainTableItem> rtn = new ArrayList<>();
        // add content data
        for (LotteryItem item : mLotteryData) {
            MainTableItem mainTableItem = new TypePlusTogether(MainTableAdapter.TYPE_PLUS_TOGETHER,
                    item.getSequence(), item.getDrawingDateTime(),
                    item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                    , mMaximumSpecialNumber);
            mainTableItem.setWindowBackgroundColor(mWindowBackgroundColor);
            mainTableItem.setIsContentView(true);
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

            mainTableItem.getSpannableString();
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
                    mainTableItem.setWindowBackgroundColor(mWindowBackgroundColor);
                    mainTableItem.setIsContentView(false);

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

    private ArrayList<MainTableItem> initListTypeNumeric() {
        ArrayList<MainTableItem> rtn = new ArrayList<>();
        // add content data
        for (LotteryItem item : mLotteryData) {
            MainTableItem mainTableItem = new TypeNumeric(MainTableAdapter.TYPE_NUMERIC,
                    item.getSequence(), item.getDrawingDateTime(),
                    item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                    , mMaximumSpecialNumber);
            mainTableItem.setWindowBackgroundColor(mWindowBackgroundColor);
            mainTableItem.setIsContentView(true);
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

            mainTableItem.getSpannableString();
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
                    mainTableItem.setWindowBackgroundColor(mWindowBackgroundColor);
                    mainTableItem.setIsContentView(false);

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
        for (LotteryItem item : mLotteryData) {
            MainTableItem mainTableItem = new TypeOverall(MainTableAdapter.TYPE_OVER_ALL_CONTENT,
                    item.getSequence(), item.getDrawingDateTime(),
                    item.getMemo(), item.getExtraMessage(), mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber
                    , mMaximumSpecialNumber);
            for (int i = 0; i < mNormalNumberCount; ++i) {
                mainTableItem.addNormalNumber(i, item.getNormalNumbers().get(i));
            }
            for (int i = 0; i < mSpecialNumberCount; ++i) {
                mainTableItem.addSpecialNumber(i, item.getSpecialNumbers().get(i));
            }
            mainTableItem.getSpannableString();
            rtn.add(mainTableItem);
        }
        return rtn;
    }

    private void initParameters() {
        if (mLotteryData.get(0) instanceof Lto) {
            mNormalNumberCount = Lto.getNormalNumbersCount();
            mSpecialNumberCount = Lto.getSpecialNumbersCount();
            mMaximumNormalNumber = Lto.getMaximumNormalNumber();
            mMaximumSpecialNumber = Lto.getMaximumSpecialNumber();
        } else if (mLotteryData.get(0) instanceof Lto2C) {
            mNormalNumberCount = Lto2C.getNormalNumbersCount();
            mSpecialNumberCount = Lto2C.getSpecialNumbersCount();
            mMaximumNormalNumber = Lto2C.getMaximumNormalNumber();
            mMaximumSpecialNumber = Lto2C.getMaximumSpecialNumber();
        } else if (mLotteryData.get(0) instanceof Lto7C) {
            mNormalNumberCount = Lto7C.getNormalNumbersCount();
            mSpecialNumberCount = Lto7C.getSpecialNumbersCount();
            mMaximumNormalNumber = Lto7C.getMaximumNormalNumber();
            mMaximumSpecialNumber = Lto7C.getMaximumSpecialNumber();
        } else if (mLotteryData.get(0) instanceof LtoBig) {
            mNormalNumberCount = LtoBig.getNormalNumbersCount();
            mSpecialNumberCount = LtoBig.getSpecialNumbersCount();
            mMaximumNormalNumber = LtoBig.getMaximumNormalNumber();
            mMaximumSpecialNumber = LtoBig.getMaximumSpecialNumber();
        } else if (mLotteryData.get(0) instanceof LtoDof) {
            mNormalNumberCount = LtoDof.getNormalNumbersCount();
            mSpecialNumberCount = LtoDof.getSpecialNumbersCount();
            mMaximumNormalNumber = LtoDof.getMaximumNormalNumber();
            mMaximumSpecialNumber = LtoDof.getMaximumSpecialNumber();
        } else if (mLotteryData.get(0) instanceof LtoHK) {
            mNormalNumberCount = LtoHK.getNormalNumbersCount();
            mSpecialNumberCount = LtoHK.getSpecialNumbersCount();
            mMaximumNormalNumber = LtoHK.getMaximumNormalNumber();
            mMaximumSpecialNumber = LtoHK.getMaximumSpecialNumber();
        } else {
            throw new RuntimeException("unexpected instance");
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MainTableItem> mainTableItems) {
        super.onPostExecute(mainTableItems);
        if (mCallback == null) return;
        mCallback.onFinished(mainTableItems);
    }
}
