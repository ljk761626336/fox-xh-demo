package com.titan.mobile.arcruntime.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

/**
 * @author zy
 * 测量屏幕尺寸
 */
public final class MeasureScreen {

    MeasureScreen() {

    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕宽度的分辨率
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度的分辨率
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 通过反射获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
            statusBarHeight = 0;
        }
        return statusBarHeight;
    }

    /**
     * 获取app显示高度
     *
     * @return
     */
    public static int getAppDisplayHeight(Context context) {
        return getScreenHeight(context) - getStatusBarHeight(context);
    }


    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean isVertical(@NonNull Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation;
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            return false;
        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            return true;
        }
        return true;
    }


    public static class DisplayUtil {

        public static float px2dip(Context context, float pxValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (pxValue / scale + 0.5f);
        }

        public static float dip2px(Context context, float dipValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (dipValue * scale + 0.5f);
        }

        public static float px2sp(Context context, float pxValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (pxValue / fontScale + 0.5f);
        }

        public static float sp2px(Context context, float spValue) {
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return (spValue * fontScale + 0.5f);
        }

        public static float sp2dip(Context context, float spValue) {
            float sp = sp2px(context, spValue);
            float dip = px2dip(context, sp);
            return dip;
        }

        public static float dip2sp(Context context, float spValue) {
            float px = dip2px(context, spValue);
            float sp = px2sp(context, px);
            return sp;
        }
    }
}
