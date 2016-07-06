package yhh.bj4.lotterylover.fragments.calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.fragments.calendar.holder.CalendarBaseHolder;
import yhh.bj4.lotterylover.fragments.calendar.holder.CalendarItemDateHolder;
import yhh.bj4.lotterylover.fragments.calendar.holder.CalendarItemWeekDayHolder;
import yhh.bj4.lotterylover.fragments.calendar.item.CalendarItem;
import yhh.bj4.lotterylover.fragments.calendar.item.CalendarItemDate;
import yhh.bj4.lotterylover.fragments.calendar.item.CalendarItemWeekDay;

/**
 * Created by yenhsunhuang on 2016/7/4.
 */
public class CalendarAdapter extends RecyclerView.Adapter implements RetrieveDateDataTask.Callback {
    public interface Callback {
        void onDateSelected(int y, int m, int d);
    }

    public static final int TYPE_DATE = 0;
    public static final int TYPE_WEEKDAY = 1;
    private final List<CalendarItem> mItems = new ArrayList<>();
    private final Context mContext;
    private final LayoutInflater mInflater;
    private int mYear, mMonth;
    private int mSelectedYear, mSelectedMonth, mSelectedDay;

    private Callback mCallback;

    public CalendarAdapter(Context context, Callback cb) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCallback = cb;

        Calendar c = Calendar.getInstance();
        mSelectedYear = c.get(Calendar.YEAR);
        mSelectedMonth = c.get(Calendar.MONTH);
        mSelectedDay = c.get(Calendar.DAY_OF_MONTH);
    }

    public void setDateInfo(int y, int m) {
        mYear = y;
        mMonth = m;
        new RetrieveDateDataTask(mYear, mMonth, mContext, this)
                .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onFinish(List<CalendarItem> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CalendarBaseHolder holder;
        if (viewType == TYPE_DATE) {
            holder = new CalendarItemDateHolder(mInflater.inflate(R.layout.calendar_adapter_holder_item_date, null));
        } else if (viewType == TYPE_WEEKDAY) {
            holder = new CalendarItemWeekDayHolder(mInflater.inflate(R.layout.calendar_adapter_holder_item_weekday, null));
        } else {
            throw new RuntimeException("unexpected type");
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == TYPE_DATE) {
            bindTypeDate(holder, position);
        } else if (viewType == TYPE_WEEKDAY) {
            bindTypeWeekDay(holder, position);
        } else {
            throw new RuntimeException("unexpected type");
        }
    }

    private void bindTypeDate(RecyclerView.ViewHolder holder, int position) {
        CalendarItemDate item = (CalendarItemDate) getItem(position);
        CalendarItemDateHolder dateHolder = ((CalendarItemDateHolder) holder);
        Calendar currentCalendar = Calendar.getInstance();

        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);

        final Calendar dataCalendar = Calendar.getInstance();
        dataCalendar.setTime(item.getDate());
        int dataMonth = dataCalendar.get(Calendar.MONTH);

        dataCalendar.set(Calendar.HOUR_OF_DAY, 0);
        dataCalendar.set(Calendar.MINUTE, 0);
        dataCalendar.set(Calendar.SECOND, 0);
        dataCalendar.set(Calendar.MILLISECOND, 0);

        dateHolder.getBorder().setBackgroundResource(0);
        dateHolder.getBorder().setOnClickListener(null);
        if (dataMonth != mMonth) {
            dateHolder.getText().setTextColor(Color.LTGRAY);
            dateHolder.getBorder().setClickable(false);
        } else {
            if (currentCalendar.getTimeInMillis() == dataCalendar.getTimeInMillis()) {
                dateHolder.getText().setTextColor(Color.RED);
            } else {
                dateHolder.getText().setTextColor(Color.BLACK);
            }
            if (mSelectedYear == dataCalendar.get(Calendar.YEAR) &&
                    mSelectedMonth == dataCalendar.get(Calendar.MONTH) &&
                    mSelectedDay == dataCalendar.get(Calendar.DAY_OF_MONTH)) {
                dateHolder.getBorder().setBackgroundResource(R.drawable.calendar_item_background);
            }
            dateHolder.getBorder().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedYear = dataCalendar.get(Calendar.YEAR);
                    mSelectedMonth = dataCalendar.get(Calendar.MONTH);
                    mSelectedDay = dataCalendar.get(Calendar.DAY_OF_MONTH);
                    mCallback.onDateSelected(mSelectedYear, mSelectedMonth, mSelectedDay);
                    notifyDataSetChanged();
                }
            });
        }
        dateHolder.getText().setText(String.valueOf(dataCalendar.get(Calendar.DAY_OF_MONTH)));
    }

    private void bindTypeWeekDay(RecyclerView.ViewHolder holder, int position) {
        CalendarItemWeekDay item = (CalendarItemWeekDay) getItem(position);
        CalendarItemWeekDayHolder weekDayHolder = ((CalendarItemWeekDayHolder) holder);
        weekDayHolder.getText().setText(item.getWeekDay());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public CalendarItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        final CalendarItem item = getItem(position);
        if (item instanceof CalendarItemDate) {
            return TYPE_DATE;
        } else if (item instanceof CalendarItemWeekDay) {
            return TYPE_WEEKDAY;
        } else {
            throw new RuntimeException("unexpected type");
        }
    }
}
