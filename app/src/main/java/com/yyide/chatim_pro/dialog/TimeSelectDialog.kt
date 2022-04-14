package com.yyide.chatim_pro.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.loper7.date_time_picker.DateTimeConfig
import com.yyide.chatim_pro.databinding.DialogTimeSelectBinding
import com.yyide.chatim_pro.utils.DateUtils
import com.yyide.chatim_pro.utils.hide
import com.yyide.chatim_pro.utils.logd
import com.yyide.chatim_pro.utils.show

/**
 *
 * @author shenzhibin
 * @date 2022/4/2 9:41
 * @description 时间选择的dialog
 */
class TimeSelectDialog : DialogFragment {

    lateinit var binding: DialogTimeSelectBinding
    var currentMillisecond = 0L

    private var timeCallBack: SelectTimeCallBack? = null

    constructor() : super(){
        currentMillisecond = System.currentTimeMillis()
    }


    constructor(date: String) : this() {
        if (date != "") {
            val timeTamp = DateUtils.parseTimestamp(date, "")
            currentMillisecond = timeTamp
        }
    }

    interface SelectTimeCallBack {
        fun selectTimeStr(dateTamp: Long)
    }

    fun setSelectTimeCallBack(callBack: SelectTimeCallBack?) {
        timeCallBack = callBack
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTimeSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
        val window: Window? = dialog?.window
        window?.let {
            val params: WindowManager.LayoutParams = it.attributes
            val display: Display = it.windowManager.defaultDisplay
            params.width = (display.width * 0.92).toInt()
            //params.height = (display.getHeight() * 0.4) //使用这种方式更改了dialog的框宽
            it.attributes = params
        }
    }

    private fun initView() {

        binding.dpDialogTimeHour.setDisplayType(
            intArrayOf(
                DateTimeConfig.HOUR, // 显示小时
                DateTimeConfig.MIN,//显示分
            )
        )

        binding.calendarView.setSelectSingleMode()

        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = currentMillisecond
        binding.calendarView.scrollToCalendar(
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH) + 1,
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
        binding.dpDialogTimeHour.setDefaultMillisecond(currentMillisecond)
        val timeStr = "${binding.calendarView.curYear}年${binding.calendarView.curMonth}月"
        binding.btnDialogTimeYearMonth.text = timeStr
        val hourStr = "${calendar.get(java.util.Calendar.HOUR_OF_DAY)}:${calendar.get(java.util.Calendar.MINUTE)}"
        binding.btnDialogTimeHour.text = hourStr


    }

    private fun initListener() {


        binding.btnDialogTimeSubmit.setOnClickListener {
            timeCallBack?.selectTimeStr(currentMillisecond)
            dismiss()
        }

        binding.btnDialogTimeCancel.setOnClickListener {
            dismiss()
        }

        binding.btnDialogTimeYearMonth.setOnClickListener {
            binding.blDialogYearMonthLine.show()
            binding.blDialogHourLine.hide()

            binding.calendarView.show()
            binding.clShowContent.hide()
        }

        binding.btnDialogTimeHour.setOnClickListener {
            binding.blDialogYearMonthLine.hide()
            binding.blDialogHourLine.show()

            binding.calendarView.hide()
            binding.clShowContent.show()
        }

        binding.calendarView.setOnMonthChangeListener { year, month ->
            val timeStr = "${year}年${month}月"
            binding.btnDialogTimeYearMonth.text = timeStr
        }

        binding.calendarView.setOnCalendarSelectListener(object :
            CalendarView.OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar?) {
                logd("out of range")
            }

            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                calendar?.let {
                    if (isClick) {
                        currentMillisecond = calendar.timeInMillis
                        val hourStr = DateUtils.stampToDate(currentMillisecond, "HH:mm")
                        binding.btnDialogTimeHour.text = hourStr
                        logd("hourStr = $hourStr")
                        binding.dpDialogTimeHour.setDefaultMillisecond(currentMillisecond)
                        binding.btnDialogTimeHour.callOnClick()
                    }
                }
            }
        })

        binding.dpDialogTimeHour.setOnDateTimeChangedListener {
            currentMillisecond = it
            val hourStr = DateUtils.stampToDate(currentMillisecond, "HH:mm")
            binding.btnDialogTimeHour.text = hourStr
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun dismiss() {
        setSelectTimeCallBack(null)
        super.dismiss()
    }

}