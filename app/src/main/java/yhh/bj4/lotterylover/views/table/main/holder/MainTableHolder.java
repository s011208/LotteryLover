package yhh.bj4.lotterylover.views.table.main.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public abstract class MainTableHolder extends RecyclerView.ViewHolder {
    private View mItemView;

    public MainTableHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
    }

    public View getMainView() {
        return mItemView;
    }
}
