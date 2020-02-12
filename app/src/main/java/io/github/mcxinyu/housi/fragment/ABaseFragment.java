package io.github.mcxinyu.housi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import androidx.core.app.Fragment;

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
        mCallbacks.setToolbarTitle(getToolBarTitle());
        mCallbacks.setDrawerMenuClicked(getMenuItemId());
    }

    @Override
    public void onStart() {
        super.onStart();
        // mCallbacks.setToolbarTitle(getToolBarTitle());
        // mCallbacks.setDrawerMenuClicked(getMenuItemId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCallbacks = null;
    }

    public interface FragmentCallbacks {
        void setToolbarTitle(String title);

        void setDrawerMenuClicked(int item);
    }

    protected abstract String getToolBarTitle();

    @IdRes
    protected abstract int getMenuItemId();
}
