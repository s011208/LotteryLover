package yhh.bj4.lotterylover.views.table.main.item;

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
public class TypeOverall extends MainTableItem {
    private boolean mCombineSpecialNumber;

    public TypeOverall(int viewType, long sequence, long drawingTime, String memo, String extra, int nnc, int snc, int mnn, int msn) {
        super(viewType, sequence, drawingTime, memo, extra, nnc, snc, mnn, msn);
    }

    public void setCombineSpecialNumber(boolean c) {
        mCombineSpecialNumber = c;
    }

    @Override
    public SpannableString generateSpannableString() {
        List<Integer> indexOfSep = new ArrayList<>();
        List<Integer> indexOfNormal = new ArrayList<>();
        List<Integer> indexOfSpecial = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(SEP);
        builder.deleteCharAt(0);
        if (mShowSequence) {
            if (mSequence < 100000) {
                // for List3 & List4
                builder.append(Utilities.getLotterySequenceString(mSequence));
                indexOfSep.add(builder.length());
                builder.append(SEP);
            }
        }
        builder.append(Utilities.getDateTimeYMDString(mDrawingTime));
        indexOfSep.add(builder.length());
        builder.append(SEP);

        if (mCombineSpecialNumber && mMaximumSpecialNumber == -1) {
            int nextIndexOfSpecial = 0;
            for (int i = 0; i < mNormalNumberData.size(); ++i) {
                Integer value = mNormalNumberData.get(i);
                for (int k = nextIndexOfSpecial; k < mSpecialNumberData.size(); ++k) {
                    if (value > mSpecialNumberData.get(k)) {
                        Integer specialValue = mSpecialNumberData.get(k);
                        indexOfSpecial.add(builder.length());
                        builder.append(Utilities.getLotteryNumberString(specialValue, mDigitLength));
                        indexOfSep.add(builder.length());
                        builder.append(SEP);
                        ++nextIndexOfSpecial;
                    } else {
                        break;
                    }
                }
                indexOfNormal.add(builder.length());
                builder.append(Utilities.getLotteryNumberString(value, mDigitLength));
                indexOfSep.add(builder.length());
                builder.append(SEP);
            }
            for (int k = nextIndexOfSpecial; k < mSpecialNumberData.size(); ++k) {
                Integer specialValue = mSpecialNumberData.get(k);
                indexOfSpecial.add(builder.length());
                builder.append(Utilities.getLotteryNumberString(specialValue, mDigitLength));
                indexOfSep.add(builder.length());
                builder.append(SEP);
                ++nextIndexOfSpecial;
            }
        } else {
            for (int i = 0; i < mNormalNumberData.size(); ++i) {
                Integer value = mNormalNumberData.get(i);
                indexOfNormal.add(builder.length());
                builder.append(Utilities.getLotteryNumberString(value, mDigitLength));
                indexOfSep.add(builder.length());
                builder.append(SEP);
            }

            for (int i = 0; i < mSpecialNumberData.size(); ++i) {
                Integer value = mSpecialNumberData.get(i);
                indexOfSpecial.add(builder.length());
                builder.append(Utilities.getLotteryNumberString(value, mDigitLength));
                indexOfSep.add(builder.length());
                builder.append(SEP);
            }
        }

        builder.deleteCharAt(builder.length() - 1);

        SpannableString rtn = new SpannableString(builder.toString());

        // first spe
        rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_NORMAL), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // rest of spe
        for (int i = 0; i < indexOfSep.size(); ++i) {
            final int startIndex = indexOfSep.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            if (!mShowSequence && i == 0) {
                rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_NORMAL_OF_GROUP), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (mShowSequence && (mSequence < 100000 && i == 1) || (mSequence >= 100000 && i == 0)) {
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

        return rtn;
    }

}
