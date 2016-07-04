package yhh.bj4.lotterylover.fragments.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yhh.bj4.lotterylover.R;

/**
 * Created by yenhsunhuang on 2016/7/1.
 */
public class CalendarFragment extends Fragment {
    private RecyclerView mCalendar;
    private RecyclerView.Adapter mCalendarAdapter;

    private RecyclerView mTodayLottery;
    private RecyclerView.Adapter mTodayLotteryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.calendar_fragment, null);
        initCalendar(root);
        initTodayLottery(root);
        return root;
    }

    private void initCalendar(View root) {
        mCalendar = (RecyclerView) root.findViewById(R.id.calendar);
        mCalendar.setBackgroundColor(Color.GREEN);
        mCalendar.setHasFixedSize(true);
        mCalendar.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mCalendarAdapter = new CalendarAdapter();
//        mCalendar.setAdapter(mCalendarAdapter);
    }

    private void initTodayLottery(View root) {
        mTodayLottery = (RecyclerView) root.findViewById(R.id.today_lottery);
    }
}
