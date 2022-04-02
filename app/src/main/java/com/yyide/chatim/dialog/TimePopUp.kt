package com.yyide.chatim.dialog

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.loper7.date_time_picker.DateTimeConfig
import com.yyide.chatim.R
import com.yyide.chatim.databinding.PopupTableClassBinding
import com.yyide.chatim.databinding.PopupTimeSelectBinding
import com.yyide.chatim.model.attendance.teacher.MonthDayBean
import com.yyide.chatim.model.table.ClassInfo
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

        binding.dpPopupTime.setDisplayType(
            intArrayOf(
                DateTimeConfig.YEAR, // 显示年
                DateTimeConfig.MONTH,//显示月

            )
        )
        /*binding.dpPopupTime.showLabel(true)
        binding.dpPopupTime.setLabelText(year = "年", month = "月")*/


        binding.dpPopupTime.setOnDateTimeChangedListener {
            tempMillisecond = it
        }


        binding.btnPopupTimeSubmit.setOnClickListener {
            dismiss()
        }

        binding.btnPopupTimeCancel.setOnClickListener {
            selectMillisecond = tempMillisecond
            val date = Calendar.getInstance()
            date.timeInMillis = selectMillisecond
            val bean = MonthDayBean()
            bean.year = date.get(Calendar.YEAR).toString()
            bean.month = (date.get(Calendar.MONTH) + 1).toString()
            bean.day = date.get(Calendar.DAY_OF_MONTH).toString()
            mSubmitCallback?.getSubmitData(bean)
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