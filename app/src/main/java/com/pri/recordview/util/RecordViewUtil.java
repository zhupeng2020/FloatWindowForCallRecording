package com.pri.recordview.util;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static com.pri.recordview.feature.PriRecordSet.NOT_SHOW_PACKAGES;

public class RecordViewUtil {

    private static final String TAG = "RecordViewUtil";
    private static long lastClickTime = 0;

    /**
     * format String[] to String
     *
     * @param packages
     * @return
     */
    private static String formatToString(String[] packages) {
        StringBuilder sb = new StringBuilder();
        if (packages != null && packages.length > 0) {
            for (int i = 0; i < packages.length; i++) {
                if (i < packages.length - 1) {
                    sb.append(packages[i]).append(",");
                } else {
                    sb.append(packages[i]);
                }
            }
        }
        return sb.toString();
    }


    /**
     * check app is Installed or not
     * <uses-permission android:name="android.permission.GET_PACKAGE_SIZE" />
     * <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName, int userId) {
        if (packageName == null || packageName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            //packageInfo = context.getPackageManager().getPackageInfoAsUser(packageName, GET_ACTIVITIES, userId);
            packageInfo = context.getPackageManager().getPackageInfo(packageName, GET_ACTIVITIES);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }


    public static boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    public static boolean isUserApp(PackageInfo pInfo) {
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
    }

    /**
     * get Top App packageName
     *
     * @param context
     * @return packageName
     */
    public static String getTopAppPackageName(Context context) {
        String packageName = "";
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        if (runningTasks != null && runningTasks.size() > 0 && runningTasks.get(0) != null) {
            packageName = runningTasks.get(0).topActivity.getPackageName();
        }
        return packageName;
    }

    /**
     * get Top App ClassName
     *
     * @param context
     * @return ClassName
     */
    public static String getTopAppClassName(Context context) {
        String className = "";
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
        if (runningTasks != null && runningTasks.size() > 0 && runningTasks.get(0) != null) {
            className = runningTasks.get(0).topActivity.getClassName();
        }
        return className;
    }


    public static boolean isFastClick(Long minDelayTime) {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= minDelayTime) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * @return isKeyGuardLocked
     */
    public static boolean isKeyGuardLocked(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.isKeyguardLocked();
    }

    public static boolean isInNotShowPackagesApps(String pkgName) {
        List<String> lists = Arrays.asList(NOT_SHOW_PACKAGES);
        return lists.contains(pkgName);
    }

    /**
     * in this case, clicked follow touch image will not show floatView
     *
     * @param context
     * @return
     */
    public static boolean notShowScreenMaskFloatView(Context context) {
        String packageName = getTopAppPackageName(context);
        LogUtils.d(TAG, " getRunningTasks topActivity packageName: " + packageName);
        boolean isInNotShowPackages = isInNotShowPackagesApps(packageName);
        boolean isKeyguardLocked = RecordViewUtil.isKeyGuardLocked(context);
        return isInNotShowPackages || isKeyguardLocked;
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningService : runningServices) {
            if (runningService.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningService : runningServices) {
            if (runningService.service.getClassName().equals(className)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isMtkPlatform() {
        if (Build.HARDWARE.matches("mt[0-9]*")) {
            return true;
        }
        return false;
    }

    public static float formatFloat(float alpha) {
        DecimalFormat df = new DecimalFormat("#.00");
        // fixbug X9ANF4-2312
        String fs = df.format(alpha);
        fs = fs.replace(",", ".");
        //LogUtils.d(TAG, "formatFloat fs: " + fs);
        return Float.parseFloat(fs);
    }

    public static void goToFloatRecordService(Context context, boolean shouldStart) {
        String PACKAGE_NAME = "com.pri.recordview";
        String CLASS_NAME = "com.pri.recordview.ScreenRecordFloatWindowService";
        boolean isServiceRunning = isServiceRunning(context, CLASS_NAME);
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(PACKAGE_NAME, CLASS_NAME);
        intent.setComponent(componentName);

        if (isServiceRunning) {
            if (shouldStart) {
                LogUtils.d(TAG, "isServiceRunning, return. ");
            } else {
                context.stopService(intent);
            }
        } else {
            if (shouldStart) {
                context.startService(intent);
            } else {
                LogUtils.d(TAG, "isService not Running, return. ");
            }
        }
    }

}
