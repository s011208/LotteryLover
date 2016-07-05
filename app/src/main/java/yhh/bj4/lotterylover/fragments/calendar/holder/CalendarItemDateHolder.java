package yhh.bj4.lotterylover.fragments.calendar.holder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import yhh.bj4.lotterylover.R;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class CalendarItemDateHolder extends CalendarBaseHolder {
    private TextView mText;
    private FrameLayout mBorder;

    public CalendarItemDateHolder(View itemView) {
        super(itemView);
        mBorder = (FrameLayout) itemView;
        mText = (TextView) itemView.findViewById(R.id.calendar_text);
    }

    public FrameLayout getBorder() {
        return mBorder;
    }

    public TextView getText() {
        return mText;
    }
}
