package yhh.bj4.lotterylover.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.helpers.RetrieveLotteryItemDataHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.provider.AppSettings;
import yhh.bj4.lotterylover.views.DividerItemDecoration;
import yhh.bj4.lotterylover.views.table.main.MainTableAdapter;
import yhh.bj4.lotterylover.views.table.main.item.MainTableItem;
import yhh.bj4.lotterylover.views.table.main.item.TypeLastDigit;
import yhh.bj4.lotterylover.views.table.main.item.TypeNumeric;
import yhh.bj4.lotterylover.views.table.main.item.TypePlusTogether;

/**
 * Created by User on 2016/6/15.
 */
public class MainTableFragment extends Fragment implements MainTableAdapter.Callback, View.OnClickListener {
    private static final String TAG = "MainTableFragment";
    private static final boolean DEBUG = Utilities.DEBUG;

    private static final int MAXIMUM_CACHE_SIZE_OF_EACH_ITEMS = 40;
    private static final int HEADER_AND_FOOTER_BACKGROUND_COLOR_RES = R.color.colorPrimary;

    private int mLtoType, mListType;

    private ProgressBar mLoadingProgress;
    private RecyclerView mMainTable;
    private MainTableAdapter mMainTableAdapter;

    private HorizontalScrollView mScrollView;

    private TextView mHeader, mFooter;
    private View mTopSep, mBottomSep;

    private float mOriginHeaderTextSize, mOriginFooterTextSize;

