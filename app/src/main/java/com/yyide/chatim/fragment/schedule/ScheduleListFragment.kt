package com.yyide.chatim.fragment.schedule

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.schedule.CalendarComposeLayout
import com.yide.calendar.schedule.ScheduleLayout
import com.yide.calendar.schedule.ScheduleRecyclerView
import com.yyide.chatim.R
import com.yyide.chatim.adapter.schedule.ScheduleListOuterAdapter
import com.yyide.chatim.databinding.FragmentScheduleListBinding
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.viewmodel.ScheduleListViewViewModel
import org.joda.time.DateTime
import com.yyide.chatim.MainActivity
import com.yyide.chatim.activity.meeting.MeetingSaveActivity
import com.yyide.chatim.activity.schedule.ScheduleEditActivity
import com.yyide.chatim.adapter.schedule.ListViewEvent
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.viewmodel.ScheduleEditViewModel
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程/列表
 */
class ScheduleListFragment : Fragment(), OnCalendarClickListener {
    lateinit var fragmentScheduleListBinding: FragmentScheduleListBinding
    private val scheduleListViewViewModel: ScheduleListViewViewModel by viewModels()
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    private lateinit var rvScheduleList: ScheduleRecyclerView
    private lateinit var calendarComposeLayout: CalendarComposeLayout
    private var list = mutableListOf<MonthViewScheduleData>()
    private var first: Boolean = true
    private lateinit var adapterOuter: ScheduleListOuterAdapter
    private lateinit var curBottomDateTime: DateTime
    private lateinit var curTopDateTime: DateTime
    private val scheduleViewModel by activityViewModels<ScheduleMangeViewModel>()
    //是否显示时间轴
    private var showTimeAxis:Boolean = true
    private lateinit var timeAxisDateTime:DateTime
    //当前滚动方向是 0没滚动 1 向下滚动底部 -1向上滚动打顶部
    private var scrollOrientation: Int = 0
    private var curModifySchedule: ScheduleData? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScheduleListBinding = FragmentScheduleListBinding.inflate(layoutInflater)
        return fragmentScheduleListBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        calendarComposeLayout = view.findViewById(R.id.calendarComposeLayout)
        rvScheduleList = calendarComposeLayout.rvScheduleList
        calendarComposeLayout.setOnCalendarClickListener(this)
        initScheduleList()
        initData()
        fragmentScheduleListBinding.fab.setOnClickListener {
            DialogUtil.showAddScheduleDialog(context, this)
            //DialogUtil.showAddLabelDialog(context, labelList)
        }

