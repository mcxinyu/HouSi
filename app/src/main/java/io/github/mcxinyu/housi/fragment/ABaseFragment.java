package io.github.mcxinyu.housi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import androidx.appcompat.widget.Toolbar;

/**
 * Created by huangyuefeng on 2017/9/14.
 * Contact me : mcxinyu@gmail.com
 */
public abstract class ABaseFragment extends Fragment {

    private FragmentCallbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallbacks) {
            mCallbacks = (FragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BackHandledInterface");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks.setDrawerMenuClicked(getMenuItemId());
        mCallbacks.initToolbar(getToolBar());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mCallbacks.setDrawerMenuClicked(getMenuItemId());
            mCallbacks.initToolbar(getToolBar());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCallbacks = null;
    }

    public interface FragmentCallbacks {
        void setDrawerMenuClicked(int item);

        void initToolbar(Toolbar toolbar);
    }

    protected abstract Toolbar getToolBar();

    @IdRes
    protected abstract int getMenuItemId();
}
