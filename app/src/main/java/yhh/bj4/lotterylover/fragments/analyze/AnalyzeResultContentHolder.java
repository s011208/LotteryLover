package yhh.bj4.lotterylover.fragments.analyze;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import yhh.bj4.lotterylover.R;

/**
 * Created by yenhsunhuang on 2016/7/13.
 */
public class AnalyzeResultContentHolder extends RecyclerView.ViewHolder {
    private TextView mTitle, mSummary;

    private View mContainer;

    public AnalyzeResultContentHolder(View itemView) {
        super(itemView);
        mContainer = itemView;
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mSummary = (TextView) itemView.findViewById(R.id.summary);
    }

    public TextView getTitle() {
        return mTitle;
    }

    public TextView getSummary() {
        return mSummary;
    }

    public View getContainer() {
        return mContainer;
    }
}
