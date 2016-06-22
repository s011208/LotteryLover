package yhh.bj4.lotterylover.views.table.main;

import android.os.AsyncTask;

import java.util.ArrayList;

import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.views.table.main.item.MainTableItem;

/**
 * Created by yenhsunhuang on 2016/6/22.
 */
public class UpdatePlusAndMinusHelper extends AsyncTask<Void, Void, Void> {
    public interface Callback {
        void onFinished();
    }

    private final ArrayList<LotteryItem> mItems = new ArrayList<>();
    private final ArrayList<MainTableItem> mResults = new ArrayList<>();
    private final int mValue;
    private final Callback mCallback;

    public UpdatePlusAndMinusHelper(ArrayList<LotteryItem> items, int value, ArrayList<MainTableItem> data,
                                    Callback cb) {
        mItems.addAll(items);
        mValue = value;
        mResults.addAll(data);
        mCallback = cb;
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallback == null) return;
        mCallback.onFinished();
    }
}
