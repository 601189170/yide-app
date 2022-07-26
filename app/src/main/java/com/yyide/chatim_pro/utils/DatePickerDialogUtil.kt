package com.yyide.chatim_pro.utils

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.database.ScheduleDaoUtil.toDateTime
import com.yyide.chatim_pro.databinding.DialogShowDateYearAndMonth2Binding
import com.yyide.chatim_pro.widget.WheelView
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
        var minDate: Long = 0
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
     * 时间选择控件
     * @param context Context
     * @param title String 标题
     * @param currentMillseconds String? 所选时间毫秒数
     * @param onDateSetListener OnDateSetListener 监听器
     * @param isAllDay Boolean 是否显示时分
     */
    fun showDateTime(
        context: Context,
        title: String,
        currentMillseconds: String?,
        onDateSetListener: OnDateSetListener,
        isAllDay: Boolean = false
    ) {
        val displayList: MutableList<Int> = mutableListOf()
        displayList.add(DateTimeConfig.YEAR)
        displayList.add(DateTimeConfig.MONTH)
        displayList.add(DateTimeConfig.DAY)
        if (!isAllDay) {
            logd("not is AllDay")
            displayList.add(DateTimeConfig.HOUR)
            displayList.add(DateTimeConfig.MIN)
        }
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
        minDate: Long = 0,
        maxDate: Long = 0,
        onDateSetListener: OnDateSetListener
    ) {
        val displayList: MutableList<Int> = mutableListOf()
        displayList.add(DateTimeConfig.YEAR)
        displayList.add(DateTimeConfig.MONTH)
        displayList.add(DateTimeConfig.DAY)
//        displayList.add(DateTimeConfig.HOUR)
//        displayList.add(DateTimeConfig.MIN)
        val model = CardDatePickerDialog.STACK
//        val maxDate: Long = 0
//        val minDate: Long = 0
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
        val defaultDate: Long = 0
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
        curDateTime: DateTime,
        startDate: DateTime,
        endDate: DateTime,
        onDateSetListener: (datetime: DateTime) -> Unit
    ) {
        val binding = DialogShowDateYearAndMonth2Binding.inflate(LayoutInflater.from(context))
        val rootView = binding.root
        val mDialog = Dialog(context, R.style.dialog)
        mDialog.setContentView(rootView)
        var yearIndex = 0
        var monthIndex = 0
        val yearList = CalendarYear.getYearList(startDate, endDate)
        if (curDateTime in startDate..endDate) {
            //日期回显
            val year = curDateTime.year
            val monthOfYear = curDateTime.monthOfYear
            for (index in yearList.indices) {
                if (yearList[index].year.toInt() == year) {
                    yearIndex = index
                    val months = yearList[index].months
                    for (index1 in months.indices) {
                        if (months[index1].month.toInt() == monthOfYear) {
                            monthIndex = index1
                            break
                        }
                    }
                    break
                }
            }
        }
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
            val now = DateTime.now()
            val year = now.year
            val monthOfYear = now.monthOfYear
            var dayOfMonth = now.dayOfMonth
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
            if (listOf("4", "6", "9", "11").contains(calendarMonth.month) && dayOfMonth == 31) {
                dayOfMonth = 30
            }
            //不是当前年月，默认显示当前月的第一天
            if (calendarYear.year.toInt() != year || calendarMonth.month.toInt() != monthOfYear) {
                dayOfMonth = 1
            }
            var toDateTime =
                toDateTime("${calendarYear.year}-${calendarMonth.month}-$dayOfMonth", "yyyy-MM-dd")
            if (toDateTime <= startDate) {
                dayOfMonth = startDate.dayOfMonth
                toDateTime =
                    toDateTime(
                        "${calendarYear.year}-${calendarMonth.month}-$dayOfMonth",
                        "yyyy-MM-dd"
                    )
            }
            loge("${calendarYear.year}-${calendarMonth.month}-$dayOfMonth")
            onDateSetListener(toDateTime)
        }
        val dialogWindow = mDialog.window
        dialogWindow!!.setGravity(Gravity.TOP)
        dialogWindow.setWindowAnimations(R.style.popwin_anim_style_top)
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
                    if (years == 0) {
                        //startDate和endDate是同一年，则月的结束是endDate的月
                        for (month in plusYears.monthOfYear..endDate.monthOfYear) {
                            val calendarMonth = CalendarMonth("$month")
                            monthList.add(calendarMonth)
                        }
                    } else {
                        for (month in plusYears.monthOfYear..12) {
                            val calendarMonth = CalendarMonth("$month")
                            monthList.add(calendarMonth)
                        }
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