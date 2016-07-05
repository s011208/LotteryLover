package yhh.bj4.lotterylover;

import android.os.Bundle;

/**
 * Created by yenhsunhuang on 2016/7/5.
 */
public class CalendarActivity extends BaseActivity {
    @Override
    public int getContentViewResource() {
        return R.layout.calendar_fragment;
    }

    @Override
    public String getActivityName() {
        return CalendarActivity.class.getSimpleName();
    }

    @Override
    protected void initActionBar(Bundle savedInstanceState) {

    }

    @Override
    protected void initViewComponents(Bundle savedInstanceState) {

    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {

    }
}
