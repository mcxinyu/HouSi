package io.github.mcxinyu.housi.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.activity.MainActivity;
import io.github.mcxinyu.housi.services.AlarmService;
import io.github.mcxinyu.housi.util.LogUtils;

/**
 * Created by huangyuefeng on 2017/9/27.
 * Contact me : mcxinyu@gmail.com
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    private static final int REQUEST_CODE_NOTIFICATION = 2048;
    private static final int NOTIFICATION_ID = 2049;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("NOTIFICATION")) {

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent newIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE_NOTIFICATION, newIntent, 0);

            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification_alarm)
                    .setTicker("后司提醒您更新 hosts")
                    .setContentTitle("后司提醒")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("经常更新 hosts 可以获得更好的网络体验。"))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setNumber(1)
                    .build();
            if (notificationManager != null) {
                notificationManager.notify(TAG, NOTIFICATION_ID, notification);
                LogUtils.d(TAG, "notification showed " + Calendar.getInstance().getTime().toString());
            }

            context.startService(AlarmService.newIntent(context, true));
        }
    }
}
