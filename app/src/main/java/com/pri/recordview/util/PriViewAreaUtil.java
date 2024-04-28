package com.pri.recordview.util;

import android.view.View;

public class PriViewAreaUtil {

    private static final String TAG = "PriViewAreaUtil";

    /**
     * clicked position is in View area return true
     *
     * @param view
     * @param x
     * @param y
     * @return boolean
     */
    public static boolean isTouchPointInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        // view.isClickable()
        boolean isTouchPointInView = x > left && x < right && y > top && y < bottom;
        //Log.d(TAG, "isTouchPointInView left: " + left + " right: "
        //        + right + " top: " + top + " bottom: " + bottom
        //        + " isTouchPointInView: " + isTouchPointInView);
        return isTouchPointInView;
    }

    public static int getViewLeftRawX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        return left;
    }

    public static int getViewCenterX(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int width = view.getMeasuredWidth();
        return left + width / 2;
    }
}
