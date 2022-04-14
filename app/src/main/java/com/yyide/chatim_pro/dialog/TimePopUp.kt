package com.yyide.chatim_pro.dialog

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.loper7.date_time_picker.DateTimeConfig
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.databinding.PopupTimeSelectBinding
import com.yyide.chatim_pro.model.attendance.teacher.MonthDayBean
import razerdp.basepopup.BasePopupWindow
import java.util.*

/**
 *
 * @author shenzhibin
 * @date 2022/3/30 11:18
 * @description 时间选择的popup
 */
class TimePopUp : BasePopupWindow {

    lateinit var binding: PopupTimeSelectBinding

    var selectMillisecond:Long = 0

    var tempMillisecond:Long = 0

    private var mSubmitCallback:SubmitCallBack? = null

    constructor(context: Context) : super(context)

    constructor(fragment: Fragment) : super(fragment)

    init {
        setContentView(R.layout.popup_time_select)
        setBackground(0)
        //setOutSideDismiss(false)
    }

    interface SubmitCallBack {
        fun getSubmitData(data: MonthDayBean)
    }

    fun setSubmitCallBack(callBack: SubmitCallBack?) {
        mSubmitCallback = callBack
    }

    override fun onViewCreated(contentView: View) {
        binding = PopupTimeSelectBinding.bind(contentView)
        initListener()
    }

    private fun initListener() {

        binding.viewPopupBlank.setOnClickListener {
            dismiss()
        }

        binding.dpPopupTime.setDisplayType(
            intArrayOf(
                DateTimeConfig.YEAR, // 显示年
                DateTimeConfig.MONTH,//显示月

            )
        )
        binding.dpPopupTime.showLabel(false)
        //binding.dpPopupTime.setLabelText(year = "年", month = "月")


        binding.dpPopupTime.setOnDateTimeChangedListener {
            tempMillisecond = it
        }


        binding.btnPopupTimeSubmit.setOnClickListener {
            selectMillisecond = tempMillisecond
            val date = Calendar.getInstance()
            date.timeInMillis = selectMillisecond
            val bean = MonthDayBean()
            bean.year = date.get(Calendar.YEAR)
            bean.month = (date.get(Calendar.MONTH) + 1)
            bean.day = 1
            mSubmitCallback?.getSubmitData(bean)
            dismiss()
        }

        binding.btnPopupTimeCancel.setOnClickListener {
            dismiss()
        }

    }

    override fun dismiss() {
        binding.dpPopupTime.setDefaultMillisecond(selectMillisecond)
        super.dismiss()
    }

    fun setMillisecond(millSecond: Long) {
        selectMillisecond = millSecond
        binding.dpPopupTime.setDefaultMillisecond(selectMillisecond)
    }




}