package com.yyide.chatim.fragment.schedule

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.yyide.chatim.activity.meeting.MeetingSaveActivity
import com.yyide.chatim.activity.schedule.ScheduleEditActivity
import com.yyide.chatim.adapter.schedule.ScheduleTodayAdapter
import com.yyide.chatim.databinding.FragmentScheduleTodayBinding
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import com.yyide.chatim.viewmodel.TodayScheduleViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2021/9/18 16:149
 * @description 日程/今日
 */
class ScheduleTodayFragment : Fragment() {
    lateinit var fragmentScheduleTodayBinding: FragmentScheduleTodayBinding
    private val scheduleViewModel by activityViewModels<ScheduleMangeViewModel>()
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
        EventBus.getDefault().register(this)
        loge("onViewCreated")
        initData()
        initView()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(event: ScheduleEvent){
        loge("$event")
        if (event.type == ScheduleEvent.NEW_TYPE) {
            if (event.result){
                //日程新增成功
                updateData()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
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
            val scheduleData = weekUndoList[position]
            if (scheduleData.type.toInt() == Schedule.SCHEDULE_TYPE_CONFERENCE){
                MeetingSaveActivity.jumpUpdate(requireContext(),scheduleData.id)
                return@setOnItemClickListener
            }
            val intent = Intent(context, ScheduleEditActivity::class.java)
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
            val scheduleData = todayList[position]
            if (scheduleData.type.toInt() == Schedule.SCHEDULE_TYPE_CONFERENCE){
                MeetingSaveActivity.jumpUpdate(requireContext(),scheduleData.id)
                return@setOnItemClickListener
            }
            val intent = Intent(context, ScheduleEditActivity::class.java)
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
        updateData()
        loge("onResume")
        scheduleViewModel.curDateTime.value = DateTime.now().simplifiedDataTime()
    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        loge("onHiddenChanged $hidden")
        if(!hidden){
            //更新头部日期
            updateData()
            scheduleViewModel.curDateTime.value = DateTime.now().simplifiedDataTime()
        }
    }
    private fun updateData() {
        todayScheduleViewModel.getThisWeekScheduleList()
        todayScheduleViewModel.getTodayScheduleList()
    }
}