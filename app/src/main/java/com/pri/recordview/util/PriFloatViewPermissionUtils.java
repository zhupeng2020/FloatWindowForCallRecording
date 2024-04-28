package com.pri.recordview.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PriFloatViewPermissionUtils {
    public static void grantFloatPermission(Context context) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            appOpsManager.setMode(AppOpsManager.OP_SYSTEM_ALERT_WINDOW,
                    packageInfo.applicationInfo.uid, context.getPackageName(), AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            LogUtils.e("grantFloatPermission", "Exception when retrieving package.");
        }
    }
}
