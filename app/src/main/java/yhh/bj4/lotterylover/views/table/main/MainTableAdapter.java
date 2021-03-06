package yhh.bj4.lotterylover.views.table.main;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.helpers.RetrieveLotteryItemDataHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.provider.AppSettings;
import yhh.bj4.lotterylover.views.table.main.holder.MainTableHolder;
import yhh.bj4.lotterylover.views.table.main.holder.OverallContentHolder;
import yhh.bj4.lotterylover.views.table.main.item.MainTableItem;
import yhh.bj4.lotterylover.views.table.main.item.TypeCombinedList;
import yhh.bj4.lotterylover.views.table.main.item.TypeLastDigit;
import yhh.bj4.lotterylover.views.table.main.item.TypeNumeric;
import yhh.bj4.lotterylover.views.table.main.item.TypeOverall;
import yhh.bj4.lotterylover.views.table.main.item.TypePlusAndMinus;
import yhh.bj4.lotterylover.views.table.main.item.TypePlusTogether;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class MainTableAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MainTableAdapter";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final int TYPE_OVER_ALL_CONTENT = 0;
    public static final int TYPE_NUMERIC = 1;
    public static final int TYPE_PLUS_TOGETHER = 2;
    public static final int TYPE_LAST_DIGIT = 3;
    public static final int TYPE_PLUS_AND_MINUS = 4;
    public static final int TYPE_COMBINE_LIST = 5;

    public interface Callback {
        void onFinishLoadingData();
    }

    private int mLtoType, mListType;

    private int mPlusAndMinus = 0;

    private float mDigitScale = LotteryLover.DIGIT_SCALE_SIZE_NORMAL;

    private boolean mCombineSpecialNumber = false, mShowMonthlyDataOnly = false;

    private ArrayList<MainTableItem> mShowData = new ArrayList<>();
    private ArrayList<MainTableItem> mCachedData = new ArrayList<>();

    private Context mContext;
    private LayoutInflater mInflater;

    private Callback mCallback;

    private ArrayList<LotteryItem> mLotteryItems = new ArrayList<>();

    private int mSelectedIndex = -1;

    private View mPreviousSelectedView = null;

    public MainTableAdapter(Context context, int ltoType, int listType, boolean showMonthlyDataOnly) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCombineSpecialNumber = AppSettings.get(mContext, LotteryLover.KEY_COMBINE_SPECIAL, false);
        updateData(ltoType, listType, showMonthlyDataOnly);
    }

    public void setCallback(Callback cb) {
        mCallback = cb;
    }

    public void updateData(final int ltoType, final int listType, boolean showMonthlyDataOnly) {
        mListType = listType;
        mLtoType = ltoType;
        mShowMonthlyDataOnly = showMonthlyDataOnly;
        mShowData.clear();
        mCachedData.clear();
        mLotteryItems.clear();
        notifyDataSetChanged();
        if (mContext == null) return;
        new RetrieveLotteryItemDataHelper(mContext, new RetrieveLotteryItemDataHelper.Callback() {
            @Override
            public void onFinished(List<LotteryItem> data) {
                mLotteryItems.addAll(data);
                final boolean showSequence = AppSettings.get(mContext, LotteryLover.KEY_SHOW_COLUMN_SEQUENCE, true);
                new AdapterDataGenerator(ltoType, listType, mCombineSpecialNumber, Utilities.getWindowBackgroundColor(mContext),
                        data, new AdapterDataGenerator.Callback() {
                    @Override
                    public void onFinished(ArrayList<MainTableItem> data) {
                        if (listType != mListType || ltoType != mLtoType) return;
                        mShowData.clear();
                        mCachedData.clear();
                        if (data != null && !data.isEmpty()) {
                            if (mShowMonthlyDataOnly &&
                                    listType != LotteryLover.LIST_TYPE_PLUS_AND_MINUS &&
                                    listType != LotteryLover.LIST_TYPE_OVERALL) {
                                Iterator<MainTableItem> itemIterator = data.iterator();
                                while (itemIterator.hasNext()) {
                                    MainTableItem item = itemIterator.next();
                                    if (item.getItemType() != MainTableItem.ITEM_TYPE_SUB_TOTAL) {
                                        itemIterator.remove();
                                    }
                                }
                            }
                            mCachedData.addAll(data);
                            mShowData.addAll(data);
                        } else {
                            mLotteryItems.clear();
                        }
                        if (DEBUG) {
                            Log.i(TAG, "AdapterDataGenerator cb, size: " + data.size());
                        }
                        if (listType == LotteryLover.LIST_TYPE_PLUS_AND_MINUS) {
                            updateAddAndMinus();
                        } else {
                            notifyDataSetChanged();
                        }
                        if (mCallback != null) {
                            mCallback.onFinishLoadingData();
                        }
                    }
                }, showSequence).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        }, ltoType, listType).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_OVER_ALL_CONTENT:
            case TYPE_PLUS_TOGETHER:
            case TYPE_NUMERIC:
            case TYPE_LAST_DIGIT:
            case TYPE_PLUS_AND_MINUS:
            case TYPE_COMBINE_LIST:
                holder = new OverallContentHolder(mInflater.inflate(R.layout.main_table_adapter_text, null), mDigitScale);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_OVER_ALL_CONTENT:
                bindOverallContent(holder, position);
                break;
            case TYPE_NUMERIC:
                bindNumericContent(holder, position);
                break;
            case TYPE_PLUS_TOGETHER:
                bindPlusTogetherContent(holder, position);
                break;
            case TYPE_LAST_DIGIT:
                bindLastDigitContent(holder, position);
                break;
            case TYPE_PLUS_AND_MINUS:
                bindPlusAndMinusContent(holder, position);
                break;
            case TYPE_COMBINE_LIST:
                bindCombinedListContent(holder, position);
                break;
        }
        final MainTableHolder mainTableHolder = (MainTableHolder) holder;
        mainTableHolder.getMainView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedIndex = position;
                if (mPreviousSelectedView != null) {
                    mPreviousSelectedView.setBackgroundColor(Color.TRANSPARENT);
                }
                mainTableHolder.getMainView().setBackgroundColor(Color.CYAN);
                mPreviousSelectedView = mainTableHolder.getMainView();
            }
        });
        if (mSelectedIndex == position) {
            mainTableHolder.getMainView().setBackgroundColor(Color.CYAN);
            mPreviousSelectedView = mainTableHolder.getMainView();
        } else {
            mainTableHolder.getMainView().setBackgroundDrawable(null);
        }
    }

    private void bindCombinedListContent(RecyclerView.ViewHolder holder, int position) {
        final TypeCombinedList item = (TypeCombinedList) getItem(position);
        OverallContentHolder contentHolder = (OverallContentHolder) holder;
        contentHolder.getTextView().setText(item.makeSpannableString());
        contentHolder.updateScaleIfNecessary(mDigitScale);
    }

    private void bindPlusAndMinusContent(RecyclerView.ViewHolder holder, int position) {
        final TypePlusAndMinus item = (TypePlusAndMinus) getItem(position);
        OverallContentHolder contentHolder = (OverallContentHolder) holder;
        contentHolder.getTextView().setText(item.makeSpannableString());
        contentHolder.updateScaleIfNecessary(mDigitScale);
    }

    private void bindLastDigitContent(RecyclerView.ViewHolder holder, int position) {
        final TypeLastDigit item = (TypeLastDigit) getItem(position);
        OverallContentHolder contentHolder = (OverallContentHolder) holder;
        contentHolder.getTextView().setText(item.makeSpannableString());
        contentHolder.updateScaleIfNecessary(mDigitScale);
    }

    private void bindPlusTogetherContent(RecyclerView.ViewHolder holder, int position) {
        final TypePlusTogether item = (TypePlusTogether) getItem(position);
        OverallContentHolder contentHolder = (OverallContentHolder) holder;
        contentHolder.getTextView().setText(item.makeSpannableString());
        contentHolder.updateScaleIfNecessary(mDigitScale);
    }

    private void bindNumericContent(RecyclerView.ViewHolder holder, int position) {
        final TypeNumeric item = (TypeNumeric) getItem(position);
        OverallContentHolder contentHolder = (OverallContentHolder) holder;
        contentHolder.getTextView().setText(item.makeSpannableString());
        contentHolder.updateScaleIfNecessary(mDigitScale);
    }

    private void bindOverallContent(RecyclerView.ViewHolder holder, int position) {
        final TypeOverall item = (TypeOverall) getItem(position);
        OverallContentHolder contentHolder = (OverallContentHolder) holder;
        contentHolder.getTextView().setText(item.makeSpannableString());
        contentHolder.updateScaleIfNecessary(mDigitScale);
    }

    @Override
    public int getItemCount() {
        return mShowData.size();
    }

    public MainTableItem getItem(int position) {
        return mShowData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    public void setDigitSize(float scale) {
        mDigitScale = scale;
    }

    public void setAddAndMinus(int value, boolean update) {
        mPlusAndMinus = value;
        if (update) {
            updateAddAndMinus();
        }
    }

    private void updateAddAndMinus() {
        int queryOrder = AppSettings.get(mContext, LotteryLover.KEY_ORDER, LotteryLover.ORDER_BY_ASC);
        new UpdatePlusAndMinusHelper(mLotteryItems, mPlusAndMinus, mCombineSpecialNumber, mCachedData, queryOrder, new UpdatePlusAndMinusHelper.Callback() {
            @Override
            public void onFinished() {
                if (DEBUG)
                    Log.d(TAG, "UpdatePlusAndMinusHelper, onFinished");
                mShowData.clear();
                mShowData.addAll(mCachedData);
                if (mShowMonthlyDataOnly) {
                    Iterator<MainTableItem> itemIterator = mShowData.iterator();
                    while (itemIterator.hasNext()) {
                        MainTableItem item = itemIterator.next();
                        if (item.getItemType() != MainTableItem.ITEM_TYPE_SUB_TOTAL) {
                            itemIterator.remove();
                        }
                    }
                }
                notifyDataSetChanged();
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void setCombineSpecialNumber(boolean num) {
        mCombineSpecialNumber = num;
    }
}
