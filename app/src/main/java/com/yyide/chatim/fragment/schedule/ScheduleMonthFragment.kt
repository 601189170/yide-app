package com.yyide.chatim.fragment.schedule

import android.content.Intent
import android.graphics.Color
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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alibaba.fastjson.JSON
import com.yanzhenjie.recyclerview.SwipeMenu
import com.yanzhenjie.recyclerview.SwipeMenuCreator
import com.yanzhenjie.recyclerview.SwipeMenuItem
import com.yide.calendar.CalendarUtils
import com.yide.calendar.HintCircle
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.month.MonthCalendarView
import com.yide.calendar.schedule.CalendarComposeLayout
import com.yide.calendar.schedule.CustomCalendarLayout
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.R
import com.yyide.chatim.activity.meeting.MeetingSaveActivity
import com.yyide.chatim.activity.schedule.ScheduleEditActivityMain
import com.yyide.chatim.activity.schedule.ScheduleEditActivitySimple
import com.yyide.chatim.activity.schedule.ScheduleTimetableClassActivity
import com.yyide.chatim.adapter.schedule.ScheduleMonthListAdapter
import com.yyide.chatim.adapter.schedule.ScheduleTodayAdapter
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.dateTimeJointNowTime
import com.yyide.chatim.database.ScheduleDaoUtil.promoterSelf
import com.yyide.chatim.databinding.FragmentScheduleMonth2Binding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.logd
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import com.yyide.chatim.viewmodel.ScheduleMonthViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

/**
 *
 * @author liu tao
 * @date 2021/9/7 14:19
 * @description 日程/月
 */
class ScheduleMonthFragment : Fragment(), OnCalendarClickListener,
    SwipeRefreshLayout.OnRefreshListener {
//    lateinit var fragmentScheduleMonthBinding: FragmentScheduleMonth2Binding
    lateinit var fragmentScheduleMonthBinding: FragmentScheduleMonth2Binding
    private var list = mutableListOf<ScheduleOuter>()
//    private var mcvCalendar: MonthCalendarView? = null
    private var mCurrentSelectYear = 2021
    private var mCurrentSelectMonth = 8
    private var mCurrentSelectDay = 12
    private var curDateTime:DateTime = DateTime.now()
    private val scheduleMonthViewModel:ScheduleMonthViewModel by viewModels()
    private val scheduleViewModel by activityViewModels<ScheduleMangeViewModel>()
    private val hints = mutableListOf<HintCircle>()
    private var refresh = false
    private lateinit var swipeRefreshLayout:SwipeRefreshLayout


    private lateinit var ScheduleMonthAdapter: ScheduleMonthListAdapter

    private lateinit var calendarComposeLayout: CustomCalendarLayout

    private val todayList = mutableListOf<ScheduleData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentScheduleMonthBinding = FragmentScheduleMonth2Binding.inflate(layoutInflater)
        return fragmentScheduleMonthBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loge("onViewCreated")
        EventBus.getDefault().register(this)
//        mcvCalendar = view.findViewById(R.id.mcvCalendar)
        calendarComposeLayout = fragmentScheduleMonthBinding.layoutCalendar.calendarComposeLayout

//        calendarComposeLayout = view.findViewById(R.id.calendarComposeLayout)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

//        initData()
        initView()
        //计算当前的年月日
        mCurrentSelectYear = DateTime.now().year
        mCurrentSelectMonth = DateTime.now().monthOfYear-1
        mCurrentSelectDay = DateTime.now().dayOfMonth
        loge("当前日期：$mCurrentSelectYear-$mCurrentSelectMonth-$mCurrentSelectDay")
        scheduleMonthViewModel.monthDataList.observe(requireActivity(),{
            loge("keys ${it.keys.size}")
            removeTaskHints(hints)
            it.keys.forEach {dateTime->
                val value = it[dateTime]
                if (value != null){
                    val hintCircle = HintCircle(dateTime,dateTime.dayOfMonth, value.size)
                    hints.add(hintCircle)
                    addTaskHint(hintCircle)
                }
            }
            if (refresh){
                refresh = false
                fragmentScheduleMonthBinding.swipeRefreshLayout.isRefreshing = false
            }
        })
        scheduleMonthViewModel.scheduleList(DateTime.now())

        scheduleViewModel.requestAllScheduleResult.observe(requireActivity()){
            loge("刷新数据列表 $it")
            updateData()
        }


//        addTaskHint(HintCircle(5, 3))
//        addTaskHints(listOf(HintCircle(9, 2), HintCircle(10, 1), HintCircle(13, 5)))
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
        removeTaskHints(hints)
        CalendarUtils.getInstance(activity).clearAllTask()
    }

    private fun updateData(){
        //计算当前的年月日
        mCurrentSelectYear = DateTime.now().year
        mCurrentSelectMonth = DateTime.now().monthOfYear-1
        mCurrentSelectDay = DateTime.now().dayOfMonth
        scheduleMonthViewModel.scheduleList(DateTime.now())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {

//        fragmentScheduleMonthBinding.layoutCalendar.calendarComposeLayout.setOnClickListener {
//            DialogUtil.showAddScheduleDialog(context, this,curDateTime.dateTimeJointNowTime())
//        }
        calendarComposeLayout?.setOnCalendarClickListener(this)




      val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentScheduleMonthBinding.rvScheduleList.layoutManager = linearLayoutManager
        ScheduleMonthAdapter = ScheduleMonthListAdapter()
        fragmentScheduleMonthBinding.rvScheduleList.setSwipeMenuCreator(mTodaySwipeMenuCreator)
//        fragmentScheduleMonthBinding.rvScheduleList.setOnItemMenuClickListener(
//                mTodayMenuItemClickListener
//        )
        fragmentScheduleMonthBinding.rvScheduleList.addItemDecoration(SpaceItemDecoration(DisplayUtils.dip2px(context,10f)))
        fragmentScheduleMonthBinding.rvScheduleList.adapter = ScheduleMonthAdapter
        ScheduleMonthAdapter.setOnItemClickListener { adapter, view, position ->
            loge("本周未完成：$position")
            val scheduleData = todayList[position]
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


        calendarComposeLayout.setOnCalendarClickListener(this)

        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))





    }

