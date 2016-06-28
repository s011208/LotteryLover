package yhh.bj4.lotterylover.parser.LtoList3;

import android.net.Uri;

import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.provider.LotteryProvider;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class LtoList3 extends LotteryItem {
    public static final String TABLE_NAME = "ltolist3";
    public static final Uri DATA_URI = LotteryProvider.getUri(TABLE_NAME);

    public LtoList3(Map<String, String> map) {
        super(map);
    }

    public LtoList3(long seq, long dateTime, List<Integer> normalNumbers, List<Integer> specialNumbers, String memo, String extra) {
        super(seq, dateTime, normalNumbers, specialNumbers, memo, extra);
    }

    public static int getNormalNumbersCount() {
        return 3;
    }

    public static int getSpecialNumbersCount() {
        return 0;
    }

    public static int getMaximumNormalNumber() {
        return 3;
    }

    public static int getMaximumSpecialNumber() {
        return -1;
    }

    @Override
    public boolean sortNumbers() {
        return false;
    }
}
