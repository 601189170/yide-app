package com.yyide.chatim.fragment.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.yide.calendar.CalendarUtils
import com.yide.calendar.HintCircle
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.month.MonthCalendarView
import com.yide.calendar.schedule.ScheduleLayout
import com.yyide.chatim.R
import com.yyide.chatim.adapter.schedule.ScheduleAdapter
import com.yyide.chatim.adapter.schedule.ScheduleTodayAdapter
import com.yyide.chatim.databinding.FragmentScheduleMonthBinding
import com.yyide.chatim.databinding.FragmentScheduleTodayBinding
import com.yyide.chatim.model.schedule.Label
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleInner
import com.yyide.chatim.model.schedule.ScheduleOuter
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.view.SpaceItemDecoration
import kotlin.math.log

/**
 *
 * @author liu tao
 * @date 2021/9/18 16:149
 * @description 日程/今日
 */
class ScheduleTodayFragment : Fragment() {
    lateinit var fragmentScheduleTodayBinding: FragmentScheduleTodayBinding
    private var labelList = mutableListOf<Label>()

    private val weekUndoList = mutableListOf<Schedule>()
    private val todayList = mutableListOf<Schedule>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScheduleTodayBinding = FragmentScheduleTodayBinding.inflate(layoutInflater)
        return fragmentScheduleTodayBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initView() {
        fragmentScheduleTodayBinding.fab.setOnClickListener {
            DialogUtil.showAddScheduleDialog(context, labelList)
        }

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentScheduleTodayBinding.rvWeekUndoList.layoutManager = linearLayoutManager
        val scheduleAdapter = ScheduleTodayAdapter(weekUndoList)
        fragmentScheduleTodayBinding.rvWeekUndoList.addItemDecoration(SpaceItemDecoration(10))
        fragmentScheduleTodayBinding.rvWeekUndoList.adapter = scheduleAdapter
        scheduleAdapter.setOnItemClickListener { adapter, view, position ->
            loge("本周未完成：$position")
        }

        val linearLayoutManager2 = LinearLayoutManager(context)
        linearLayoutManager2.orientation = LinearLayoutManager.VERTICAL
        fragmentScheduleTodayBinding.rvTodayList.layoutManager = linearLayoutManager2
        val scheduleAdapter2 = ScheduleTodayAdapter(todayList)
        fragmentScheduleTodayBinding.rvTodayList.addItemDecoration(SpaceItemDecoration(10))
        fragmentScheduleTodayBinding.rvTodayList.adapter = scheduleAdapter2
        scheduleAdapter2.setOnItemClickListener { adapter, view, position ->
            loge("今日清单：$position")
        }
    }

    fun initData() {
        labelList.add(Label("工作", "#19ADF8", false))
        labelList.add(Label("阅读", "#56D72C", false))
        labelList.add(Label("睡觉", "#FD8208", false))
        labelList.add(Label("吃饭", "#56D72C", false))
        labelList.add(Label("嗨皮", "#FD8208", false))

        val schedule1 = Schedule(
            "开学提醒", "开学提醒内容", 1,
            "2021-08-11 09:10:00",
            "2021-08-11 14:15:00",
            0
        )
        weekUndoList.add(schedule1)
        val schedule2 = Schedule(
            "开学提醒", "开学提醒内容", 2,
            "2021-08-11 09:10:00",
            "2021-08-11 14:15:00",
            0
        )
        weekUndoList.add(schedule2)
        val schedule3 = Schedule(
            "开学提醒", "开学提醒内容", 3,
            "2021-08-11 09:10:00",
            "2021-08-11 14:15:00",
            0
        )
        weekUndoList.add(schedule3)
        val schedule4 = Schedule(
            "开学提醒", "开学提醒内容", 4,
            "2021-09-18 04:10:00",
            "2021-09-18 22:00:00",
            0
        )
        todayList.add(schedule4)
        val schedule5 = Schedule(
            "开学提醒1", "开学提醒内容", 3,
            "2021-09-18 04:10:00",
            "2021-09-18 22:00:00",
            0
        )
        todayList.add(schedule5)

        val schedule6 = Schedule(
            "开学提醒2", "开学提醒内容", 2,
            "2021-09-18 04:10:00",
            "2021-09-18 22:00:00",
            0
        )
        todayList.add(schedule6)

        val schedule7 = Schedule(
            "开学提醒3", "开学提醒内容", 1,
            "2021-09-18 09:10:00",
            "2021-09-18 23:00:00",
            0
        )
        todayList.add(schedule7)
    }

}