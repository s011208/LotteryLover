package yhh.bj4.lotterylover.views.listtype;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public ListTypeAdapter(Context context, Callback cb, int selectedPosition) {
        mContext = context;
        mCallback = cb;
        mItems = context.getResources().getStringArray(R.array.action_bar_list_type);
        mSelectedPosition = selectedPosition;
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
