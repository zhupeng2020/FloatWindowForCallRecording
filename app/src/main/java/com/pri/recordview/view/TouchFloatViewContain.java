package com.pri.recordview.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.pri.recordview.util.LogUtils;

/**
 * @Description: TouchFloatViewContain for listen onConfigurationChanged
 * @Author: zhupeng
 * @CreateDate: 2022\3\9 18:01
 */
public class TouchFloatViewContain extends LinearLayout {

    private static final String TAG = "TouchFloatViewContain";

    public interface OnScreenOrientationChangeListener {
        void onChange(boolean isLandScape);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //return super.onInterceptTouchEvent(ev);
        return false;
    }

    public void setOnScreenOrientationChangeListener(OnScreenOrientationChangeListener onScreenOrientationChangeListener) {
        this.mOnScreenOrientationChangeListener = onScreenOrientationChangeListener;
    }

    private OnScreenOrientationChangeListener mOnScreenOrientationChangeListener;

    public TouchFloatViewContain(Context context) {
        super(context, null);
    }

    public TouchFloatViewContain(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d(TAG, "[onConfigurationChanged]  newConfig = " + newConfig.orientation);
        if (mOnScreenOrientationChangeListener != null) {
            mOnScreenOrientationChangeListener.onChange(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
        }
    }


}
