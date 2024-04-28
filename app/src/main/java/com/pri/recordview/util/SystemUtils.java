package com.pri.recordview.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class SystemUtils {

    public static int getStatusBarHeight(Context context) {
        int defaultStatusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height_default", "dimen", "android");
        if (resourceId > 0) {
            defaultStatusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return defaultStatusBarHeight;
    }

    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getScreenWidth(Context context) {
        int screenWith = -1;
        try {
            screenWith = context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWith;
    }

    public static int getScreenHeight(Context context) {
        int screenHeight = -1;
        try {
            screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenHeight;
    }

    public static int getRealScreenHeight(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size.y;
    }


    public static int dp2px(Context context, float value) {
        float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

}
