package yhh.bj4.lotterylover.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.views.table.main.MainTableAdapter;

/**
 * Created by User on 2016/6/15.
 */
public class MainTableFragment extends Fragment {
    private static final String TAG = "MainTableFragment";
    private static final boolean DEBUG = Utilities.DEBUG;

    private int mLtoType, mListType;

    private ProgressBar mLoadingProgress;
    private RecyclerView mMainTable;
    private MainTableAdapter mMainTableAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.main_table_fragment, null);
        mMainTable = (RecyclerView) root.findViewById(R.id.main_table_recyclerview);
        return root;
    }

    private void updateMainTableAdapter() {
        if (mMainTableAdapter == null) {
            mMainTableAdapter = new MainTableAdapter(getActivity(), mLtoType, mListType);
            mMainTable.setAdapter(mMainTableAdapter);
        } else {
            mMainTableAdapter.updateData(mLtoType, mListType);
        }
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
    }

    public void setLtoType(int type) {
        if (type == mLtoType) return;
        mLtoType = type;
        if (DEBUG) {
            Log.d(TAG, "setLtoType: " + type);
        }
        updateMainTableAdapter();
    }

    public void setListType(int type) {
        if (type == mListType) return;
        mListType = type;
        if (DEBUG) {
            Log.d(TAG, "setListType: " + type);
        }
        updateMainTableAdapter();
    }

    public interface Callback {
        int getLtoType();

        int getListType();
    }
}
