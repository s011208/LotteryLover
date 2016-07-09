package yhh.bj4.lotterylover.fragments.calendar.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import yhh.bj4.lotterylover.R;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class CalendarItemDateHolder extends CalendarBaseHolder {
    private TextView mText;
    private RelativeLayout mBorder;
    private ImageView mSelectedBackground;
    private ImageView mShowTips;

    public CalendarItemDateHolder(View itemView) {
        super(itemView);
        mBorder = (RelativeLayout) itemView;
        mText = (TextView) itemView.findViewById(R.id.calendar_text);
        mSelectedBackground = (ImageView) itemView.findViewById(R.id.selected_background);
        mShowTips = (ImageView) itemView.findViewById(R.id.show_tip);
    }

    public ImageView getShowTips() {
        return mShowTips;
    }

    public RelativeLayout getBorder() {
        return mBorder;
    }

    public TextView getText() {
        return mText;
    }

    public ImageView getSelectedBackground() {
        return mSelectedBackground;
    }
}
