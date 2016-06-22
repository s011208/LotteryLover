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
    public TypePlusAndMinus(int viewType, long sequence, long drawingTime, String memo, String extra, int nnc, int snc, int mnn, int msn) {
        super(viewType, sequence, drawingTime, memo, extra, nnc, snc, mnn, msn);
    }

    @Override
    public SpannableString generateSpannableString() {
        List<Integer> indexOfSep = new ArrayList<>();
        List<Integer> indexOfNormal = new ArrayList<>();
        List<Integer> indexOfSpecial = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(SEP);
        builder.deleteCharAt(0);
        builder.append(Utilities.getLotterySequenceString(mSequence));
        indexOfSep.add(builder.length());
        builder.append(SEP);
        builder.append(Utilities.getDateTimeYMDString(mDrawingTime));
        indexOfSep.add(builder.length());
        builder.append(SEP);

        for (int i = 0; i < mNormalNumberData.size(); ++i) {
            Integer value = mNormalNumberData.get(i);
            indexOfNormal.add(builder.length());
            builder.append(Utilities.getLotteryNumberString(value));
            indexOfSep.add(builder.length());
            builder.append(SEP);
        }

        for (int i = 0; i < mSpecialNumberData.size(); ++i) {
            Integer value = mSpecialNumberData.get(i);
            indexOfSpecial.add(builder.length());
            builder.append(Utilities.getLotteryNumberString(value));
            indexOfSep.add(builder.length());
            builder.append(SEP);
        }

        builder.deleteCharAt(builder.length() - 1);

        SpannableString rtn = new SpannableString(builder.toString());

        // first spe
        rtn.setSpan(new BackgroundColorSpan(Color.LTGRAY), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // rest of spe
        for (int i = 0; i < indexOfSep.size(); ++i) {
            final int startIndex = indexOfSep.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            if (i == 1) {
                rtn.setSpan(new BackgroundColorSpan(Color.RED), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                rtn.setSpan(new BackgroundColorSpan(Color.LTGRAY), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // normal number
        // do nothing

        // special number
        for (int i = 0; i < indexOfSpecial.size(); ++i) {
            final int startIndex = indexOfSpecial.get(i);
            final int endIndex = startIndex + 2;
            rtn.setSpan(new ForegroundColorSpan(Color.RED), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return rtn;
    }

}