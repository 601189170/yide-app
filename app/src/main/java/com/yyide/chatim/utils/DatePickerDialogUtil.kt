package com.yyide.chatim.utils

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.yyide.chatim.R
import com.yyide.chatim.database.ScheduleDaoUtil.toDateTime
import com.yyide.chatim.databinding.DialogShowDateYearAndMonthBinding
import com.yyide.chatim.widget.WheelView
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/8/30 9:56
 * @description 描述
 */
object DatePickerDialogUtil {
    fun showDateTime(
        context: Context,
        title: String,
        currentMillseconds: String?,
        onDateSetListener: OnDateSetListener
    ) {
        val displayList: MutableList<Int> = mutableListOf()
        displayList.add(DateTimeConfig.YEAR)
        displayList.add(DateTimeConfig.MONTH)
        displayList.add(DateTimeConfig.DAY)
        displayList.add(DateTimeConfig.HOUR)
        displayList.add(DateTimeConfig.MIN)
        val model = CardDatePickerDialog.STACK
        val maxDate: Long = 0
        val minDate: Long = 0
        val pickerLayout = 0
        var defaultDate: Long = 0
        if (!TextUtils.isEmpty(currentMillseconds)) {
            defaultDate = DateUtils.parseTimestamp(currentMillseconds, "")
        }

        CardDatePickerDialog.builder(context)
            .setTitle(title)
            .setDisplayType(displayList)
            .setBackGroundModel(model)
            .setMaxTime(maxDate)
            .setPickerLayout(pickerLayout)
            .setMinTime(minDate)
            .setDefaultTime(defaultDate)
            .setWrapSelectorWheel(false)
            .showBackNow(false)
            .showDateLabel(true)
            .showFocusDateInfo(true)
            .setOnChoose("确定") {
                onDateSetListener.onDateSet(null, it)
            }
            .setOnCancel("取消") {
            }.build().show()
    }

    fun showDate(
        context: Context,
        title: String,
        currentMillseconds: String?,
        onDateSetListener: OnDateSetListener
    ) {
        val displayList: MutableList<Int> = mutableListOf()
        displayList.add(DateTimeConfig.YEAR)
        displayList.add(DateTimeConfig.MONTH)
        displayList.add(DateTimeConfig.DAY)
//        displayList.add(DateTimeConfig.HOUR)
//        displayList.add(DateTimeConfig.MIN)
        val model = CardDatePickerDialog.STACK
        val maxDate: Long = 0
        val minDate: Long = 0
        val pickerLayout = 0
        var defaultDate: Long = 0
        if (!TextUtils.isEmpty(currentMillseconds)) {
            defaultDate = DateUtils.parseTimestamp(currentMillseconds, "")
        }

        CardDatePickerDialog.builder(context)
            .setTitle(title)
            .setDisplayType(displayList)
            .setBackGroundModel(model)
            .setMaxTime(maxDate)
            .setPickerLayout(pickerLayout)
            .setMinTime(minDate)
            .setDefaultTime(defaultDate)
            .setWrapSelectorWheel(false)
            .showBackNow(false)
            .showDateLabel(true)
            .showFocusDateInfo(true)
            .setOnChoose("确定") {
                onDateSetListener.onDateSet(null, it)
            }
            .setOnCancel("取消") {
            }.build().show()
    }

    /**
     * @param minDate 最小时间
     * @param maxDate 最大时间
     * @param displayList 显示的日期格式
     */
    fun showDate(
        context: Context,
        title: String,
        currentMillseconds: String?,
        minDate: Long,
        maxDate: Long,
        displayList: MutableList<Int>,
        onDateSetListener: OnDateSetListener
    ) {
        val model = CardDatePickerDialog.STACK
        val pickerLayout = 0
        var defaultDate: Long = 0
        if (!TextUtils.isEmpty(currentMillseconds)) {
            //defaultDate = DateUtils.parseTimestamp(currentMillseconds, "yyyy-MM-dd")
        }

        CardDatePickerDialog.builder(context)
            .setTitle(title)
            .setDisplayType(displayList)
            .setBackGroundModel(model)
            .setMaxTime(maxDate)
            .setPickerLayout(pickerLayout)
            .setMinTime(minDate)
            .setDefaultTime(defaultDate)
            .setWrapSelectorWheel(false)
            .showBackNow(false)
            .showDateLabel(true)
            .showFocusDateInfo(true)
            .setOnChoose("确定") {
                onDateSetListener.onDateSet(null, it)
            }
            .setOnCancel("取消") {
            }.build().show()
    }

