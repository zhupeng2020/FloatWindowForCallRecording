package com.pri.recordview;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: MyApplication
 * @Author: zhupeng
 * @CreateDate: 2024\4\25 10:14
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static ExecutorService mExecutorService;
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        mExecutorService = Executors.newCachedThreadPool();
    }

    public static Context getContext() {
        return sContext;
    }

    public static ExecutorService getsExecutorService() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
        return mExecutorService;
    }


}
