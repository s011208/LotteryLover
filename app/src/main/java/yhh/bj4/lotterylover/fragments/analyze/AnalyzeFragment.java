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
import android.widget.ProgressBar;

import java.util.List;

import yhh.bj4.lotterylover.LotteryLover;
import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;
import yhh.bj4.lotterylover.fragments.analyze.result.AnalyzeResult;
import yhh.bj4.lotterylover.helpers.RetrieveLotteryItemDataHelper;
import yhh.bj4.lotterylover.parser.LotteryItem;
import yhh.bj4.lotterylover.views.ltotype.LtoTypeAdapter;

/**
 * Created by yenhsunhuang on 2016/7/12.
 */
public class AnalyzeFragment extends Fragment implements LtoTypeAdapter.Callback, LotteryItemsLoader.Callback {
    private static final String TAG = "AnalyzeFragment";
    private static final boolean DEBUG = Utilities.DEBUG;

    private ProgressBar mProgressBar;

    private RecyclerView mLtoList, mAnalyzeResult;

    private int mLtoType = LotteryLover.LTO_TYPE_LTO;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.analyze_fragment, null);
        mProgressBar = (ProgressBar) root.findViewById(R.id.loading_progressbar);

        mLtoList = (RecyclerView) root.findViewById(R.id.lto_type_recyclerview);
        mLtoList.setAdapter(new LtoTypeAdapter(getActivity(), this, mLtoType));

        mAnalyzeResult = (RecyclerView) root.findViewById(R.id.analyze_list);
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
                        new LotteryItemsLoader(getActivity(), AnalyzeFragment.this,
                                mLtoType, data).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
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
    }
}
