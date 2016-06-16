package yhh.bj4.lotterylover.views.table.main;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.helpers.RetrieveLotteryItemDataHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class MainTableAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MainTableAdapter";
    private static final boolean DEBUG = Utilities.DEBUG;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CONTENT = 1;
    private static final int TYPE_MONTHLY_TOTAL = 2;
    private static final int TYPE_OVERALL_TOTAL = 3;

    private int mLtoType, mListType;

    private ArrayList<Object> mData = new ArrayList<>();

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
                mData.clear();
                mData.addAll(data);
                notifyDataSetChanged();
            }
        }, ltoType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView text = new TextView(mContext);
        text.setSingleLine();
        text.setMaxLines(1);
        return new Content(text);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
