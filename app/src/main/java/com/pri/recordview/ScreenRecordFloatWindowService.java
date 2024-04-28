package com.pri.recordview;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;

import com.pri.recordview.activity.MainActivity;
import com.pri.recordview.listener.FloatClickListener;
import com.pri.recordview.touchfloat.FollowTouchView;
import com.pri.recordview.util.LogUtils;
import com.pri.recordview.util.PriFloatViewPermissionUtils;

import androidx.core.app.NotificationCompat;

/**
 * @Description: ScreenRecordFloatWindowService
 * @Author: zhupeng
 * @CreateDate: 2024\4\25 11:11
 */
public class ScreenRecordFloatWindowService extends Service {

    private static final String TAG = "ScreenRecordService";
    private static final String ACTION_CALL_RECORDING = "com.pri.PRI_CALL_RECORDING";
    private static final String EXTRA_PHONE_NUMBER = "extra_phone_number";
    private NotificationManager mNotificationManager;
    private int NOTIFICATION_ID = 10081;
    private FollowTouchView mFollowTouchView;
    private String mPhoneNumber = "";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // grant FloatPermission
        if (!Settings.canDrawOverlays(ScreenRecordFloatWindowService.this)) {
            PriFloatViewPermissionUtils.grantFloatPermission(ScreenRecordFloatWindowService.this);
        }
        LogUtils.d(TAG, "onCreate. ");
        registerShutDownReceiver();
        showFollowTouch();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildNotification();
        if (intent != null) {
            mPhoneNumber  = intent.getStringExtra(EXTRA_PHONE_NUMBER);
            if (mFollowTouchView != null) {
                mFollowTouchView.setPhoneNumber(mPhoneNumber);
            }
        }
        LogUtils.d(TAG, "onStartCommand mPhoneNumber: " + mPhoneNumber);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelNotification();
        unRegisterCallRingReceiver();
        resetFollowTouch();
        LogUtils.d(TAG, "onDestroy. ");
    }

    private void showFollowTouch() {
        resetFollowTouch();
        LogUtils.d(TAG, "showFollowTouch start ");
        // FollowTouchView init
        mFollowTouchView = new FollowTouchView(this);
        mFollowTouchView.show();

        mFollowTouchView.setFloatClickListener(new FloatClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void resetFollowTouch() {
        if (mFollowTouchView != null) {
            mFollowTouchView.setFloatClickListener(null);
            mFollowTouchView.release();
            mFollowTouchView = null;
        }
    }


    private void buildNotification() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Intent settingIntent = new Intent(this, MainActivity.class);
        settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        String id = "channel_1";
        CharSequence name = getString(R.string.app_name_title);
        NotificationChannel notificationChannel =
                new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW); // not sound
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(false);
        mNotificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new NotificationCompat.Builder(this, id)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.qs_icon)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(PendingIntent.getActivity(ScreenRecordFloatWindowService.this, 0, settingIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE))
                .build();
        startForeground(NOTIFICATION_ID, notification);
        //mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void cancelNotification() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
    }

    /**
     * registerCallRingReceiver
     */
    private void registerShutDownReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SHUTDOWN);
        registerReceiver(mReceiver, filter, RECEIVER_EXPORTED);
    }

    /**
     * unRegisterCallRingReceiver
     */
    private void unRegisterCallRingReceiver() {
        LogUtils.d(TAG, "unRegisterCallRingReceiver  = ");
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.d(TAG, "Received broadcast with action: " + action);
            if (Intent.ACTION_SHUTDOWN.equals(action)) {
                stopSelf();
            }

        }
    };

}
