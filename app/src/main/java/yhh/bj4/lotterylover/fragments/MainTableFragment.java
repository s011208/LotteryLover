package yhh.bj4.lotterylover.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 2016/6/15.
 */
public class MainTableFragment extends Fragment {
    private int mLtoType, mListType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rtn = new View(getActivity());
        rtn.setBackgroundColor(Color.MAGENTA);
        return rtn;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof Callback) {
            mLtoType = ((Callback) activity).getLtoType();
            mListType = ((Callback) activity).getListType();
        }
    }

    public void setLtoType(int type) {
        if (type == mLtoType) return;
        mLtoType = type;
    }

    public void setListType(int type) {
        if (type == mListType) return;
        mListType = type;
    }

    public interface Callback {
        int getLtoType();

        int getListType();
    }
}
