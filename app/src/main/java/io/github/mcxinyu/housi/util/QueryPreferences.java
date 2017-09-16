package io.github.mcxinyu.housi.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by huangyuefeng on 2017/4/25.
 * Contact me : mcxinyu@foxmail.com
 */
public class QueryPreferences {
    private static final String IS_DRAWER_OPENED = "is_drawer_opened";

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
}
