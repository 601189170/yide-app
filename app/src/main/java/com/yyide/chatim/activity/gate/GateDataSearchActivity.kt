package com.yyide.chatim.activity.gate

import android.os.Bundle
import com.yide.calendar.OnCalendarClickListener
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityGateDataSearchBinding
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/12/24 9:18
 * @description 闸机搜索界面
 */
class GateDataSearchActivity : BaseActivity(), OnCalendarClickListener {
    private lateinit var activityGateDataSearchBinding: ActivityGateDataSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityGateDataSearchBinding = ActivityGateDataSearchBinding.inflate(layoutInflater)
        setContentView(activityGateDataSearchBinding.root)
        initView()
    }

    private fun initView() {
        activityGateDataSearchBinding.cancel.setOnClickListener {
            finish()
        }

        val calendarComposeLayout = activityGateDataSearchBinding.layoutCalendar.calendarComposeLayout
        calendarComposeLayout.setOnCalendarClickListener(this)
        val startMonths = DateTime.now().simplifiedDataTime().minusMonths(3)
        val endMonths = DateTime.now().simplifiedDataTime().plusMonths(3)
        calendarComposeLayout.setCalendarInterval(startMonths,endMonths)
    }

    override fun onClickDate(year: Int, month: Int, day: Int) {
        loge("onClickDate: $year/${month+1}/$day")
    }

    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onPageChange: $year/${month+1}/$day")
    }
}