    /**
     * 显示指定开始日期和结束日期的日期选择框
     * @param startDate 最小日期
     * @param endDate 最大日期
     * @param onDateSetListener 选中的日期回调
     */
    fun showDateYearAndMonth(
        context: Context,
        title: String = "选择日期",
        startDate: DateTime,
        endDate: DateTime,
        onDateSetListener: (datetime: DateTime) -> Unit
    ) {
        val binding = DialogShowDateYearAndMonthBinding.inflate(LayoutInflater.from(context))
        val rootView = binding.root
        val mDialog = Dialog(context, R.style.dialog)
        mDialog.setContentView(rootView)
        var yearIndex = 0
        var monthIndex = 0
        val yearList = CalendarYear.getYearList(startDate, endDate)

        binding.yearWv.setData(yearList.map { "${it.year} 年" })
        binding.yearWv.setDefault(yearIndex)
        binding.monthWv.setData(yearList[yearIndex].months.map { "${it.month} 月" })
        binding.monthWv.setDefault(monthIndex)
        binding.yearWv.setOnSelectListener(object : WheelView.OnSelectListener {
            override fun endSelect(id: Int, text: String?) {
                loge("id=$id,选中${text}")
                if (id != yearIndex) {
                    yearIndex = id
                    binding.monthWv.setData(yearList[yearIndex].months.map { "${it.month} 月" })
                    monthIndex = 0
                    binding.monthWv.setDefault(monthIndex)
                }
            }

            override fun selecting(id: Int, text: String?) {
                loge("selecting id=$id,text=$text")
            }
        })
        binding.monthWv.setOnSelectListener(object : WheelView.OnSelectListener {
            override fun endSelect(id: Int, text: String?) {
                monthIndex = id
            }

            override fun selecting(id: Int, text: String?) {
            }

        })

        binding.tvCancel.setOnClickListener {
            mDialog.dismiss()
        }
        binding.tvEnsure.setOnClickListener {
            mDialog.dismiss()
            val calendarYear = yearList[yearIndex]
            val calendarMonth = calendarYear.months[monthIndex]
            var dayOfMonth = DateTime.now().dayOfMonth
            if (calendarMonth.month == "2" && dayOfMonth >= 28) {
                val y = calendarYear.year.toInt()
                if (y % 4 == 0 && y % 100 != 0 || y % 400 == 0) {
                    if (dayOfMonth > 29) {
                        dayOfMonth = 29
                    }
                } else {
                    dayOfMonth = 28
                }
            }
            if (listOf("4","6","9","11").contains(calendarMonth.month) && dayOfMonth == 31){
                dayOfMonth = 30
            }
            loge("${calendarYear.year}-${calendarMonth.month}-$dayOfMonth")
            onDateSetListener(toDateTime("${calendarYear.year}-${calendarMonth.month}-$dayOfMonth","yyyy-MM-dd"))
        }
        val dialogWindow = mDialog.window
        dialogWindow!!.setGravity(Gravity.BOTTOM)
        dialogWindow.setWindowAnimations(R.style.popwin_anim_style2)
        val lp = dialogWindow.attributes
        lp.width = context.resources.displayMetrics.widthPixels
        lp.height = DisplayUtils.dip2px(context, 380f)
        rootView.measure(0, 0)
        lp.dimAmount = 0.75f
        dialogWindow.attributes = lp
        mDialog.setCancelable(true)
        mDialog.show()
    }
}

data class CalendarYear(
    var year: String,
    var months: List<CalendarMonth>
) {
    data class CalendarMonth(
        var month: String
    )

    companion object {
        fun getYearList(startDate: DateTime, endDate: DateTime): List<CalendarYear> {
            val yearList = mutableListOf<CalendarYear>()
            val years = endDate.year - startDate.year
            for (year in 0..years) {
                val plusYears = startDate.plusYears(year)
                if (year == 0) {
                    //第一年
                    val monthList = mutableListOf<CalendarMonth>()
                    for (month in plusYears.monthOfYear..12) {
                        val calendarMonth = CalendarMonth("$month")
                        monthList.add(calendarMonth)
                    }
                    val calendarYear = CalendarYear("${plusYears.year}", monthList)
                    yearList.add(calendarYear)
                } else if (year == years) {
                    //最后一年
                    val monthList = mutableListOf<CalendarMonth>()
                    for (month in 1..endDate.monthOfYear) {
                        val calendarMonth = CalendarMonth("$month")
                        monthList.add(calendarMonth)
                    }
                    val calendarYear = CalendarYear("${plusYears.year}", monthList)
                    yearList.add(calendarYear)
                } else {
                    //中间的年
                    val monthList = mutableListOf<CalendarMonth>()
                    for (month in 1..12) {
                        val calendarMonth = CalendarMonth("$month")
                        monthList.add(calendarMonth)
                    }
                    val calendarYear = CalendarYear("${plusYears.year}", monthList)
                    yearList.add(calendarYear)
                }
            }
            loge("yearList=${yearList}")
            return yearList
        }
    }
}