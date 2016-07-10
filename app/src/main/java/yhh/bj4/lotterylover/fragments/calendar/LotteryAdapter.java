package yhh.bj4.lotterylover.fragments.calendar;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private final List<TodayLotteryItem> mItems = new ArrayList<>();

    public LotteryAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDate(int y, int m, int d) {
        new RetrieveTodayLotteryTask(mContext, this, y, m, d).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TodayLotteryItemHolder(mInflater.inflate(R.layout.lottery_adapter_holder_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TodayLotteryItem item = getItem(position);
        TextView text = ((TodayLotteryItemHolder) holder).getText();
        text.setText(item.getSpannableString());
        if (item.isShowTipItem()) {
            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar_item_show_tips_background, 0, 0, 0);
        } else {
            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    public TodayLotteryItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onFinish(List<TodayLotteryItem> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }
}
