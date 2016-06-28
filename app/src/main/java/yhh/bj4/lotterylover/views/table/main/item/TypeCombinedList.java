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
public class TypeCombinedList extends MainTableItem {
    public TypeCombinedList(int viewType, long sequence, long drawingTime, String memo, String extra, int nnc, int snc, int mnn, int msn) {
        super(viewType, sequence, drawingTime, memo, extra, nnc, snc, mnn, msn);
    }

    @Override
    public SpannableString generateSpannableString() {
        List<Integer> indexOfSep = new ArrayList<>();
        List<Integer> indexOfColorSep = new ArrayList<>();
        List<Integer> indexOfNormal = new ArrayList<>();
        List<Integer> indexOfEmpty = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(SEP);
        builder.deleteCharAt(0);
        builder.append(Utilities.getDateTimeYMDString(mDrawingTime));
        indexOfColorSep.add(builder.length());
        builder.append(SEP);

        for (int i = 0; i < mNormalNumberData.size(); ++i) {
            Integer value = mNormalNumberData.get(i);
            if (value == -1) {
                indexOfEmpty.add(builder.length());
                builder.append(0);
            } else {
                indexOfNormal.add(builder.length());
                builder.append(value);
            }
            if (i == mNormalNumberData.size() - 1) {
                indexOfColorSep.add(builder.length());
            } else {
                indexOfSep.add(builder.length());
            }
            builder.append(SEP);
        }

        for (int i = 0; i < mNormalNumberData.size(); ++i) {
            Integer value = mNormalNumberData.get(i);
            if (value == -1) {
                indexOfEmpty.add(builder.length());
                builder.append(Utilities.oddString());
            } else {
                indexOfNormal.add(builder.length());
                builder.append(value % 2 == 0 ? Utilities.pluralString() : Utilities.oddString());
            }
            if (i == mNormalNumberData.size() - 1) {
                indexOfColorSep.add(builder.length());
            } else {
                indexOfSep.add(builder.length());
            }
            builder.append(SEP);
        }

        for (int i = 0; i < mNormalNumberData.size(); ++i) {
            Integer value = mNormalNumberData.get(i);
            if (value == -1) {
                indexOfEmpty.add(builder.length());
                builder.append(0);
            } else {
                indexOfNormal.add(builder.length());
                if (value == 1 || value == 4 || value == 7)
                    builder.append(1);
                else if (value == 2 || value == 5 || value == 8)
                    builder.append(2);
                else builder.append(3);
            }
            if (i == mNormalNumberData.size() - 1) {
                indexOfColorSep.add(builder.length());
            } else {
                indexOfSep.add(builder.length());
            }
            builder.append(SEP);
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
            rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_SPECIAL), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // color spe
        for (int i = 0; i < indexOfColorSep.size(); ++i) {
            final int startIndex = indexOfColorSep.get(i) + 1;
            final int endIndex = startIndex + SEP.length() - 2;
            rtn.setSpan(new BackgroundColorSpan(SEP_COLOR_OF_NORMAL_OF_GROUP), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rtn.setSpan(new RelativeSizeSpan(SEP_RELATIVE_SIZE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // normal number
        // do nothing

        // empty number
        for (int i = 0; i < indexOfEmpty.size(); ++i) {
            final int startIndex = indexOfEmpty.get(i);
            final int endIndex = startIndex + 1;
            rtn.setSpan(new ForegroundColorSpan(mWindowBackgroundColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return rtn;
    }

}
