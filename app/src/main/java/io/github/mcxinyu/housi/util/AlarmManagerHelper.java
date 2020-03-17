package io.github.mcxinyu.housi.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

/**
 * Created by huangyuefeng on 2017/9/28.
 * Contact me : mcxinyu@gmail.com
 */
public class AlarmManagerHelper {

    public static void setAlarm(Context context, long triggerAtMillis, PendingIntent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, intent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, intent);
            }
        }
    }

    public static void setAlarmRepeat(Context context, long triggerAtMillis, long intervalMillis, PendingIntent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, intent);
        }
    }
}
