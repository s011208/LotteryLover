package yhh.bj4.lotterylover.fragments.analyze.list34;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;

/**
 * Created by yenhsunhuang on 2016/7/14.
 */
public class List34ResultAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final List34Result mResult;

    public List34ResultAdapter(Context context, List34Result result) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResult = result;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mResult.getLotteryType() == LotteryLover.LTO_TYPE_LTO_LIST3) {
            return new ViewHolder(mInflater.inflate(R.layout.list3_result_adapter_item, parent, false));
        } else if (mResult.getLotteryType() == LotteryLover.LTO_TYPE_LTO_LIST4) {
            return new ViewHolder(mInflater.inflate(R.layout.list4_result_adapter_item, parent, false));
        }
        throw new RuntimeException("unexpected type: " + mResult.getLotteryType());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        List34Result.ItemContent item = mResult.getResultList().get(position);
        ViewHolder holderItem = ((ViewHolder) holder);
        holderItem.mDrawingDate.setText(item.mDrawingDate);
        holderItem.mDrawingNumber.setText(item.mDrawingNumber);
        holderItem.mOddAndPlural.setText(item.mOddAndPlural);
        holderItem.mMod3.setText(item.mMod3);
        holderItem.mSum.setText(item.mSum);
        holderItem.mAverage.setText(item.mAverage);
        holderItem.mNumberOfOddAndPlural.setText(item.mNumberOfOddAndPlural);
    }

    @Override
    public int getItemViewType(int position) {
        return mResult.getLotteryType();
    }

    @Override
    public int getItemCount() {
        return mResult.getResultList().size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDrawingDate, mDrawingNumber, mOddAndPlural,
                mMod3, mSum, mAverage, mNumberOfOddAndPlural;

        public ViewHolder(View itemView) {
            super(itemView);
            mDrawingDate = (TextView) itemView.findViewById(R.id.drawing_time);
            mDrawingNumber = (TextView) itemView.findViewById(R.id.drawing_number);
            mOddAndPlural = (TextView) itemView.findViewById(R.id.odd_and_plural);
            mMod3 = (TextView) itemView.findViewById(R.id.mod3);
            mSum = (TextView) itemView.findViewById(R.id.sum);
            mAverage = (TextView) itemView.findViewById(R.id.average);
            mNumberOfOddAndPlural = (TextView) itemView.findViewById(R.id.number_of_odd_and_plural);
        }
    }
}
