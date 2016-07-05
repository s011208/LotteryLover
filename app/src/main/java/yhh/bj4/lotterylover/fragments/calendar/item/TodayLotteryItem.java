package yhh.bj4.lotterylover.fragments.calendar.item;

import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class TodayLotteryItem {
    private final LotteryItem mLotteryItem;

    public TodayLotteryItem(LotteryItem lotteryItem) {
        mLotteryItem = lotteryItem;
    }

    public LotteryItem getLotteryItem() {
        return mLotteryItem;
    }
}
