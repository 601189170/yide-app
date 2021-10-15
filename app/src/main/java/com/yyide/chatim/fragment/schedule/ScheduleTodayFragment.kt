package com.yyide.chatim.fragment.schedule

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.yide.calendar.CalendarUtils
import com.yide.calendar.HintCircle
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.month.MonthCalendarView
import com.yide.calendar.schedule.ScheduleLayout
import com.yyide.chatim.R
import com.yyide.chatim.activity.schedule.ScheduleEditActivity
import com.yyide.chatim.adapter.schedule.ScheduleAdapter
import com.yyide.chatim.adapter.schedule.ScheduleTodayAdapter
import com.yyide.chatim.databinding.FragmentScheduleMonthBinding
import com.yyide.chatim.databinding.FragmentScheduleTodayBinding
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.viewmodel.TodayScheduleViewModel
import kotlin.math.log

/**
 *
 * @author liu tao
 * @date 2021/9/18 16:149
 * @description 日程/今日
 */
class ScheduleTodayFragment : Fragment() {
    lateinit var fragmentScheduleTodayBinding: FragmentScheduleTodayBinding

    private val todayScheduleViewModel: TodayScheduleViewModel by viewModels()
    private val weekUndoList = mutableListOf<ScheduleData>()
    private val todayList = mutableListOf<ScheduleData>()
    private lateinit var thisWeekScheduleTodayAdapter: ScheduleTodayAdapter
    private lateinit var todayScheduleTodayAdapter: ScheduleTodayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScheduleTodayBinding = FragmentScheduleTodayBinding.inflate(layoutInflater)
        return fragmentScheduleTodayBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        fragmentScheduleTodayBinding.fab.setOnClickListener {
            DialogUtil.showAddScheduleDialog(context, this)
        }

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentScheduleTodayBinding.rvWeekUndoList.layoutManager = linearLayoutManager
        thisWeekScheduleTodayAdapter = ScheduleTodayAdapter(weekUndoList)
        fragmentScheduleTodayBinding.rvWeekUndoList.addItemDecoration(SpaceItemDecoration(10))
        fragmentScheduleTodayBinding.rvWeekUndoList.adapter = thisWeekScheduleTodayAdapter
        thisWeekScheduleTodayAdapter.setOnItemClickListener { adapter, view, position ->
            loge("本周未完成：$position")
            val intent = Intent(context, ScheduleEditActivity::class.java)
            val scheduleData = weekUndoList[position]
            intent.putExtra("data",JSON.toJSONString(scheduleData))
            startActivity(intent)
        }

        val linearLayoutManager2 = LinearLayoutManager(context)
        linearLayoutManager2.orientation = LinearLayoutManager.VERTICAL
        fragmentScheduleTodayBinding.rvTodayList.layoutManager = linearLayoutManager2
        todayScheduleTodayAdapter = ScheduleTodayAdapter(todayList)
        fragmentScheduleTodayBinding.rvTodayList.addItemDecoration(SpaceItemDecoration(10))
        fragmentScheduleTodayBinding.rvTodayList.adapter = todayScheduleTodayAdapter
        todayScheduleTodayAdapter.setOnItemClickListener { adapter, view, position ->
            loge("今日清单：$position")
            val intent = Intent(context, ScheduleEditActivity::class.java)
            val scheduleData = todayList[position]
            intent.putExtra("data",JSON.toJSONString(scheduleData))
            startActivity(intent)
        }
    }

    fun initData() {
        todayScheduleViewModel.getThisWeekUndoList().observe(requireActivity(), {
            it?.let {
                weekUndoList.clear()
                weekUndoList.addAll(it)
                thisWeekScheduleTodayAdapter.setList(weekUndoList)
            }
        })
        todayScheduleViewModel.getTodayList().observe(requireActivity(), {
            it?.let {
                todayList.clear()
                todayList.addAll(it)
                todayScheduleTodayAdapter.setList(todayList)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        ///todayScheduleViewModel.getThisWeekAndTodayList()
        todayScheduleViewModel.getThisWeekScheduleList()
        todayScheduleViewModel.getTodayScheduleList()
    }
}