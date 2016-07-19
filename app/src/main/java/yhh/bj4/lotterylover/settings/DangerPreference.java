package yhh.bj4.lotterylover.settings;

import android.content.Context;
import android.graphics.Color;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yenhsunhuang on 2016/7/19.
 */
public class DangerPreference extends Preference {
    public DangerPreference(Context context) {
        super(context);
    }

    public DangerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DangerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        ViewGroup root = (ViewGroup) super.onCreateView(parent);
        if (root.findViewById(android.R.id.title) != null) {
            ((TextView) root.findViewById(android.R.id.title)).setTextColor(Color.RED);
        }
        if (root.findViewById(android.R.id.summary) != null) {
            ((TextView) root.findViewById(android.R.id.summary)).setTextColor(Color.RED);
        }
        return root;
    }
}