    private RadioGroup mPlusGroup, mMinusGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.main_table_fragment, null);
        mScrollView = (HorizontalScrollView) root;
        mMainTable = (RecyclerView) root.findViewById(R.id.main_table_recyclerview);
        mMainTable.addItemDecoration(new DividerItemDecoration(getActivity()));
        mMainTable.getRecycledViewPool().setMaxRecycledViews(MainTableAdapter.TYPE_OVER_ALL_CONTENT, MAXIMUM_CACHE_SIZE_OF_EACH_ITEMS);
        mMainTable.getRecycledViewPool().setMaxRecycledViews(MainTableAdapter.TYPE_NUMERIC, MAXIMUM_CACHE_SIZE_OF_EACH_ITEMS);
        mMainTable.getRecycledViewPool().setMaxRecycledViews(MainTableAdapter.TYPE_PLUS_TOGETHER, MAXIMUM_CACHE_SIZE_OF_EACH_ITEMS);

        mHeader = (TextView) root.findViewById(R.id.header);
        mFooter = (TextView) root.findViewById(R.id.footer);

        mTopSep = root.findViewById(R.id.top_sep);
        mBottomSep = root.findViewById(R.id.bottom_sep);

        mOriginHeaderTextSize = mHeader.getTextSize();
        mOriginFooterTextSize = mFooter.getTextSize();

        mHeader.setVisibility(View.INVISIBLE);
        mFooter.setVisibility(View.INVISIBLE);
        mTopSep.setVisibility(View.INVISIBLE);
        mBottomSep.setVisibility(View.INVISIBLE);

        final float digitScaleSize = getDigitScaleSize();
        mHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOriginHeaderTextSize * digitScaleSize);
        mFooter.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOriginFooterTextSize * digitScaleSize);

        mPlusGroup = (RadioGroup) root.findViewById(R.id.plus_rg_group);
        RadioButton btn;
        for (int i = 1; i < 11; ++i) {
            btn = new RadioButton(getActivity());
            btn.setText("+" + i);
            btn.setTag(i);
            btn.setOnClickListener(this);
            mPlusGroup.addView(btn);
        }

        mMinusGroup = (RadioGroup) root.findViewById(R.id.minus_rg_group);
        for (int i = 1; i < 11; ++i) {
            btn = new RadioButton(getActivity());
            btn.setText("-" + i);
            btn.setTag(-i);
            btn.setOnClickListener(this);
            mMinusGroup.addView(btn);
        }

        return root;
    }

    private void updatePlusAndMinus(int value) {
        if (DEBUG) {
            Log.d(TAG, "updatePlusAndMinus, value: " + value);
        }
        if (mMainTableAdapter == null) return;
        mMainTableAdapter.setAddAndMinus(value);
    }

    private void updateMainTableAdapter() {
        if (mMainTableAdapter == null) {
            mMainTableAdapter = new MainTableAdapter(getActivity(), mLtoType, mListType);
            mMainTableAdapter.setDigitSize(getDigitScaleSize());
            mMainTableAdapter.setCallback(this);
            mMainTable.setAdapter(mMainTableAdapter);
        } else {
            mMainTableAdapter.updateData(mLtoType, mListType);
        }
        mScrollView.smoothScrollTo(0, mScrollView.getScrollY());
    }

    private void updateHeaderAndFooterWidth() {
        mMainTable.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final int width = ((View) mHeader.getParent()).getWidth();
                final int mainTableWidth = mMainTable.getWidth();
                if (DEBUG)
                    Log.v(TAG, "width: " + width + ", mainTableWidth: " + mainTableWidth);
                if (width == 0 || mainTableWidth == 0) return true;
                if (mMainTable.getViewTreeObserver().isAlive()) {
                    mMainTable.getViewTreeObserver().removeOnPreDrawListener(this);
                }

                final int finalWidth = Math.min(width, mainTableWidth);
                mHeader.setMinWidth(finalWidth);
                mFooter.setMinWidth(finalWidth);

                mTopSep.setMinimumWidth(finalWidth);
                mBottomSep.setMinimumWidth(finalWidth);
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof Callback) {
            mLtoType = ((Callback) activity).getLtoType();
            mListType = ((Callback) activity).getListType();
            if (DEBUG) {
                Log.d(TAG, "onActivityCreated, mLtoType: " + mLtoType + ", mListType: " + mListType);
            }
        }
        updateMainTableAdapter();
        updateHeaderAndFooter();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setLtoType(int type) {
        if (type == mLtoType) return;
        mLtoType = type;
        if (DEBUG) {
            Log.d(TAG, "setLtoType: " + type);
        }
        updateMainTableAdapter();
        updateHeaderAndFooter();
    }

    public void setListType(int type) {
        if (type == mListType) return;
        mListType = type;
        if (DEBUG) {
            Log.d(TAG, "setListType: " + type);
        }
        updateMainTableAdapter();
        updateHeaderAndFooter();
    }

    private void updateHeaderAndFooter() {
        updateHeader();
        updateFooter();
    }

    private void updateHeader() {
        new AsyncTask<Void, Void, MainTableItem>() {
            final int listType = mListType;
            final int ltoType = mLtoType;

            @Override
            protected void onPreExecute() {
                mHeader.setVisibility(View.INVISIBLE);
                mTopSep.setVisibility(View.INVISIBLE);
                mPlusGroup.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onPostExecute(MainTableItem mainTableItem) {
                if (mainTableItem == null) {
                    mHeader.setText("");
                    if (listType == LotteryLover.LIST_TYPE_OVERALL) {
                        mPlusGroup.setVisibility(View.INVISIBLE);
                        mHeader.setVisibility(View.INVISIBLE);
                    } else {
                        mPlusGroup.setVisibility(View.VISIBLE);
                        mHeader.setVisibility(View.INVISIBLE);
                    }
                } else {
                    mPlusGroup.setVisibility(View.INVISIBLE);
                    mHeader.setVisibility(View.VISIBLE);
                    mHeader.setText(mainTableItem.makeSpannableString());
                }
                mTopSep.setVisibility(View.VISIBLE);
            }

            @Override
            protected MainTableItem doInBackground(Void... params) {
                int[] parameters = MainTableItem.initParameters(ltoType);
                MainTableItem rtn = null;
                if (listType == LotteryLover.LIST_TYPE_OVERALL) {
                    return null;
                } else if (listType == LotteryLover.LIST_TYPE_NUMERIC) {
                    rtn = new TypeNumeric(MainTableAdapter.TYPE_NUMERIC,
                            0, 0, "", "", parameters[0], parameters[1], parameters[2], parameters[3]);
                    rtn.setWindowBackgroundColor(getResources().getColor(HEADER_AND_FOOTER_BACKGROUND_COLOR_RES));
                    rtn.setItemType(MainTableItem.ITEM_TYPE_HEADER);
                    for (int i = 1; i < parameters[2] + 1; ++i) {
                        rtn.addNormalNumber(i - 1, i);
                    }

                    if (parameters[3] > 0) {
                        for (int i = 1; i < parameters[3] + 1; ++i) {
                            rtn.addSpecialNumber(i - 1, i);
                        }
                    }

                    rtn.makeSpannableString();
                } else if (listType == LotteryLover.LIST_TYPE_PLUS_TOGETHER) {
                    Map<Integer, Integer> index = Utilities.getPlusAndLastDigitMap(parameters[2]);
                    rtn = new TypePlusTogether(MainTableAdapter.TYPE_PLUS_TOGETHER,
                            0, 0, "", "", parameters[0], parameters[1], parameters[2], parameters[3]);
                    rtn.setWindowBackgroundColor(getResources().getColor(HEADER_AND_FOOTER_BACKGROUND_COLOR_RES));
                    rtn.setItemType(MainTableItem.ITEM_TYPE_HEADER);
                    for (int i = 1; i < parameters[2] + 1; ++i) {
                        rtn.addNormalNumber(i - 1, index.get(i));
                    }

                    if (parameters[3] > 0) {
                        index = Utilities.getPlusAndLastDigitMap(parameters[3]);
                        for (int i = 1; i < parameters[3] + 1; ++i) {
                            rtn.addSpecialNumber(i - 1, index.get(i));
                        }
                    }
                } else if (listType == LotteryLover.LIST_TYPE_LAST_DIGIT) {
                    Map<Integer, Integer> index = Utilities.getLastDigitMap(parameters[2]);
                    rtn = new TypeLastDigit(MainTableAdapter.TYPE_LAST_DIGIT,
                            0, 0, "", "", parameters[0], parameters[1], parameters[2], parameters[3]);
                    rtn.setWindowBackgroundColor(getResources().getColor(HEADER_AND_FOOTER_BACKGROUND_COLOR_RES));
                    rtn.setItemType(MainTableItem.ITEM_TYPE_HEADER);
                    for (int i = 1; i < parameters[2] + 1; ++i) {
                        rtn.addNormalNumber(i - 1, index.get(i));
                    }

                    if (parameters[3] > 0) {
                        index = Utilities.getLastDigitMap(parameters[3]);
                        for (int i = 1; i < parameters[3] + 1; ++i) {
                            rtn.addSpecialNumber(i - 1, index.get(i));
                        }
                    }
                } else if (listType == LotteryLover.LIST_TYPE_PLUS_AND_MINUS) {
                    return null;
                } else {
                    throw new RuntimeException("unexpected list type");
                }
                return rtn;
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private void updateFooter() {
        new AsyncTask<Void, Void, MainTableItem>() {
            final int listType = mListType;
            final int ltoType = mLtoType;

            @Override
            protected void onPreExecute() {
                mFooter.setVisibility(View.INVISIBLE);
                mBottomSep.setVisibility(View.INVISIBLE);
                mMinusGroup.setVisibility(View.INVISIBLE);
            }

            @Override
            protected void onPostExecute(MainTableItem mainTableItem) {
                if (mainTableItem == null) {
                    mFooter.setText("");
                    if (listType == LotteryLover.LIST_TYPE_OVERALL) {
                        mMinusGroup.setVisibility(View.INVISIBLE);
                        mFooter.setVisibility(View.INVISIBLE);
                    } else {
                        mMinusGroup.setVisibility(View.VISIBLE);
                        mFooter.setVisibility(View.INVISIBLE);
                    }
                } else {
                    mMinusGroup.setVisibility(View.INVISIBLE);
                    mFooter.setVisibility(View.VISIBLE);
                    mFooter.setText(mainTableItem.makeSpannableString());
                }
            }

            @Override
            protected MainTableItem doInBackground(Void... params) {
                int[] parameters = MainTableItem.initParameters(ltoType);
                ArrayList<LotteryItem> items = RetrieveLotteryItemDataHelper.getDataFromCursor(RetrieveLotteryItemDataHelper.getDataCursor(getActivity(), ltoType), ltoType);
                Pair<ArrayList<Integer>, ArrayList<Integer>> combinedResult = Utilities.collectLotteryItemsData(items);
                if (combinedResult.first.isEmpty() || combinedResult.second.isEmpty()) return null;
                MainTableItem rtn = null;
                if (parameters[3] <= 0) {
                    // combine
                    for (int i = 0; i < combinedResult.first.size(); ++i) {
                        combinedResult.first.set(i, combinedResult.first.get(i) + combinedResult.second.get(i));
                    }
                }
                if (listType == LotteryLover.LIST_TYPE_OVERALL) {
                    return null;
                } else if (listType == LotteryLover.LIST_TYPE_NUMERIC) {
                    rtn = new TypeNumeric(MainTableAdapter.TYPE_NUMERIC,
                            0, 0, "", "", parameters[0], parameters[1], parameters[2], parameters[3]);
                    rtn.setWindowBackgroundColor(getResources().getColor(HEADER_AND_FOOTER_BACKGROUND_COLOR_RES));
                    rtn.setItemType(MainTableItem.ITEM_TYPE_FOOTER);
                    for (int i = 0; i < combinedResult.first.size(); ++i) {
                        rtn.addNormalNumber(i, combinedResult.first.get(i));
                    }

                    if (parameters[3] > 0) {
                        for (int i = 0; i < combinedResult.second.size(); ++i) {
                            rtn.addSpecialNumber(i, combinedResult.second.get(i));
                        }
                    }

                    rtn.makeSpannableString();
                } else if (listType == LotteryLover.LIST_TYPE_PLUS_TOGETHER) {
                    rtn = new TypePlusTogether(MainTableAdapter.TYPE_PLUS_TOGETHER,
                            0, 0, "", "", parameters[0], parameters[1], parameters[2], parameters[3]);
                    rtn.setWindowBackgroundColor(getResources().getColor(HEADER_AND_FOOTER_BACKGROUND_COLOR_RES));
                    rtn.setItemType(MainTableItem.ITEM_TYPE_FOOTER);
                    List<Integer> tempData = new ArrayList<>();
                    for (int k = 0; k < parameters[2]; ++k) {
                        tempData.add(k, combinedResult.first.get(k));
                    }

                    Map<Integer, Integer> tempMap = Utilities.getPlusAndLastDigitMap(parameters[2]);
                    for (int in = 1; in < parameters[2] + 1; ++in) {
                        int newKey = in - 1;
                        int oldKey = tempMap.get(in) - 1;
                        rtn.addNormalNumber(newKey, combinedResult.first.get(oldKey));
                    }

                    if (parameters[3] != -1) {
                        tempData.clear();
                        for (int k = 0; k < parameters[3]; ++k) {
                            tempData.add(k, combinedResult.second.get(k));
                        }

                        tempMap = Utilities.getPlusAndLastDigitMap(parameters[3]);
                        for (int in = 1; in < parameters[3] + 1; ++in) {
                            int newKey = in - 1;
                            int oldKey = tempMap.get(in) - 1;
                            rtn.addSpecialNumber(newKey, combinedResult.second.get(oldKey));
                        }
                    }
                } else if (listType == LotteryLover.LIST_TYPE_LAST_DIGIT) {
                    rtn = new TypeLastDigit(MainTableAdapter.TYPE_LAST_DIGIT,
                            0, 0, "", "", parameters[0], parameters[1], parameters[2], parameters[3]);
                    rtn.setWindowBackgroundColor(getResources().getColor(HEADER_AND_FOOTER_BACKGROUND_COLOR_RES));
                    rtn.setItemType(MainTableItem.ITEM_TYPE_FOOTER);

                    List<Integer> tempData = new ArrayList<>();
                    for (int k = 0; k < parameters[2]; ++k) {
                        tempData.add(k, combinedResult.first.get(k));
                    }

                    Map<Integer, Integer> tempMap = Utilities.getLastDigitMap(parameters[2]);
                    for (int in = 1; in < parameters[2] + 1; ++in) {
                        int newKey = in - 1;
                        int oldKey = tempMap.get(in) - 1;
                        rtn.addNormalNumber(newKey, combinedResult.first.get(oldKey));
                    }

                    if (parameters[3] != -1) {
                        tempData.clear();
                        for (int k = 0; k < parameters[3]; ++k) {
                            tempData.add(k, combinedResult.second.get(k));
                        }

                        tempMap = Utilities.getLastDigitMap(parameters[3]);
                        for (int in = 1; in < parameters[3] + 1; ++in) {
                            int newKey = in - 1;
                            int oldKey = tempMap.get(in) - 1;
                            rtn.addSpecialNumber(newKey, combinedResult.second.get(oldKey));
                        }
                    }
                } else if (listType == LotteryLover.LIST_TYPE_PLUS_AND_MINUS) {
                    return null;
                } else {
                    throw new RuntimeException("unexpected list type");
                }
                return rtn;
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onFinishLoadingData() {
        updateHeaderAndFooterWidth();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof RadioButton) {
            final Integer tag = (Integer) v.getTag();
            if (tag == null) return;
            updatePlusAndMinus(tag);
            if (tag < 0) {
                for (int i = 0; i < mPlusGroup.getChildCount(); ++i) {
                    ((RadioButton) mPlusGroup.getChildAt(i)).setChecked(false);
                }
            } else {
                for (int i = 0; i < mMinusGroup.getChildCount(); ++i) {
                    ((RadioButton) mMinusGroup.getChildAt(i)).setChecked(false);
                }
            }
        }
    }

    public interface Callback {
        int getLtoType();

        int getListType();
    }

    public void updateDigitScaleSize() {
        final float digitScaleSize = getDigitScaleSize();
        mHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOriginHeaderTextSize * digitScaleSize);
        mFooter.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOriginFooterTextSize * digitScaleSize);
        if (mMainTableAdapter != null) {
            mMainTableAdapter.setDigitSize(digitScaleSize);
            mMainTableAdapter.notifyDataSetChanged();
        }
        updateHeaderAndFooterWidth();
    }

    private float getDigitScaleSize() {
        if (getActivity() == null) return LotteryLover.VALUE_DIGIT_SCALE_SIZE_NORMAL;
        return Utilities.getDigitSizeScale(AppSettings.get(getActivity(), LotteryLover.KEY_DIGIT_SCALE_SIZE, LotteryLover.DIGIT_SCALE_SIZE_NORMAL));
    }

    public void updateAllList() {
        if (mMainTableAdapter != null) {
            mMainTableAdapter.notifyDataSetChanged();
            updateMainTableAdapter();
            updateHeaderAndFooter();
        }
    }
}
