package yhh.bj4.lotterylover.views.table.main.item;

import android.graphics.Color;
import android.support.v4.util.Pair;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/6/20.
 */
public class TypeLastDigit extends MainTableItem {
    public TypeLastDigit(int viewType, long sequence, long drawingTime, String memo, String extra, int nnc, int snc, int mnn, int msn) {
        super(viewType, sequence, drawingTime, memo, extra, nnc, snc, mnn, msn);
    }

    @Override
    SpannableString generateSpannableString() {
        List<Integer> indexOfSepOfNormal = new ArrayList<>();
        List<Integer> indexOfSepOfSpecial = new ArrayList<>();
        List<Integer> indexOfSepOfNormalGroup = new ArrayList<>();
        List<Integer> indexOfSepOfSpecialGroup = new ArrayList<>();
        List<Integer> indexOfNormal = new ArrayList<>();
        List<Integer> indexOfSpecial = new ArrayList<>();
        List<Integer> indexOfZero = new ArrayList<>();
        Pair<Integer, Integer> indexOfSequence = null, indexOfDrawingTime;

        StringBuilder builder = new StringBuilder();
        builder.append(SEP);
        builder.deleteCharAt(0);
        int start;
        if (mShowSequence) {
            start = builder.length();
            builder.append(Utilities.getLotterySequenceString(mSequence));
            indexOfSequence = new Pair<>(start, builder.length());
            indexOfSepOfNormal.add(builder.length());
            builder.append(SEP);
        }
        start = builder.length();
        builder.append(Utilities.getDateTimeYMDString(mDrawingTime));
        indexOfDrawingTime = new Pair<>(start, builder.length());
        indexOfSepOfNormalGroup.add(builder.length());
        builder.append(SEP);

        if (mMaximumSpecialNumber == -1) {
            // combine special & normal
            Map<Integer, Integer> indexMap = Utilities.getLastDigitMap(mMaximumNormalNumber);
            for (int i = 0; i < mSpecialNumberData.size(); ++i) {
                final int specialNumber = mSpecialNumberData.get(i);
                int newIndex = 0;
                for (int j = 1; j < mMaximumNormalNumber + 1; ++j) {
                    if (indexMap.get(j) == specialNumber) {
                        newIndex = j;
                        break;
                    }
                }
                mNormalNumberData.remove(newIndex - 1);
                mNormalNumberData.add(newIndex - 1, specialNumber);
            }
        }

        Map<Integer, Integer> indexMap = Utilities.getLastDigitMap(mMaximumNormalNumber);
        int digit = -1;
        for (int i = 0; i < mNormalNumberData.size(); ++i) {
            int newDigit = Utilities.getLastDigit(indexMap.get(i + 1));
            if (digit == -1) {
                digit = newDigit;
            } else {
                if (newDigit != digit) {
                    digit = newDigit;
                    indexOfSepOfNormalGroup.add(builder.length());
                    builder.append(SEP);
                } else {
                    indexOfSepOfNormal.add(builder.length());
                    builder.append(SEP);
                }
            }

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
        }
        indexOfSepOfNormalGroup.add(builder.length());
        builder.append(SEP);

        if (mMaximumSpecialNumber != -1) {
            // ignore if combined above
            indexMap = Utilities.getLastDigitMap(mMaximumSpecialNumber);
            digit = -1;
            for (int i = 0; i < mSpecialNumberData.size(); ++i) {
                int newDigit = Utilities.getLastDigit(indexMap.get(i + 1));
                if (digit == -1) {
                    digit = newDigit;
                } else {
                    if (newDigit != digit) {
                        digit = newDigit;
                        indexOfSepOfSpecialGroup.add(builder.length());
                        builder.append(SEP);
                    } else {
                        indexOfSepOfSpecial.add(builder.length());
                        builder.append(SEP);
                    }
                }

                Integer value = mSpecialNumberData.get(i);
                indexOfSpecial.add(builder.length());
                final boolean isZero = value < 0;
                if (isZero) {
                    indexOfZero.add(builder.length());
                }
                builder.append(isZero ? Utilities.getLotteryNumberString(0, mDigitLength) : Utilities.getLotteryNumberString(value, mDigitLength));
            }
            indexOfSepOfSpecialGroup.add(builder.length());
            builder.append(SEP);
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
            if (indexOfSequence != null) {
                rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), indexOfSequence.first, indexOfSequence.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), indexOfDrawingTime.first, indexOfDrawingTime.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (mItemType == ITEM_TYPE_SUB_TOTAL) {
            rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), indexOfDrawingTime.second - 3, indexOfDrawingTime.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (indexOfSequence != null) {
                rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), indexOfSequence.first, indexOfSequence.second, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        // rest of spe of normal
        for (int i = 0; i < indexOfSepOfNormal.size(); ++i) {
            final int startIndex = indexOfSepOfNormal.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_NORMAL), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // rest of spe of normal of group
        for (int i = 0; i < indexOfSepOfNormalGroup.size(); ++i) {
            final int startIndex = indexOfSepOfNormalGroup.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_NORMAL_OF_GROUP), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // rest of spe of special
        for (int i = 0; i < indexOfSepOfSpecial.size(); ++i) {
            final int startIndex = indexOfSepOfSpecial.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_SPECIAL), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // rest of spe of special of group
        for (int i = 0; i < indexOfSepOfSpecialGroup.size(); ++i) {
            final int startIndex = indexOfSepOfSpecialGroup.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_SPECIAL_OF_GROUP), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        final int digitLengthDiff = mDigitLength - 2;
        // normal number
        if (mItemType != ITEM_TYPE_FOOTER && digitLengthDiff > 0) {
            for (int i = 0; i < indexOfNormal.size(); ++i) {
                final int startIndex = indexOfNormal.get(i);
                final int endIndex = startIndex + digitLengthDiff;
                rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        // special number
        for (int i = 0; i < indexOfSpecial.size(); ++i) {
            final int startIndex = indexOfSpecial.get(i);
            final int endIndex = startIndex + mDigitLength;
            if (mItemType == ITEM_TYPE_HEADER) {
                if (digitLengthDiff > 0) {
                    rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), startIndex, startIndex + digitLengthDiff, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    rtn.setSpan(new ForegroundColorSpan(SPECIAL_NUMBER_COLOR_OF_HEADER_AND_FOOTER), startIndex + digitLengthDiff, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    rtn.setSpan(new ForegroundColorSpan(SPECIAL_NUMBER_COLOR_OF_HEADER_AND_FOOTER), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (mItemType == ITEM_TYPE_FOOTER) {
                rtn.setSpan(new ForegroundColorSpan(SPECIAL_NUMBER_COLOR_OF_HEADER_AND_FOOTER), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                if (digitLengthDiff > 0) {
                    rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), startIndex, startIndex + digitLengthDiff, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    rtn.setSpan(new ForegroundColorSpan(SPECIAL_NUMBER_COLOR), startIndex + digitLengthDiff, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    rtn.setSpan(new ForegroundColorSpan(SPECIAL_NUMBER_COLOR), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            }
        }

        for (int i = 0; i < indexOfZero.size(); ++i) {
            final int startIndex = indexOfZero.get(i);
            final int endIndex = startIndex + mDigitLength;
            rtn.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return rtn;
    }
}