//    private val mTodayMenuItemClickListener =
//            OnItemMenuClickListener { menuBridge, position ->
//                menuBridge.closeMenu()
//                val direction = menuBridge.direction // 左侧还是右侧菜单。
//                val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
//                if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
//                    val scheduleData = todayList[position]
//                    val type = scheduleData.type
//                    when(type.toInt()){
//                        Schedule.SCHEDULE_TYPE_SCHEDULE ->{
//                            if (menuPosition == 0) {
//                                loge("修改")
//                                curModifySchedule = scheduleData
//                                scheduleEditViewModel.changeScheduleState(scheduleData)
//                                return@OnItemMenuClickListener
//                            }
//                            if (menuPosition == 1) {
//                                loge("删除")
//                                curModifySchedule = scheduleData
//                                scheduleEditViewModel.deleteScheduleById(scheduleData.id)
//                                return@OnItemMenuClickListener
//                            }
//                        }
//                        Schedule.SCHEDULE_TYPE_CONFERENCE ->{
//                            if (menuPosition == 0) {
//                                loge("删除")
//                                curModifySchedule = scheduleData
//                                scheduleEditViewModel.deleteScheduleById(scheduleData.id)
//                                return@OnItemMenuClickListener
//                            }
//                        }
//                    }
//
//                } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
//                    loge("list第$position; 左侧菜单第$menuPosition")
//                }
//            }
    private val mTodaySwipeMenuCreator: SwipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(
                swipeLeftMenu: SwipeMenu,
                swipeRightMenu: SwipeMenu,
                position: Int
        ) {
            loge("position: $position")
            val scheduleData = todayList[position]
            ////日程类型【0：校历日程，1：课表日程，2：事务日程 3：会议日程】
            val type = scheduleData.type
            val promoterSelf = scheduleData.promoterSelf()
            if (promoterSelf){
                run {
                    when(type.toInt()){
                        Schedule.SCHEDULE_TYPE_SCHEDULE ->{
                            if (scheduleData.status == "1") {
                                swipeRightMenu.addMenuItem(markUnCompletedMenuItem)
                            } else {
                                swipeRightMenu.addMenuItem(markCompletedMenuItem)
                            }
                            swipeRightMenu.addMenuItem(delMenuItem)
                        }
                        Schedule.SCHEDULE_TYPE_CONFERENCE ->{
                            swipeRightMenu.addMenuItem(delMenuItem)
                        }
                    }
                }
            }
        }
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

    private val markUnCompletedMenuItem: SwipeMenuItem =
            SwipeMenuItem(BaseApplication.getInstance()).setBackground(R.drawable.selector_orange)
                    //.setImage(R.drawable.ic_action_delete)
                    .setText("标为\n未完成")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height)

    private val delMenuItem: SwipeMenuItem =
            SwipeMenuItem(BaseApplication.getInstance()).setBackground(R.drawable.selector_red)
                    .setImage(R.drawable.icon_delete_whilte)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height)
    private fun initData() {
        calendarComposeLayout?.currentMonthView?.let {
            mCurrentSelectYear = it.selectYear
            mCurrentSelectMonth = it.selectMonth
            mCurrentSelectDay = if (it.selectDay == -1) 1 else it.selectDay
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
        curDateTime = dateTime
        val value = scheduleMonthViewModel.monthDataList.value
        if (value!=null && value[dateTime] != null){
            val mutableList = value[dateTime]
            mutableList?.sort()
            val showDataTime = dateTime.toString("yyyy-MM-dd HH:mm:ss")
            DialogUtil.showMonthScheduleListDialog(requireContext(),showDataTime,mutableList,this){_,date->
                scheduleViewModel.monthAddSchedule.value = date
            }
        }

    }

    private fun setData() {


    }


    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onPageChange year=$year,month=$month,day=$day")
        mCurrentSelectYear = year
        mCurrentSelectMonth = month
        mCurrentSelectDay = day
        val dateTime = DateTime(year,month+1,day,0,0,0).simplifiedDataTime()
        loge("dateTime=$dateTime")
        scheduleMonthViewModel.scheduleList(dateTime)
        scheduleViewModel.curDateTime.postValue(dateTime)
    }

    override fun onResume() {
        super.onResume()
        updateData()
        scheduleViewModel.curDateTime.value = DateTime.now().simplifiedDataTime()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        loge("onHiddenChanged $hidden")
        if (hidden){
            CalendarUtils.getInstance(requireContext()).clearAllTask()
        }
        if(!hidden){
            //更新头部日期
            updateData()
            scheduleViewModel.curDateTime.value = DateTime.now().simplifiedDataTime()
        }
    }

    /**
     * 添加一个圆点提示
     *
     * @param day
     */
