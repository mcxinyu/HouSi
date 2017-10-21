package io.github.mcxinyu.housi.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;

import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.activity.MainActivity;
import io.github.mcxinyu.housi.util.LogUtils;

/**
 * Created by huangyuefeng on 2017/10/16.
 * Contact me : mcxinyu@gmail.com
 */
public class FCMMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMMessagingService";
    private static final int REQUEST_CODE_NOTIFICATION = 2048;
    private static final int NOTIFICATION_ID = 2049;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        LogUtils.d(TAG, "remoteMessage from : " + remoteMessage.getFrom());
        doNotify();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    private void doNotify() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent newIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_NOTIFICATION, newIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
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
    }
}
