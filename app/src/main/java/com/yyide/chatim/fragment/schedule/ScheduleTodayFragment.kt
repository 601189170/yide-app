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
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yanzhenjie.recyclerview.*
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.R
import com.yyide.chatim.activity.meeting.MeetingSaveActivity
import com.yyide.chatim.activity.schedule.ScheduleEditActivity
import com.yyide.chatim.activity.schedule.ScheduleTimetableClassActivity
import com.yyide.chatim.adapter.schedule.ScheduleTodayAdapter
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.promoterSelf
import com.yyide.chatim.databinding.FragmentScheduleTodayBinding
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.model.schedule.Schedule.Companion.SCHEDULE_TYPE_CLASS_SCHEDULE
import com.yyide.chatim.model.schedule.Schedule.Companion.SCHEDULE_TYPE_CONFERENCE
import com.yyide.chatim.model.schedule.Schedule.Companion.SCHEDULE_TYPE_SCHEDULE
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.viewmodel.ScheduleEditViewModel
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
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    private var curModifySchedule: ScheduleData? = null
    private var todayOpen = true
    private var weekOpen = true
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
    fun event(event: ScheduleEvent) {
        loge("$event")
        if (event.type == ScheduleEvent.NEW_TYPE) {
            if (event.result) {
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
            DialogUtil.showAddScheduleDialog(context, this, DateTime.now())
        }
        val drawableLeft = resources.getDrawable(R.drawable.schedule_title_indicate_vertical_line_shape)
        drawableLeft.setBounds(0, 0, drawableLeft.minimumWidth, drawableLeft.minimumHeight)
        fragmentScheduleTodayBinding.tvTodayList.setOnClickListener {
            if (todayOpen){
                val drawable = resources.getDrawable(R.drawable.schedule_fold_up_icon)
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                fragmentScheduleTodayBinding.tvTodayList.setCompoundDrawables(drawableLeft,null,drawable,null)
                fragmentScheduleTodayBinding.rvTodayList.visibility = View.GONE
                todayOpen = false
                return@setOnClickListener
            }
            val drawable = resources.getDrawable(R.drawable.schedule_fold_down_icon)
            //设置图片大小，必须设置
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            fragmentScheduleTodayBinding.tvTodayList.setCompoundDrawables(drawableLeft,null,drawable,null)
            fragmentScheduleTodayBinding.rvTodayList.visibility = View.VISIBLE
            todayOpen = true
        }

        fragmentScheduleTodayBinding.tvWeekUndo.setOnClickListener {
            if (weekOpen){
                val drawable = resources.getDrawable(R.drawable.schedule_fold_up_icon)
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                fragmentScheduleTodayBinding.tvWeekUndo.setCompoundDrawables(drawableLeft,null,drawable,null)
                fragmentScheduleTodayBinding.rvWeekUndoList.visibility = View.GONE
                weekOpen = false
                return@setOnClickListener
            }
            val drawable = resources.getDrawable(R.drawable.schedule_fold_down_icon)
            //设置图片大小，必须设置
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            fragmentScheduleTodayBinding.tvWeekUndo.setCompoundDrawables(drawableLeft,null,drawable,null)
            fragmentScheduleTodayBinding.rvWeekUndoList.visibility = View.VISIBLE
            weekOpen = true
        }

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentScheduleTodayBinding.rvWeekUndoList.layoutManager = linearLayoutManager
        thisWeekScheduleTodayAdapter = ScheduleTodayAdapter(weekUndoList)
        fragmentScheduleTodayBinding.rvWeekUndoList.setSwipeMenuCreator(mWeekSwipeMenuCreator)
        fragmentScheduleTodayBinding.rvWeekUndoList.setOnItemMenuClickListener(
            mWeekMenuItemClickListener
        )
        fragmentScheduleTodayBinding.rvWeekUndoList.addItemDecoration(SpaceItemDecoration(DisplayUtils.dip2px(context,10f)))
        fragmentScheduleTodayBinding.rvWeekUndoList.adapter = thisWeekScheduleTodayAdapter
        thisWeekScheduleTodayAdapter.setOnItemClickListener { adapter, view, position ->
            loge("本周未完成：$position")
            val scheduleData = weekUndoList[position]
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

        val linearLayoutManager2 = LinearLayoutManager(context)
        linearLayoutManager2.orientation = LinearLayoutManager.VERTICAL
        fragmentScheduleTodayBinding.rvTodayList.layoutManager = linearLayoutManager2
        todayScheduleTodayAdapter = ScheduleTodayAdapter(todayList)
        fragmentScheduleTodayBinding.rvTodayList.setSwipeMenuCreator(mTodaySwipeMenuCreator)
        fragmentScheduleTodayBinding.rvTodayList.setOnItemMenuClickListener(
            mTodayMenuItemClickListener
        )
        fragmentScheduleTodayBinding.rvTodayList.addItemDecoration(SpaceItemDecoration(DisplayUtils.dip2px(context,10f)))
        fragmentScheduleTodayBinding.rvTodayList.adapter = todayScheduleTodayAdapter
        todayScheduleTodayAdapter.setOnItemClickListener { adapter, view, position ->
            loge("今日清单：$position")
            val scheduleData = todayList[position]
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
        //删除监听
        scheduleEditViewModel.deleteResult.observe(requireActivity(), {
            if (it) {
                ScheduleDaoUtil.deleteScheduleData(curModifySchedule?.id ?: "")
                updateData()
            }
        })
        //修改日程状态监听
        scheduleEditViewModel.changeStatusResult.observe(requireActivity(), {
            if (it) {
                ToastUtils.showShort("日程修改成功")
                ScheduleDaoUtil.changeScheduleState(
                    curModifySchedule?.id ?: "",curModifySchedule?.status?:""
                )
                updateData()
            } else {
                ToastUtils.showShort("日程修改失败")
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
        if (!hidden) {
            //更新头部日期
            updateData()
            scheduleViewModel.curDateTime.value = DateTime.now().simplifiedDataTime()
        }
    }

    private fun updateData() {
        todayScheduleViewModel.getThisWeekScheduleList()
        todayScheduleViewModel.getTodayScheduleList()
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
            .setText("删除")
            .setTextColor(Color.WHITE)
            .setWidth(width)
            .setHeight(height)

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
                        SCHEDULE_TYPE_SCHEDULE->{
                            if (scheduleData.status == "1") {
                                swipeRightMenu.addMenuItem(markUnCompletedMenuItem)
                            } else {
                                swipeRightMenu.addMenuItem(markCompletedMenuItem)
                            }
                            swipeRightMenu.addMenuItem(delMenuItem)
                        }
                        SCHEDULE_TYPE_CONFERENCE->{
                            swipeRightMenu.addMenuItem(delMenuItem)
                        }
                    }
                }
            }
        }
    }

    private val mWeekSwipeMenuCreator: SwipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(
            swipeLeftMenu: SwipeMenu,
            swipeRightMenu: SwipeMenu,
            position: Int
        ) {
            loge("position: $position")
            val scheduleData = weekUndoList[position]
            ////日程类型【0：校历日程，1：课表日程，2：事务日程 3：会议日程】
            val type = scheduleData.type
            val promoterSelf = scheduleData.promoterSelf()
            if (promoterSelf){
                run {
                    when(type.toInt()){
                        SCHEDULE_TYPE_SCHEDULE->{
                            if (scheduleData.status == "1") {
                                swipeRightMenu.addMenuItem(markUnCompletedMenuItem)
                            } else {
                                swipeRightMenu.addMenuItem(markCompletedMenuItem)
                            }
                            swipeRightMenu.addMenuItem(delMenuItem)
                        }
                        SCHEDULE_TYPE_CONFERENCE->{
                            swipeRightMenu.addMenuItem(delMenuItem)
                        }
                    }
                }
            }
        }
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private val mWeekMenuItemClickListener =
        OnItemMenuClickListener { menuBridge, position ->
            menuBridge.closeMenu()
            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                val scheduleData = weekUndoList[position]
                val type = scheduleData.type
                when(type.toInt()){
                    SCHEDULE_TYPE_SCHEDULE->{
                        if (menuPosition == 0) {
                            loge("修改")
                            curModifySchedule = scheduleData
                            scheduleEditViewModel.changeScheduleState(scheduleData)
                            return@OnItemMenuClickListener
                        }
                        if (menuPosition == 1) {
                            loge("删除")
                            curModifySchedule = scheduleData
                            scheduleEditViewModel.deleteScheduleById(scheduleData.id)
                            return@OnItemMenuClickListener
                        }
                    }
                    SCHEDULE_TYPE_CONFERENCE->{
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

    private val mTodayMenuItemClickListener =
        OnItemMenuClickListener { menuBridge, position ->
            menuBridge.closeMenu()
            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                val scheduleData = todayList[position]
                val type = scheduleData.type
                when(type.toInt()){
                    SCHEDULE_TYPE_SCHEDULE->{
                        if (menuPosition == 0) {
                            loge("修改")
                            curModifySchedule = scheduleData
                            scheduleEditViewModel.changeScheduleState(scheduleData)
                            return@OnItemMenuClickListener
                        }
                        if (menuPosition == 1) {
                            loge("删除")
                            curModifySchedule = scheduleData
                            scheduleEditViewModel.deleteScheduleById(scheduleData.id)
                            return@OnItemMenuClickListener
                        }
                    }
                    SCHEDULE_TYPE_CONFERENCE->{
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
}