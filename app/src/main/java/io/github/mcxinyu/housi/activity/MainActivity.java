package io.github.mcxinyu.housi.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.views.PgyerDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.mcxinyu.housi.BuildConfig;
import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.fragment.ABaseFragment;
import io.github.mcxinyu.housi.fragment.BasicFragment;
import io.github.mcxinyu.housi.fragment.SourceFragment;
import io.github.mcxinyu.housi.util.CheckUpdateHelper;
import io.github.mcxinyu.housi.util.QueryPreferences;

/**
 * Created by huangyuefeng on 2017/9/13.
 * Contact me : mcxinyu@gmail.com
 */
public class MainActivity extends BaseAppCompatActivity
        implements ABaseFragment.FragmentCallbacks, BasicFragment.Callbacks,
        SourceFragment.Callbacks {

    private static final int WHAT_CHECK_UPDATE = 1024;

    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.no_su_text_view)
    TextView mNoSuTextView;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private Unbinder unbinder;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!QueryPreferences.getDrawerOpenState(MainActivity.this)) {
                mDrawerLayout.openDrawer(Gravity.START);
                QueryPreferences.setDrawerOpenState(MainActivity.this, true);
            }

            switch (msg.what) {
                case WHAT_CHECK_UPDATE:
                    checkForUpdate();
                    break;
            }
        }
    };

    ActionBarDrawerToggle mToggle;

    private FragmentManager mFragmentManager;
    private Fragment currentFragment;

    private BasicFragment mBasicFragment;
    private SourceFragment mSourceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar_TransparentStatusBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        initDrawer();

        mFragmentManager = getFragmentManager();
        currentFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            currentFragment = mBasicFragment = BasicFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
                    .commit();
        }

        mHandler.sendEmptyMessageDelayed(WHAT_CHECK_UPDATE, 6000);
    }

    private void initDrawer() {
        // setSupportActionBar(mToolbar);

        // ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(
        //         this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // mDrawerLayout.addDrawerListener(mToggle);
        // mToggle.syncState();

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_basic:
                        if (mBasicFragment == null)
                            mBasicFragment = BasicFragment.newInstance();
                        switchFragment(mBasicFragment);
                        break;
                    case R.id.nav_source:
                        if (mSourceFragment == null)
                            mSourceFragment = SourceFragment.newInstance();
                        switchFragment(mSourceFragment);
                        break;
                    case R.id.nav_setting:
                        break;
                    case R.id.nav_feedback:
                        showPgyerDialog();
                        break;
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        TextView versionTextView = mNavView.getHeaderView(0).findViewById(R.id.version_text_view);
        versionTextView.setText(getString(R.string.version) + ":" + BuildConfig.VERSION_NAME + "-" + BuildConfig.GIT_COMMIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        currentFragment = null;
        PgyUpdateManager.unregister();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (!(currentFragment instanceof BasicFragment)) {
            switchFragment(mBasicFragment);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // int id = item.getItemId();

        // if (id == R.id.action_settings) {
        //     return true;
        // }

        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment) {

        if (currentFragment != fragment) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (fragment.isAdded()) {
                transaction.hide(currentFragment)
                        .show(fragment)
                        .commit();
            } else {
                transaction.hide(currentFragment)
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
            currentFragment.setUserVisibleHint(false);
            fragment.setUserVisibleHint(true);
            currentFragment = fragment;
        }
    }

    private void showPgyerDialog() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START, false);
        }
        PgyerDialog.setDialogTitleBackgroundColor("#FF4081");
        PgyFeedback.getInstance().showDialog(this);
    }

    private void checkForUpdate() {

        PgyUpdateManager.register(MainActivity.this, "io.github.mcxinyu.housi.pgy",
                new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {

                    }

                    @Override
                    public void onUpdateAvailable(String result) {
                        Log.d("MainActivity", result);
                        final AppBean appBean = getAppBeanFromString(result);

                        if (Integer.parseInt(appBean.getVersionCode()) >
                                CheckUpdateHelper.getCurrentVersionCode(MainActivity.this)) {
                            if (appBean.getVersionName().contains("force")) {
                                CheckUpdateHelper.buildForceUpdateDialog(MainActivity.this, appBean);
                            } else {
                                CheckUpdateHelper.buildUpdateDialog(MainActivity.this, appBean);
                            }
                        }
                    }
                });
    }

    @Override
    public void setDrawerMenuClicked(int item) {
        mNavView.getMenu().findItem(item).setChecked(true);
    }

    @Override
    public void initToolbar(Toolbar toolbar) {
        mToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    @Override
    public void hasRoot(boolean hasRoot) {
        mNoSuTextView.setVisibility(hasRoot ? View.INVISIBLE : View.VISIBLE);
    }
}
