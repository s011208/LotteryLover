package yhh.bj4.lotterylover.fragments.analyze;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yhh.bj4.lotterylover.R;
import yhh.bj4.lotterylover.Utilities;

/**
 * Created by yenhsunhuang on 2016/7/13.
 */
public class AnalyzeResultAdapter extends RecyclerView.Adapter {
    private static final String TAG = "AnalyzeResultAdapter";
    private static final boolean DEBUG = Utilities.DEBUG;

    public static final String KEY_TYPE = "type";
    public static final String KEY_CATEGORY = "category";

    public static final int TYPE_CATEGORY = 1;
    public static final int TYPE_CONTENT = 2;

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final List<JSONObject> mItems = new ArrayList<>();

    private final AnalyzeResult mResult;

    public AnalyzeResultAdapter(Context context, AnalyzeResult result) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResult = result;
        buildItemList();
    }

    private void buildItemList() {
        final Resources res = mContext.getResources();
        // all time
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_all_time)));
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_recent_10)));
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_recent_20)));
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_this_month)));
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_last_month)));
        mItems.add(buildTypeCategory(res.getString(R.string.analyze_fragment_result_last_2_month)));
    }

    private JSONObject buildTypeCategory(String categoryTitle) {
        JSONObject rtn = new JSONObject();
        try {
            rtn.put(KEY_TYPE, TYPE_CATEGORY);
            rtn.put(KEY_CATEGORY, categoryTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CATEGORY) {
            return new AnalyzeResultCategoryHolder(mInflater.inflate(R.layout.analyze_fragment_result_category, null));
        } else if (viewType == TYPE_CONTENT) {

        }
        throw new RuntimeException("unexpected type: " + viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == TYPE_CATEGORY) {
            bindTypeCategory(holder, position);
        } else if (viewType == TYPE_CONTENT) {

        } else {
            throw new RuntimeException("unexpected type: " + viewType);
        }
    }

    private void bindTypeCategory(RecyclerView.ViewHolder holder, int position) {
        JSONObject json = getItem(position);
        String category = "";
        try {
            category = json.getString(KEY_CATEGORY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((AnalyzeResultCategoryHolder) holder).getCategory().setText(category);
    }

    @Override
    public int getItemViewType(int position) {
        try {
            return mItems.get(position).getInt(KEY_TYPE);
        } catch (JSONException e) {
            throw new RuntimeException("missing key type");
        }
    }

    public JSONObject getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
