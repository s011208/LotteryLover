package yhh.bj4.lotterylover.views.table.main.item;

import android.graphics.Color;
import android.text.SpannableString;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
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

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public abstract class MainTableItem {
    public static final int ITEM_TYPE_CONTENT = 0;
    public static final int ITEM_TYPE_SUB_TOTAL = 1;
    public static final int ITEM_TYPE_HEADER = 2;
    public static final int ITEM_TYPE_FOOTER = 3;

    static final int SPECIAL_NUMBER_COLOR_OF_HEADER_AND_FOOTER = Color.WHITE;
    static final int SPECIAL_NUMBER_COLOR = Color.RED;
    static final int SEP_COLOR_OF_NORMAL = Color.LTGRAY;
    static final int SEP_COLOR_OF_NORMAL_OF_GROUP = Color.RED;
    static final int SEP_COLOR_OF_SPECIAL = Color.LTGRAY;
    static final int SEP_COLOR_OF_SPECIAL_OF_GROUP = Color.DKGRAY;
    static final int HIT_INDEX_OF_NORMAL = Color.BLUE;
    static final int HIT_INDEX_OF_SPECIAL = Color.MAGENTA;

    static final String SEP = "   ";
    static final float SEP_RELATIVE_SIZE = 0.3f;
    /**
     * key = position to show
     * value = showing value
     */
    final List<Integer> mNormalNumberData = new ArrayList<>();
    final List<Integer> mSpecialNumberData = new ArrayList<>();
    final long mSequence, mDrawingTime;
    final String mMemo, mExtra;
    final int mViewType;
    int mNormalNumberCount, mSpecialNumberCount, mMaximumNormalNumber, mMaximumSpecialNumber;
    int mWindowBackgroundColor = Color.WHITE;
    int mDigitLength = 2;

    private SpannableString mSpannableString;
    private boolean mCacheSpannableString = true;
    int mItemType = ITEM_TYPE_CONTENT;

    public MainTableItem(int viewType, long sequence, long drawingTime, String memo, String extra,
                         int nnc, int snc, int mnn, int msn) {
        mViewType = viewType;
        mSequence = sequence;
        mDrawingTime = drawingTime;
        mMemo = memo;
        mExtra = extra;
        mNormalNumberCount = nnc;
        mSpecialNumberCount = snc;
        mMaximumNormalNumber = mnn;
        mMaximumSpecialNumber = msn;
    }

    public final void setWindowBackgroundColor(int color) {
        mWindowBackgroundColor = color;
    }

    public void setItemType(int itemType) {
        mItemType = itemType;
    }

    public final int getItemType() {
        return mItemType;
    }

    public final int getViewType() {
        return mViewType;
    }

    public final void addNormalNumber(int index, int value) {
        mNormalNumberData.add(index, value);
    }

    public final void addSpecialNumber(int index, int value) {
        mSpecialNumberData.add(index, value);
    }

    public final void setNormalNumber(int index, int value) {
        mNormalNumberData.set(index, value);
    }

    public final void clearNormalNumber() {
        mNormalNumberData.clear();
    }

    public final void clearSpecialNumber() {
        mSpecialNumberData.clear();
    }

    public final void setSpecialNumber(int index, int value) {
        mSpecialNumberData.set(index, value);
    }

    public final void setCacheSpannableString(boolean b) {
        mCacheSpannableString = b;
    }

    public final void setDigitLength(int length) {
        mDigitLength = length;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Utilities.getLotterySequenceString(mSequence)).append(SEP)
                .append(Utilities.getDateTimeYMDString(mDrawingTime)).append(SEP);
        return builder.toString();
    }

    abstract SpannableString generateSpannableString();

    public final SpannableString makeSpannableString() {
        if (mSpannableString == null || !mCacheSpannableString) {
            mSpannableString = generateSpannableString();
        }
        return mSpannableString;
    }

    /**
     * @param ltoType
     * @return rtn[0] = mNormalNumberCount, rtn[1] = mSpecialNumberCount,
     * rtn[2] = mMaximumNormalNumber, rtn[e] = mMaximumSpecialNumber,
     */
    public static int[] initParameters(int ltoType) {
        int[] rtn = new int[4];
        if (ltoType == LotteryLover.LTO_TYPE_LTO) {
            rtn[0] = Lto.getNormalNumbersCount();
            rtn[1] = Lto.getSpecialNumbersCount();
            rtn[2] = Lto.getMaximumNormalNumber();
            rtn[3] = Lto.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO2C) {
            rtn[0] = Lto2C.getNormalNumbersCount();
            rtn[1] = Lto2C.getSpecialNumbersCount();
            rtn[2] = Lto2C.getMaximumNormalNumber();
            rtn[3] = Lto2C.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO7C) {
            rtn[0] = Lto7C.getNormalNumbersCount();
            rtn[1] = Lto7C.getSpecialNumbersCount();
            rtn[2] = Lto7C.getMaximumNormalNumber();
            rtn[3] = Lto7C.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_BIG) {
            rtn[0] = LtoBig.getNormalNumbersCount();
            rtn[1] = LtoBig.getSpecialNumbersCount();
            rtn[2] = LtoBig.getMaximumNormalNumber();
            rtn[3] = LtoBig.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_DOF) {
            rtn[0] = LtoDof.getNormalNumbersCount();
            rtn[1] = LtoDof.getSpecialNumbersCount();
            rtn[2] = LtoDof.getMaximumNormalNumber();
            rtn[3] = LtoDof.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_HK) {
            rtn[0] = LtoHK.getNormalNumbersCount();
            rtn[1] = LtoHK.getSpecialNumbersCount();
            rtn[2] = LtoHK.getMaximumNormalNumber();
            rtn[3] = LtoHK.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_539) {
            rtn[0] = Lto539.getNormalNumbersCount();
            rtn[1] = Lto539.getSpecialNumbersCount();
            rtn[2] = Lto539.getMaximumNormalNumber();
            rtn[3] = Lto539.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_POW) {
            rtn[0] = LtoPow.getNormalNumbersCount();
            rtn[1] = LtoPow.getSpecialNumbersCount();
            rtn[2] = LtoPow.getMaximumNormalNumber();
            rtn[3] = LtoPow.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_MM) {
            rtn[0] = LtoMM.getNormalNumbersCount();
            rtn[1] = LtoMM.getSpecialNumbersCount();
            rtn[2] = LtoMM.getMaximumNormalNumber();
            rtn[3] = LtoMM.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_J6) {
            rtn[0] = LtoJ6.getNormalNumbersCount();
            rtn[1] = LtoJ6.getSpecialNumbersCount();
            rtn[2] = LtoJ6.getMaximumNormalNumber();
            rtn[3] = LtoJ6.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_TOTO) {
            rtn[0] = LtoToTo.getNormalNumbersCount();
            rtn[1] = LtoToTo.getSpecialNumbersCount();
            rtn[2] = LtoToTo.getMaximumNormalNumber();
            rtn[3] = LtoToTo.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_AU_POW) {
            rtn[0] = LtoAuPow.getNormalNumbersCount();
            rtn[1] = LtoAuPow.getSpecialNumbersCount();
            rtn[2] = LtoAuPow.getMaximumNormalNumber();
            rtn[3] = LtoAuPow.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_EM) {
            rtn[0] = LtoEm.getNormalNumbersCount();
            rtn[1] = LtoEm.getSpecialNumbersCount();
            rtn[2] = LtoEm.getMaximumNormalNumber();
            rtn[3] = LtoEm.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_LIST3) {
            rtn[0] = LtoList3.getNormalNumbersCount();
            rtn[1] = LtoList3.getSpecialNumbersCount();
            rtn[2] = LtoList3.getMaximumNormalNumber();
            rtn[3] = LtoList3.getMaximumSpecialNumber();
        } else if (ltoType == LotteryLover.LTO_TYPE_LTO_LIST4) {
            rtn[0] = LtoList4.getNormalNumbersCount();
            rtn[1] = LtoList4.getSpecialNumbersCount();
            rtn[2] = LtoList4.getMaximumNormalNumber();
            rtn[3] = LtoList4.getMaximumSpecialNumber();
        } else {
            throw new RuntimeException("unexpected instance");
        }
        return rtn;
    }

    /**
     * @param item
     * @return rtn[0] = mNormalNumberCount, rtn[1] = mSpecialNumberCount,
     * rtn[2] = mMaximumNormalNumber, rtn[e] = mMaximumSpecialNumber,
     */
    public static int[] initParameters(LotteryItem item) {
        int[] rtn = new int[4];
        if (item instanceof Lto) {
            rtn[0] = Lto.getNormalNumbersCount();
            rtn[1] = Lto.getSpecialNumbersCount();
            rtn[2] = Lto.getMaximumNormalNumber();
            rtn[3] = Lto.getMaximumSpecialNumber();
        } else if (item instanceof Lto2C) {
            rtn[0] = Lto2C.getNormalNumbersCount();
            rtn[1] = Lto2C.getSpecialNumbersCount();
            rtn[2] = Lto2C.getMaximumNormalNumber();
            rtn[3] = Lto2C.getMaximumSpecialNumber();
        } else if (item instanceof Lto7C) {
            rtn[0] = Lto7C.getNormalNumbersCount();
            rtn[1] = Lto7C.getSpecialNumbersCount();
            rtn[2] = Lto7C.getMaximumNormalNumber();
            rtn[3] = Lto7C.getMaximumSpecialNumber();
        } else if (item instanceof LtoBig) {
            rtn[0] = LtoBig.getNormalNumbersCount();
            rtn[1] = LtoBig.getSpecialNumbersCount();
            rtn[2] = LtoBig.getMaximumNormalNumber();
            rtn[3] = LtoBig.getMaximumSpecialNumber();
        } else if (item instanceof LtoDof) {
            rtn[0] = LtoDof.getNormalNumbersCount();
            rtn[1] = LtoDof.getSpecialNumbersCount();
            rtn[2] = LtoDof.getMaximumNormalNumber();
            rtn[3] = LtoDof.getMaximumSpecialNumber();
        } else if (item instanceof LtoHK) {
            rtn[0] = LtoHK.getNormalNumbersCount();
            rtn[1] = LtoHK.getSpecialNumbersCount();
            rtn[2] = LtoHK.getMaximumNormalNumber();
            rtn[3] = LtoHK.getMaximumSpecialNumber();
        } else if (item instanceof Lto539) {
            rtn[0] = Lto539.getNormalNumbersCount();
            rtn[1] = Lto539.getSpecialNumbersCount();
            rtn[2] = Lto539.getMaximumNormalNumber();
            rtn[3] = Lto539.getMaximumSpecialNumber();
        } else if (item instanceof LtoPow) {
            rtn[0] = LtoPow.getNormalNumbersCount();
            rtn[1] = LtoPow.getSpecialNumbersCount();
            rtn[2] = LtoPow.getMaximumNormalNumber();
            rtn[3] = LtoPow.getMaximumSpecialNumber();
        } else if (item instanceof LtoMM) {
            rtn[0] = LtoMM.getNormalNumbersCount();
            rtn[1] = LtoMM.getSpecialNumbersCount();
            rtn[2] = LtoMM.getMaximumNormalNumber();
            rtn[3] = LtoMM.getMaximumSpecialNumber();
        } else if (item instanceof LtoJ6) {
            rtn[0] = LtoJ6.getNormalNumbersCount();
            rtn[1] = LtoJ6.getSpecialNumbersCount();
            rtn[2] = LtoJ6.getMaximumNormalNumber();
            rtn[3] = LtoJ6.getMaximumSpecialNumber();
        } else if (item instanceof LtoToTo) {
            rtn[0] = LtoToTo.getNormalNumbersCount();
            rtn[1] = LtoToTo.getSpecialNumbersCount();
            rtn[2] = LtoToTo.getMaximumNormalNumber();
            rtn[3] = LtoToTo.getMaximumSpecialNumber();
        } else if (item instanceof LtoAuPow) {
            rtn[0] = LtoAuPow.getNormalNumbersCount();
            rtn[1] = LtoAuPow.getSpecialNumbersCount();
            rtn[2] = LtoAuPow.getMaximumNormalNumber();
            rtn[3] = LtoAuPow.getMaximumSpecialNumber();
        } else if (item instanceof LtoEm) {
            rtn[0] = LtoEm.getNormalNumbersCount();
            rtn[1] = LtoEm.getSpecialNumbersCount();
            rtn[2] = LtoEm.getMaximumNormalNumber();
            rtn[3] = LtoEm.getMaximumSpecialNumber();
        } else if (item instanceof LtoList3) {
            rtn[0] = LtoList3.getNormalNumbersCount();
            rtn[1] = LtoList3.getSpecialNumbersCount();
            rtn[2] = LtoList3.getMaximumNormalNumber();
            rtn[3] = LtoList3.getMaximumSpecialNumber();
        } else if (item instanceof LtoList4) {
            rtn[0] = LtoList4.getNormalNumbersCount();
            rtn[1] = LtoList4.getSpecialNumbersCount();
            rtn[2] = LtoList4.getMaximumNormalNumber();
            rtn[3] = LtoList4.getMaximumSpecialNumber();
        } else {
            throw new RuntimeException("unexpected instance");
        }
        return rtn;
    }
}
