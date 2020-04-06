package io.github.mcxinyu.housi.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Calendar;

import io.github.mcxinyu.housi.R;
import io.github.mcxinyu.housi.receiver.AlarmReceiver;
import io.github.mcxinyu.housi.util.LogUtils;
import io.github.mcxinyu.housi.util.QueryPreferences;

/**
 * Created by huangyuefeng on 2017/9/28.
 * Contact me : mcxinyu@gmail.com
 */
public class AlarmService extends IntentService {
    private static final String TAG = "AlarmService";
    private static final String IS_RECEIVER_START = "is_service_start";

    private static final int REQUEST_CODE_ALARM = 1024;

    public static Intent newIntent(Context context, boolean isReceiverStart) {

        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(IS_RECEIVER_START, isReceiverStart);
        return intent;
    }

    public AlarmService() {
        super(TAG);
    }

    public AlarmService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean isReceiverStart = false;
        if (intent != null) {
            isReceiverStart = intent.getBooleanExtra(IS_RECEIVER_START, false);
        }

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent receiverIntent = new Intent(this, AlarmReceiver.class);
        receiverIntent.setAction("NOTIFICATION");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                REQUEST_CODE_ALARM,
                receiverIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        String repeat = QueryPreferences.getSettingAlarmRepeat(this);
        String startTime = QueryPreferences.getSettingServiceStartTime(this);
        Calendar calendarStartTime = Calendar.getInstance();
        calendarStartTime.setTimeInMillis(Long.parseLong(startTime));
        Calendar now = Calendar.getInstance();

        if (!QueryPreferences.getSettingSwitchAlarmService(this)) {
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
                LogUtils.d(TAG, "set alarm cancel : " + repeat + " : " + calendarStartTime.getTime().toString());
            }
            return;
        }

        switch (repeat) {
            case "0":   // 仅一次
                if (isReceiverStart) {
                    return;
                }

                while (now.after(calendarStartTime)) {
                    calendarStartTime.add(Calendar.DAY_OF_MONTH, 1);
                }

                if (alarmManager != null) {
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendarStartTime.getTimeInMillis(),
                            0,
                            pendingIntent);
                }
                LogUtils.d(TAG, "set alarm success : " + repeat + " : " + calendarStartTime.getTime().toString());
                break;
            case "1":   // 周末两天
                if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    while (now.after(calendarStartTime)) {
                        calendarStartTime.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    if (alarmManager != null) {
                        alarmManager.setRepeating(
                                AlarmManager.RTC_WAKEUP,
                                calendarStartTime.getTimeInMillis(),
                                0,
                                pendingIntent
                        );
                    }
                } else if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    while (now.after(calendarStartTime)) {
                        calendarStartTime.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    if (alarmManager != null) {
                        alarmManager.setRepeating(
                                AlarmManager.RTC_WAKEUP,
                                calendarStartTime.getTimeInMillis(),
                                6 * 24 * 60 * 60 * 1000,
                                pendingIntent
                        );
                    }
                } else {
                    while (calendarStartTime.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                        calendarStartTime.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    while (now.after(calendarStartTime)) {
                        calendarStartTime.add(Calendar.DAY_OF_MONTH, 7);
                    }
                    if (alarmManager != null) {
                        alarmManager.setRepeating(
                                AlarmManager.RTC_WAKEUP,
                                calendarStartTime.getTimeInMillis(),
                                0,
                                pendingIntent
                        );
                    }
                }
                LogUtils.d(TAG, "set alarm success : " + repeat + " : " + calendarStartTime.getTime().toString());
                break;
            case "2":   // 周一至周五
                if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                        now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    while (now.after(calendarStartTime)) {
                        calendarStartTime.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    // 把时间调整到周一
                    if (calendarStartTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        calendarStartTime.add(Calendar.DAY_OF_MONTH, 2);
                    } else if (calendarStartTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        calendarStartTime.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    if (alarmManager != null) {
                        alarmManager.setRepeating(
                                AlarmManager.RTC_WAKEUP,
                                calendarStartTime.getTimeInMillis(),
                                0,
                                pendingIntent
                        );
                    }
                } else {
                    while (now.after(calendarStartTime)) {
                        calendarStartTime.add(Calendar.DAY_OF_MONTH, 1);
                    }

                    if (alarmManager != null) {
                        alarmManager.setRepeating(
                                AlarmManager.RTC_WAKEUP,
                                calendarStartTime.getTimeInMillis(),
                                0,
                                pendingIntent
                        );
                    }
                }
                LogUtils.d(TAG, "set alarm success : " + repeat + " : " + calendarStartTime.getTime().toString());
                break;
            case "3":   // 每天
                if (isReceiverStart) {
                    return;
                }
                while (now.after(calendarStartTime)) {
                    calendarStartTime.add(Calendar.DAY_OF_MONTH, 1);
                }

                if (alarmManager != null) {
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendarStartTime.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent
                    );
                }
                LogUtils.d(TAG, "set alarm success : " + repeat + " : " + calendarStartTime.getTime().toString());
                break;
        }
        addAccount();
    }

    private static final String ACCOUNT_TYPE = "io.github.mcxinyu.housi.account.anonymous";

    private void addAccount() {
        AccountManager accountManager = (AccountManager) this.getSystemService(Context.ACCOUNT_SERVICE);
        Account account = null;
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = new Account(getString(R.string.account_name), ACCOUNT_TYPE);
        }

        if (accountManager.addAccountExplicitly(account, null, null)) {
            ContentResolver.setIsSyncable(account, "io.mcxinyu.github", 1);
            ContentResolver.setSyncAutomatically(account, "io.mcxinyu.github", true);
            ContentResolver.addPeriodicSync(account, "io.mcxinyu.github", new Bundle(), 60 * 60);
        }
    }
}
