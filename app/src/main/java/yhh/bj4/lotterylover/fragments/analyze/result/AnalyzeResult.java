package yhh.bj4.lotterylover.fragments.analyze.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/13.
 */
public class AnalyzeResult {
    private static final String TAG = "AnalyzeResult";
    private static final boolean DEBUG = Utilities.DEBUG;

    private final int mLotteryType;

    private final DrawingTime mDrawingTime;

    private final List<LotteryItem> mLotteryItems = new ArrayList<>();


    public AnalyzeResult(int ltoType, List<LotteryItem> items) {
        mLotteryType = ltoType;
        mLotteryItems.addAll(items);

        Collections.sort(mLotteryItems, new Comparator<LotteryItem>() {
            @Override
            public int compare(LotteryItem lhs, LotteryItem rhs) {
                if (lhs.getDrawingDateTime() > rhs.getDrawingDateTime()) return -1;
                else if (lhs.getDrawingDateTime() < rhs.getDrawingDateTime()) return 1;
                return 0;
            }
        });

        mDrawingTime = new DrawingTime(mLotteryType, mLotteryItems);
    }

    public void process() {
        mDrawingTime.process();
    }

    public DrawingTime getDrawingTime() {
        return mDrawingTime;
    }


    public int getLotteryType() {
        return mLotteryType;
    }
}
