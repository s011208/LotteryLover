package yhh.bj4.lotterylover.fragments.calendar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import yhh.bj4.lotterylover.R;

/**
 * Created by yenhsunhuang on 2016/7/1.
 */
public class CalendarFragment extends Fragment implements CalendarAdapter.Callback {
    private ImageView mPreviousMonth, mNextMonth;
    private TextView mSelectYearAndMonth;

    private RecyclerView mCalendar;
    private CalendarAdapter mCalendarAdapter;

    private RecyclerView mTodayLottery;
    private LotteryAdapter mTodayLotteryAdapter;

    private int mYear, mMonth, mDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.calendar_fragment, null);
        if (savedInstanceState == null) {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        } else {

        }
        initComponents(root);
        return root;
    }

    private void initComponents(View root) {
        mPreviousMonth = (ImageView) root.findViewById(R.id.month_previous);
        mPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                c.add(Calendar.MONTH, -1);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mCalendarAdapter.setDateInfo(mYear, mMonth);
                mSelectYearAndMonth.setText(new SimpleDateFormat("yyyy.MM",
                        getActivity().getResources().getConfiguration().locale).format(c.getTime()));
            }
        });
        mNextMonth = (ImageView) root.findViewById(R.id.month_next);
        mNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay);
                c.add(Calendar.MONTH, 1);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mCalendarAdapter.setDateInfo(mYear, mMonth);
                mSelectYearAndMonth.setText(new SimpleDateFormat("yyyy.MM",
                        getActivity().getResources().getConfiguration().locale).format(c.getTime()));
            }
        });
        mSelectYearAndMonth = (TextView) root.findViewById(R.id.select_y_and_m);
        Calendar c = Calendar.getInstance();
        mSelectYearAndMonth.setText(new SimpleDateFormat("yyyy.MM",
                getActivity().getResources().getConfiguration().locale).format(c.getTime()));
        mSelectYearAndMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        Calendar c = Calendar.getInstance();
                        c.set(mYear, mMonth, mDay);
                        mCalendarAdapter.setDateInfo(mYear, mMonth);
                        mTodayLotteryAdapter.setDate(mYear, mMonth, mDay);
                        mSelectYearAndMonth.setText(new SimpleDateFormat("yyyy.MM",
                                getActivity().getResources().getConfiguration().locale).format(c.getTime()));
                    }
                }, mYear, mMonth, mDay).show();
            }
        });
        initCalendar(root);
        initTodayLottery(root);
    }

    private void initCalendar(View root) {
        mCalendar = (RecyclerView) root.findViewById(R.id.calendar);
        mCalendar.setHasFixedSize(true);
        mCalendar.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mCalendar.getRecycledViewPool().setMaxRecycledViews(CalendarAdapter.TYPE_WEEKDAY, 7);
        mCalendar.getRecycledViewPool().setMaxRecycledViews(CalendarAdapter.TYPE_DATE, 42);
        mCalendarAdapter = new CalendarAdapter(getActivity(), this);
        mCalendarAdapter.setDateInfo(mYear, mMonth);
        mCalendar.setAdapter(mCalendarAdapter);
    }

    private void initTodayLottery(View root) {
        mTodayLottery = (RecyclerView) root.findViewById(R.id.today_lottery);
        mTodayLottery.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTodayLotteryAdapter = new LotteryAdapter(getActivity());
        mTodayLotteryAdapter.setDate(mYear, mMonth, mDay);
        mTodayLottery.setAdapter(mTodayLotteryAdapter);
    }

    @Override
    public void onDateSelected(int y, int m, int d) {
        mYear = y;
        mMonth = m;
        mDay = d;
        mTodayLotteryAdapter.setDate(mYear, mMonth, mDay);
    }

    public void updateCalendar() {
        mCalendarAdapter.setDateInfo(mYear, mMonth);
    }
}
