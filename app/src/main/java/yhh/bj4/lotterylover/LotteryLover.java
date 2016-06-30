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
    public static final int LTO_TYPE_LTO_539 = 6;
    public static final int LTO_TYPE_LTO_POW = 7;
    public static final int LTO_TYPE_LTO_MM = 8;
    public static final int LTO_TYPE_LTO_J6 = 9;
    public static final int LTO_TYPE_LTO_TOTO = 10;
    public static final int LTO_TYPE_LTO_AU_POW = 11;
    public static final int LTO_TYPE_LTO_EM = 12;
    public static final int LTO_TYPE_LTO_LIST3 = 13;
    public static final int LTO_TYPE_LTO_LIST4 = 14;

    public static final String KEY_LIST_TYPE = "list_type";

    public static final int LIST_TYPE_OVERALL = 0;
    public static final int LIST_TYPE_NUMERIC = 1;
    public static final int LIST_TYPE_PLUS_TOGETHER = 2;
    public static final int LIST_TYPE_LAST_DIGIT = 3;
    public static final int LIST_TYPE_PLUS_AND_MINUS = 4;
    public static final int LIST_TYPE_COMBINE_LIST = 5;

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
    public static final String KEY_INIT_LTO_539 = "init_lto_539";
    public static final String KEY_INIT_LTO_POW = "init_lto_pow";
    public static final String KEY_INIT_LTO_MM = "init_lto_mm";
    public static final String KEY_INIT_LTO_J6 = "init_lto_j6";
    public static final String KEY_INIT_LTO_TOTO = "init_lto_toto";
    public static final String KEY_INIT_LTO_AU_POW = "init_lto_au_pow";
    public static final String KEY_INIT_LTO_EM = "init_lto_em";
    public static final String KEY_INIT_LTO_LIST3 = "init_lto_list3";
    public static final String KEY_INIT_LTO_LIST4 = "init_lto_list4";

    public static final String KEY_COMBINE_SPECIAL = "combine_special";
    public static final String KEY_SHOW_SUB_TOTAL_ONLY = "show_sub_total_only";

    public static final String KEY_LTO_UPDATE_TIME(String className) {
        return "update_time_" + className;
    }

    public static final String KEY_DISPLAY_ORIENTATION = "display_orientation";
    public static final int VALUE_BY_DEVICE = 0;
    public static final int VALUE_LANDSCAPE = 1;
    public static final int VALUE_PORTRAIT = 2;

    public static final String KEY_REMOTE_CONFIG_VERSION = "key_remote_config_version";
    public static final String KEY_SHOW_MONTHLY_DATA_ALWAYS = "show_monthly_data_only_always";
    public static final String KEY_SHOW_ADS = "show_ads";
    public static final String KEY_SET_TABLE_BACKGROUND_FROM_WEB = "set_table_background_from_web";
}
