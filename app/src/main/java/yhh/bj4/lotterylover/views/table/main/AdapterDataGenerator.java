package yhh.bj4.lotterylover.views.table.main;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.parser.lto.Lto;
import yhh.bj4.lotterylover.parser.lto2c.Lto2C;
import yhh.bj4.lotterylover.parser.lto7c.Lto7C;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHK;
import yhh.bj4.lotterylover.parser.ltobig.LtoBig;
import yhh.bj4.lotterylover.parser.ltodof.LtoDof;
import yhh.bj4.lotterylover.views.table.main.item.MainTableItem;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class AdapterDataGenerator extends AsyncTask<Void, Void, ArrayList<MainTableItem>> {
    public interface Callback {
        void onFinished(ArrayList<MainTableItem> data);
    }

    private final int mLtoType, mListType;
    private final ArrayList<LotteryItem> mLotteryData = new ArrayList<>();
    private final Callback mCallback;
    private int mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber, mMaximumSpecialNumber;

    public AdapterDataGenerator(int lto, int list, List<LotteryItem> data, Callback cb) {
        mListType = list;
        mLtoType = lto;
        mLotteryData.addAll(data);
        mCallback = cb;
    }

    @Override
    protected ArrayList<MainTableItem> doInBackground(Void... params) {
        final ArrayList<MainTableItem> rtn = new ArrayList<>();
        if (mLotteryData == null || mLotteryData.isEmpty()) return rtn;
        initParameters();

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
        }
    }

    @Override
    protected void onPostExecute(ArrayList<MainTableItem> mainTableItems) {
        super.onPostExecute(mainTableItems);
        if (mCallback == null) return;
        mCallback.onFinished(mainTableItems);
    }
}
