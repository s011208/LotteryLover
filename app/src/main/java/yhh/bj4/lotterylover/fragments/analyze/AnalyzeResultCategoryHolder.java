package yhh.bj4.lotterylover.fragments.analyze;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yenhsunhuang on 2016/7/13.
 */
public class AnalyzeResultCategoryHolder extends RecyclerView.ViewHolder {
    private TextView mCategory;

    public AnalyzeResultCategoryHolder(View itemView) {
        super(itemView);
        mCategory = (TextView) itemView;
    }

    public TextView getCategory() {
        return mCategory;
    }
}
