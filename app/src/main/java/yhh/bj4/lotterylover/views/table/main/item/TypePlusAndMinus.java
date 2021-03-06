package yhh.bj4.lotterylover.views.table.main.item;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/6/17.
 */
public class TypePlusAndMinus extends MainTableItem {
    private ArrayList<Integer> mHitIndexOfNormal = new ArrayList<>();
    private ArrayList<Integer> mHitIndexOfSpecial = new ArrayList<>();
    private boolean mCombineSpecialNumber;

    public TypePlusAndMinus(int viewType, long sequence, long drawingTime, String memo, String extra, int nnc, int snc, int mnn, int msn) {
        super(viewType, sequence, drawingTime, memo, extra, nnc, snc, mnn, msn);
    }

    public void setCombineSpecialNumber(boolean c) {
        mCombineSpecialNumber = c;
    }

    public void cleanHitResult() {
        mHitIndexOfNormal.clear();
        mHitIndexOfSpecial.clear();
    }

    public void clearCacheAndMakeNewSpannableString() {
        setCacheSpannableString(false);
        makeSpannableString();
        setCacheSpannableString(true);
    }

    public void addHitIndexOfNormal(int hitIndex) {
        mHitIndexOfNormal.add(hitIndex);
    }

    public void addHitIndexOfSpecial(int hitIndex) {
        mHitIndexOfSpecial.add(hitIndex);
    }

    @Override
    public SpannableString generateSpannableString() {
        List<Integer> indexOfSep = new ArrayList<>();
        List<Integer> indexOfNormal = new ArrayList<>();
        List<Integer> indexOfSpecial = new ArrayList<>();
        List<Integer> indexOfHitNormal = new ArrayList<>();
        List<Integer> indexOfHitSpecial = new ArrayList<>();
        int indexOfDrawingTime, indexOfSequence = -1;

        StringBuilder builder = new StringBuilder();
        builder.append(SEP);
        builder.deleteCharAt(0);
        if (mShowSequence) {
            builder.append(Utilities.getLotterySequenceString(mSequence));
            indexOfSequence = builder.length();
            indexOfSep.add(builder.length());
            builder.append(SEP);
        }
        builder.append(Utilities.getDateTimeYMDString(mDrawingTime));
        indexOfDrawingTime = builder.length();
        indexOfSep.add(builder.length());
        builder.append(SEP);

        if (mCombineSpecialNumber && mMaximumSpecialNumber == -1) {
            if (mItemType == ITEM_TYPE_SUB_TOTAL) {
                for (int i = 0; i < mNormalNumberData.size(); ++i) {
                    Integer value = mNormalNumberData.get(i);
                    if (mHitIndexOfNormal.contains(i)) {
                        indexOfHitNormal.add(builder.length());
                    } else {
                        indexOfNormal.add(builder.length());
                    }
                    builder.append(Utilities.getLotteryNumberString(value, mDigitLength));
                    indexOfSep.add(builder.length());
                    builder.append(SEP);
                }
            } else {
                int nextIndexOfSpecial = 0;
                for (int i = 0; i < mNormalNumberData.size(); ++i) {
                    Integer value = mNormalNumberData.get(i);
                    for (int k = nextIndexOfSpecial; k < mSpecialNumberData.size(); ++k) {
                        if (value > mSpecialNumberData.get(k)) {
                            Integer specialValue = mSpecialNumberData.get(k);
                            if (mHitIndexOfNormal.contains(k + i)) {
                                indexOfHitSpecial.add(builder.length());
                            } else {
                                indexOfSpecial.add(builder.length());
                            }
                            builder.append(Utilities.getLotteryNumberString(specialValue, mDigitLength));
                            indexOfSep.add(builder.length());
                            builder.append(SEP);
                            ++nextIndexOfSpecial;
                        } else {
                            break;
                        }
                    }
                    if (mHitIndexOfNormal.contains(i + nextIndexOfSpecial)) {
                        indexOfHitNormal.add(builder.length());
                    } else {
                        indexOfNormal.add(builder.length());
                    }
                    builder.append(Utilities.getLotteryNumberString(value, mDigitLength));
                    indexOfSep.add(builder.length());
                    builder.append(SEP);
                }
                for (int k = nextIndexOfSpecial; k < mSpecialNumberData.size(); ++k) {
                    Integer specialValue = mSpecialNumberData.get(k);
                    if (mHitIndexOfNormal.contains(k + mNormalNumberData.size())) {
                        indexOfHitSpecial.add(builder.length());
                    } else {
                        indexOfSpecial.add(builder.length());
                    }
                    builder.append(Utilities.getLotteryNumberString(specialValue, mDigitLength));
                    indexOfSep.add(builder.length());
                    builder.append(SEP);
                    ++nextIndexOfSpecial;
                }
            }
        } else {
            for (int i = 0; i < mNormalNumberData.size(); ++i) {
                Integer value = mNormalNumberData.get(i);
                if (mHitIndexOfNormal.contains(i)) {
                    indexOfHitNormal.add(builder.length());
                } else {
                    indexOfNormal.add(builder.length());
                }
                builder.append(Utilities.getLotteryNumberString(value, mDigitLength));
                indexOfSep.add(builder.length());
                builder.append(SEP);
            }

            for (int i = 0; i < mSpecialNumberData.size(); ++i) {
                Integer value = mSpecialNumberData.get(i);
                if (mHitIndexOfSpecial.contains(i)) {
                    indexOfHitSpecial.add(builder.length());
                } else {
                    indexOfSpecial.add(builder.length());
                }
                builder.append(Utilities.getLotteryNumberString(value, mDigitLength));
                indexOfSep.add(builder.length());
                builder.append(SEP);
            }
        }

        builder.deleteCharAt(builder.length() - 1);

        SpannableString rtn = new SpannableString(builder.toString());

        if (mItemType == ITEM_TYPE_HEADER || mItemType == ITEM_TYPE_FOOTER || mItemType == ITEM_TYPE_SUB_TOTAL) {
            rtn.setSpan(new BackgroundColorSpan(mWindowBackgroundColor), 0, rtn.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // first spe
        rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // hide day of month
        if (mItemType == ITEM_TYPE_SUB_TOTAL) {
            rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), indexOfDrawingTime - 3, indexOfDrawingTime, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (indexOfSequence != -1) {
                rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), indexOfSequence - 5, indexOfSequence, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        // rest of spe
        for (int i = 0; i < indexOfSep.size(); ++i) {
            final int startIndex = indexOfSep.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            if ((mShowSequence && i == 1) || (!mShowSequence && i == 0)) {
                rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_NORMAL_OF_GROUP), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_SPECIAL), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // normal number
        // do nothing

        // special number
        for (int i = 0; i < indexOfSpecial.size(); ++i) {
            final int startIndex = indexOfSpecial.get(i);
            final int endIndex = startIndex + mDigitLength;
            rtn.setSpan(new ForegroundColorSpan(SPECIAL_NUMBER_COLOR), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // hit normal
        for (int i = 0; i < indexOfHitNormal.size(); ++i) {
            final int startIndex = indexOfHitNormal.get(i);
            final int endIndex = startIndex + mDigitLength;
            rtn.setSpan(new ForegroundColorSpan(HIT_INDEX_OF_NORMAL), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // hit special
        for (int i = 0; i < indexOfHitSpecial.size(); ++i) {
            final int startIndex = indexOfHitSpecial.get(i);
            final int endIndex = startIndex + mDigitLength;
            rtn.setSpan(new ForegroundColorSpan(HIT_INDEX_OF_SPECIAL), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return rtn;
    }

}
