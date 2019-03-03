package com.riyol.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.DimenRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


public final class DimenUtil {
    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getDisplayMetrics(context));
    }

    public static int roundedDpToPx(Context context, float dp) {
        return Math.round(dpToPx(context, dp));
    }

    public static float pxToDp(Context context, float px) {
        return px / getDensityScalar(context);
    }

    public static int roundedPxToDp(Context context, float px) {
        return Math.round(pxToDp(context, px));
    }

    public static float getDensityScalar(Context context) {
        return getDisplayMetrics(context).density;
    }

    public static float getFloat(Context context, @DimenRes int id) {
        return getValue(context, id).getFloat();
    }

    /**
     * @return Dimension in dp.
     */
    public static float getDimension(Context context, @DimenRes int id) {
        return TypedValue.complexToFloat(getValue(context, id).data);
    }

    /**
     * Calculates the actual font size for the current device, based on an "sp" measurement.
     *
     * @param window The window on which the font will be rendered.
     * @param fontSp Measurement in "sp" units of the font.
     * @return Actual font size for the given sp amount.
     */
    public static float getFontSizeFromSp(Window window, float fontSp) {
        final DisplayMetrics metrics = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return fontSp / metrics.scaledDensity;
    }


    public static int getDisplayWidthPx(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getDisplayHeightPx(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static int getContentTopOffsetPx(Context context) {
        return roundedDpToPx(context, getContentTopOffset(context));
    }

    public static float getContentTopOffset(Context context) {
        return getToolbarHeight(context);
    }

    private static TypedValue getValue(Context context, @DimenRes int id) {
        TypedValue typedValue = new TypedValue();
        getResources(context).getValue(id, typedValue, true);
        return typedValue;
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        return getResources(context).getDisplayMetrics();
    }

    private static Resources getResources(Context context) {
        return context.getResources();
    }


    private static float getStatusBarHeight(Context context) {
        int id = getStatusBarId(context);
        return id > 0 ? DimenUtil.getDimension(context, id) : 0;
    }

    private static float getToolbarHeight(Context context) {
        return DimenUtil.roundedPxToDp(context, getToolbarHeightPx(context));
    }

    /**
     * Returns the height of the toolbar in the current activity. The system controls the height of
     * the toolbar, which may be slightly different depending on screen orientation, and device
     * version.
     *
     * @param context Context used for retrieving the height attribute.
     * @return Height of the toolbar.
     */
    public static int getToolbarHeightPx(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{
                android.support.v7.appcompat.R.attr.actionBarSize
        });
        int size = styledAttributes.getDimensionPixelSize(0, 0);
        styledAttributes.recycle();
        return size;
    }

    @DimenRes
    private static int getStatusBarId(Context context) {
        return context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    }

    public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
    }

    private DimenUtil() {
    }
}