        scheduleListViewViewModel.listViewData.observe(requireActivity(), {
            if (it.isEmpty()) {
                return@observe
            }
            if (scrollOrientation == -1) {
                list.addAll(0, it)
            } else {
                list.addAll(it)
            }
            adapterOuter.setList(list)
        })
        //删除监听
        scheduleEditViewModel.deleteResult.observe(requireActivity(), {
            if (it) {
                ScheduleDaoUtil.deleteScheduleData(curModifySchedule?.id ?: "")
                updateDate()
            }
        })
        //修改日程状态监听
        scheduleEditViewModel.changeStatusResult.observe(requireActivity(), {
            if (it) {
                ToastUtils.showShort("日程修改成功")
                ScheduleDaoUtil.changeScheduleState(
                    curModifySchedule?.id ?: "","1"
                )
                updateDate()
            } else {
                ToastUtils.showShort("日程修改失败")
            }
        })
    }

    private fun initScheduleList() {
        val manager = LinearLayoutManager(activity)
        manager.setOrientation(LinearLayoutManager.VERTICAL)
        rvScheduleList.layoutManager = manager
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        rvScheduleList.setItemAnimator(itemAnimator)
        adapterOuter = ScheduleListOuterAdapter()
        rvScheduleList.adapter = adapterOuter
        adapterOuter.addListViewEvent(object : ListViewEvent {
            override fun deleteItem(scheduleData: ScheduleData) {
                loge("删除日程 $scheduleData")
                curModifySchedule = scheduleData
                scheduleEditViewModel.deleteScheduleById(scheduleData.id)
            }

            override fun clickItem(scheduleData: ScheduleData) {
                loge("点击日程 $scheduleData")
                curModifySchedule = scheduleData
                if (scheduleData.type.toInt() == Schedule.SCHEDULE_TYPE_CONFERENCE){
                    MeetingSaveActivity.jumpUpdate(requireContext(),scheduleData.id)
                    return
                }
                val intent = Intent(context, ScheduleEditActivity::class.java)
                intent.putExtra("data", JSON.toJSONString(scheduleData))
                startActivity(intent)
            }

            override fun modifyItem(scheduleData: ScheduleData) {
                loge("修改日程状态 $scheduleData")
                curModifySchedule = scheduleData
                scheduleEditViewModel.changeScheduleState(scheduleData)
            }

        })
    }

    override fun onClickDate(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
        scrollToPosition(year, month, day)
        val dateTime = DateTime(year,month+1,day,0,0,0).simplifiedDataTime()
        loge("dateTime=$dateTime")
        scheduleViewModel.curDateTime.value = dateTime
    }

    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onPageChange year=$year,month=$month,day=$day")
        val dateTime = DateTime(year,month+1,day,0,0,0).simplifiedDataTime()
        loge("dateTime=$dateTime")
        scheduleViewModel.curDateTime.value = dateTime
    }

    fun scrollToPosition(year: Int, month: Int, day: Int){
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        //定位到指定日期
        val dateTime = DateTime.parse("$year-${month + 1}-$day 00:00:00", dateTimeFormatter)
        var scrollOuter:Int = 0
        var scrollInner:Int = 0
        list.let {
            for (i in 0 until list.size){
                val monthViewScheduleData = list[i]
                val list1 = monthViewScheduleData.list
                for (j in 0 until list1.size){
                    val dayViewScheduleData = list1[j]
                    if (dayViewScheduleData.dateTime == dateTime){
                        scrollOuter = i
                        scrollInner = j
                        return@let
                    }
                }
            }
        }

        loge("需要滚动到 scrollOuter=$scrollOuter,scrollInner=$scrollInner")
        moveToPosition(scrollInner,adapterOuter.getInnerRecyclerView())
        moveToPosition(scrollOuter,rvScheduleList)

    }

    fun moveToPosition(position: Int,recyclerView:RecyclerView?) {
        if (recyclerView == null){
            return
        }
        val firstItem: Int = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0))
        val lastItem: Int =
            recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1))
        if (position < firstItem || position > lastItem) {
            recyclerView.smoothScrollToPosition(position)
        } else {
            val movePosition = position - firstItem
            val top: Int = recyclerView.getChildAt(movePosition).getTop()
            recyclerView.smoothScrollBy(0, top)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(event: ScheduleEvent){
        loge("$event")
        if (event.type == ScheduleEvent.NEW_TYPE) {
            if (event.result){
                //日程新增成功
                updateDate()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateDate()
        scheduleViewModel.curDateTime.value = DateTime.now().simplifiedDataTime()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        loge("onHiddenChanged $hidden")
        if(!hidden){
            //更新头部日期
            updateDate()
            scheduleViewModel.curDateTime.value = DateTime.now().simplifiedDataTime()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    private fun initData() {
        updateDate()
        //监听列表滚动
        rvScheduleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // canScrollVertically(1) 为false 的时候滑动到底部了
                if (!rvScheduleList.canScrollVertically(1)) {
                    loge("滑动到底部了")
                    scrollOrientation = 1
                    curBottomDateTime = curBottomDateTime.plusMonths(1)
                    scheduleListViewViewModel.scheduleList(curBottomDateTime,timeAxisDateTime)
                }
                // canScrollVertically(-1) 为false 的时候滑动到顶部了
                if (!rvScheduleList.canScrollVertically(-1)) {
                    if (first) {
                        first = false
                        return
                    }
                    loge("滑动到顶部了")
                    //日期减一请求数据
                    scrollOrientation = -1
                    curTopDateTime = curTopDateTime.minusMonths(1)
                    scheduleListViewViewModel.scheduleList(curTopDateTime,timeAxisDateTime)
                }
            }
        })
    }

    private fun updateDate() {
        list.clear()
        curTopDateTime = DateTime.now()
        curBottomDateTime = DateTime.now()
        timeAxisDateTime = DateTime.now().simplifiedDataTime()
        scheduleListViewViewModel.scheduleList(DateTime.now(),timeAxisDateTime)
    }
}