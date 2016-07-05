package yhh.bj4.lotterylover.fragments.calendar;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.fragments.calendar.holder.TodayLotteryItemHolder;
import yhh.bj4.lotterylover.fragments.calendar.item.TodayLotteryItem;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class LotteryAdapter extends RecyclerView.Adapter implements RetrieveTodayLotteryTask.Callback {
    private final Context mContext;
    private final LayoutInflater mInflater;

    private int mYear, mMonth, mDay;
    private final List<TodayLotteryItem> mItems = new ArrayList<>();

    public LotteryAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDate(int y, int m, int d) {
        mYear = y;
        mMonth = m;
        mDay = d;
        new RetrieveTodayLotteryTask(mContext, this, y, m, d).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TodayLotteryItemHolder(mInflater.inflate(R.layout.lottery_adapter_holder_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onFinish(ArrayList<TodayLotteryItem> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }
}
