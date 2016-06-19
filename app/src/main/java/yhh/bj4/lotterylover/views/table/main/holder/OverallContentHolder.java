package yhh.bj4.lotterylover.views.table.main.holder;

import android.view.View;
import android.widget.TextView;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class OverallContentHolder extends MainTableHolder {
    private TextView mTextView;
    public OverallContentHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView;
    }

    public TextView getTextView() {
        return mTextView;
    }
}
