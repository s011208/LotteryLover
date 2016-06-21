package yhh.bj4.lotterylover;

/**
 * Created by yenhsunhuang on 2016/6/15.
 */
public class LotteryLover {
    public static final String KEY_LTO_TYPE = "lto_type";

    public static final int LTO_TYPE_LTO = 0;
    public static final int LTO_TYPE_LTO2C = 1;
    public static final int LTO_TYPE_LTO7C = 2;
    public static final int LTO_TYPE_LTO_BIG = 3;
    public static final int LTO_TYPE_LTO_DOF = 4;
    public static final int LTO_TYPE_LTO_HK = 5;

    public static final String KEY_LIST_TYPE = "list_type";

    public static final int LIST_TYPE_OVERALL = 0;
    public static final int LIST_TYPE_NUMERIC = 1;
    public static final int LIST_TYPE_PLUS_TOGETHER = 2;
    public static final int LIST_TYPE_LAST_DIGIT = 3;
    public static final int LIST_TYPE_PLUS_AND_MINUS = 4;

    public static final String KEY_DIGIT_SIZE = "digit_size";
    public static final int DIGIT_SIZE_TINY = 0;
    public static final int DIGIT_SIZE_SMALL = 1;
    public static final int DIGIT_SIZE_NORMAL = 2;
    public static final int DIGIT_SIZE_LARGE = 3;
    public static final int DIGIT_SIZE_HUGE = 4;

    public static final float VALUE_DIGIT_SIZE_TINY = .70f;
    public static final float VALUE_DIGIT_SIZE_SMALL = .85f;
    public static final float VALUE_DIGIT_SIZE_NORMAL = 1f;
    public static final float VALUE_DIGIT_SIZE_LARGE = 1.15f;
    public static final float VALUE_DIGIT_SIZE_HUGE = 1.3f;
}
