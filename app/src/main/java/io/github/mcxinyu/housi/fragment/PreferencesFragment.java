package io.github.mcxinyu.housi.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.util.Colors;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.views.PgyerDialog;

import java.io.File;
import java.text.DecimalFormat;

import io.github.mcxinyu.housi.BuildConfig;
import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.util.CheckUpdateHelper;
import io.github.mcxinyu.housi.util.LogUtils;
import io.github.mcxinyu.housi.util.QueryPreferences;

/**
 * Created by huangyuefeng on 2017/9/21.
 * Contact me : mcxinyu@gmail.com
 */
public class PreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private static final String TAG = "PreferencesFragment";

    private PreferenceScreen mSettingCurrentSourceUrl;
    private PreferenceScreen mSettingCheckForUpdate;
    private PreferenceScreen mSettingCleanCache;
    private PreferenceScreen mSettingFaq;
    private PreferenceScreen mSettingFeedback;
    private PreferenceScreen mSettingAbout;

    private boolean isPgyRegister;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // count cache
                case -1:
                    break;
                case 1:
                    mSettingCleanCache.setSummary("缓存占用：" + formatFileSize((Long) msg.obj));
                    break;
                //clean cache
                case -2:
                    Toast.makeText(getActivity(), getString(R.string.clean_cache_fail), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    initCache();
                    Toast.makeText(getActivity(), getString(R.string.clean_cache_up), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public static PreferencesFragment newInstance() {

        Bundle args = new Bundle();

        PreferencesFragment fragment = new PreferencesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_fragment);
        initPreferences();
    }

    private void initPreferences() {
        mSettingCurrentSourceUrl = (PreferenceScreen) findPreference("setting_current_source_url");
        mSettingCheckForUpdate = (PreferenceScreen) findPreference("setting_check_for_update");
        mSettingCleanCache = (PreferenceScreen) findPreference("setting_clean_cache");
        mSettingFaq = (PreferenceScreen) findPreference("setting_faq");
        mSettingFeedback = (PreferenceScreen) findPreference("setting_feedback");
        mSettingAbout = (PreferenceScreen) findPreference("setting_about");

        mSettingCurrentSourceUrl.setOnPreferenceClickListener(this);
        mSettingCheckForUpdate.setOnPreferenceClickListener(this);
        mSettingCleanCache.setOnPreferenceClickListener(this);
        mSettingFaq.setOnPreferenceClickListener(this);
        mSettingFeedback.setOnPreferenceClickListener(this);
        mSettingAbout.setOnPreferenceClickListener(this);

        mSettingCheckForUpdate.setSummary("当前版本：" + CheckUpdateHelper.getCurrentVersionName(getActivity()));

        initSourceUrl();
        initCache();
    }

    public void initSourceUrl() {
        mSettingCurrentSourceUrl.setSummary(getCurrentDownloadUrl());
    }

    @NonNull
    private String getCurrentDownloadUrl() {
        String hostsUrl = null;
        int routing = QueryPreferences.getSourceRouting(getActivity());
        switch (routing) {
            case 0:
                hostsUrl = QueryPreferences.getSourceBuiltInDownloadUrl(getActivity());
                break;
            case 1:
                hostsUrl = QueryPreferences.getSourceDiyDownloadUrl(getActivity());
                if (hostsUrl == null) {
                    hostsUrl = QueryPreferences.getSourceBuiltInDownloadUrl(getActivity());
                }
                break;
        }

        if (hostsUrl == null) {
            hostsUrl = BuildConfig.DEFAULT_HOSTS_URL;
        }
        return hostsUrl;
    }

    private void showPgyerDialog() {
        PgyerDialog.setDialogTitleBackgroundColor("#FF4081");
        PgyFeedback.getInstance().showDialog(getActivity());
    }

    private void initCache() {
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    msg.obj = getCacheSize();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    private String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileS == 0) {
            fileSizeString = "0MB";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    private long getCacheSize() {
        try {
            return getFolderSize(getActivity().getCacheDir()) +
                    getFolderSize(getActivity().getExternalCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private void clearAppCache() {
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    clearCache();
                    msg.what = 2;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -2;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void clearCache() {
        //清除数据缓存
        cleanCacheFolder(getActivity().getCacheDir(), System.currentTimeMillis());
        cleanCacheFolder(getActivity().getExternalCacheDir(), System.currentTimeMillis());
    }

    private int cleanCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += cleanCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    private void checkForUpdate() {
        Toast.makeText(getActivity(), getString(R.string.checking_for_update), Toast.LENGTH_SHORT).show();

        PgyUpdateManager.register(getActivity(), "io.github.mcxinyu.housi.pgy",
                new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        mSettingCheckForUpdate.setSummary("当前为最新版本：" +
                                CheckUpdateHelper.getCurrentVersionName(getActivity()));
                    }

                    @Override
                    public void onUpdateAvailable(String result) {
                        LogUtils.d(TAG, result);
                        final AppBean appBean = getAppBeanFromString(result);

                        if (Integer.parseInt(appBean.getVersionCode()) >
                                CheckUpdateHelper.getCurrentVersionCode(getActivity())) {
                            Toast.makeText(getActivity(), getString(R.string.has_new_version), Toast.LENGTH_SHORT).show();
                            mSettingCheckForUpdate.setSummary("最新版本：" +
                                    appBean.getVersionName() + "（当前版本：" +
                                    CheckUpdateHelper.getCurrentVersionName(getActivity()) + "）");

                            // if (appBean.getVersionName().contains("force")) {
                            //     CheckUpdateHelper.buildForceUpdateDialog(getActivity(), appBean);
                            // } else {
                            //     CheckUpdateHelper.buildUpdateDialog(getActivity(), appBean);
                            // }

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("更新")
                                    .setMessage(appBean.getReleaseNote())
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("下载", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startDownloadTask(getActivity(), appBean.getDownloadURL());
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                });
        isPgyRegister = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isPgyRegister)
            PgyUpdateManager.unregister();
    }

    private void copyUrlToClipboard(Preference preference) {
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newRawUri(preference.getSummary(),
                Uri.parse(preference.getSummary().toString()));
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getActivity(), getString(R.string.clipboard_hint), Toast.LENGTH_SHORT).show();
    }

    private void startAboutActivity() {
        new LibsBuilder()
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withActivityColor(new Colors(getResources().getColor(R.color.colorAccent),
                        getResources().getColor(R.color.colorAccent)))
                .withAboutSpecial1Description(getString(R.string.aboutLibraries_description_text))
                .withAboutAppName(getString(R.string.app_name))
                .withActivityTitle(getString(R.string.about_title))
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .start(getActivity());
    }

    private void showFaqTab() {
        String faqUrl = BuildConfig.FAQ_URL;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorAccent));
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(getActivity(), Uri.parse(faqUrl));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case "setting_current_source_url":
                copyUrlToClipboard(preference);
                return true;
            case "setting_check_for_update":
                checkForUpdate();
                return true;
            case "setting_clean_cache":
                clearAppCache();
                return true;
            case "setting_faq":
                showFaqTab();
                return true;
            case "setting_feedback":
                showPgyerDialog();
                return true;
            case "setting_about":
                startAboutActivity();
                return true;
        }
        return false;
    }
}
