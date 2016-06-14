package yhh.bj4.lotterylover.parser;

import android.os.AsyncTask;

import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/6/14.
 */
public abstract class LotteryParser extends AsyncTask<Void, Void, Void> {
    public static final boolean DEBUG = Utilities.DEBUG;

    public final String TAG = getTag();

    public abstract String getTag();

    public abstract String getBaseUrl();

    public abstract String getPageParameter();

    public abstract int getPageParameterValue();

    public abstract String getOrderByParameter();

    public abstract String getOrderByParameterValue();

    public String getUrl() {
        return (getBaseUrl() + "?" + getPageParameter() + "=" + getPageParameterValue() + "&" + getOrderByParameter() + "=" + getOrderByParameterValue());
    }

}
