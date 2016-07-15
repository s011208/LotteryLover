package yhh.bj4.lotterylover.fragments.analyze.list34;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yenhsunhuang on 2016/7/14.
 */
public class List34ResultAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final List34Result mResult;

    public List34ResultAdapter(Context context, List34Result result) {
        mContext = context;
        mResult = result;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new TextView(mContext));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        List34Result.ItemContent item = mResult.getResultList().get(position);
        ((ViewHolder)holder).getText().setText(item.toString());
    }

    @Override
    public int getItemCount() {
        return mResult.getResultList().size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mText;

        public ViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView;
        }

        TextView getText() {
            return mText;
        }
    }
}
