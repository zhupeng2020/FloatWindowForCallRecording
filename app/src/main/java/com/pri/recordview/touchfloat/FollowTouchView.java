package com.pri.recordview.touchfloat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pri.recordview.R;
import com.pri.recordview.listener.FloatClickListener;
import com.pri.recordview.listener.FloatMoveListener;
import com.pri.recordview.util.LogUtils;
import com.pri.recordview.util.PriAntiShakeUtils;
import com.pri.recordview.util.PriViewAreaUtil;
import com.pri.recordview.util.RecorderManager;
import com.pri.recordview.util.SystemUtils;
import com.pri.recordview.view.TouchFloatViewContain;

import static com.pri.recordview.feature.PriRecordSet.MARGIN_EDGE_H;
import static com.pri.recordview.feature.PriRecordSet.MARGIN_EDGE_V;
import static com.pri.recordview.feature.PriRecordSet.NAVIGATION_BAR_HEIGHT;

/**
 * @Description: FollowTouchView for phone recording
 * @Author: zhupeng
 * @CreateDate: 2024\4\25 11:11
 */

public class FollowTouchView extends AbsFloatBase {

    private static final String TAG = "FollowTouchView";
    private final int mScaledTouchSlop;
    private long mLastTouchDownTime;
    private Context mContext;
    // record down / up position
    private float mLastY;
    private float mLastX;
    private float mDownY;
    private float mDownX;

    private FloatClickListener mMagnetViewListener;
    private FloatMoveListener mOnMoveListener;

    private TouchFloatViewContain mContains;
    private ImageView mImageView;
    private LinearLayout mLinLayoutRecord;
    private TextView mTextView;
    private int mOriginScreenWidth;
    private int mScreenHeight;

    // for move
    private int mFloatViewWidth;
    private int mFloatViewHeight;
    private int mFloatViewMarginHor;
    private int mFloatViewMarginVer;
    private int mFloatViewEdgeHor;
    private int mStatusBarHeight;
    private int mNavigationBarHeight;
    private int mRotation;

    private ValueAnimator mAnimator;
    private TimeInterpolator mDecelerateInterpolator;
    private static final long DURING_ANIMATION_NORMAL = 300;
    private static final boolean SHOW_LEFT = false;
    private boolean mIsRecording = false;
    private RecorderManager mRecorderManager;
    private long mStartTimeMillis;
    private String mPhoneNumber = "";
    private Handler mHandler;

