package com.yyide.chatim.fragment.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.schedule.ScheduleLayout
import com.yide.calendar.schedule.ScheduleRecyclerView
import com.yyide.chatim.R
import com.yyide.chatim.adapter.schedule.ScheduleListOuterAdapter
import com.yyide.chatim.databinding.FragmentScheduleListBinding
import com.yyide.chatim.model.schedule.Schedule
import com.yyide.chatim.model.schedule.ScheduleInner
import com.yyide.chatim.model.schedule.ScheduleOuter
import com.yyide.chatim.utils.loge

/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程/列表
 */
class ScheduleListFragment : Fragment(), OnCalendarClickListener {
    lateinit var fragmentScheduleListBinding: FragmentScheduleListBinding
    private var slSchedule: ScheduleLayout? = null
    private var rvScheduleList: ScheduleRecyclerView? = null
    private var rLNoTask: RelativeLayout? = null
    private var list = mutableListOf<ScheduleOuter>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScheduleListBinding = FragmentScheduleListBinding.inflate(layoutInflater)
        return fragmentScheduleListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slSchedule = view.findViewById(R.id.slSchedule)
        rvScheduleList = view.findViewById(R.id.rvScheduleList)
        rLNoTask = view.findViewById(R.id.rlNoTask)
        slSchedule?.setOnCalendarClickListener(this)
        initScheduleList()
        fragmentScheduleListBinding.fab.setOnClickListener {

        }
    }

    private fun initScheduleList() {
        initData()
        rLNoTask?.visibility = View.GONE
        rvScheduleList = slSchedule?.schedulerRecyclerView
        val manager = LinearLayoutManager(activity)
        manager.setOrientation(LinearLayoutManager.VERTICAL)
        rvScheduleList?.setLayoutManager(manager)
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        rvScheduleList?.setItemAnimator(itemAnimator)
        val adapterOuter = ScheduleListOuterAdapter()
        adapterOuter.setList(list)
        rvScheduleList?.adapter = adapterOuter
    }

    override fun onClickDate(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
    }

    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
    }

    private fun initData() {
        val scheduleInnerList = mutableListOf<ScheduleInner>()
        val scheduleList = mutableListOf<Schedule>()
        val schedule1 = Schedule(
            "开学提醒", "开学提醒内容", 1,
            "2021-08-11 09:10:00",
            "2021-08-11 14:15:00",
            0
        )
        scheduleList.add(schedule1)
        val schedule2 = Schedule(
            "开学提醒2", "开学提醒内容", 2,
            "2021-08-11 16:10:00",
            "2021-08-11 20:10:00",
            1
        )
        scheduleList.add(schedule2)
        val scheduleInner1 = ScheduleInner("2021-08-11 00:00:00", "周五", scheduleList)
        scheduleInnerList.add(scheduleInner1)

        val scheduleList2 = mutableListOf<Schedule>()
        val schedule3 = Schedule(
            "开学提醒3", "开学提醒内容", 3,
            "2021-08-12 09:10:00",
            "2021-08-12 14:15:00",
            1
        )
        scheduleList2.add(schedule3)
        val scheduleInner2 = ScheduleInner("2021-08-12 00:00:00", "周五", scheduleList2)
        scheduleInnerList.add(scheduleInner2)

        val scheduleOuter = ScheduleOuter("2021-08-12 00:00:00", scheduleInnerList)
        list.add(scheduleOuter)
        //下一个月的
        val scheduleInnerList1 = mutableListOf<ScheduleInner>()
        val scheduleList3 = mutableListOf<Schedule>()
        val schedule13 = Schedule(
            "开学提醒", "开学提醒内容", 1,
            "2021-09-08 09:10:00",
            "2021-09-08 14:15:00",
            1
        )
        scheduleList3.add(schedule13)
        val scheduleInner13 = ScheduleInner("2021-09-08 00:00:00", "周一", scheduleList3)
        scheduleInnerList1.add(scheduleInner13)

        val scheduleList23 = mutableListOf<Schedule>()
        val schedule33 = Schedule(
            "开学提醒", "开学提醒内容", 4,
            "2021-09-11 09:10:00",
            "2021-09-11 14:15:00",
            0
        )
        scheduleList23.add(schedule33)
        val scheduleInner23 = ScheduleInner("2021-09-11 00:00:00", "周六", scheduleList23)
        scheduleInnerList1.add(scheduleInner23)
        val scheduleOuter2 = ScheduleOuter("2021-09-11 00:00:00", scheduleInnerList1)
        list.add(scheduleOuter2)
        loge("initData: ${JSON.toJSON(list)}")
    }
}