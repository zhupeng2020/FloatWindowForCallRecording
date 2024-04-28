package com.pri.recordview.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pri.recordview.ScreenRecordFloatWindowService;
import com.pri.recordview.feature.PriRecordSet;
import com.pri.recordview.util.LogUtils;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent == null ? "" : intent.getAction();
        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                boolean isSwitchOpen = PriRecordSet.isCallRecordEnable(context);
                LogUtils.d(TAG, "receive action isSwitchOpen = " + isSwitchOpen);
                if (isSwitchOpen) {
                    // fixbug F1US-234
                    //context.startService(new Intent(context, ScreenRecordFloatWindowService.class));
                    //PriRecordSet.putCallRecordValue(context, 0);
                }
                break;
            default:
                break;
        }
    }

}
