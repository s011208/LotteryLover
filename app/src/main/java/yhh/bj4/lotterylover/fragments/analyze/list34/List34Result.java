package yhh.bj4.lotterylover.fragments.analyze.list34;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/15.
 */
public class List34Result {
    private static final String SEP = " | ";

    private final int mLtoType;
    private final List<LotteryItem> mItems = new ArrayList<>();
    private final List<ItemContent> mResultList = new ArrayList<>();
    private final String mOdd, mPlural;

    public List34Result(Context context, int ltoType, List<LotteryItem> items) {
        mLtoType = ltoType;
        mItems.addAll(items);
        final Resources res = context.getResources();
        mOdd = res.getString(R.string.odd);
        mPlural = res.getString(R.string.plural);
        mResultList.add(new ItemContent(
                res.getString(R.string.analyze_list34_result_drawing_time),
                res.getString(R.string.analyze_list34_result_drawing_number),
                res.getString(R.string.analyze_list34_result_odd_and_plural),
                res.getString(R.string.analyze_list34_result_mod_3),
                res.getString(R.string.analyze_list34_result_sum),
                res.getString(R.string.analyze_list34_result_average),
                res.getString(R.string.analyze_list34_result_number_of_odd_and_plural)));
        process();
    }

    public List<ItemContent> getResultList() {
        return mResultList;
    }

    public int getLotteryType() {
        return mLtoType;
    }

    private void process() {
        if (mItems.isEmpty()) return;
        for (LotteryItem item : mItems) {
            // drawing time
            String drawingTime = Utilities.getDateTimeYMDString(item.getDrawingDateTime());

            String drawingNumber = "";
            // number
            for (int i = 0; i < item.getNormalNumbers().size(); ++i) {
                final int value = item.getNormalNumbers().get(i);
                drawingNumber += value;
                if (i < item.getNormalNumbers().size() - 1) {
                    drawingNumber += SEP;
                }
            }

            String oddAndPlural = "";
            // odd & plural
            for (int i = 0; i < item.getNormalNumbers().size(); ++i) {
                final int value = item.getNormalNumbers().get(i);
                oddAndPlural += value % 2 == 0 ? mPlural : mOdd;
                if (i < item.getNormalNumbers().size() - 1) {
                    oddAndPlural += SEP;
                }
            }

            // mod 3
            String mod3 = "";
            for (int i = 0; i < item.getNormalNumbers().size(); ++i) {
                final int value = item.getNormalNumbers().get(i);
                final int modValue = value % 3;
                mod3 += modValue == 0 ? 3 : modValue;
                if (i < item.getNormalNumbers().size() - 1) {
                    mod3 += SEP;
                }
            }

            // sum
            int sum = 0;
            for (int i = 0; i < item.getNormalNumbers().size(); ++i) {
                sum += item.getNormalNumbers().get(i);
            }

            // average

            String average = String.valueOf(Math.round(sum / (float) item.getNormalNumbers().size()));

            // number of odd & plural
            int numberOfOdd = 0;
            int numberOfPlural = 0;
            for (int i = 0; i < item.getNormalNumbers().size(); ++i) {
                final int value = item.getNormalNumbers().get(i);
                if (value % 2 == 0) {
                    ++numberOfPlural;
                } else {
                    ++numberOfOdd;
                }
            }
            String numberOfOddAndPlural = numberOfOdd + mOdd + numberOfPlural + mPlural;

            mResultList.add(new ItemContent(drawingTime, drawingNumber, oddAndPlural,
                    mod3, String.valueOf(sum), average, numberOfOddAndPlural));
        }
    }

    public static class ItemContent {
        public final String mDrawingDate;
        public final String mDrawingNumber;
        public final String mOddAndPlural;
        public final String mMod3;
        public final String mSum;
        public final String mAverage;
        public final String mNumberOfOddAndPlural;

        ItemContent(String s1, String s2, String s3, String s4, String s5, String s6, String s7) {
            mDrawingDate = s1;
            mDrawingNumber = s2;
            mOddAndPlural = s3;
            mMod3 = s4;
            mSum = s5;
            mAverage = s6;
            mNumberOfOddAndPlural = s7;
        }

        @Override
        public String toString() {
            return mDrawingDate + ", " + mDrawingNumber + ", " + mOddAndPlural + ", " +
                    mMod3 + ", " + mSum + ", " + mAverage + ", " + mNumberOfOddAndPlural;
        }
    }
}
