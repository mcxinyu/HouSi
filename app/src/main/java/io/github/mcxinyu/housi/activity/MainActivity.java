package io.github.mcxinyu.housi.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.views.PgyerDialog;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.mcxinyu.housi.BuildConfig;
import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.fragment.ABaseFragment;
import io.github.mcxinyu.housi.fragment.BasicFragment;
import io.github.mcxinyu.housi.fragment.SettingsFragment;
import io.github.mcxinyu.housi.fragment.SourceFileFragment;
import io.github.mcxinyu.housi.fragment.SourceFragment;
import io.github.mcxinyu.housi.util.CheckUpdateHelper;
import io.github.mcxinyu.housi.util.LogUtils;
import io.github.mcxinyu.housi.util.QueryPreferences;
import io.github.mcxinyu.housi.util.StateUtils;
import rx.functions.Action1;

/**
 * Created by huangyuefeng on 2017/9/13.
 * Contact me : mcxinyu@gmail.com
 */
public class MainActivity extends BaseAppCompatActivity
        implements ABaseFragment.Callbacks, BasicFragment.Callbacks,
        SourceFragment.Callbacks, SourceFileFragment.Callbacks {
    private static final String TAG = "MainActivity";
    private static final int WHAT_CHECK_UPDATE = 1024;

    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private Unbinder unbinder;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!QueryPreferences.getDrawerOpenState(MainActivity.this)) {
                // mDrawerLayout.openDrawer(Gravity.START);
                // QueryPreferences.setDrawerOpenState(MainActivity.this, true);
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
    private SettingsFragment mSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar_TransparentStatusBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        initDrawer();

        mFragmentManager = getSupportFragmentManager();
        currentFragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            currentFragment = mBasicFragment = BasicFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, currentFragment)
                    .commit();
        }

        mHandler.sendEmptyMessageDelayed(WHAT_CHECK_UPDATE, 3000);
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
                        if (mSettingsFragment == null)
                            mSettingsFragment = SettingsFragment.newInstance();
                        switchFragment(mSettingsFragment);
                        break;
                    case R.id.nav_feedback:
                        showPgyerDialog();
                        break;
                    case R.id.nav_faq:
                        showFaqTab();
                        break;
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        TextView versionTextView = (TextView) mNavView.getHeaderView(0).findViewById(R.id.version_text_view);
        versionTextView.setText(getString(R.string.version) + ":" + BuildConfig.VERSION_NAME + "-" + BuildConfig.GIT_COMMIT);
    }

    private void showFaqTab() {
        String faqUrl = BuildConfig.FAQ_URL;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorAccent))
                .setShowTitle(true)
                .addDefaultShareMenuItem();
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(this, Uri.parse(faqUrl));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            PgyUpdateManager.unregister();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, e.getMessage());
        }
        currentFragment = null;
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (!(currentFragment instanceof BasicFragment)) {
            if (mBasicFragment == null)
                mBasicFragment = BasicFragment.newInstance();
            switchFragment(mBasicFragment);
        } else {
            super.onBackPressed();
        }
    }

    private void switchFragment(@NonNull Fragment fragment) {

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
        RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            PgyerDialog.setDialogTitleBackgroundColor("#FF4081");
                            PgyFeedback.getInstance().showDialog(MainActivity.this);
                        } else {
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.please_grant_permission), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkForUpdate() {

        PgyUpdateManager.register(MainActivity.this, "io.github.mcxinyu.housi.pgy",
                new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        PgyUpdateManager.unregister();
                    }

                    @Override
                    public void onUpdateAvailable(String result) {
                        LogUtils.d("MainActivity", result);
                        final AppBean appBean = getAppBeanFromString(result);

                        if (Integer.parseInt(appBean.getVersionCode()) >
                                CheckUpdateHelper.getCurrentVersionCode(MainActivity.this)) {
                            if (!isFinishing()) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("更新")
                                        .setMessage(appBean.getReleaseNote())
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setCancelable(!appBean.getVersionName().contains("force"))
                                        .setPositiveButton("下载", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
                                                RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                                                rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                        .subscribe(new Action1<Permission>() {
                                                            @Override
                                                            public void call(Permission permission) {
                                                                if (permission.granted) {
                                                                    if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                                                        if (!StateUtils.isNetworkAvailable(MainActivity.this)) {
                                                                            Toast.makeText(MainActivity.this,
                                                                                    getString(R.string.network_is_not_available),
                                                                                    Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            startDownloadTask(MainActivity.this,
                                                                                    appBean.getDownloadURL());
                                                                        }
                                                                    }
                                                                } else if (permission.shouldShowRequestPermissionRationale) {
                                                                    // 用户拒绝了权限申请
                                                                    Toast.makeText(MainActivity.this,
                                                                            getString(R.string.need_storage),
                                                                            Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    // 用户拒绝，并且选择不再提示
                                                                    // 可以引导用户进入权限设置界面开启权限
                                                                    Toast.makeText(MainActivity.this,
                                                                            getString(R.string.need_storage),
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        }
                        PgyUpdateManager.unregister();
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
}
