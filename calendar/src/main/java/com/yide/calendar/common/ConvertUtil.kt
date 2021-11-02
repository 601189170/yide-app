package com.yide.calendar.common

import android.content.Context

/**
 * @author liu tao
 * @date 2021/9/8 17:02
 * @description 描述
 */
object ConvertUtil {
    /**
     * dp转px.
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转dp.
     *
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp转px.
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * px转sp.
     *
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
}