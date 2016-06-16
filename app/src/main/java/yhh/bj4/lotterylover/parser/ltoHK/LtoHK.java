package yhh.bj4.lotterylover.parser.ltoHK;

import android.net.Uri;

import java.util.List;

import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.provider.LotteryProvider;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public class LtoHK extends LotteryItem {
    public static final String TABLE_NAME = "lto";
    public static final Uri DATA_URI = LotteryProvider.getUri(TABLE_NAME);

    public LtoHK(long seq, long dateTime, List<Integer> normalNumbers, List<Integer> specialNumbers, String memo, String extra) {
        super(seq, dateTime, normalNumbers, specialNumbers, memo, extra);
    }

    public static int getNormalNumbersCount() {
        return 6;
    }

    public static int getSpecialNumbersCount() {
        return 1;
    }

    public static int getMaximumNormalNumber() {
        return 49;
    }

    public static int getMaximumSpecialNumber() {
        return -1;
    }
}
