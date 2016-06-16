package yhh.bj4.lotterylover.views.table.main.item;

import android.util.SparseArray;

/**
 * Created by yenhsunhuang on 2016/6/16.
 */
public class MainTableItem {
    private final SparseArray<Integer> mArrayData = new SparseArray<>();
    private final int mViewType;

    public MainTableItem(int viewType) {
        mViewType = viewType;
    }

    public int getViewType() {
        return mViewType;
    }

    public void set(int index, int value) {
        mArrayData.put(index, value);
    }

    public Integer get(int index) {
        return mArrayData.get(index);
    }
}
