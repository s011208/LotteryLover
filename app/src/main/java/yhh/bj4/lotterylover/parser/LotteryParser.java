package yhh.bj4.lotterylover.parser;

import android.app.Activity;
import android.os.AsyncTask;

import yhh.bj4.lotterylover.Utilities;

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

    public String getUrl() {
        return (getBaseUrl() + "?" + getPageParameter() + "=" + getPageParameterValue() + "&" + getOrderByParameter() + "=" + getOrderByParameterValue());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallback.onStart(getPageParameterValue());
    }

    @Override
    protected void onPostExecute(int[] results) {
        super.onPostExecute(results);
        mCallback.onFinish(getPageParameterValue(), results);
    }

    public interface Callback {
        void onStart(int page);

        void onFinish(int page, int[] results);
    }
}
