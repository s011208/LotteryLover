package yhh.bj4.lotterylover.fragments.calendar.holder;

import android.view.View;
import android.widget.TextView;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class CalendarItemWeekDayHolder extends CalendarBaseHolder {
    private TextView mText;


    public CalendarItemWeekDayHolder(View itemView) {
        super(itemView);
        mText = (TextView) itemView;
    }

    public TextView getText() {
        return mText;
    }
}
