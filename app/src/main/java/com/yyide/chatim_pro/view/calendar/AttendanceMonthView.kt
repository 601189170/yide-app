package com.yyide.chatim_pro.view.calendar

import android.content.Context
import android.graphics.Canvas
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.MonthView
import java.lang.String
import kotlin.Boolean
import kotlin.Int


/**
 *
 * @author shenzhibin
 * @date 2022/3/28 20:02
 * @description 考勤通知的月视图
 */
class AttendanceMonthView(context: Context) : MonthView(context) {

    private var mRadius = 0

    override fun onPreviewHook() {
        mRadius = mItemWidth.coerceAtMost(mItemHeight) / 5 * 2
    }

    override fun onDrawSelected(
        canvas: Canvas,
        calendar: Calendar,
        x: Int,
        y: Int,
        hasScheme: Boolean
    ): Boolean {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSelectedPaint)
        return true
    }

    override fun onDrawScheme(canvas: Canvas, calendar: Calendar, x: Int, y: Int) {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2
        //canvas.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSchemePaint)
        canvas.drawCircle(
                    cx.toFloat(), mTextBaseLine + y + mItemHeight / 10,
                    6F, mSchemePaint)
    }

    override fun onDrawText(
        canvas: Canvas,
        calendar: Calendar,
        x: Int,
        y: Int,
        hasScheme: Boolean,
        isSelected: Boolean
    ) {
        val cx = x + mItemWidth / 2
        val top = y - mItemHeight / 8
        when {
            isSelected -> {
                canvas.drawText(
                    String.valueOf(calendar.day), cx.toFloat(), mTextBaseLine + top,
                    mSelectTextPaint
                )
            }
            hasScheme -> {
                canvas.drawText(
                    String.valueOf(calendar.day), cx.toFloat(), mTextBaseLine + top,
                    if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mSchemeTextPaint else mOtherMonthTextPaint
                )
            }
            else -> {
                canvas.drawText(
                    String.valueOf(calendar.day), cx.toFloat(), mTextBaseLine + top,
                    if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mCurMonthTextPaint else mOtherMonthTextPaint
                )
            }
        }
    }
}