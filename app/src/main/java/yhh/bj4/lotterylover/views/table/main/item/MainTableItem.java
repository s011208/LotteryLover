package yhh.bj4.lotterylover.views.table.main.item;

import android.graphics.Color;
import android.text.SpannableString;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public abstract class MainTableItem {
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

    private SpannableString mSpannableString;
    private boolean mCacheSpannableString = true;
    boolean mIsContentView = true;

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

    public void setIsContentView(boolean b) {
        mIsContentView = b;
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
        mNormalNumberData.remove(index);
        mNormalNumberData.add(index, value);
    }

    public final void setSpecialNumber(int index, int value) {
        mSpecialNumberData.remove(index);
        mSpecialNumberData.add(index, value);
    }

    public final void setCacheSpannableString(boolean b) {
        mCacheSpannableString = b;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Utilities.getLotterySequenceString(mSequence)).append(SEP)
                .append(Utilities.getDateTimeYMDString(mDrawingTime)).append(SEP);
        return builder.toString();
    }

    abstract SpannableString generateSpannableString();

    public final SpannableString getSpannableString() {
        if (mSpannableString == null || !mCacheSpannableString) {
            mSpannableString = generateSpannableString();
        }
        return mSpannableString;
    }
}
