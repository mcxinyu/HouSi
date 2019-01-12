package io.github.mcxinyu.housi.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by huangyuefeng on 2019/1/13.
 * Contact me : mcxinyu@foxmail.com
 */
public class ScreenUtils {

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dpToPx(Context context, float dp) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi /
                DisplayMetrics.DENSITY_DEFAULT));
    }
}
