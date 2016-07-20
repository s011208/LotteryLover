package yhh.bj4.lotterylover.parser.lto2c;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.firebase.FirebaseDatabaseHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.parser.LotteryParser;

/**
 * Created by yenhsunhuang on 2016/6/14.
 * http://www.pilio.idv.tw/ltobig/list.asp?indexpage=1&orderby=new
 */
public class Lto2CParser extends LotteryParser {

    private int mParsePage = 0;
    private Context mContext;

    public Lto2CParser(Context context, int parsePage, Callback cb) {
        super(cb);
        mParsePage = parsePage;
        mContext = context.getApplicationContext();
    }

    @Override
    public String getTag() {
        return Lto2CParser.class.getSimpleName();
    }

    @Override
    public String getBaseUrl() {
        return "http://www.pilio.idv.tw/lto2c/listbbk.asp";
    }

    @Override
    public String getPageParameter() {
        return "indexpage";
    }

    @Override
    public int getPageParameterValue() {
        return mParsePage;
    }

    @Override
    public String getOrderByParameter() {
        return "orderby";
    }

    @Override
    public String getOrderByParameterValue() {
        return "new";
    }

    @Override
    public int getTableTdCount() {
        return 5;
    }

    @Override
    protected int[] doInBackground(Void... params) {
        if (DEBUG) {
            Log.d(TAG, "doInBackground, " + getUrl());
        }
        try {
            List<LotteryItem> items = new ArrayList<>();
            Document doc = Jsoup.connect(getUrl()).timeout(3000).execute().parse();
            if (DEBUG) {
                Log.d(TAG, "parse done");
                Log.d(TAG, "title: " + doc.title());
            }
            Elements tableTr = doc.select("table tr");
            for (Element ele : tableTr) {
                Elements tds = ele.select("td");
                if (tds.size() != getTableTdCount()) continue;
                try {
                    long seq = Long.valueOf(tds.get(0).text());
                    long drawingTime = Utilities.convertStringDateToLong(tds.get(1).text());
                    List<Integer> normalNumber = Utilities.convertStringNumberToList(tds.get(2).text());
                    List<Integer> specialNumber = Utilities.convertStringNumberToList(tds.get(3).text());
                    if (normalNumber.size() != Lto2C.getNormalNumbersCount() ||
                            specialNumber.size() != Lto2C.getSpecialNumbersCount()) {
                        // TODO failed to get right results
                        continue;
                    }
                    String memo = tds.get(4).text();
                    items.add(new Lto2C(seq, drawingTime, normalNumber, specialNumber, memo, ""));
                    if (DEBUG) {
                        Log.v(TAG, "----------");
                    }
                    for (Element td : tds) {
                        if (DEBUG) {
                            Log.v(TAG, "td txt: " + td.text());
                        }
                    }
                } catch (NumberFormatException e) {
                    if (DEBUG) {
                        Log.v(TAG, "ignore wrong data set");
                    }
                }
            }
            if (!items.isEmpty()) {
                // bulk insert
                ContentValues[] cvs = new ContentValues[items.size()];
                for (int i = 0; i < items.size(); ++i) {
                    cvs[i] = items.get(i).toContentValues();
                }
                int result = mContext.getContentResolver().bulkInsert(Lto2C.DATA_URI, cvs);
                if (result != 0) {
                    FirebaseDatabaseHelper.setLtoValues(items, mContext);
                }
            }
        } catch (IOException e) {
            if (DEBUG) {
                Log.w(TAG, "unexpected exception", e);
            }
            return new int[]{RESULT_CANCELED};
        }
        return new int[]{RESULT_OK};
    }
}
