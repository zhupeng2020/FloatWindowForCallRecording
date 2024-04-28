package com.pri.recordview.util;

import android.content.Context;
import android.content.SharedPreferences;

import static com.pri.recordview.feature.PriRecordSet.DEFAULT_ALPHA_PERCENTAGE;
import static com.pri.recordview.feature.PriRecordSet.DEFAULT_BOTTOM_Y_PERCENTAGE;
import static com.pri.recordview.feature.PriRecordSet.DEFAULT_TOP_Y_PERCENTAGE;

/**
 * @Description: PriSharedPreferenceUtil
 * @Author: zhupeng
 * @CreateDate: 2021\9\27 0009 14:37
 */

public class PriSharedPreferenceUtil {

    //set for save values start
    public static final String KEY_TOP_Y_PERCENTAGE = "sm_top_y";
    public static final String KEY_BOTTOM_Y_PERCENTAGE = "sm_bottom_y";
    public static final String KEY_ALPHA_PERCENTAGE = "sm_alpha";

    public static float getTopYPercentage(Context context) {
        return getFloatValue(context, KEY_TOP_Y_PERCENTAGE, DEFAULT_TOP_Y_PERCENTAGE);
    }

    public static float getBottomYPercentage(Context context) {
        return getFloatValue(context, KEY_BOTTOM_Y_PERCENTAGE, DEFAULT_BOTTOM_Y_PERCENTAGE);
    }

    public static float getAlphaPercentage(Context context) {
        return getFloatValue(context, KEY_ALPHA_PERCENTAGE, DEFAULT_ALPHA_PERCENTAGE);
    }

    public static void putTopYPercentage(Context context, float value) {
        putFloatValue(context, KEY_TOP_Y_PERCENTAGE, value);
    }

    public static void putBottomYPercentage(Context context, float value) {
        putFloatValue(context, KEY_BOTTOM_Y_PERCENTAGE, value);
    }

    public static void putAlphaPercentage(Context context, float value) {
        putFloatValue(context, KEY_ALPHA_PERCENTAGE, value);
    }
    //set for save values end

    public static void putIntValue(Context context, String key, int value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
        sp.putInt(key, value);
        sp.apply();
    }

    public static void putLongValue(Context context, String key, long value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
        sp.putLong(key, value);
        sp.apply();
    }

    public static void putFloatValue(Context context, String key, float value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
        sp.putFloat(key, value);
        sp.apply();
    }

    public static void putStringValue(Context context, String key, String value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
        sp.putString(key, value);
        sp.apply();
    }

    public static void putBooleanValue(Context context, String key, boolean value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
        sp.putBoolean(key, value);
        sp.apply();
    }

    public static int getIntValue(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static long getLongValue(Context context, String key, long defValue) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    public static float getFloatValue(Context context, String key, float defValue) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    public static String getStringValue(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static boolean getBooleanValue(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }
}
