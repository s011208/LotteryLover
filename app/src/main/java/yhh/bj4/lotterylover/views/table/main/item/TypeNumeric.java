package yhh.bj4.lotterylover.views.table.main.item;

import android.support.v4.util.Pair;
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
public class TypeNumeric extends MainTableItem {
    public TypeNumeric(int viewType, long sequence, long drawingTime, String memo, String extra, int nnc, int snc, int mnn, int msn) {
        super(viewType, sequence, drawingTime, memo, extra, nnc, snc, mnn, msn);
    }

    @Override
    SpannableString generateSpannableString() {
        List<Integer> indexOfSepOfNormal = new ArrayList<>();
        List<Integer> indexOfSepOfSpecial = new ArrayList<>();
        List<Integer> indexOfNormal = new ArrayList<>();
        List<Integer> indexOfSpecial = new ArrayList<>();
        List<Integer> indexOfZero = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        Pair<Integer, Integer> indexOfSequence, indexOfDrawingTime;

        builder.append(SEP);
        builder.deleteCharAt(0);
        int start = builder.length();
        builder.append(Utilities.getLotterySequenceString(mSequence));
        indexOfSequence = new Pair<>(start, builder.length());
        indexOfSepOfNormal.add(builder.length());
        builder.append(SEP);
        start = builder.length();
        builder.append(Utilities.getDateTimeYMDString(mDrawingTime));
        indexOfDrawingTime = new Pair<>(start, builder.length());
        indexOfSepOfNormal.add(builder.length());
        builder.append(SEP);

        if (mMaximumSpecialNumber == -1) {
            // combine special & normal
            for (int i = 0; i < mSpecialNumberData.size(); ++i) {
                final int specialNumber = mSpecialNumberData.get(i);
                mNormalNumberData.remove(specialNumber - 1);
                mNormalNumberData.add(specialNumber - 1, specialNumber);
            }
        }

        for (int i = 0; i < mNormalNumberData.size(); ++i) {
            Integer value = mNormalNumberData.get(i);
            if (mSpecialNumberData.contains(value) && mMaximumSpecialNumber == -1) {
                indexOfSpecial.add(builder.length());
            } else {
                indexOfNormal.add(builder.length());
            }
            final boolean isZero = value < 0;
            if (isZero) {
                indexOfZero.add(builder.length());
            }
            builder.append(isZero ? Utilities.getLotteryNumberString(0, mDigitLength) : Utilities.getLotteryNumberString(value, mDigitLength));
            indexOfSepOfNormal.add(builder.length());
            builder.append(SEP);
        }

        if (mMaximumSpecialNumber != -1) {
            // ignore if combined above
            for (int i = 0; i < mSpecialNumberData.size(); ++i) {
                Integer value = mSpecialNumberData.get(i);
                indexOfSpecial.add(builder.length());
                final boolean isZero = value < 0;
                if (isZero) {
                    indexOfZero.add(builder.length());
                }
                builder.append(isZero ? Utilities.getLotteryNumberString(0, mDigitLength) : Utilities.getLotteryNumberString(value, mDigitLength));
                indexOfSepOfSpecial.add(builder.length());
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

        // drawing time & sequence
        if (mItemType == ITEM_TYPE_HEADER || mItemType == ITEM_TYPE_FOOTER) {
            rtn.setSpan(new ForegroundColorSpan(mWindowBackgroundColor), indexOfSequence.first, indexOfSequence.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rtn.setSpan(new ForegroundColorSpan(mWindowBackgroundColor), indexOfDrawingTime.first, indexOfDrawingTime.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // hide day of month
        if (mItemType == ITEM_TYPE_SUB_TOTAL) {
            rtn.setSpan(new ForegroundColorSpan(mWindowBackgroundColor), indexOfDrawingTime.second - 3, indexOfDrawingTime.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rtn.setSpan(new ForegroundColorSpan(mWindowBackgroundColor), indexOfSequence.first, indexOfSequence.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // rest of spe of normal
        for (int i = 0; i < indexOfSepOfNormal.size(); ++i) {
            final int startIndex = indexOfSepOfNormal.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            if (i % 10 == 1 || i == indexOfSepOfNormal.size() - 1) {
                rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_NORMAL_OF_GROUP), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_NORMAL), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // rest of spe of special
        for (int i = 0; i < indexOfSepOfSpecial.size(); ++i) {
            final int startIndex = indexOfSepOfSpecial.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_SPECIAL), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // normal number
        // do nothing

        // special number
        for (int i = 0; i < indexOfSpecial.size(); ++i) {
            final int startIndex = indexOfSpecial.get(i);
            final int endIndex = startIndex + mDigitLength;
            if (mItemType == ITEM_TYPE_HEADER || mItemType == ITEM_TYPE_FOOTER) {
                rtn.setSpan(new ForegroundColorSpan(SPECIAL_NUMBER_COLOR_OF_HEADER_AND_FOOTER), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                rtn.setSpan(new ForegroundColorSpan(SPECIAL_NUMBER_COLOR), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        for (int i = 0; i < indexOfZero.size(); ++i) {
            final int startIndex = indexOfZero.get(i);
            final int endIndex = startIndex + mDigitLength;
            rtn.setSpan(new ForegroundColorSpan(mWindowBackgroundColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return rtn;
    }
}
