package yhh.bj4.lotterylover.fragments.calendar.item;

import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class TodayLotteryItem {
    private final LotteryItem mLotteryItem;
    private final int mLotteryType;

    public TodayLotteryItem(LotteryItem lotteryItem, int type) {
        mLotteryItem = lotteryItem;
        mLotteryType = type;
    }

    public LotteryItem getLotteryItem() {
        return mLotteryItem;
    }

    public int getLotteryType() {
        return mLotteryType;
    }
}
