package io.github.mcxinyu.housi.util;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * Created by huangyuefeng on 2017/4/25.
 * Contact me : mcxinyu@foxmail.com
 */
public class QueryPreferences {
    private static final String IS_DRAWER_OPENED = "is_drawer_opened";

    private static final String SOURCE_ROUTING = "source_routing";

    private static final String SOURCE_BUILT_IN_DOWNLOAD_URL = "source_built_in_download_url";
    private static final String SOURCE_BUILT_IN_MULTI_DOWNLOAD_URL = "source_built_in_multi_download_url";
    private static final String SOURCE_DIY_DOWNLOAD_URL = "source_diy_download_url";

    private static final String SETTING_SWITCH_ALARM_SERVICE = "setting_switch_alarm_service";
    private static final String SETTING_SERVICE_START_TIME = "setting_service_start_time";
    private static final String SETTING_ALARM_REPEAT = "setting_alarm_repeat";

    public static boolean getDrawerOpenState(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(IS_DRAWER_OPENED, false);
    }

    public static void setDrawerOpenState(Context context, boolean enable) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(IS_DRAWER_OPENED, enable)
                .apply();
    }

    /**
     * 获取更新 hosts 的方式：内置源、自定义源、文件
     *
     * @param context
     * @return
     */
    public static int getSourceRouting(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(SOURCE_ROUTING, 0);
    }

    public static void setSourceRouting(Context context, int routing) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(SOURCE_ROUTING, routing)
                .apply();
    }

    /**
     * 获取内置源的下载 url（单个）
     *
     * @param context
     * @return
     */
    @Deprecated
    public static String getSourceBuiltInDownloadUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SOURCE_BUILT_IN_DOWNLOAD_URL, null);
    }

    @Deprecated
    public static void setSourceBuiltInDownloadUrl(Context context, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SOURCE_BUILT_IN_DOWNLOAD_URL, value)
                .apply();
    }

    /**
     * 获取内置源的下载 url（多个源）
     *
     * @param context
     * @return
     */
    public static Set<String> getSourceBuiltInMultiDownloadUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(SOURCE_BUILT_IN_MULTI_DOWNLOAD_URL, null);
    }

    public static void setSourceBuiltInMultiDownloadUrl(Context context, Set<String> value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(SOURCE_BUILT_IN_MULTI_DOWNLOAD_URL, value)
                .apply();
    }

    /**
     * 获取自定义源的 url
     *
     * @param context
     * @return
     */
    public static String getSourceDiyDownloadUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SOURCE_DIY_DOWNLOAD_URL, null);
    }

    /**
     * 后去通知功能总开关状态
     * @param context
     * @return
     */
    public static boolean getSettingSwitchAlarmService(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SETTING_SWITCH_ALARM_SERVICE, false);
    }

    /**
     * 获取通知功能的通知时间
     * @param context
     * @return
     */
    public static String getSettingServiceStartTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SETTING_SERVICE_START_TIME, null);
    }

    public static void setSettingServiceStartTime(Context context, String time) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SETTING_SERVICE_START_TIME, time)
                .apply();
    }

    /**
     * 后去通知功能通知时间的重复方式
     * @param context
     * @return
     */
    public static String getSettingAlarmRepeat(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SETTING_ALARM_REPEAT, null);
    }
}
