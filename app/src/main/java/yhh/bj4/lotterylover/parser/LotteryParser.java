package yhh.bj4.lotterylover.parser;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.parser.lto.LtoParser;
import yhh.bj4.lotterylover.parser.lto2c.Lto2CParser;
import yhh.bj4.lotterylover.parser.lto7c.Lto7CParser;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHKParser;
import yhh.bj4.lotterylover.parser.ltobig.LtoBigParser;
import yhh.bj4.lotterylover.parser.ltodof.LtoDofParser;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public abstract class LotteryParser extends AsyncTask<Void, Void, int[]> {
    public static final boolean DEBUG = Utilities.DEBUG;

    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;

    public final String TAG = getTag();
    private Callback mCallback;

    public LotteryParser(Callback cb) {
        mCallback = cb;
    }

    public abstract String getTag();

    public abstract String getBaseUrl();

    public abstract String getPageParameter();

    public abstract int getPageParameterValue();

    public abstract String getOrderByParameter();

    public abstract String getOrderByParameterValue();

    public abstract int getTableTdCount();

    public String getUrl() {
        return (getBaseUrl() + "?" + getPageParameter() + "=" + getPageParameterValue() + "&" + getOrderByParameter() + "=" + getOrderByParameterValue());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mCallback != null) {
            mCallback.onStart(getPageParameterValue());
        }
    }

    @Override
    protected void onPostExecute(int[] results) {
        super.onPostExecute(results);
        if (mCallback != null) {
            mCallback.onFinish(getPageParameterValue(), results);
        }
    }

    public interface Callback {
        void onStart(int page);

        void onFinish(int page, int[] results);
    }

    public static LotteryParser getParserFromType(Context context, int ltoType, int page, Callback cb) {
        switch (ltoType) {
            case LotteryLover.LTO_TYPE_LTO:
                return new LtoParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO2C:
                return new Lto2CParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO7C:
                return new Lto7CParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_BIG:
                return new LtoBigParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_DOF:
                return new LtoDofParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_HK:
                return new LtoHKParser(context, page, cb);
            default:
                throw new RuntimeException("wrong lto type");
        }
    }
}
