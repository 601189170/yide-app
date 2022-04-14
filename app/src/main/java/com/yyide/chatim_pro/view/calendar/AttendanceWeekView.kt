package com.yyide.chatim_pro.view.calendar

import android.content.Context
import android.graphics.Canvas
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.WeekView
import java.lang.String
import kotlin.Boolean
import kotlin.Int


/**
 *
 * @author shenzhibin
 * @date 2022/3/28 20:07
 * @description 描述
 */
class AttendanceWeekView(context: Context) : WeekView(context) {

    private var mRadius = 0


    override fun onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2
    }

    override fun onDrawSelected(
        canvas: Canvas?,
        calendar: Calendar?,
        x: Int,
        hasScheme: Boolean
    ): Boolean {
        val cx = x + mItemWidth / 2
        val cy = mItemHeight / 2
        canvas!!.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSelectedPaint)
        return true
    }


    override fun onDrawScheme(canvas: Canvas, calendar: Calendar, x: Int) {
        val cx = x + mItemWidth / 2
        val cy = mItemHeight / 2
        canvas.drawCircle(
            cx.toFloat(), mTextBaseLine + y + mItemHeight / 10,
            6F, mSchemePaint)
    }

    override fun onDrawText(
        canvas: Canvas,
        calendar: Calendar,
        x: Int,
        hasScheme: Boolean,
        isSelected: Boolean
    ) {
        val cx = x + mItemWidth / 2
        val top = -mItemHeight / 8
        when {
            isSelected -> {
                canvas.drawText(
                    String.valueOf(calendar.day), cx.toFloat(), mTextBaseLine + top,
                    mSelectTextPaint
                )
                /*canvas.drawText(
                    calendar.lunar, cx.toFloat(), mTextBaseLine + mItemHeight / 10,
                    if (calendar.isCurrentDay) mCurDayLunarTextPaint else mSelectedLunarTextPaint
                )*/
            }
            hasScheme -> {
                canvas.drawText(
                    String.valueOf(calendar.day), cx.toFloat(), mTextBaseLine + top,
                    if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mSchemeTextPaint else mSchemeTextPaint
                )
                /*canvas.drawText(
                    calendar.lunar,
                    cx.toFloat(), mTextBaseLine + mItemHeight / 10, mSchemeLunarTextPaint
                )*/
            }
            else -> {
                canvas.drawText(
                    String.valueOf(calendar.day), cx.toFloat(), mTextBaseLine + top,
                    if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mCurMonthTextPaint else mCurMonthTextPaint
                )
                /*canvas.drawText(
                    calendar.lunar, cx.toFloat(), mTextBaseLine + mItemHeight / 10,
                    if (calendar.isCurrentDay) mCurDayLunarTextPaint else mCurMonthLunarTextPaint
                )*/
            }
        }
    }


}