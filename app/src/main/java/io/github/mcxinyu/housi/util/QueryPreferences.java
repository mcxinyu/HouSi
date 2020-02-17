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

    public static String getSourceDiyDownloadUrl(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SOURCE_DIY_DOWNLOAD_URL, null);
    }
}
