package yhh.bj4.lotterylover.views.table.main;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.helpers.RetrieveLotteryItemDataHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.views.table.main.holder.OverallContentHolder;
import yhh.bj4.lotterylover.views.table.main.item.MainTableItem;
import yhh.bj4.lotterylover.views.table.main.item.TypeNumeric;
import yhh.bj4.lotterylover.views.table.main.item.TypeOverall;
import yhh.bj4.lotterylover.views.table.main.item.TypePlusTogether;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class MainTableAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MainTableAdapter";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final int TYPE_OVER_ALL_CONTENT = 0;
    public static final int TYPE_NUMERIC = 1;
    public static final int TYPE_PLUS_TOGETHER = 2;

    private int mLtoType, mListType;

    private ArrayList<MainTableItem> mData = new ArrayList<>();

    private Context mContext;
    private LayoutInflater mInflater;

    public MainTableAdapter(Context context, int ltoType, int listType) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        updateData(ltoType, listType);
    }

    public void updateData(int ltoType, int listType) {
        mListType = listType;
        mLtoType = ltoType;
        new RetrieveLotteryItemDataHelper(mContext, new RetrieveLotteryItemDataHelper.Callback() {
            @Override
            public void onFinished(List<LotteryItem> data) {
                new AdapterDataGenerator(mLtoType, mListType, Utilities.getWindowBackgroundColor(mContext), data, new AdapterDataGenerator.Callback() {
                    @Override
                    public void onFinished(ArrayList<MainTableItem> data) {
                        mData.clear();
                        if (data != null && !data.isEmpty()) {
                            mData.addAll(data);
                        }
                        if (DEBUG) {
                            Log.i(TAG, "AdapterDataGenerator cb, size: " + data.size());
                        }
                        notifyDataSetChanged();
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, ltoType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_OVER_ALL_CONTENT:
            case TYPE_PLUS_TOGETHER:
            case TYPE_NUMERIC:
                holder = new OverallContentHolder(mInflater.inflate(R.layout.main_table_adapter_text, null));
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_OVER_ALL_CONTENT:
                bindOverallContent(holder, position);
                break;
            case TYPE_NUMERIC:
                bindNumericContent(holder, position);
                break;
            case TYPE_PLUS_TOGETHER:
                bindPlusTogetherContent(holder, position);
                break;
        }
    }

    private void bindPlusTogetherContent(RecyclerView.ViewHolder holder, int position) {
        final TypePlusTogether item = (TypePlusTogether) getItem(position);
        OverallContentHolder contentHolder = (OverallContentHolder) holder;
        contentHolder.getTextView().setText(item.getSpannableString());
    }

    private void bindNumericContent(RecyclerView.ViewHolder holder, int position) {
        final TypeNumeric item = (TypeNumeric) getItem(position);
        OverallContentHolder contentHolder = (OverallContentHolder) holder;
        contentHolder.getTextView().setText(item.getSpannableString());
    }

    private void bindOverallContent(RecyclerView.ViewHolder holder, int position) {
        final TypeOverall item = (TypeOverall) getItem(position);
        OverallContentHolder contentHolder = (OverallContentHolder) holder;
        contentHolder.getTextView().setText(item.getSpannableString());
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
