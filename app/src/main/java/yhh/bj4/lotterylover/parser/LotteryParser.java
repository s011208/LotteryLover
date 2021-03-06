package yhh.bj4.lotterylover.parser;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.parser.LtoList3.LtoList3Parser;
import yhh.bj4.lotterylover.parser.lto.LtoParser;
import yhh.bj4.lotterylover.parser.lto2c.Lto2CParser;
import yhh.bj4.lotterylover.parser.lto539.Lto539Parser;
import yhh.bj4.lotterylover.parser.lto7c.Lto7CParser;
import yhh.bj4.lotterylover.parser.ltoHK.LtoHKParser;
import yhh.bj4.lotterylover.parser.ltoJ6.LtoJ6Parser;
import yhh.bj4.lotterylover.parser.ltoMM.LtoMMParser;
import yhh.bj4.lotterylover.parser.ltoToTo.LtoToToParser;
import yhh.bj4.lotterylover.parser.ltoapow.LtoAuPowParser;
import yhh.bj4.lotterylover.parser.ltobig.LtoBigParser;
import yhh.bj4.lotterylover.parser.ltodof.LtoDofParser;
import yhh.bj4.lotterylover.parser.ltoem.LtoEmParser;
import yhh.bj4.lotterylover.parser.ltolist4.LtoList4Parser;
import yhh.bj4.lotterylover.parser.ltopow.LtoPowParser;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public abstract class LotteryParser extends AsyncTask<Void, Void, int[]> {
    public static final boolean DEBUG = false;

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
            case LotteryLover.LTO_TYPE_LTO_539:
                return new Lto539Parser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_POW:
                return new LtoPowParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_MM:
                return new LtoMMParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_J6:
                return new LtoJ6Parser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_TOTO:
                return new LtoToToParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_AU_POW:
                return new LtoAuPowParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_EM:
                return new LtoEmParser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_LIST3:
                return new LtoList3Parser(context, page, cb);
            case LotteryLover.LTO_TYPE_LTO_LIST4:
                return new LtoList4Parser(context, page, cb);
            default:
                throw new RuntimeException("wrong lto type");
        }
    }
}
