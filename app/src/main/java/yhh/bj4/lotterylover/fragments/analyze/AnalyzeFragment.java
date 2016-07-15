package yhh.bj4.lotterylover.fragments.analyze;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;

import java.util.List;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.fragments.analyze.list34.List34ItemsLoader;
import yhh.bj4.lotterylover.fragments.analyze.list34.List34Result;
import yhh.bj4.lotterylover.fragments.analyze.list34.List34ResultAdapter;
import yhh.bj4.lotterylover.fragments.analyze.result.AnalyzeResult;
import yhh.bj4.lotterylover.helpers.RetrieveLotteryItemDataHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.views.DividerItemDecoration;
import yhh.bj4.lotterylover.views.ltotype.LtoTypeAdapter;

/**
 * Created by yenhsunhuang on 2016/7/12.
 */
public class AnalyzeFragment extends Fragment implements LtoTypeAdapter.Callback,
        LotteryItemsLoader.Callback, List34ItemsLoader.Callback {
    private static final String TAG = "AnalyzeFragment";
    private static final boolean DEBUG = Utilities.DEBUG;

    private ProgressBar mProgressBar;

    private RecyclerView mLtoList, mAnalyzeResult, mAnalyzeList34Result;

    private int mLtoType = LotteryLover.LTO_TYPE_LTO;

    private HorizontalScrollView mList34AdapterContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.analyze_fragment, null);
        mProgressBar = (ProgressBar) root.findViewById(R.id.loading_progressbar);

        mLtoList = (RecyclerView) root.findViewById(R.id.lto_type_recyclerview);
        mLtoList.setAdapter(new LtoTypeAdapter(getActivity(), this, mLtoType));

        mAnalyzeResult = (RecyclerView) root.findViewById(R.id.analyze_list);

        mList34AdapterContainer = (HorizontalScrollView) root.findViewById(R.id.list34_container);
        mAnalyzeList34Result = (RecyclerView) root.findViewById(R.id.analyze_list34);
        mAnalyzeList34Result.addItemDecoration(new DividerItemDecoration(getActivity()));

        updateAnalyzeResult();
        return root;
    }

    private void updateAnalyzeResult() {
        mProgressBar.setVisibility(View.VISIBLE);
        new RetrieveLotteryItemDataHelper(getActivity(),
                new RetrieveLotteryItemDataHelper.Callback() {
                    @Override
                    public void onFinished(List<LotteryItem> data) {
                        if (DEBUG) {
                            Log.d(TAG, "data size: " + data.size());
                        }
                        if (mLtoType == LotteryLover.LTO_TYPE_LTO_LIST3 || mLtoType == LotteryLover.LTO_TYPE_LTO_LIST4) {
                            new List34ItemsLoader(getActivity(), mLtoType, AnalyzeFragment.this, data)
                                    .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                        } else {
                            new LotteryItemsLoader(getActivity(), AnalyzeFragment.this,
                                    mLtoType, data).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                        }
                    }
                }, mLtoType, -1, false)
                .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void onLtoTypeChanged(int ltoType) {
        if (DEBUG) {
            Log.d(TAG, "lto type: " + ltoType);
        }
        mLtoType = ltoType;
        updateAnalyzeResult();
    }

    @Override
    public void onFinish(AnalyzeResult result) {
        if (result.getLotteryType() != mLtoType) {
            if (DEBUG) {
                Log.w(TAG, "lto type expired, ignore update");
            }
            return;
        }

        mAnalyzeResult.setAdapter(new AnalyzeResultAdapter(getActivity(), result));

        mProgressBar.setVisibility(View.INVISIBLE);
        mList34AdapterContainer.setVisibility(View.INVISIBLE);
        mAnalyzeResult.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadList34Finished(List34Result result) {
        if (result.getLotteryType() != mLtoType) {
            if (DEBUG) {
                Log.w(TAG, "lto type expired, ignore update");
            }
            return;
        }
        mAnalyzeList34Result.setAdapter(new List34ResultAdapter(getActivity(), result));

        mProgressBar.setVisibility(View.INVISIBLE);
        mList34AdapterContainer.setVisibility(View.VISIBLE);
        mAnalyzeResult.setVisibility(View.INVISIBLE);
    }
}
