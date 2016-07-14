package yhh.bj4.lotterylover.fragments.analyze.result;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/14.
 */
public class DrawingTime {
    private final int mLotteryType;

    private final List<LotteryItem> mLotteryItems = new ArrayList<>();

    private final List<Pair<Integer, Integer>> mNormalDrawingTimesAllPeriod = new ArrayList<>();
    private final List<Pair<Integer, Integer>> mSpecialDrawingTimesAllPeriod = new ArrayList<>();

    // top 10
    private final List<Pair<Integer, Integer>> mNormalDrawingTimes10 = new ArrayList<>();
    private final List<Pair<Integer, Integer>> mSpecialDrawingTimes10 = new ArrayList<>();

    // top 20
    private final List<Pair<Integer, Integer>> mNormalDrawingTimes20 = new ArrayList<>();
    private final List<Pair<Integer, Integer>> mSpecialDrawingTimes20 = new ArrayList<>();

    // this month
    private final List<Pair<Integer, Integer>> mNormalDrawingTimesThisMonth = new ArrayList<>();
    private final List<Pair<Integer, Integer>> mSpecialDrawingTimesThisMonth = new ArrayList<>();

    // last 1 month
    private final List<Pair<Integer, Integer>> mNormalDrawingTimesLast1Month = new ArrayList<>();
    private final List<Pair<Integer, Integer>> mSpecialDrawingTimesLast1Month = new ArrayList<>();

    // last 2 months
    private final List<Pair<Integer, Integer>> mNormalDrawingTimesLast2Month = new ArrayList<>();
    private final List<Pair<Integer, Integer>> mSpecialDrawingTimesLast2Month = new ArrayList<>();


    public DrawingTime(int ltoType, List<LotteryItem> items) {
        mLotteryType = ltoType;
        mLotteryItems.addAll(items);
    }

    public void process() {
        processDrawingTimes();
    }

    private void processDrawingTimes(Map<Integer, Integer> drawingTimesNormalMap, Map<Integer, Integer> drawingTimesSpecialMap,
                                     boolean hasSpecial, final int period) {
        drawingTimesNormalMap.clear();
        drawingTimesSpecialMap.clear();
        int index = 0;
        for (LotteryItem item : mLotteryItems) {
            if (period != -1 && index >= period) return;
            for (int normal : item.getNormalNumbers()) {
                Integer value = drawingTimesNormalMap.get(normal);
                if (value == null) {
                    drawingTimesNormalMap.put(normal, 1);
                } else {
                    drawingTimesNormalMap.put(normal, value + 1);
                }
            }
            if (hasSpecial) {
                for (int normal : item.getSpecialNumbers()) {
                    Integer value = drawingTimesSpecialMap.get(normal);
                    if (value == null) {
                        drawingTimesSpecialMap.put(normal, 1);
                    } else {
                        drawingTimesSpecialMap.put(normal, value + 1);
                    }
                }
            } else {
                for (int normal : item.getSpecialNumbers()) {
                    Integer value = drawingTimesNormalMap.get(normal);
                    if (value == null) {
                        drawingTimesNormalMap.put(normal, 1);
                    } else {
                        drawingTimesNormalMap.put(normal, value + 1);
                    }
                }
            }
            ++index;
        }
    }

