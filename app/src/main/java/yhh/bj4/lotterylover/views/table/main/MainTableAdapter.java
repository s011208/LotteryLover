package yhh.bj4.lotterylover.views.table.main;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.helpers.RetrieveLotteryItemDataHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.views.table.main.item.MainTableItem;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class MainTableAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MainTableAdapter";
    private static final boolean DEBUG = Utilities.DEBUG;

    private static final int TYPE_OVER_ALL_CONTENT = 0;
    private static final int TYPE_OVER_ALL_MONTHLY_TOTAL = 1;

    private int mLtoType, mListType;

    private ArrayList<MainTableItem> mData = new ArrayList<>();

    private Context mContext;

    public MainTableAdapter(Context context, int ltoType, int listType) {
        mContext = context;
        updateData(ltoType, listType);
    }

    public void updateData(int ltoType, int listType) {
        mListType = listType;
        mLtoType = ltoType;
        new RetrieveLotteryItemDataHelper(mContext, new RetrieveLotteryItemDataHelper.Callback() {
            @Override
            public void onFinished(List<LotteryItem> data) {
                if (data == null || data.isEmpty()) return;
                new AdapterDataGenerator(mLtoType, mListType, data, new AdapterDataGenerator.Callback() {
                    @Override
                    public void onFinished(ArrayList<MainTableItem> data) {
                        mData.clear();
                        if (data != null && !data.isEmpty()) {
                            mData.addAll(data);
                        }
                        notifyDataSetChanged();
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, ltoType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_OVER_ALL_CONTENT:
                break;
            case TYPE_OVER_ALL_MONTHLY_TOTAL:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public MainTableItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }
}
