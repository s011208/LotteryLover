package yhh.bj4.lotterylover.views.table.main.holder;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import yhh.bj4.lotterylover.LotteryLover;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class OverallContentHolder extends MainTableHolder {
    private TextView mTextView;

    private float mCurrentScale = LotteryLover.VALUE_DIGIT_SCALE_SIZE_NORMAL;

    private float mOriginTextSize;

    public OverallContentHolder(View itemView, float scale) {
        super(itemView);
        mTextView = (TextView) itemView;
        mOriginTextSize = mTextView.getTextSize();
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextView.getTextSize() * scale);
        mCurrentScale = scale;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void updateScaleIfNecessary(float scale) {
        if (mCurrentScale == scale) {
            return;
        }
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOriginTextSize * scale);
        mCurrentScale = scale;
    }
}
