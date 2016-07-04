package yhh.bj4.lotterylover.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by yenhsunhuang on 2016/6/3.
 */
public class FullyExpandedGridView extends GridView {
    public FullyExpandedGridView(Context context) {
        this(context, null);
    }

    public FullyExpandedGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullyExpandedGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getLayoutParams().height = getMeasuredHeight();
    }
}