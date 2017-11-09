package io.github.mcxinyu.housi.fragment;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AlertDialog;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.util.Colors;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.views.PgyerDialog;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import io.github.mcxinyu.housi.BuildConfig;
import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.activity.AboutActivity;
import io.github.mcxinyu.housi.services.AlarmService;
import io.github.mcxinyu.housi.util.CheckUpdateHelper;
import io.github.mcxinyu.housi.util.LogUtils;
import io.github.mcxinyu.housi.util.QueryPreferences;
import io.github.mcxinyu.housi.util.StateUtils;
import moe.shizuku.preference.ListPreference;
import moe.shizuku.preference.Preference;
import moe.shizuku.preference.PreferenceFragment;
import moe.shizuku.preference.PreferenceScreen;
import moe.shizuku.preference.SwitchPreference;
import rx.functions.Action1;

/**
 * Created by huangyuefeng on 2017/9/21.
 * Contact me : mcxinyu@gmail.com
 */
public class PreferencesFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {
    private static final String TAG = "PreferencesFragment";

    private static final SimpleDateFormat SERVICE_START_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final int REQUEST_CODE_ALARM = 1024;

    private ListPreference mSettingCurrentSourceUrlMulti;
    private SwitchPreference mSettingSwitchAlarmService;
    private PreferenceScreen mSettingServiceStartTime;
    private ListPreference mSettingAlarmRepeat;
    private PreferenceScreen mSettingAboutService;
    private PreferenceScreen mSettingCheckForUpdate;
    private PreferenceScreen mSettingCleanCache;
    private PreferenceScreen mSettingFaq;
    private PreferenceScreen mSettingFeedback;
    private PreferenceScreen mSettingAbout;
    private PreferenceScreen mSettingAboutCache;

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
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_fragment);
        initPreferences();
    }

    private void initPreferences() {
        mSettingCurrentSourceUrlMulti = (ListPreference) findPreference("setting_current_source_url_multi");
        mSettingSwitchAlarmService = (SwitchPreference) findPreference("setting_switch_alarm_service");
        mSettingServiceStartTime = (PreferenceScreen) findPreference("setting_service_start_time");
        mSettingAlarmRepeat = (ListPreference) findPreference("setting_alarm_repeat");
        mSettingAboutService = (PreferenceScreen) findPreference("setting_about_service");
        mSettingCheckForUpdate = (PreferenceScreen) findPreference("setting_check_for_update");
        mSettingCleanCache = (PreferenceScreen) findPreference("setting_clean_cache");
        mSettingFaq = (PreferenceScreen) findPreference("setting_faq");
        mSettingFeedback = (PreferenceScreen) findPreference("setting_feedback");
        mSettingAbout = (PreferenceScreen) findPreference("setting_about");
        mSettingAboutCache = (PreferenceScreen) findPreference("setting_about_cache");

        mSettingCurrentSourceUrlMulti.setOnPreferenceClickListener(this);
        mSettingServiceStartTime.setOnPreferenceClickListener(this);
        mSettingCheckForUpdate.setOnPreferenceClickListener(this);
        mSettingCleanCache.setOnPreferenceClickListener(this);
        mSettingFaq.setOnPreferenceClickListener(this);
        mSettingFeedback.setOnPreferenceClickListener(this);
        mSettingAbout.setOnPreferenceClickListener(this);

        mSettingCurrentSourceUrlMulti.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String[] urlEntries = (String[]) ((ListPreference) preference).getEntries();

                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newRawUri("url", Uri.parse(urlEntries[Integer.parseInt((String) newValue)]));
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getActivity(), getString(R.string.clipboard_hint), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        initSourceUrl();
        initSwitchAlarmService();
        initVersionInfo();
        initCache();
    }

    public void initSourceUrl() {
        int routing = QueryPreferences.getSourceRouting(getActivity());
        switch (routing) {
            case 0:
                initBuiltInDownloadUrlMulti();
                break;
            case 1:
                initDiyDownloadUrl();
                break;
        }
    }

    private void initBuiltInDownloadUrlMulti() {
        Set<String> downloadUrlSet = QueryPreferences.getSourceBuiltInMultiDownloadUrl(getActivity());
        if (downloadUrlSet == null) {
            String[] urlEntries = {getDefaultDownloadUrl()};
            mSettingCurrentSourceUrlMulti.setEntryValues(new String[]{"0"});
            mSettingCurrentSourceUrlMulti.setEntries(urlEntries);
            mSettingCurrentSourceUrlMulti.setSummary(
                    String.format(getString(R.string.current_has_multi_sources), urlEntries.length));
        } else {
            String[] urlEntries = new String[downloadUrlSet.size()];
            downloadUrlSet.toArray(urlEntries);
            String[] values = new String[downloadUrlSet.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = "" + i;
            }
            mSettingCurrentSourceUrlMulti.setEntryValues(values);
            mSettingCurrentSourceUrlMulti.setEntries(urlEntries);
            mSettingCurrentSourceUrlMulti.setSummary(
                    String.format(getString(R.string.current_has_multi_sources), urlEntries.length));
        }
    }

    private void initDiyDownloadUrl() {
        String hostsUrl = QueryPreferences.getSourceDiyDownloadUrl(getActivity());
        if (hostsUrl == null) {
            hostsUrl = getDefaultDownloadUrl();
        }
        String[] urlEntries = {hostsUrl};

        mSettingCurrentSourceUrlMulti.setEntryValues(new String[]{"0"});
        mSettingCurrentSourceUrlMulti.setEntries(urlEntries);
        mSettingCurrentSourceUrlMulti.setSummary(
                String.format(getString(R.string.current_diy_sources), urlEntries.length));
    }

    private String getDefaultDownloadUrl() {
        return BuildConfig.DEFAULT_HOSTS_URL;
    }

    private void initSwitchAlarmService() {
        if (mSettingSwitchAlarmService.isChecked()) {
            mSettingAboutService.setVisible(true);
            mSettingServiceStartTime.setVisible(true);
            mSettingAlarmRepeat.setVisible(true);
        } else {
            mSettingAboutService.setVisible(false);
            mSettingServiceStartTime.setVisible(false);
            mSettingAlarmRepeat.setVisible(false);
        }

        mSettingSwitchAlarmService.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mSettingSwitchAlarmService.setChecked((Boolean) newValue);

                mSettingAboutService.setVisible((Boolean) newValue);
                mSettingServiceStartTime.setVisible((Boolean) newValue);
                mSettingAlarmRepeat.setVisible((Boolean) newValue);
                return true;
            }
        });

        initServiceAlarmTime();
        initAlarmRepeat();
    }

    private void initServiceAlarmTime() {
        final Calendar calendar = Calendar.getInstance();

        String startTime = QueryPreferences.getSettingServiceStartTime(getContext());
        if (startTime != null) {
            calendar.setTimeInMillis(Long.parseLong(startTime));
        }
        QueryPreferences.setSettingServiceStartTime(getContext(), calendar.getTimeInMillis() + "");
        mSettingServiceStartTime.setSummary(SERVICE_START_TIME_FORMAT.format(calendar.getTime()));
    }

    private void initAlarmRepeat() {
        mSettingAlarmRepeat.setEntries(getResources().getStringArray(R.array.setting_repeat));
        mSettingAlarmRepeat.setEntryValues(getResources().getStringArray(R.array.setting_repeat_values));
        String repeat = QueryPreferences.getSettingAlarmRepeat(getContext());
        if (repeat == null) {
            repeat = getResources().getStringArray(R.array.setting_repeat_values)[0];
            mSettingAlarmRepeat.setValue(repeat);
        }
        setAlarmRepeatSummary(repeat);
        mSettingAlarmRepeat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                setAlarmRepeatSummary((String) newValue);
                return true;
            }
        });
    }

    private void setAlarmRepeatSummary(String index) {
        switch (index) {
            case "0":
                mSettingAlarmRepeat.setSummary(getResources().getStringArray(R.array.setting_repeat)[0]);
                break;
            case "1":
                mSettingAlarmRepeat.setSummary(getResources().getStringArray(R.array.setting_repeat)[1]);
                break;
            case "2":
                mSettingAlarmRepeat.setSummary(getResources().getStringArray(R.array.setting_repeat)[2]);
                break;
            case "3":
                mSettingAlarmRepeat.setSummary(getResources().getStringArray(R.array.setting_repeat)[3]);
                break;
        }
    }

    private void initVersionInfo() {
        mSettingCheckForUpdate.setSummary("当前版本：" + CheckUpdateHelper.getCurrentVersionName(getActivity()));
    }

    private void showPgyerDialog() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            PgyerDialog.setDialogTitleBackgroundColor("#FF4081");
                            PgyFeedback.getInstance().showDialog(getActivity());
                        } else {
                            Toast.makeText(getActivity(),
                                    getString(R.string.please_grant_permission), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void initCache() {
        mSettingAboutCache.setVisible(false);
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

    private long getCacheSize() {
        try {
            return getFolderSize(getContext().getCacheDir()) +
                    getFolderSize(getContext().getExternalCacheDir());
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

    private String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileS == 0) {
            mSettingAboutCache.setVisible(false);
            return "0MB";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        mSettingAboutCache.setVisible(true);
        return fileSizeString;
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
        cleanCacheFolder(getContext().getCacheDir(), System.currentTimeMillis());
        cleanCacheFolder(getContext().getExternalCacheDir(), System.currentTimeMillis());
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
        Toast.makeText(getContext(), getString(R.string.checking_for_update), Toast.LENGTH_SHORT).show();

        PgyUpdateManager.register(getActivity(), "io.github.mcxinyu.housi.pgy",
                new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        mSettingCheckForUpdate.setSummary("当前为最新版本：" +
                                CheckUpdateHelper.getCurrentVersionName(getContext()));
                        PgyUpdateManager.unregister();
                    }

                    @Override
                    public void onUpdateAvailable(String result) {
                        LogUtils.d(TAG, result);
                        final AppBean appBean = getAppBeanFromString(result);

                        if (Integer.parseInt(appBean.getVersionCode()) >
                                CheckUpdateHelper.getCurrentVersionCode(getContext())) {
                            Toast.makeText(getContext(), getString(R.string.has_new_version), Toast.LENGTH_SHORT).show();
                            mSettingCheckForUpdate.setSummary("最新版本：" +
                                    appBean.getVersionName() + "（当前版本：" +
                                    CheckUpdateHelper.getCurrentVersionName(getContext()) + "）");

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
                                        public void onClick(final DialogInterface dialog, int which) {
                                            RxPermissions rxPermissions = new RxPermissions(getActivity());
                                            rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                    .subscribe(new Action1<Permission>() {
                                                        @Override
                                                        public void call(Permission permission) {
                                                            if (permission.granted) {
                                                                if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                                                    if (!StateUtils.isNetworkAvailable(getContext())) {
                                                                        Toast.makeText(getContext(),
                                                                                getString(R.string.network_is_not_available),
                                                                                Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        startDownloadTask(getActivity(), appBean.getDownloadURL());
                                                                    }
                                                                }
                                                            } else if (permission.shouldShowRequestPermissionRationale) {
                                                                // 用户拒绝了权限申请
                                                                Toast.makeText(getContext(),
                                                                        getString(R.string.need_storage),
                                                                        Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                // 用户拒绝，并且选择不再提示
                                                                // 可以引导用户进入权限设置界面开启权限
                                                                Toast.makeText(getContext(),
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
                        PgyUpdateManager.unregister();
                    }
                });
    }

    private void copyUrlToClipboard(Preference preference) {
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newRawUri(preference.getSummary(),
                Uri.parse(preference.getSummary().toString()));
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getContext(), getString(R.string.clipboard_hint), Toast.LENGTH_SHORT).show();
        }
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
                .start(getContext());
    }

    private void showFaqTab() {
        String faqUrl = BuildConfig.FAQ_URL;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorAccent))
                .setShowTitle(true)
                .addDefaultShareMenuItem();
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(getContext(), Uri.parse(faqUrl));
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();

        String startTime = QueryPreferences.getSettingServiceStartTime(getContext());
        if (startTime != null) {
            calendar.setTimeInMillis(Long.parseLong(startTime));
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        QueryPreferences.setSettingServiceStartTime(getContext(), calendar.getTimeInMillis() + "");
                        mSettingServiceStartTime.setSummary(SERVICE_START_TIME_FORMAT.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.setTitle("设置时间");
        timePickerDialog.show();
    }

    public void setAlarm() {
        getContext().startService(AlarmService.newIntent(getContext(), false));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case "setting_current_source_url":
                copyUrlToClipboard(preference);
                return true;
            case "setting_service_start_time":
                showTimePicker();
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
                startActivity(AboutActivity.newIntent(getContext()));
                return true;
        }
        return false;
    }
}
