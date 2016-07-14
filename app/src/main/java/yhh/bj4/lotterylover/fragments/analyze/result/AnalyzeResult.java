package yhh.bj4.lotterylover.fragments.analyze.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private final NotDrawingNumbers mNotDrawingNumbers;

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
        mNotDrawingNumbers = new NotDrawingNumbers(mLotteryType, mLotteryItems);
    }

    public void process() {
        mDrawingTime.process();
        mNotDrawingNumbers.process();
    }

    public DrawingTime getDrawingTime() {
        return mDrawingTime;
    }

    public NotDrawingNumbers getNotDrawingNumbers() {
        return mNotDrawingNumbers;
    }


    public int getLotteryType() {
        return mLotteryType;
    }

    public static <K extends Comparable<? super K>,
            V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                final int rtn = (o1.getValue()).compareTo(o2.getValue());
                if (rtn == 0) {
                    return (o1.getKey()).compareTo(o2.getKey());
                }
                return rtn;
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
