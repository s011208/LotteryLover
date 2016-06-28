package yhh.bj4.lotterylover.parser.LtoList3;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.firebase.FirebaseDatabaseHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.parser.LotteryParser;

/**
 * Created by yenhsunhuang on 2016/6/14.
 * http://www.pilio.idv.tw/ltobig/list.asp?indexpage=1&orderby=new
 */
public class LtoList3Parser extends LotteryParser {

    private int mParsePage = 0;
    private Context mContext;

    public LtoList3Parser(Context context, int parsePage, Callback cb) {
        super(cb);
        mParsePage = parsePage;
        mContext = context.getApplicationContext();
    }

    @Override
    public String getTag() {
        return LtoList3Parser.class.getSimpleName();
    }

    @Override
    public String getBaseUrl() {
        return "http://www.pilio.idv.tw/lto/list3bbk.asp";
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
        return 7;
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
                if (tds.size() != getTableTdCount()) {
                    if (DEBUG) {
                        Log.v(TAG, "tds.size(): " + tds.size() + ", getTableTdCount(): " + getTableTdCount());
                    }
                    continue;
                }
                try {
                    long seq = Long.valueOf(tds.get(0).text());
                    long drawingTime = Utilities.convertStringDateToLong(tds.get(1).text());
                    List<Integer> normalNumber = new ArrayList<>();
                    int value = Integer.valueOf(tds.get(2).text());
                    for (int i=0; i<3; ++i) {
                        normalNumber.add(value % 10);
                        value /= 10;
                    }
                    Collections.reverse(normalNumber);
                    if (normalNumber.size() != LtoList3.getNormalNumbersCount()) {
                        // TODO failed to get right results
                        if (DEBUG) {
                            Log.w(TAG, "failed to get right results");
                        }
                        continue;
                    }
                    items.add(new LtoList3(seq, drawingTime, normalNumber, new ArrayList<Integer>(), "", ""));
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
                        Log.v(TAG, "ignore wrong data set", e);
                    }
                }
            }
            if (!items.isEmpty()) {
                // bulk insert
                ContentValues[] cvs = new ContentValues[items.size()];
                for (int i = 0; i < items.size(); ++i) {
                    cvs[i] = items.get(i).toContentValues();
                }
                int result = mContext.getContentResolver().bulkInsert(LtoList3.DATA_URI, cvs);
                if (DEBUG) {
                    Log.d(TAG, "insert result: " + result);
                }
                if (result != 0) {
                    FirebaseDatabaseHelper.setLtoValues(items);
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
