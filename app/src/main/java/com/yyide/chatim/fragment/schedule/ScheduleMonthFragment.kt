package com.yyide.chatim.fragment.schedule

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.JSON
import com.yide.calendar.CalendarUtils
import com.yide.calendar.HintCircle
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.month.MonthCalendarView
import com.yide.calendar.schedule.ScheduleLayout
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentScheduleMonthBinding
import com.yyide.chatim.model.schedule.Label
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleInner
import com.yyide.chatim.model.schedule.ScheduleOuter
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil

/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程/月
 */
class ScheduleMonthFragment : Fragment(), OnCalendarClickListener {
    lateinit var fragmentScheduleMonthBinding: FragmentScheduleMonthBinding
    private var labelList = mutableListOf<Label>()
    private var list = mutableListOf<ScheduleOuter>()
    private var mcvCalendar: MonthCalendarView? = null
    private var mCurrentSelectYear = 2021
    private var mCurrentSelectMonth = 8
    private var mCurrentSelectDay = 12

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScheduleMonthBinding = FragmentScheduleMonthBinding.inflate(layoutInflater)
        return fragmentScheduleMonthBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mcvCalendar = view.findViewById(R.id.mcvCalendar)
        initData()
        initView()
        addTaskHint(HintCircle(5, 3))
        addTaskHints(listOf(HintCircle(9, 2), HintCircle(10, 1), HintCircle(13, 5)))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        fragmentScheduleMonthBinding.fab.setOnClickListener {
            DialogUtil.showAddScheduleDialog(context, this)
        }
        mcvCalendar?.setOnCalendarClickListener(this)
    }

    private fun initData() {
        mcvCalendar?.currentMonthView?.let {
            mCurrentSelectYear = it.selectYear
            mCurrentSelectMonth = it.selectMonth
            mCurrentSelectDay = it.selectDay
        }
        val scheduleInnerList = mutableListOf<ScheduleInner>()
        val scheduleList = mutableListOf<Schedule>()
        val schedule1 = Schedule(
            "开学提醒", "开学提醒内容", 1,
            "2021-09-11 09:10:00",
            "2021-09-11 14:15:00",
            0
        )
        scheduleList.add(schedule1)
        val schedule2 = Schedule(
            "开学提醒2", "开学提醒内容", 2,
            "2021-09-11 16:10:00",
            "2021-09-11 20:10:00",
            1
        )
        scheduleList.add(schedule2)

        scheduleList.add(Schedule(
            "开学提醒2", "开学提醒内容", 2,
            "2021-09-11 16:10:00",
            "2021-09-11 20:10:00",
            1
        ))

        scheduleList.add(Schedule(
            "开学提醒2", "开学提醒内容", 2,
            "2021-09-11 16:10:00",
            "2021-09-11 20:10:00",
            1
        ))
        scheduleList.add(Schedule(
            "开学提醒2", "开学提醒内容", 2,
            "2021-09-11 16:10:00",
            "2021-09-11 20:10:00",
            1
        ))

        val scheduleInner1 = ScheduleInner("2021-09-11 00:00:00", "周五", scheduleList)
        scheduleInnerList.add(scheduleInner1)

        val scheduleList2 = mutableListOf<Schedule>()
        val schedule3 = Schedule(
            "开学提醒3", "开学提醒内容", 3,
            "2021-09-13 09:10:00",
            "2021-09-13 14:15:00",
            1
        )
        scheduleList2.add(schedule3)
        val scheduleInner2 = ScheduleInner("2021-09-13 00:00:00", "周五", scheduleList2)
        scheduleInnerList.add(scheduleInner2)

        val scheduleOuter = ScheduleOuter("2021-09-13 00:00:00", scheduleInnerList)
        list.add(scheduleOuter)

        labelList.add(Label("工作", "#19ADF8", false))
        labelList.add(Label("阅读", "#56D72C", false))
        labelList.add(Label("睡觉", "#FD8208", false))
        labelList.add(Label("吃饭", "#56D72C", false))
        labelList.add(Label("嗨皮", "#FD8208", false))
    }

    fun getDayScheduleList(year: Int, month: Int, day: Int):List<Schedule>{
        list.forEach {
            it.list.forEach {
                if (DateUtils.compareDate("$year-${month+1}-$day 00:00",it.day,true)){
                    return it.list
                }
            }
        }
        return emptyList()
    }

    override fun onClickDate(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
        val date = DateUtils.formatTime("$year-${month+1}-$day","yyyy-MM-dd","",true)
        val dayScheduleList = getDayScheduleList(year, month, day)
        if (dayScheduleList.isEmpty()){
            return
        }
        DialogUtil.showMonthScheduleListDialog(requireContext(),date,dayScheduleList,this)
    }

    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onPageChange year=$year,month=$month,day=$day")
        mCurrentSelectYear = year
        mCurrentSelectMonth = month
        mCurrentSelectDay = day
    }

    /**
     * 添加一个圆点提示
     *
     * @param day
     */
    fun addTaskHint(day: HintCircle) {
        CalendarUtils.getInstance(context).addTaskHint(mCurrentSelectYear, mCurrentSelectMonth, day)
        if (mcvCalendar!!.currentMonthView != null) {
            mcvCalendar!!.currentMonthView.invalidate()
        }
    }

    /**
     * 删除一个圆点提示
     *
     * @param day
     */
    fun removeTaskHint(day: HintCircle) {
        mcvCalendar?.currentMonthView?.removeTaskHint(day)
    }

    /**
     * 添加多个圆点提示
     *
     * @param hints
     */
    fun addTaskHints(hints: List<HintCircle>) {
        CalendarUtils.getInstance(context)
            .addTaskHints(mCurrentSelectYear, mCurrentSelectMonth, hints)
        if (mcvCalendar!!.currentMonthView != null) {
            mcvCalendar!!.currentMonthView.invalidate()
        }
    }
}