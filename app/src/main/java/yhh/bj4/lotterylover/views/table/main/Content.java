package yhh.bj4.lotterylover.views.table.main;

import android.view.View;
import android.widget.TextView;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class Content extends MainTableItem {
    private TextView mText;

    public Content(View itemView) {
        super(itemView);
        mText = (TextView) itemView;
    }

    public TextView getText() {
        return mText;
    }
}
