package yhh.bj4.lotterylover.parser.ltoMM;

import android.net.Uri;

import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.provider.LotteryProvider;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class LtoMM extends LotteryItem {
    public static final String TABLE_NAME = "ltomm";
    public static final Uri DATA_URI = LotteryProvider.getUri(TABLE_NAME);

    public LtoMM(Map<String, String> map) {
        super(map);
    }

    public LtoMM(long seq, long dateTime, List<Integer> normalNumbers, List<Integer> specialNumbers, String memo, String extra) {
        super(seq, dateTime, normalNumbers, specialNumbers, memo, extra);
    }

    public static int getNormalNumbersCount() {
        return 5;
    }

    public static int getSpecialNumbersCount() {
        return 1;
    }

    public static int getMaximumNormalNumber() {
        return 75;
    }

    public static int getMaximumSpecialNumber() {
        return 15;
    }
}