    public void setRecordStatus(boolean isRecording) {
        this.mIsRecording = isRecording;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public void setFloatClickListener(FloatClickListener magnetViewListener) {
        this.mMagnetViewListener = magnetViewListener;
    }

    public void setFloatMoveListener(FloatMoveListener viewMoveListener) {
        this.mOnMoveListener = viewMoveListener;
    }

    public FollowTouchView(final Context context) {
        super(context);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mFloatViewWidth = context.getResources().getDimensionPixelSize(R.dimen.touch_float_view_width);
        mFloatViewHeight = context.getResources().getDimensionPixelSize(R.dimen.touch_float_view_height);

        mOriginScreenWidth = SystemUtils.getScreenWidth(context);
        mScreenHeight = SystemUtils.getScreenHeight(context);

        mFloatViewEdgeHor = SystemUtils.dp2px(context, MARGIN_EDGE_H);

        mFloatViewMarginHor = mFloatViewWidth + mFloatViewEdgeHor;
        mFloatViewMarginVer = SystemUtils.dp2px(context, MARGIN_EDGE_V);

        mNavigationBarHeight = SystemUtils.dp2px(context, NAVIGATION_BAR_HEIGHT);

        mViewMode = WRAP_CONTENT_TOUCHABLE;
        mGravity = Gravity.TOP | Gravity.LEFT | Gravity.CENTER;

        //set float view location and screen width/height
        updateScreenLocationAndSize(context);

        inflate(R.layout.pri_floating_view);

        initView(context);
        mHandler = new Handler(Looper.getMainLooper());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context) {
        this.mContext = context;
        mContains = mInflate.findViewById(R.id.ll_contains);
        mImageView = mInflate.findViewById(R.id.iv_record_icon);
        mLinLayoutRecord = mInflate.findViewById(R.id.ll_record_time);
        mTextView = mInflate.findViewById(R.id.tv_record_time);

        mContains.setOnTouchListener(floatViewOnTouchListener);
        mContains.setOnScreenOrientationChangeListener(new TouchFloatViewContain.OnScreenOrientationChangeListener() {
            @Override
            public void onChange(boolean isLandScape) {
                // stop move runnable
                cancelAnimator();
                updateScreenLocationAndSize(mContext);
                // update new location
                mLayoutParams.x = mAddX;
                mLayoutParams.y = mAddY;
                mWindowManager.updateViewLayout(mInflate, mLayoutParams);
                autoMoveX((int) mLastX, getMoveToEdgeEndX());
            }
        });
    }

    @Override
    protected void onAddWindowFailed(Exception e) {
    }

    @Override
    public synchronized void show() {
        super.show();
        mLayoutParams.width = mFloatViewWidth;
        mLayoutParams.height = mFloatViewHeight;
        mWindowManager.updateViewLayout(mInflate, mLayoutParams);
    }

    public int getTopTouchViewHeight() {
        return mFloatViewHeight;
    }

    private void updateScreenLocationAndSize(Context context) {
        mOriginScreenWidth = SystemUtils.getScreenWidth(context);
        mScreenHeight = SystemUtils.getScreenHeight(context);
        mStatusBarHeight = SystemUtils.getStatusBarHeight(context);

        if (SHOW_LEFT) {
            mAddX = mFloatViewEdgeHor;
        } else {
            mAddX = mOriginScreenWidth - mFloatViewMarginHor;
        }
        mAddY = mScreenHeight / 2;
        mRotation = mWindowManager.getDefaultDisplay().getRotation();

        // top
        if (mAddY <= mStatusBarHeight) {
            mAddY = mStatusBarHeight;
        }
        // bottom position
        if (mRotation != Surface.ROTATION_0) {
            int maxBottomLpY = mScreenHeight - mFloatViewHeight - mFloatViewMarginVer;
            if (mAddY > maxBottomLpY - mFloatViewHeight) {
                mAddY = maxBottomLpY - mFloatViewHeight;
            }
        }

        LogUtils.d(TAG, "default mAddX: " + mAddX + " mAddY: " + mAddY + " mRotation: " + mRotation);
    }

    private final View.OnTouchListener floatViewOnTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getRawX();
            float y = event.getRawY();

            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = mLastX = x;
                    mDownY = mLastY = y;
                    mLastTouchDownTime = SystemClock.elapsedRealtime();
                    LogUtils.d(TAG, "ACTION_DOWN: ");
                    cancelAnimator();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float moveX = x - mLastX;
                    float moveY = y - mLastY;
                    LogUtils.d(TAG, "ACTION_MOVE: " + moveX + " " + moveY);
                    mLastX = x;
                    mLastY = y;
                    if (moveX != 0 || moveY != 0) {
                        updateViewPosition(moveX, moveY);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    mLastX = PriViewAreaUtil.getViewLeftRawX(mInflate);
                    float disX = x - mDownX;
                    float disY = y - mDownY;
                    boolean mClick = Math.abs(disX) <= mScaledTouchSlop && Math.abs(disY) <= mScaledTouchSlop;
                    LogUtils.d(TAG, "ACTION_UP mClick: " + mClick + " mLastX: " + mLastX);
                    if (mClick) {
                        dealClickEvent(mInflate);
                    } else {
                        autoMoveX((int) mLastX, getMoveToEdgeEndX());
                    }
                    break;
            }

            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    public void release() {
        if (mContains != null) {
            mContains.setOnTouchListener(null);
            mContains.setOnScreenOrientationChangeListener(null);
            stopRecording(mContains.getContext(), mIsRecording);
        }
        cancelAnimator();
        remove();
        mContains = null;
    }

    protected void dealClickEvent(View view) {
        if (PriAntiShakeUtils.isInvalidClick(view)) {
            LogUtils.d(TAG, "dealClickEvent isInvalidClick return. ");
            return;
        }
        if (SystemClock.elapsedRealtime() - mLastTouchDownTime < 300) {
            if (mMagnetViewListener != null) {
                mMagnetViewListener.onClick(view);
            }
            LogUtils.d(TAG, "dealClickEvent onClick ");
            updateRecordView(view);
        }
    }

    public void updateRecordView(View view) {
        if (mIsRecording) {
            stopRecording(view.getContext(), true);
        } else {
            if (mRecorderManager == null) {
                mRecorderManager = new RecorderManager();
            }
            mStartTimeMillis = mRecorderManager.startRecording(mPhoneNumber);
            if (mStartTimeMillis != 0) {
                mIsRecording = true;
                mImageView.setVisibility(mIsRecording ? View.GONE : View.VISIBLE);
                mLinLayoutRecord.setVisibility(mIsRecording ? View.VISIBLE : View.GONE);
                updateUI();
            } else {
                stopRecording(view.getContext(), false);
            }
        }
    }

    private void updateUI() {
        long currentTimeMillis = SystemClock.elapsedRealtime();
        long duration = currentTimeMillis - mStartTimeMillis;
        final String timeString = String.format("%02d:%02d", duration / 1000 / 60, duration / 1000 % 60);

        mTextView.setText(timeString);
        if (mIsRecording) {
            mHandler.postDelayed(this::updateUI, 1000);
        }
    }

    private void stopRecording(Context context, boolean showToast) {
        if (mRecorderManager != null) {
            mRecorderManager.stopRecording();
            mRecorderManager = null;
            if (showToast) {
                Toast.makeText(context, context.getString(R.string.phone_recording_prompt), Toast.LENGTH_SHORT).show();
            }
        }
        mIsRecording = false;
        mHandler.removeCallbacksAndMessages(null);
        mImageView.setVisibility(mIsRecording ? View.GONE : View.VISIBLE);
        mLinLayoutRecord.setVisibility(mIsRecording ? View.VISIBLE : View.GONE);
    }

    private void updateViewPosition(float moveX, float moveY) {
        mLayoutParams.x += moveX;
        LogUtils.d(TAG, "updateViewPosition mLayoutParams.x: " + mLayoutParams.x
                + " mLayoutParams.y: " + mLayoutParams.y +
                " moveX: " + moveX
                + " mOriginScreenWidth: " + mOriginScreenWidth);
        // The limit cannot exceed the screen width
        if (mLayoutParams.x <= mFloatViewEdgeHor) {
            mLayoutParams.x = mFloatViewEdgeHor;
        }
        if (mLayoutParams.x > mOriginScreenWidth - mFloatViewMarginHor) {
            mLayoutParams.x = mOriginScreenWidth - mFloatViewMarginHor;
        }
        // position mLayoutParams.y = 0; up: < 0; down: > 0
        mLayoutParams.y += moveY;
        int maxHeight = mScreenHeight - mFloatViewMarginVer - mFloatViewHeight;
        boolean isLandScape = mRotation != Surface.ROTATION_0;
        if (isLandScape) {
            maxHeight = mScreenHeight - mFloatViewMarginVer - mFloatViewHeight - mNavigationBarHeight / 2;
        }
        // The limit cannot exceed the screen height
        if (mLayoutParams.y > maxHeight) {
            mLayoutParams.y = maxHeight;
        }
        if (mLayoutParams.y < mStatusBarHeight) {
            mLayoutParams.y = mStatusBarHeight;
        }

        LogUtils.d(TAG, "onMove mFloatViewHeight " + mFloatViewHeight + " mLayoutParams.y: " + mLayoutParams.y);

        mWindowManager.updateViewLayout(mInflate, mLayoutParams);

        if (mOnMoveListener != null) {
            mOnMoveListener.onMove(mLayoutParams.x, mLayoutParams.y);
        }
    }

    public boolean isNearestLeft() {
        int middle = mOriginScreenWidth / 2;
        int viewRawX = PriViewAreaUtil.getViewCenterX(mInflate);
        return viewRawX < middle;
    }

    private int getMoveToEdgeEndX() {
        return isNearestLeft() ? mFloatViewEdgeHor : mOriginScreenWidth - mFloatViewMarginHor;
    }

    public void autoMoveX(int startX, int endX) {
        Log.d(TAG, "startX: " + startX + " endX: " + endX);
        cancelAnimator();
        mAnimator = ObjectAnimator.ofInt(startX, endX);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int xx = (int) animation.getAnimatedValue();
                autoMoveToX(xx);
            }
        });
        startAnimator();
    }

    private void startAnimator() {
        if (mDecelerateInterpolator == null) {
            mDecelerateInterpolator = new DecelerateInterpolator();
        }
        mAnimator.setInterpolator(mDecelerateInterpolator);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator.removeAllUpdateListeners();
                mAnimator.removeAllListeners();
                mAnimator = null;
            }
        });
        mAnimator.setDuration(DURING_ANIMATION_NORMAL).start();
    }

    private void cancelAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    public void autoMoveToX(int newX) {
        if (mLayoutParams.x != newX) {
            mLayoutParams.x = newX;
            mWindowManager.updateViewLayout(mInflate, mLayoutParams);
        }
    }

}
