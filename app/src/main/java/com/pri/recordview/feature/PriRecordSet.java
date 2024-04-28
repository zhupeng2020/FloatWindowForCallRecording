package com.pri.recordview.feature;

import android.content.Context;
import android.provider.Settings;

import com.pri.recordview.util.SystemProperties;

public class PriRecordSet {

    private static final String TAG = "PriRecordSet";

    public static final String PRI_RECORD_CALL_KEY = "pri_recording_at";

    // screen mask support ?
    public static final boolean PRI_RECORD_CALL = getValue("ro.odm.recording_at");

    public static final float DEFAULT_TOP_Y_PERCENTAGE = 0.3f;
    public static final float DEFAULT_BOTTOM_Y_PERCENTAGE = 0.8f;

    public static final float DEFAULT_ALPHA_PERCENTAGE = 0.8f;

    // top and bottom, left and right margin
    public static final int MARGIN_EDGE_H = 6;
    public static final int MARGIN_EDGE_V = 6;

    public static final int NAVIGATION_BAR_HEIGHT = 52;

    /**
     * get value from system properties
     *
     * @param value
     * @return
     */
    private static boolean getValue(String value) {
        return SystemProperties.get(value, "0").equals("1");
    }


    /**
     * @param key
     * @param c
     * @return 0 close  1 open
     */
    public static int getKey(Context c, String key) {
        int result = 0;
        try {
            //result = Settings.System.getIntForUser(c.getContentResolver(), key, 0, UserHandle.USER_CURRENT);
            result = Settings.System.getInt(c.getContentResolver(), key, 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //LogUtils.d(TAG, "getKey key: " + key + " result: " + result);
        return result;
    }

    /**
     * @param context
     * @return
     */
    public static boolean isCallRecordEnable(Context context) {
        return getKey(context, PRI_RECORD_CALL_KEY) == 1;
    }

    public static void putCallRecordValue(Context context, int value) {
        Settings.System.putInt(context.getContentResolver(), PRI_RECORD_CALL_KEY, value);
    }

    /**
     * in this top packages, clicked follow touch image will not show screen mask floatView
     */
    public static final String[] NOT_SHOW_PACKAGES = {
            "com.google.android.setupwizard",
            "com.pri.factorytest.TouchPanelEdge",
            "com.pri.toolbox.protractor"
    };

}
