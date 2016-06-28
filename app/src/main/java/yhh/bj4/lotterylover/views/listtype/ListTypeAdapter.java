package yhh.bj4.lotterylover.views.listtype;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/6/15.
 */
public class ListTypeAdapter extends RecyclerView.Adapter {

    public interface Callback {
        void onListTypeChanged(int type);
    }

    private Context mContext;
    private final String[] mItems;
    private int mSelectedPosition = 0;
    private Callback mCallback;
    private int mLtoType;

    public ListTypeAdapter(Context context, Callback cb, int selectedPosition, int ltoType) {
        mContext = context;
        mCallback = cb;
        mItems = context.getResources().getStringArray(R.array.action_bar_list_type);
        mSelectedPosition = selectedPosition;
        mLtoType = ltoType;
        updateSelectedPosition();
    }

    public void setLtoType(int type) {
        mLtoType = type;
        updateSelectedPosition();
        notifyDataSetChanged();
    }

    public boolean updateSelectedPosition() {
        if (mLtoType == LotteryLover.LTO_TYPE_LTO_LIST3 || mLtoType == LotteryLover.LTO_TYPE_LTO_LIST4) {
            if (mSelectedPosition == LotteryLover.LIST_TYPE_LAST_DIGIT ||
                    mSelectedPosition == LotteryLover.LIST_TYPE_NUMERIC ||
                    mSelectedPosition == LotteryLover.LIST_TYPE_PLUS_AND_MINUS ||
                    mSelectedPosition == LotteryLover.LIST_TYPE_PLUS_TOGETHER) {
                mSelectedPosition = LotteryLover.LIST_TYPE_OVERALL;
                mCallback.onListTypeChanged(mSelectedPosition);
                return true;
            }
        } else {
            if (mSelectedPosition == LotteryLover.LIST_TYPE_COMBINE_LIST) {
                mSelectedPosition = LotteryLover.LIST_TYPE_OVERALL;
                mCallback.onListTypeChanged(mSelectedPosition);
                return true;
            }
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_type_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder textHolder = (ViewHolder) holder;
        textHolder.mText.setText(mItems[position]);
        if (mSelectedPosition == position) {
            textHolder.mText.setBackgroundColor(Utilities.getWindowBackgroundColor(mContext));
            textHolder.mText.setTextColor(Color.BLACK);
        } else {
            textHolder.mText.setBackgroundColor(Utilities.getPrimaryColor(mContext));
            textHolder.mText.setTextColor(Color.WHITE);
        }
        final int finalPosition = holder.getAdapterPosition();
        textHolder.mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(mSelectedPosition);
                mSelectedPosition = finalPosition;
                notifyItemChanged(mSelectedPosition);
                mCallback.onListTypeChanged(mSelectedPosition);
            }
        });
        if (mLtoType == LotteryLover.LTO_TYPE_LTO_LIST3 || mLtoType == LotteryLover.LTO_TYPE_LTO_LIST4) {
            if (position == LotteryLover.LIST_TYPE_COMBINE_LIST || position == LotteryLover.LIST_TYPE_OVERALL) {
                textHolder.mText.setEnabled(true);
            } else {
                textHolder.mText.setEnabled(false);
                textHolder.mText.setBackgroundColor(Utilities.getPrimaryDarkColor(mContext));
            }
        } else {
            if (position == LotteryLover.LIST_TYPE_COMBINE_LIST) {
                textHolder.mText.setEnabled(false);
                textHolder.mText.setBackgroundColor(Utilities.getPrimaryDarkColor(mContext));
            } else {
                textHolder.mText.setEnabled(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.length;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView;
        }
    }
}
