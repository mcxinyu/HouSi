package io.github.mcxinyu.housi.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by huangyuefeng on 2017/4/25.
 * Contact me : mcxinyu@foxmail.com
 */
public class QueryPreferences {
    private static final String IS_DRAWER_OPENED = "is_drawer_opened";

    private static final String SOURCE_ROUTING = "source_routing";

    private static final String SOURCE_BUILT_IN_DOWNLOAD_URL = "source_built_in_download_url";
    private static final String SOURCE_DIY_DOWNLOAD_URL = "source_diy_download_url";

    private static final String SETTING_SWITCH_ALARM_SERVICE = "setting_switch_alarm_service";
    private static final String SETTING_SERVICE_START_TIME = "setting_service_start_time";
    private static final String SETTING_ALARM_REPEAT = "setting_alarm_repeat";

    private static final String USE_FIREBASE_CLOUD_MESSAGE = "use_firebase_cloud_message";

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

    public static String getSourceBuiltInDownloadUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SOURCE_BUILT_IN_DOWNLOAD_URL, null);
    }

    public static void setSourceBuiltInDownloadUrl(Context context, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SOURCE_BUILT_IN_DOWNLOAD_URL, value)
                .apply();
    }

    public static String getSourceDiyDownloadUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SOURCE_DIY_DOWNLOAD_URL, null);
    }

    public static boolean getSettingSwitchAlarmService(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SETTING_SWITCH_ALARM_SERVICE, false);
    }

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

    public static String getSettingAlarmRepeat(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SETTING_ALARM_REPEAT, null);
    }

    public static boolean getUseFirebaseCloudMessage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(USE_FIREBASE_CLOUD_MESSAGE, false);
    }

    public static void setUseFirebaseCloudMessage(Context context, boolean enable) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(USE_FIREBASE_CLOUD_MESSAGE, enable)
                .apply();
    }
}
