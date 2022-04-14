package com.yyide.chatim_pro.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.yyide.chatim_pro.databinding.DialogYearMothSelectBinding
import com.yyide.chatim_pro.utils.DateUtils
import com.yyide.chatim_pro.utils.logd

/**
 *
 * @author shenzhibin
 * @date 2022/4/4 9:18
 * @description 全天选择时间的弹框
 */
class YearMonthSelectDialog : DialogFragment {

    lateinit var binding: DialogYearMothSelectBinding
    var currentMillisecond = 0L

    private var monthCallBack: SelectTimeCallBack? = null


    constructor() : super()


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
        monthCallBack = callBack
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogYearMothSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    fun initView(){
        binding.calendarView.setSelectSingleMode()

        val calendar = java.util.Calendar.getInstance()
        if (currentMillisecond != 0L) {
            calendar.timeInMillis = currentMillisecond
        }
        binding.calendarView.scrollToCalendar(
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH) + 1,
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
        val timeStr = "${binding.calendarView.curYear}年${binding.calendarView.curMonth}月"
        binding.tvYearMonthSelectContent.text = timeStr
    }


    fun initListener(){
        binding.btnDialogMonthSubmit.setOnClickListener {
            monthCallBack?.selectTimeStr(currentMillisecond)
            dismiss()
        }

        binding.btnDialogMonthCancel.setOnClickListener {
            dismiss()
        }

        binding.ivNextMonth.setOnClickListener {
            binding.calendarView.scrollToNext()
        }

        binding.ivPreMonth.setOnClickListener {
            binding.calendarView.scrollToPre()
        }

        binding.calendarView.setOnMonthChangeListener { year, month ->
            val timeStr = "${year}年${month}月"
            binding.tvYearMonthSelectContent.text = timeStr
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
                    }
                }
            }
        })

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

    override fun dismiss() {
        setSelectTimeCallBack(null)
        super.dismiss()
    }

}