//    fun addTaskHint(day: HintCircle) {
//        CalendarUtils.getInstance(context).addTaskHint(mCurrentSelectYear, mCurrentSelectMonth, day)
//        if (calendarComposeLayout!!.currentMonthView != null) {
//            calendarComposeLayout!!.currentMonthView.invalidate()
//        }
//    }
    /**
     * 添加一个圆点提示
     *
     * @param day
     */
    fun addTaskHint(day: HintCircle) {
        val mCurrentSelectYear = day.dateTime.year
        val mCurrentSelectMonth = day.dateTime.monthOfYear -1
        CalendarUtils.getInstance(activity).addTaskHint(mCurrentSelectYear, mCurrentSelectMonth, day)
        if (calendarComposeLayout.currentMonthView != null) {
            calendarComposeLayout.currentMonthView.invalidate()
        }

        if (calendarComposeLayout.currentWeekView != null){
            calendarComposeLayout.currentWeekView.invalidate()
        }
    }
    /**
     * 删除一个圆点提示
     *
     * @param day
     */
    fun removeTaskHint(day: HintCircle) {
        calendarComposeLayout?.currentMonthView?.removeTaskHint(day)
    }

    /**
     * 移除任务点 #FFFF4140
     */
//    fun removeTaskHints(hints:List<HintCircle>){
//        hints.forEach {
//            val dateTime = it.dateTime
//            if (CalendarUtils.getInstance(activity).removeTaskHint(dateTime.year,dateTime.monthOfYear-1,it)) {
//                if (calendarComposeLayout.currentMonthView != null) {
//                    calendarComposeLayout.currentMonthView.invalidate()
//                }
//                if (calendarComposeLayout.currentWeekView != null){
//                    calendarComposeLayout.currentWeekView.invalidate()
//                }
//            }
//        }
//        this.hints.clear()
//    }
    /**
     * 移除任务点 #FFFF4140
     */
    fun removeTaskHints(hints:List<HintCircle>){
        hints.forEach {
            val dateTime = it.dateTime
            if (CalendarUtils.getInstance(activity).removeTaskHint(dateTime.year,dateTime.monthOfYear-1,it)) {
                if (calendarComposeLayout.currentMonthView != null) {
                    calendarComposeLayout.currentMonthView.invalidate()
                }
                if (calendarComposeLayout.currentWeekView != null){
                    calendarComposeLayout.currentWeekView.invalidate()
                }
            }
        }
        this.hints.clear()
    }
    /**
     * 添加多个圆点提示
     *
     * @param hints
     */
    fun addTaskHints(hints: List<HintCircle>) {
        CalendarUtils.getInstance(context)
            .addTaskHints(mCurrentSelectYear, mCurrentSelectMonth, hints)
        if (calendarComposeLayout!!.currentMonthView != null) {
            calendarComposeLayout!!.currentMonthView.invalidate()
        }
    }

    override fun onRefresh() {
        refresh = true
        EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA,""))
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

    fun scrollToPosition(year: Int, month: Int, day: Int) {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        //定位到指定日期
        val dateTime = DateTime.parse("$year-${month + 1}-$day 00:00:00", dateTimeFormatter)
                .simplifiedDataTime()
        var scrollOuter: Int = -1
//        dateTime.let {
//            for (i in 0 until schoolCalendarList.size - 1) {
//                val scheduleData1 = schoolCalendarList[i]
//                val scheduleData2 = schoolCalendarList[i + 1]
//                val dateTime1 = ScheduleDaoUtil.toDateTime((scheduleData1.startTime
//                        ?: "2000-01-01"), "yyyy-MM-dd").simplifiedDataTime()
//                val dateTime2 = ScheduleDaoUtil.toDateTime((scheduleData2.startTime
//                        ?: "2000-01-01"), "yyyy-MM-dd").simplifiedDataTime()
//                if (i != 0 && i != schoolCalendarList.size - 1 && it in dateTime1..dateTime2) {
//                    loge("----找到定位的位置----")
//                    if (scheduleData1.type != 1) {
//                        scrollOuter = i
//                        return@let
//                    }
//                    return@let
//                }
//            }
//        }
//        loge("需要滚动到 scrollOuter=$scrollOuter,dateTime=$dateTime")
//        moveToPosition(scrollOuter, rvScheduleList)
    }
}