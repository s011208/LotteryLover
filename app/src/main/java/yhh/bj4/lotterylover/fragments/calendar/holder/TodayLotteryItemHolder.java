package yhh.bj4.lotterylover.fragments.calendar.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class TodayLotteryItemHolder extends RecyclerView.ViewHolder {

    private final TextView mText;

    public TodayLotteryItemHolder(View itemView) {
        super(itemView);
        mText = (TextView) itemView;
    }

    public TextView getText() {
        return mText;
    }
}
