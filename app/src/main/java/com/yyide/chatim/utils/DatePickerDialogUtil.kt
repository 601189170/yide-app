package com.yyide.chatim.utils

import android.content.Context
import android.text.TextUtils
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog

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
}