    private void processDrawingTimes() {
        final boolean hasSpecial = LotteryItem.getMaximumSpecialNumber(mLotteryItems.get(0)) > 0;
        final Map<Integer, Integer> drawingTimesNormalMap = new HashMap<>();
        final Map<Integer, Integer> drawingTimesSpecialMap = new HashMap<>();

        // all period
        processDrawingTimes(drawingTimesNormalMap, drawingTimesSpecialMap, hasSpecial, -1);
        Map<Integer, Integer> sortedDrawingTimesNormalMap = AnalyzeResult.sortByValue(drawingTimesNormalMap);
        Map<Integer, Integer> sortedDrawingTimesSpecialMap = AnalyzeResult.sortByValue(drawingTimesSpecialMap);

        for (Integer key : sortedDrawingTimesNormalMap.keySet()) {
            mNormalDrawingTimesAllPeriod.add(new Pair<>(key, sortedDrawingTimesNormalMap.get(key)));
        }

        for (Integer key : sortedDrawingTimesSpecialMap.keySet()) {
            mSpecialDrawingTimesAllPeriod.add(new Pair<>(key, sortedDrawingTimesSpecialMap.get(key)));
        }

        //recent 10
        processDrawingTimes(drawingTimesNormalMap, drawingTimesSpecialMap, hasSpecial, 10);
        sortedDrawingTimesNormalMap = AnalyzeResult.sortByValue(drawingTimesNormalMap);
        sortedDrawingTimesSpecialMap = AnalyzeResult.sortByValue(drawingTimesSpecialMap);

        for (Integer key : sortedDrawingTimesNormalMap.keySet()) {
            mNormalDrawingTimes10.add(new Pair<>(key, sortedDrawingTimesNormalMap.get(key)));
        }

        for (Integer key : sortedDrawingTimesSpecialMap.keySet()) {
            mSpecialDrawingTimes10.add(new Pair<>(key, sortedDrawingTimesSpecialMap.get(key)));
        }

        //recent 20
        processDrawingTimes(drawingTimesNormalMap, drawingTimesSpecialMap, hasSpecial, 20);
        sortedDrawingTimesNormalMap = AnalyzeResult.sortByValue(drawingTimesNormalMap);
        sortedDrawingTimesSpecialMap = AnalyzeResult.sortByValue(drawingTimesSpecialMap);

        for (Integer key : sortedDrawingTimesNormalMap.keySet()) {
            mNormalDrawingTimes20.add(new Pair<>(key, sortedDrawingTimesNormalMap.get(key)));
        }

        for (Integer key : sortedDrawingTimesSpecialMap.keySet()) {
            mSpecialDrawingTimes20.add(new Pair<>(key, sortedDrawingTimesSpecialMap.get(key)));
        }

        // this month
        Calendar calendar = Calendar.getInstance();
        final int currentMonth = calendar.get(Calendar.MONTH);

        int itemCount = 0;
        for (int i = 0; i < mLotteryItems.size(); ++i) {
            calendar.setTimeInMillis(mLotteryItems.get(i).getDrawingDateTime());
            final int month = calendar.get(Calendar.MONTH);
            if (currentMonth != month) {
                itemCount = i;
                break;
            }
        }

        processDrawingTimes(drawingTimesNormalMap, drawingTimesSpecialMap, hasSpecial, itemCount);
        sortedDrawingTimesNormalMap = AnalyzeResult.sortByValue(drawingTimesNormalMap);
        sortedDrawingTimesSpecialMap = AnalyzeResult.sortByValue(drawingTimesSpecialMap);

        for (Integer key : sortedDrawingTimesNormalMap.keySet()) {
            mNormalDrawingTimesThisMonth.add(new Pair<>(key, sortedDrawingTimesNormalMap.get(key)));
        }

        for (Integer key : sortedDrawingTimesSpecialMap.keySet()) {
            mSpecialDrawingTimesThisMonth.add(new Pair<>(key, sortedDrawingTimesSpecialMap.get(key)));
        }

        // last 1 month
        itemCount = 0;
        for (int i = 0; i < mLotteryItems.size(); ++i) {
            calendar.setTimeInMillis(mLotteryItems.get(i).getDrawingDateTime());
            final int month = calendar.get(Calendar.MONTH);
            if (currentMonth == Calendar.JANUARY && month == Calendar.DECEMBER) {
                continue;
            }
            if (currentMonth < month - 1) {
                itemCount = i;
                break;
            }
        }

        processDrawingTimes(drawingTimesNormalMap, drawingTimesSpecialMap, hasSpecial, itemCount);
        sortedDrawingTimesNormalMap = AnalyzeResult.sortByValue(drawingTimesNormalMap);
        sortedDrawingTimesSpecialMap = AnalyzeResult.sortByValue(drawingTimesSpecialMap);

        for (Integer key : sortedDrawingTimesNormalMap.keySet()) {
            mNormalDrawingTimesLast1Month.add(new Pair<>(key, sortedDrawingTimesNormalMap.get(key)));
        }

        for (Integer key : sortedDrawingTimesSpecialMap.keySet()) {
            mSpecialDrawingTimesLast1Month.add(new Pair<>(key, sortedDrawingTimesSpecialMap.get(key)));
        }

        // last 2 months
        itemCount = 0;
        for (int i = 0; i < mLotteryItems.size(); ++i) {
            calendar.setTimeInMillis(mLotteryItems.get(i).getDrawingDateTime());
            final int month = calendar.get(Calendar.MONTH);
            if (currentMonth == Calendar.JANUARY && (month == Calendar.DECEMBER || month == Calendar.NOVEMBER)) {
                continue;
            } else if (currentMonth == Calendar.FEBRUARY && (month == Calendar.DECEMBER || month == Calendar.JANUARY)) {
                continue;
            }
            if (currentMonth < month - 2) {
                itemCount = i;
                break;
            }
        }

        processDrawingTimes(drawingTimesNormalMap, drawingTimesSpecialMap, hasSpecial, itemCount);
        sortedDrawingTimesNormalMap = AnalyzeResult.sortByValue(drawingTimesNormalMap);
        sortedDrawingTimesSpecialMap = AnalyzeResult.sortByValue(drawingTimesSpecialMap);

        for (Integer key : sortedDrawingTimesNormalMap.keySet()) {
            mNormalDrawingTimesLast2Month.add(new Pair<>(key, sortedDrawingTimesNormalMap.get(key)));
        }

        for (Integer key : sortedDrawingTimesSpecialMap.keySet()) {
            mSpecialDrawingTimesLast2Month.add(new Pair<>(key, sortedDrawingTimesSpecialMap.get(key)));
        }
    }

