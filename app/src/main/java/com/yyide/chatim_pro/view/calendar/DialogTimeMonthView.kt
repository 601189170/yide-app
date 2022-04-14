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
 * @date 2022/4/2 9:53
 * @description dialog时间选择的日历
 */
class DialogTimeMonthView(context: Context) : MonthView(context) {


    private var mRadius = 0

    override fun onPreviewHook() {
        mRadius = mItemWidth.coerceAtMost(mItemHeight) / 5 * 2
    }

    override fun onDrawSelected(
        canvas: Canvas?,
        calendar: Calendar?,
        x: Int,
        y: Int,
        hasScheme: Boolean
    ): Boolean {
        val cx = x + mItemWidth / 2
        val cy = y + mItemHeight / 2
        canvas!!.drawCircle(cx.toFloat(), cy.toFloat(), mRadius.toFloat(), mSelectedPaint)
        return false
    }

    override fun onDrawScheme(canvas: Canvas?, calendar: Calendar?, x: Int, y: Int) {

    }

    override fun onDrawText(
        canvas: Canvas?,
        calendar: Calendar?,
        x: Int,
        y: Int,
        hasScheme: Boolean,
        isSelected: Boolean
    ) {
        val baselineY = mTextBaseLine + y
        val cx = x + mItemWidth / 2

        when {
            isSelected -> {
                canvas!!.drawText(
                    String.valueOf(calendar!!.day),
                    cx.toFloat(),
                    baselineY,
                    mSelectTextPaint
                )
            }
            hasScheme -> {
                canvas!!.drawText(
                    String.valueOf(calendar!!.day),
                    cx.toFloat(),
                    baselineY,
                    if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mSchemeTextPaint else mOtherMonthTextPaint
                )
            }
            else -> {
                canvas!!.drawText(
                    String.valueOf(calendar!!.day), cx.toFloat(), baselineY,
                    if (calendar.isCurrentDay) mCurDayTextPaint else if (calendar.isCurrentMonth) mCurMonthTextPaint else mOtherMonthTextPaint
                )
            }
        }
    }
}