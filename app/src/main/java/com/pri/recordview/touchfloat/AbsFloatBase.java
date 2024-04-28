package com.pri.recordview.touchfloat;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.pri.recordview.util.LogUtils;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import static android.content.Context.WINDOW_SERVICE;
import static android.view.WindowManager.LayoutParams.TYPE_TOAST;

/**
 * @Description: AbsFloatBase
 * @Author: zhupeng
 * @CreateDate: 2022\3\1 14:52
 */
public abstract class AbsFloatBase {

    public static final String TAG = "AbsFloatBase";

    static final int FULLSCREEN_TOUCHABLE = 1;
    static final int FULLSCREEN_NOT_TOUCHABLE = 2;
    static final int WRAP_CONTENT_TOUCHABLE = 3;
    static final int WRAP_CONTENT_NOT_TOUCHABLE = 4;

    WindowManager.LayoutParams mLayoutParams;

    View mInflate;
    Context mContext;
    WindowManager mWindowManager;
    private boolean mAdded;
    // sets whether hidden is invisible
    private boolean mInvisibleNeed = false;
    private boolean mRequestFocus = false;
    int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
    int mViewMode = WRAP_CONTENT_NOT_TOUCHABLE;
    protected int mAddX = 0;
    protected int mAddY = 0;

    public AbsFloatBase(Context context) {
        mContext = context;
        create();
    }

    /**
     * Sets whether hidden is invisible,default: gone
     *
     * @param invisibleNeed Invisible ?
     */
    public void setInvisibleNeed(boolean invisibleNeed) {
        mInvisibleNeed = invisibleNeed;
    }

    /**
     * Whether the floating window needs to obtain the focus. Usually, after obtaining the focus,
     * the floating window can interact with the soft keyboard, and the covered application loses the focus.
     * For example: the game will lose the background music
     *
     * @param requestFocus
     */
    public void requestFocus(boolean requestFocus) {
        mRequestFocus = requestFocus;
    }

    @CallSuper
    public void create() {
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(WINDOW_SERVICE);
    }


    @CallSuper
    public synchronized void show() {
        if (mInflate == null)
            throw new IllegalStateException("FloatView can not be null");

        if (mAdded) {
            //mInflate.setVisibility(View.VISIBLE);
            setViewVisibility(mInflate, true);
            return;
        }

        getLayoutParam(mViewMode);

        mInflate.setVisibility(View.VISIBLE);

        try {
            mLayoutParams.x = mAddX;
            mLayoutParams.y = mAddY;

            mWindowManager.addView(mInflate, mLayoutParams);
            mAdded = true;
        } catch (Exception e) {
            LogUtils.e(TAG, "show function, add floatView failed");
            e.printStackTrace();
            onAddWindowFailed(e);
        }
    }

    @CallSuper
    public void hide() {
        if (mInflate != null) {
            mInflate.setVisibility(View.INVISIBLE);
            //setViewVisibility(mInflate, false);
        }
    }

    /**
     * @param view
     * @param visible
     */
    public void setViewVisibility(View view, boolean visible) {
        if (view == null) {
            return;
        }
        view.setAlpha(visible ? 0f : 1f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(visible ? 1f : 0f)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    @CallSuper
    public void gone() {
        if (mInflate != null) {
            mInflate.setVisibility(View.GONE);
        }
    }

    @CallSuper
    public void remove() {
        if (mInflate != null && mWindowManager != null) {
            if (mInflate.isAttachedToWindow()) {
                mWindowManager.removeView(mInflate);
            }
            mAdded = false;
        }
    }

    @CallSuper
    protected View inflate(@LayoutRes int layout) {
        mInflate = View.inflate(mContext, layout, null);
        return mInflate;
    }

    protected abstract void onAddWindowFailed(Exception e);

    @SuppressWarnings("unchecked")
    protected <T extends View> T findView(@IdRes int id) {
        if (mInflate != null) {
            return (T) mInflate.findViewById(id);
        }
        return null;
    }


    /**
     * Get the suspended window LayoutParam
     *
     * @param mode
     */
    protected void getLayoutParam(int mode) {
        switch (mode) {
            case FULLSCREEN_TOUCHABLE:
                mLayoutParams = getFloatLayoutParam(true, true);
                break;

            case FULLSCREEN_NOT_TOUCHABLE:
                mLayoutParams = getFloatLayoutParam(true, false);
                break;

            case WRAP_CONTENT_NOT_TOUCHABLE:
                mLayoutParams = getFloatLayoutParam(false, false);
                break;

            case WRAP_CONTENT_TOUCHABLE:
                mLayoutParams = getFloatLayoutParam(false, true);
                break;
        }

        if (mRequestFocus) {
            mLayoutParams.flags = mLayoutParams.flags & ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }

        mLayoutParams.gravity = mGravity;
    }

    /**
     * @param fullScreen
     * @param touchAble
     * @return
     */
    private static WindowManager.LayoutParams getFloatLayoutParam(boolean fullScreen, boolean touchAble) {

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            layoutParams.type = TYPE_TOAST;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //layoutParams.packageName = AppUtils.getAppPackageName();

        layoutParams.flags |= WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;

        if (touchAble) {
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        } else {
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }

        if (fullScreen) {
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        // set float ball show outside
        //layoutParams.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        //layoutParams.flags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;


        layoutParams.format = PixelFormat.TRANSPARENT;

        // opt float view
        layoutParams.setTrustedOverlay();

        layoutParams.privateFlags = WindowManager.LayoutParams.SYSTEM_FLAG_SHOW_FOR_ALL_USERS;

        return layoutParams;
    }

    /**
     * Get visibility
     *
     * @return
     */
    public boolean getVisibility() {
        if (mInflate != null && mInflate.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change visibility
     */
    public void toggleVisibility() {
        if (mInflate != null) {
            if (getVisibility()) {
                if (mInvisibleNeed) {
                    hide();
                } else {
                    gone();
                }
            } else {
                show();
            }
        }
    }
}