    public List<Pair<Integer, Integer>> getTopNormalDrawingTimesLast2Month() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimesLast2Month);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastNormalDrawingTimesLast2Month() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimesLast2Month);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopSpecialDrawingTimesLast2Month() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimesLast2Month);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastSpecialDrawingTimesLast2Month() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimesLast2Month);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopNormalDrawingTimesLast1Month() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimesLast1Month);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastNormalDrawingTimesLast1Month() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimesLast1Month);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopSpecialDrawingTimesLast1Month() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimesLast1Month);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastSpecialDrawingTimesLast1Month() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimesLast1Month);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopNormalDrawingTimesThisMonth() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimesThisMonth);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastNormalDrawingTimesThisMonth() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimesThisMonth);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopSpecialDrawingTimesThisMonth() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimesThisMonth);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastSpecialDrawingTimesThisMonth() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimesThisMonth);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopNormalDrawingTimesAllPeriod() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimesAllPeriod);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastNormalDrawingTimesAllPeriod() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimesAllPeriod);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopSpecialDrawingTimesAllPeriod() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimesAllPeriod);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastSpecialDrawingTimesAllPeriod() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimesAllPeriod);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopNormalDrawingTimes10() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimes10);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastNormalDrawingTimes10() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimes10);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopSpecialDrawingTimes10() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimes10);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastSpecialDrawingTimes10() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimes10);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopNormalDrawingTimes20() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimes20);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastNormalDrawingTimes20() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mNormalDrawingTimes20);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getTopSpecialDrawingTimes20() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimes20);
        Collections.reverse(rtn);
        return rtn;
    }

    public List<Pair<Integer, Integer>> getLastSpecialDrawingTimes20() {
        List<Pair<Integer, Integer>> rtn = new ArrayList<>();
        rtn.addAll(mSpecialDrawingTimes20);
        return rtn;
    }

}
