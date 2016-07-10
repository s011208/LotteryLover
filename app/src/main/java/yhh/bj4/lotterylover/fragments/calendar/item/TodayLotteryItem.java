package yhh.bj4.lotterylover.fragments.calendar.item;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class TodayLotteryItem {
    private final LotteryItem mLotteryItem;
    private final int mLotteryType;
    private final String mLotteryString;
    private final boolean mIsShowTipItem;

    public TodayLotteryItem(LotteryItem lotteryItem, int type, String ltoString, boolean isShowTipItem) {
        mLotteryItem = lotteryItem;
        mLotteryType = type;
        mLotteryString = ltoString;
        mIsShowTipItem = isShowTipItem;
    }

    public boolean isShowTipItem() {
        return mIsShowTipItem;
    }

    public LotteryItem getLotteryItem() {
        return mLotteryItem;
    }

    public int getLotteryType() {
        return mLotteryType;
    }

    public String getLotteryString() {
        return mLotteryString;
    }

    public SpannableString getSpannableString() {
        List<Pair<Integer, Integer>> specialItemsPair = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(mLotteryString);

        builder.append(": ");

        for (int i = 0; i < mLotteryItem.getNormalNumbers().size(); ++i) {
            builder.append(mLotteryItem.getNormalNumbers().get(i));
            builder.append(", ");
        }

        for (int i = 0; i < mLotteryItem.getSpecialNumbers().size(); ++i) {
            int startLength = builder.length();
            builder.append(mLotteryItem.getSpecialNumbers().get(i));
            specialItemsPair.add(new Pair<>(startLength, builder.length()));
            builder.append(", ");
        }

        builder.delete(builder.length() - 2, builder.length());

        SpannableString rtn = new SpannableString(builder.toString());
        for (Pair<Integer, Integer> pair : specialItemsPair) {
            rtn.setSpan(new ForegroundColorSpan(Color.RED), pair.first, pair.second, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return rtn;
    }
}
