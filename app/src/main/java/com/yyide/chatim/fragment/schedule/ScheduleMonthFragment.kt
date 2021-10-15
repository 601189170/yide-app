package com.yyide.chatim.fragment.schedule

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
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
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.viewmodel.ScheduleMonthViewModel
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程/月
 */
class ScheduleMonthFragment : Fragment(), OnCalendarClickListener {
    lateinit var fragmentScheduleMonthBinding: FragmentScheduleMonthBinding
    private var list = mutableListOf<ScheduleOuter>()
    private var mcvCalendar: MonthCalendarView? = null
    private var mCurrentSelectYear = 2021
    private var mCurrentSelectMonth = 8
    private var mCurrentSelectDay = 12
    private val scheduleMonthViewModel:ScheduleMonthViewModel by viewModels()
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
        loge("onViewCreated")
        mcvCalendar = view.findViewById(R.id.mcvCalendar)
        initData()
        initView()
        scheduleMonthViewModel.monthDataList.observe(requireActivity(),{
            loge("keys ${it.keys.size}")
            it.keys.forEach {dateTime->
                val value = it[dateTime]
                if (value != null){
                    addTaskHint(HintCircle(dateTime.dayOfMonth,value.size))
                }
            }
        })
        scheduleMonthViewModel.scheduleList(DateTime.now())
        //addTaskHint(HintCircle(5, 3))
        //addTaskHints(listOf(HintCircle(9, 2), HintCircle(10, 1), HintCircle(13, 5)))
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClickDate(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
//        val date = DateUtils.formatTime("$year-${month+1}-$day","yyyy-MM-dd","",true)
//        val dayScheduleList = getDayScheduleList(year, month, day)
//        if (dayScheduleList.isEmpty()){
//            return
//        }
        val dateTime = DateTime(year,month+1,day,0,0,0).simplifiedDataTime()
        val value = scheduleMonthViewModel.monthDataList.value
        if (value!=null && value[dateTime] != null){
            val mutableList = value[dateTime]
            val showDataTime = dateTime.toString("yyyy-MM-dd")
            DialogUtil.showMonthScheduleListDialog(requireContext(),showDataTime,mutableList,this)
        }

    }

    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onPageChange year=$year,month=$month,day=$day")
        mCurrentSelectYear = year
        mCurrentSelectMonth = month
        mCurrentSelectDay = day
        val dateTime = DateTime(year,month+1,day,0,0,0).simplifiedDataTime()
        loge("dateTime=$dateTime")
        scheduleMonthViewModel.scheduleList(dateTime)
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