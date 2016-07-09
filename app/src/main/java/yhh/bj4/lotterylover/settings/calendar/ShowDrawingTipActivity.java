package yhh.bj4.lotterylover.settings.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.BaseActivity;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.provider.LotteryProvider;

/**
 * Created by yenhsunhuang on 2016/7/9.
 */
public class ShowDrawingTipActivity extends BaseActivity {
    private ListView mLtoList;

    @Override
    public int getContentViewResource() {
        return R.layout.activity_show_drawing_tip;
    }

    @Override
    public String getActivityName() {
        return ShowDrawingTipActivity.class.getSimpleName();
    }

    @Override
    protected void initActionBar(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.show_drawing_tip_activity_title);
        }
    }

    @Override
    protected void initViewComponents(Bundle savedInstanceState) {
        mLtoList = (ListView) findViewById(R.id.lto_type_list);
        mLtoList.setAdapter(new LtoAdapter(ShowDrawingTipActivity.this));
    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {

    }

    private static class LtoAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

        private final Context mContext;
        private final LayoutInflater mInflater;
        private final List<Pair<Integer, Boolean>> mItems = new ArrayList<>();
        private final String[] mLto;

        LtoAdapter(Context context) {
            mContext = context;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLto = mContext.getResources().getStringArray(R.array.action_bar_spinner_lto_type);
            mItems.addAll(ShowDrawingTip.getData(mContext));
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Pair<Integer, Boolean> getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (holder == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.show_drawing_tip_list_item, null);
                holder.mCheckBox = (CheckBox) convertView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mCheckBox.setOnCheckedChangeListener(null);
            Pair<Integer, Boolean> item = getItem(position);
            holder.mCheckBox.setChecked(item.second);
            holder.mCheckBox.setText(mLto[item.first]);
            holder.mCheckBox.setOnCheckedChangeListener(this);
            holder.mCheckBox.setTag(position);
            return convertView;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CharSequence text = buttonView.getText();
            for (int i = 0; i < mLto.length; ++i) {
                if (mLto[i].equals(text)) {
                    ContentValues cv = new ContentValues();
                    cv.put(ShowDrawingTip.COLUMN_IS_CHECKED, isChecked ? LotteryProvider.TRUE : LotteryProvider.FALSE);
                    mContext.getContentResolver().update(ShowDrawingTip.URI, cv, ShowDrawingTip.COLUMN_LTO_TYPE + "=" + i, null);
                    return;
                }
            }
        }

        private static class ViewHolder {
            CheckBox mCheckBox;
        }
    }
}
