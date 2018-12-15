package io.github.mcxinyu.housi.services;

/**
 * Created by huangyuefeng on 2017/10/16.
 * Contact me : mcxinyu@gmail.com
 */
public class FCMMessagingService
        // extends FirebaseMessagingService
{
    private static final int REQUEST_CODE_NOTIFICATION = 2048;
    private static final int NOTIFICATION_ID = 2049;

    // @Override
    // public void onMessageReceived(RemoteMessage remoteMessage) {
    //     super.onMessageReceived(remoteMessage);
    //
    //     doNotify(remoteMessage);
    // }
    //
    // @Override
    // public void onDeletedMessages() {
    //     super.onDeletedMessages();
    // }
    //
    // @Override
    // public void onMessageSent(String s) {
    //     super.onMessageSent(s);
    // }
    //
    // @Override
    // public void onSendError(String s, Exception e) {
    //     super.onSendError(s, e);
    // }
    //
    // private void doNotify(RemoteMessage remoteMessage) {
    //     NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
    //     Intent newIntent = new Intent(this, MainActivity.class);
    //     PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_NOTIFICATION, newIntent, 0);
    //
    //     Notification notification = new NotificationCompat.Builder(this)
    //             .setSmallIcon(R.mipmap.ic_launcher)
    //             .setTicker(remoteMessage.getNotification().getTitle())
    //             .setContentTitle(remoteMessage.getNotification().getTitle())
    //             .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
    //             .setContentIntent(pendingIntent)
    //             .setAutoCancel(true)
    //             .setNumber(1)
    //             .build();
    //     if (notificationManager != null) {
    //         notificationManager.notify(TAG, NOTIFICATION_ID, notification);
    //         LogUtils.d(TAG, "notification showed " + Calendar.getInstance().getTime().toString());
    //     }
    // }
}
