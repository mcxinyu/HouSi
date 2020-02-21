package io.github.mcxinyu.housi.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by huangyuefeng on 2017/9/14.
 * Contact me : mcxinyu@gmail.com
 */
public abstract class ABaseFragment extends Fragment {

    private Callbacks mCallbacks;


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (activity instanceof Callbacks) {
                mCallbacks = (Callbacks) activity;
            } else {
                throw new RuntimeException(activity.toString()
                        + " must implement Callbacks");
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callbacks) {
            mCallbacks = (Callbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callbacks");
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

    public interface Callbacks {
        void setDrawerMenuClicked(int item);

        void initToolbar(Toolbar toolbar);
    }

    protected abstract Toolbar getToolBar();

    @IdRes
    protected abstract int getMenuItemId();
}
