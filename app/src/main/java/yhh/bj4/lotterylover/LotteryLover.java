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

    public static final String KEY_DIGIT_SCALE_SIZE = "digit_scale_size";
    public static final int DIGIT_SCALE_SIZE_TINY = 0;
    public static final int DIGIT_SCALE_SIZE_SMALL = 1;
    public static final int DIGIT_SCALE_SIZE_NORMAL = 2;
    public static final int DIGIT_SCALE_SIZE_LARGE = 3;
    public static final int DIGIT_SCALE_SIZE_HUGE = 4;

    public static final float VALUE_DIGIT_SCALE_SIZE_TINY = .70f;
    public static final float VALUE_DIGIT_SCALE_SIZE_SMALL = .85f;
    public static final float VALUE_DIGIT_SCALE_SIZE_NORMAL = 1f;
    public static final float VALUE_DIGIT_SCALE_SIZE_LARGE = 1.15f;
    public static final float VALUE_DIGIT_SCALE_SIZE_HUGE = 1.3f;

    public static final String KEY_ORDER = "display_order";
    public static final int ORDER_BY_ASC = 0;
    public static final int ORDER_BY_DESC = 1;

    public static final String KEY_DISPLAY_ROWS = "display_rows";
    public static final int DISPLAY_ROWS_50 = 0;
    public static final int DISPLAY_ROWS_100 = 1;
    public static final int DISPLAY_ROWS_150 = 2;
    public static final int DISPLAY_ROWS_200 = 3;
    public static final int DISPLAY_ROWS_500 = 4;
    public static final int DISPLAY_ROWS_ALL = 5;

    public static final int VALUE_DISPLAY_ROWS_50 = 50;
    public static final int VALUE_DISPLAY_ROWS_100 = 100;
    public static final int VALUE_DISPLAY_ROWS_150 = 150;
    public static final int VALUE_DISPLAY_ROWS_200 = 200;
    public static final int VALUE_DISPLAY_ROWS_500 = 500;
    public static final int VALUE_DISPLAY_ROWS_ALL = -1;

    public static final String KEY_INIT_LTO = "init_lto";
    public static final String KEY_INIT_LTO2C = "init_lto2c";
    public static final String KEY_INIT_LTO7C = "init_lto7c";
    public static final String KEY_INIT_LTO_BIG = "init_lto_big";
    public static final String KEY_INIT_LTO_DOF = "init_lto_dof";
    public static final String KEY_INIT_LTO_HK = "init_lto_hk";

    public static final String KEY_COMBINE_SPECIAL = "combine_special";

    public static final String KEY_LTO_UPDATE_TIME(String className) {
        return "update_time_" + className;
    }
}
