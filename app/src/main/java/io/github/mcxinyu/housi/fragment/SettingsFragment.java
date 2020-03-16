package io.github.mcxinyu.housi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import androidx.core.app.Fragment;
import androidx.core.app.FragmentManager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.mcxinyu.housi.R;

/**
 * Created by huangyuefeng on 2017/9/21.
 * Contact me : mcxinyu@gmail.com
 */
public class SettingsFragment extends ABaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.parent_view)
    CoordinatorLayout mParentView;
    private Unbinder unbinder;

    Fragment mFragment;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        // fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        FragmentManager fragmentManager = getChildFragmentManager();
        mFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (mFragment == null) {
            mFragment = PreferencesFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
        return view;
    }

    @Override
    protected Toolbar getToolBar() {
        mToolbar.setTitle(getString(R.string.action_settings));
        return mToolbar;
    }

    @Override
    protected int getMenuItemId() {
        return R.id.nav_setting;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((PreferencesFragment) mFragment).initSourceUrl();
            ((PreferencesFragment) mFragment).initCache();
        } else {
            ((PreferencesFragment) mFragment).setAlarm();
        }
    }
}
