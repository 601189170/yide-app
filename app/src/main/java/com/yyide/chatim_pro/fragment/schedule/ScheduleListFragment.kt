package com.yyide.chatim_pro.fragment.schedule

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yanzhenjie.recyclerview.*
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.schedule.CalendarComposeLayout
import com.yyide.chatim_pro.BaseApplication
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.databinding.FragmentScheduleListBinding
import com.yyide.chatim_pro.model.schedule.*
import com.yyide.chatim_pro.utils.loge
import com.yyide.chatim_pro.view.DialogUtil
import com.yyide.chatim_pro.viewmodel.ScheduleListViewViewModel
import org.joda.time.DateTime
import com.yyide.chatim_pro.activity.meeting.MeetingSaveActivity
import com.yyide.chatim_pro.activity.schedule.ScheduleEditActivitySimple
import com.yyide.chatim_pro.activity.schedule.ScheduleTimetableClassActivity
import com.yyide.chatim_pro.adapter.schedule.ScheduleListAdapter
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.database.ScheduleDaoUtil
import com.yyide.chatim_pro.database.ScheduleDaoUtil.promoterSelf
import com.yyide.chatim_pro.model.EventMessage
import com.yyide.chatim_pro.utils.DisplayUtils
import com.yyide.chatim_pro.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim_pro.utils.ScheduleRepetitionRuleUtil.simplifiedToMonthOfDateTime
import com.yyide.chatim_pro.utils.logd
import com.yyide.chatim_pro.viewmodel.ScheduleEditViewModel
import com.yyide.chatim_pro.viewmodel.ScheduleMangeViewModel
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
class ScheduleListFragment : Fragment(), OnCalendarClickListener,
    SwipeRefreshLayout.OnRefreshListener, ScheduleListAdapter.ImgListener {
    lateinit var fragmentScheduleListBinding: FragmentScheduleListBinding
    private val scheduleListViewViewModel: ScheduleListViewViewModel by viewModels()
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    private lateinit var rvScheduleList: SwipeRecyclerView
    private lateinit var blankPage: ConstraintLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var calendarComposeLayout: CalendarComposeLayout
    private var list = mutableListOf<ScheduleData>()
    private var first: Boolean = true
    private var update: Boolean = true
    private lateinit var scheduleListAdapter: ScheduleListAdapter
    private lateinit var curBottomDateTime: DateTime
    private lateinit var curTopDateTime: DateTime
    private val scheduleViewModel by activityViewModels<ScheduleMangeViewModel>()
    private var refresh = false
    private var scroll = true

    //是否显示时间轴
    private var timeAxisDateTime: DateTime? = null

    //当前滚动方向是 0没滚动 1 向下滚动底部 -1向上滚动到顶部
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
        blankPage = calendarComposeLayout.blankPage
        swipeRefreshLayout = calendarComposeLayout.swipeRefreshLayout
        calendarComposeLayout.setOnCalendarClickListener(this)
        initScheduleList()
        initData()
        fragmentScheduleListBinding.fab.setOnClickListener {
            DialogUtil.showAddScheduleDialog(context, this, null)
            //DialogUtil.showAddLabelDialog(context, labelList)
        }

        scheduleListViewViewModel.listViewData.observe(requireActivity()) {
            if (it.isEmpty() && !update) {
                return@observe
            }
            if (scrollOrientation == -1) {
                list.addAll(0, it)
                scheduleListAdapter.setList(list)
            } else {
                list.addAll(it)
                scheduleListAdapter.setList(list)
            }
        }
        //重置更新列表时调用，和向上加载，向下加载区分开来
        scheduleListViewViewModel.updateListViewData.observe(requireActivity()) { it ->
            list.clear()
            list.addAll(it)
            scheduleListAdapter.setList(list)
            scheduleViewModel.curDateTime.value?.also {
                if (!refresh) {
                    scrollToPosition(it.year, it.monthOfYear - 1, it.dayOfMonth)
                }
            }
            if (refresh) {
                rvScheduleList.smoothScrollToPosition(0)
                refresh = false
                swipeRefreshLayout.isRefreshing = false
            }


            blankPage.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
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
                    curModifySchedule?.id ?: "", curModifySchedule?.status ?: ""
                )
                updateDate()
            } else {
                ToastUtils.showShort("日程修改失败")
            }
        })
        scheduleViewModel.requestAllScheduleResult.observe(requireActivity()) {
            loge("刷新数据列表 $it")
            updateDate()
        }

        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
    }

    private fun initScheduleList() {
        val manager = LinearLayoutManager(activity)
        manager.setOrientation(LinearLayoutManager.VERTICAL)
        rvScheduleList.layoutManager = manager
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        rvScheduleList.setItemAnimator(itemAnimator)
        scheduleListAdapter = ScheduleListAdapter()
        calendarComposeLayout.setOnCalendarClickListener(this)
        scheduleListAdapter.setImgListener(this)
        rvScheduleList.setSwipeMenuCreator(swipeMenuCreator)
        rvScheduleList.setOnItemMenuClickListener(mMenuItemClickListener)
//        rvScheduleList.addItemDecoration(SpaceItemDecoration(DisplayUtils.dip2px(context,10f)))
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
                val intent = Intent(context, ScheduleEditActivitySimple::class.java)
                intent.putExtra("data", JSON.toJSONString(scheduleData))
                intent.putExtra("from", "1")
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
        val monthDateList = list.filter { it.isMonthHead }
            .map { ScheduleDaoUtil.toDateTime(it.startTime).simplifiedToMonthOfDateTime() }
        if (!monthDateList.contains(dateTime.simplifiedToMonthOfDateTime())) {
            loge("monthDateList${monthDateList},dateTime=${dateTime.simplifiedToMonthOfDateTime()}")
            if (dateTime > curBottomDateTime) {
                scrollOrientation = 1
                curBottomDateTime = curBottomDateTime.plusMonths(1)
                scheduleListViewViewModel.scheduleDataList(curBottomDateTime, timeAxisDateTime)
            } else if (dateTime < curTopDateTime) {
                scrollOrientation = -1
                curTopDateTime = curTopDateTime.minusMonths(1)
                //scheduleListViewViewModel.scheduleDataList(curTopDateTime, timeAxisDateTime)
            }
        }
    }

    fun scrollToPosition(year: Int, month: Int, day: Int) {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        //定位到指定日期
        val dateTime = DateTime.parse("$year-${month + 1}-$day 00:00:00", dateTimeFormatter)
            .simplifiedDataTime()
        var scrollOuter: Int = -1
        //先找对应位置
        dateTime.let {
            for (i in 0 until list.size - 1) {
                val scheduleData1 = list[i]
                //val scheduleData2 = list[i+1]
                val dateTime1 = ScheduleDaoUtil.toDateTime(
                    scheduleData1.moreDayStartTime ?: scheduleData1.startTime
                ).simplifiedDataTime()
                //val dateTime2 = ScheduleDaoUtil.toDateTime(scheduleData2.moreDayStartTime?:scheduleData2.startTime).simplifiedDataTime()
                if (i != 0 && i != list.size - 1 && it == dateTime1) {
                    loge("----找到定位的位置----")
                    if (!scheduleData1.isMonthHead && !scheduleData1.isTimeAxis) {
                        scrollOuter = i
                        return@let
                    }
                    return@let
                }
            }
            //找不到对应点击日期，找范围
            for (i in 0 until list.size - 1) {
                val scheduleData1 = list[i]
                val scheduleData2 = list[i + 1]
                val dateTime1 = ScheduleDaoUtil.toDateTime(
                    scheduleData1.moreDayStartTime ?: scheduleData1.startTime
                ).simplifiedDataTime()
                val dateTime2 = ScheduleDaoUtil.toDateTime(
                    scheduleData2.moreDayStartTime ?: scheduleData2.startTime
                ).simplifiedDataTime()
                if (i != 0 && i != list.size - 1 && it in dateTime1..dateTime2) {
                    loge("----找到定位的位置----")
                    if (!scheduleData1.isMonthHead && !scheduleData1.isTimeAxis) {
                        scrollOuter = i
                        return@let
                    }
                    return@let
                }
            }
        }
        if (scrollOuter == -1 && list.isNotEmpty()) {
            val scheduleData1 = list[0]
            val dateTime1 = ScheduleDaoUtil.toDateTime(
                scheduleData1.moreDayStartTime ?: scheduleData1.startTime
            ).simplifiedDataTime()
            if (dateTime <= dateTime1) {
                scrollOuter = 0
            } else {
                scrollOuter = list.size - 1
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
            manager.scrollToPositionWithOffset(position, 0)
        } else {
            val movePosition = position - firstItem
            val top: Int = recyclerView.getChildAt(movePosition).getTop()
            logd("scrollBy=$top")
            //recyclerView.smoothScrollBy(0, top)
            recyclerView.scrollBy(0, top)
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
        scroll = true
        updateDate()
        scheduleViewModel.curDateTime.value = DateTime.now().simplifiedDataTime()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        loge("onHiddenChanged $hidden")
        scroll = true
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
//        updateDate()
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
                    //scrollOrientation = -1
                    //curTopDateTime = curTopDateTime.minusMonths(1)
                    //scheduleListViewViewModel.scheduleDataList(curTopDateTime, timeAxisDateTime)
                }
            }
        })
    }

    private fun updateDate() {
        update = true
        list.clear()
        curTopDateTime = DateTime.now()
        curBottomDateTime = DateTime.now().plusMonths(1)
        //timeAxisDateTime = DateTime.now().simplifiedDataTime()
        scheduleListViewViewModel.scheduleDataList(
            DateTime.now().plusMonths(1),
            timeAxisDateTime,
            false,
            true
        )

    }

    val width = DisplayUtils.dip2px(BaseApplication.getInstance(), 63f)
    val height = ViewGroup.LayoutParams.MATCH_PARENT
    private val markCompletedMenuItem: SwipeMenuItem =
        SwipeMenuItem(BaseApplication.getInstance()).setBackground(R.drawable.selector_blue)
            //.setImage(R.drawable.ic_action_delete)
            .setText("标为\n完成")
            .setTextColor(Color.WHITE)
            .setWidth(width)
            .setHeight(height)

    private val delMenuItem: SwipeMenuItem =
        SwipeMenuItem(BaseApplication.getInstance()).setBackground(R.drawable.selector_red)
            .setText("删除")
            .setTextColor(Color.WHITE)
            .setWidth(width)
            .setHeight(height)

    private val markUnCompletedMenuItem: SwipeMenuItem =
        SwipeMenuItem(BaseApplication.getInstance()).setBackground(R.drawable.selector_orange)
            //.setImage(R.drawable.ic_action_delete)
            .setText("标为\n未完成")
            .setTextColor(Color.WHITE)
            .setWidth(width)
            .setHeight(height)

    private val swipeMenuCreator: SwipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(
            swipeLeftMenu: SwipeMenu,
            swipeRightMenu: SwipeMenu,
            position: Int
        ) {
            run {
                if (list.isNotEmpty() && !list[position].isMonthHead) {
                    val scheduleData = list[position]
                    ////日程类型【0：校历日程，1：课表日程，2：事务日程 3：会议日程】
                    val type = scheduleData.type
                    val promoterSelf = scheduleData.promoterSelf()
                    if (promoterSelf) {
                        run {
                            when (type.toInt()) {
                                Schedule.SCHEDULE_TYPE_SCHEDULE -> {
//                                    if (scheduleData.status == "1") {
//                                        swipeRightMenu.addMenuItem(markUnCompletedMenuItem)
//                                    } else {
//                                        swipeRightMenu.addMenuItem(markCompletedMenuItem)
//                                    }
                                    swipeRightMenu.addMenuItem(delMenuItem)
                                }
                                Schedule.SCHEDULE_TYPE_CONFERENCE -> {
                                    swipeRightMenu.addMenuItem(delMenuItem)
                                }
                            }
                        }
                    }
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
                val scheduleData = list[position]
                val type = scheduleData.type
                when (type.toInt()) {
                    Schedule.SCHEDULE_TYPE_SCHEDULE -> {
//                        if (menuPosition == 0) {
//                            loge("修改")
//                            curModifySchedule = scheduleData
//                            scheduleEditViewModel.changeScheduleState(scheduleData)
//                            return@OnItemMenuClickListener
//                        }
//                        if (menuPosition == 1) {
//                            loge("删除")
//                            curModifySchedule = scheduleData
//                            scheduleEditViewModel.deleteScheduleById(scheduleData.id)
//                            return@OnItemMenuClickListener
//                        }
                        if (menuPosition == 0) {
                            loge("删除")
                            curModifySchedule = scheduleData
                            scheduleEditViewModel.deleteScheduleById(scheduleData.id)
                            return@OnItemMenuClickListener
                        }
                    }
                    Schedule.SCHEDULE_TYPE_CONFERENCE -> {
                        if (menuPosition == 0) {
                            loge("删除")
                            curModifySchedule = scheduleData
                            scheduleEditViewModel.deleteScheduleById(scheduleData.id)
                            return@OnItemMenuClickListener
                        }
                    }
                }

            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                loge("list第$position; 左侧菜单第$menuPosition")
            }
        }

    override fun onRefresh() {
        refresh = true
        EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA, ""))

        scrollOrientation = -1
        //curTopDateTime = curTopDateTime.minusMonths(1)
//        scheduleListViewViewModel.scheduleDataList(curTopDateTime, timeAxisDateTime)
    }

    override fun OnimgSelect(item: ScheduleData) {
        curModifySchedule = item
        scheduleEditViewModel.changeScheduleState(item)
    }
}