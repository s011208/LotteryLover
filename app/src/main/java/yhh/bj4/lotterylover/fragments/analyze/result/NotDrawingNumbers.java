package yhh.bj4.lotterylover.fragments.analyze.result;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/14.
 */
public class NotDrawingNumbers {

    private final int mLotteryType;

    private final List<LotteryItem> mLotteryItems = new ArrayList<>();

    private final List<Pair<Integer, Integer>> mNormalNotShow = new ArrayList<>();
    private final List<Pair<Integer, Integer>> mSpecialNotShow = new ArrayList<>();

    public NotDrawingNumbers(int ltoType, List<LotteryItem> items) {
        mLotteryType = ltoType;
        mLotteryItems.addAll(items);
    }

    public void process() {
        final boolean hasSpecial = LotteryItem.getMaximumSpecialNumber(mLotteryItems.get(0)) > 0;
        final int maxNumberOfNormal = LotteryItem.getMaximumNormalNumber(mLotteryItems.get(0));
        final int maxNumberOfSpecial = LotteryItem.getMaximumSpecialNumber(mLotteryItems.get(0));
        Map<Integer, Integer> normalMap = new HashMap<>();
        Map<Integer, Integer> specialMap = new HashMap<>();

        List<Boolean> hitNormalList = new ArrayList<>();
        List<Boolean> hitSpecialList = new ArrayList<>();
        for (int i = 0; i < maxNumberOfNormal + 1; ++i) {
            hitNormalList.add(i, false);
            normalMap.put(i, 0);
        }

        for (int i = 0; i < maxNumberOfSpecial + 1; ++i) {
            hitSpecialList.add(i, false);
            specialMap.put(i, 0);
        }

        for (LotteryItem item : mLotteryItems) {
            for (Integer value : item.getNormalNumbers()) {
                hitNormalList.set(value, true);
            }
            if (hasSpecial) {
                for (Integer value : item.getSpecialNumbers()) {
                    hitSpecialList.set(value, true);
                }
                for (int i = 0; i < maxNumberOfSpecial; ++i) {
                    if (!hitSpecialList.get(i)) {
                        specialMap.put(i, specialMap.get(i) + 1);
                    }
                }
            } else {
                for (Integer value : item.getSpecialNumbers()) {
                    hitNormalList.set(value, true);
                }
            }
            for (int i = 0; i < maxNumberOfNormal; ++i) {
                if (!hitNormalList.get(i)) {
                    normalMap.put(i, normalMap.get(i) + 1);
                }
            }

            boolean hitAllNumbers = true;
            for (int i = 0; i < maxNumberOfNormal; ++i) {
                hitAllNumbers &= hitNormalList.get(i);
            }
            for (int i = 0; i < maxNumberOfSpecial; ++i) {
                hitAllNumbers &= hitSpecialList.get(i);
            }

            if (hitAllNumbers) break;
        }

        normalMap.remove(0);
        specialMap.remove(0);

        Map<Integer, Integer> sortedNotShowNormalMap = AnalyzeResult.sortByValue(normalMap);
        Map<Integer, Integer> sortedNotShowSpecialMap = AnalyzeResult.sortByValue(specialMap);

        for (Integer key : sortedNotShowNormalMap.keySet()) {
            mNormalNotShow.add(new Pair<>(key, sortedNotShowNormalMap.get(key)));
        }

        for (Integer key : sortedNotShowSpecialMap.keySet()) {
            mSpecialNotShow.add(new Pair<>(key, sortedNotShowSpecialMap.get(key)));
        }
    }

    public List<Pair<Integer, Integer>> getLongestNormalNotShowList() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalNotShow);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLongestSpecialNotShowList() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialNotShow);
        Collections.reverse(rtn);
        return rtn;
    }
}
