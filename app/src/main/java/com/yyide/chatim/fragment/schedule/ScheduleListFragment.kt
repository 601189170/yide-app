package com.yyide.chatim.fragment.schedule

import android.content.Intent
import android.graphics.Color
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
import com.yanzhenjie.recyclerview.*
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
import com.yyide.chatim.activity.schedule.ScheduleTimetableClassActivity
import com.yyide.chatim.adapter.schedule.ListViewEvent
import com.yyide.chatim.adapter.schedule.ScheduleListAdapter
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.promoterSelf
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedToMonthOfDateTime
import com.yyide.chatim.utils.logd
import com.yyide.chatim.view.SpaceItemDecoration
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
    private lateinit var rvScheduleList: SwipeRecyclerView
    private lateinit var calendarComposeLayout: CalendarComposeLayout
    private var list = mutableListOf<ScheduleData>()
    private var first: Boolean = true
    private lateinit var scheduleListAdapter: ScheduleListAdapter
    private lateinit var curBottomDateTime: DateTime
    private lateinit var curTopDateTime: DateTime
    private val scheduleViewModel by activityViewModels<ScheduleMangeViewModel>()

    //是否显示时间轴
    private var timeAxisDateTime: DateTime? = null

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
            DialogUtil.showAddScheduleDialog(context, this, null)
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
            scheduleListAdapter.setList(list)
        })
        //重置更新列表时调用，和向上加载，向下加载区分开来
        scheduleListViewViewModel.updateListViewData.observe(requireActivity()){
            list.clear()
            list.addAll(it)
            scheduleListAdapter.setList(list)
        }
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
                    curModifySchedule?.id ?: "", "1"
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
        scheduleListAdapter = ScheduleListAdapter()
        rvScheduleList.setSwipeMenuCreator(swipeMenuCreator)
        rvScheduleList.setOnItemMenuClickListener(mMenuItemClickListener)
        rvScheduleList.addItemDecoration(SpaceItemDecoration(DisplayUtils.dip2px(context,10f)))
        rvScheduleList.adapter = scheduleListAdapter
        scheduleListAdapter.setOnItemClickListener { _, _, position ->
            val scheduleData = list[position]
            if (!scheduleData.isMonthHead) {
                loge("点击日程 $scheduleData")
                curModifySchedule = scheduleData
                if (scheduleData.type.toInt() == Schedule.SCHEDULE_TYPE_CONFERENCE) {
                    MeetingSaveActivity.jumpUpdate(requireContext(), scheduleData.id)
                    return@setOnItemClickListener
                }
                if (scheduleData.type.toInt() == Schedule.SCHEDULE_TYPE_CLASS_SCHEDULE) {
                    ScheduleTimetableClassActivity.jump(requireContext(), scheduleData)
                    return@setOnItemClickListener
                }
                val intent = Intent(context, ScheduleEditActivity::class.java)
                intent.putExtra("data", JSON.toJSONString(scheduleData))
                startActivity(intent)
            }
        }
    }

    override fun onClickDate(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
        scrollToPosition(year, month, day)
        val dateTime = DateTime(year, month + 1, day, 0, 0, 0).simplifiedDataTime()
        scheduleViewModel.curDateTime.value = dateTime
    }

    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onPageChange year=$year,month=$month,day=$day")
        val dateTime = DateTime(year, month + 1, day, 0, 0, 0).simplifiedDataTime()
        scheduleViewModel.curDateTime.value = dateTime
        val monthDateList = list.filter { it.isMonthHead }.map { ScheduleDaoUtil.toDateTime(it.startTime).simplifiedToMonthOfDateTime()}
        if (!monthDateList.contains(dateTime.simplifiedToMonthOfDateTime())) {
            loge("monthDateList${monthDateList},dateTime=${dateTime.simplifiedToMonthOfDateTime()}")
            if (dateTime > curBottomDateTime) {
                scrollOrientation = 1
                curBottomDateTime = curBottomDateTime.plusMonths(1)
                scheduleListViewViewModel.scheduleDataList(curBottomDateTime, timeAxisDateTime)
            } else if (dateTime < curTopDateTime) {
                scrollOrientation = -1
                curTopDateTime = curTopDateTime.minusMonths(1)
                scheduleListViewViewModel.scheduleDataList(curTopDateTime, timeAxisDateTime)
            }
        }
    }

    fun scrollToPosition(year: Int, month: Int, day: Int) {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        //定位到指定日期
        val dateTime = DateTime.parse("$year-${month + 1}-$day 00:00:00", dateTimeFormatter)
        var scrollOuter: Int = -1
        list.let {
            for (i in 0 until list.size) {
                val scheduleData = list[i]
                if (!scheduleData.isMonthHead && !scheduleData.isTimeAxis && ScheduleDaoUtil.toDateTime(scheduleData.startTime)
                        .simplifiedDataTime() == dateTime
                ) {
                    scrollOuter = i
                    return@let
                }
            }
        }
        loge("需要滚动到 scrollOuter=$scrollOuter,dateTime=$dateTime")
        moveToPosition(scrollOuter, rvScheduleList)
    }

    fun moveToPosition(position: Int, recyclerView: RecyclerView?) {
        if (recyclerView == null || position == -1) {
            return
        }
        val firstItem: Int = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0))
        val lastItem: Int =
            recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1))
        loge("moveToPosition:firstItem=$firstItem,lastItem=$lastItem")
        if (position < firstItem || position > lastItem) {
            //recyclerView.smoothScrollToPosition(position)
                logd("scrollToPosition=$position")
            //recyclerView.scrollToPosition(position)
            val manager = recyclerView.layoutManager as LinearLayoutManager
            manager.scrollToPositionWithOffset(position,0)
        } else {
            val movePosition = position - firstItem
            val top: Int = recyclerView.getChildAt(movePosition).getTop()
            logd("scrollBy=$top")
            //recyclerView.smoothScrollBy(0, top)
            recyclerView.scrollBy(0,top)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(event: ScheduleEvent) {
        loge("$event")
        if (event.type == ScheduleEvent.NEW_TYPE) {
            if (event.result) {
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
        if (!hidden) {
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
                    scheduleListViewViewModel.scheduleDataList(curBottomDateTime, timeAxisDateTime)
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
                    scheduleListViewViewModel.scheduleDataList(curTopDateTime, timeAxisDateTime)
                }
            }
        })
    }

    private fun updateDate() {
        list.clear()
        curTopDateTime = DateTime.now()
        curBottomDateTime = DateTime.now()
        //timeAxisDateTime = DateTime.now().simplifiedDataTime()
        scheduleListViewViewModel.scheduleDataList(DateTime.now(), timeAxisDateTime,false)
    }

    private val swipeMenuCreator: SwipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(
            swipeLeftMenu: SwipeMenu,
            swipeRightMenu: SwipeMenu,
            position: Int
        ) {
            val width = DisplayUtils.dip2px(context, 63f)
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            run {
                if (list.isNotEmpty() && !list[position].isMonthHead) {
                    val modifyItem: SwipeMenuItem =
                        SwipeMenuItem(context).setBackground(R.drawable.selector_blue)
                            //.setImage(R.drawable.ic_action_delete)
                            .setText("标为\n完成")
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height)
                    swipeRightMenu.addMenuItem(modifyItem) // 添加菜单到右侧。
                    val delItem: SwipeMenuItem =
                        SwipeMenuItem(context).setBackground(R.drawable.selector_red)
                            .setText("删除")
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height)
                    swipeRightMenu.addMenuItem(delItem) // 添加菜单到右侧。
                }
            }
        }
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private val mMenuItemClickListener =
        OnItemMenuClickListener { menuBridge, position ->
            menuBridge.closeMenu()
            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {
                    loge("标记为完成")
                    val scheduleData = list[position]
                    if (!scheduleData.promoterSelf()) {
                        ToastUtils.showShort("此日程不能修改！")
                        return@OnItemMenuClickListener
                    }
                    curModifySchedule = scheduleData
                    scheduleEditViewModel.changeScheduleState(scheduleData)
                    return@OnItemMenuClickListener
                }
                if (menuPosition == 1) {
                    loge("删除日程")
                    val scheduleData = list[position]
                    if (!scheduleData.promoterSelf()) {
                        ToastUtils.showShort("此日程不能删除！")
                        return@OnItemMenuClickListener
                    }
                    curModifySchedule = scheduleData
                    scheduleEditViewModel.deleteScheduleById(scheduleData.id)
                    return@OnItemMenuClickListener
                }

            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                loge("list第$position; 左侧菜单第$menuPosition")
            }
        }
}