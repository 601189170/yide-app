package com.yyide.chatim.fragment.schedule

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.yide.calendar.CalendarUtils
import com.yide.calendar.HintCircle
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.month.MonthCalendarView
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentScheduleMonthBinding
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import com.yyide.chatim.viewmodel.ScheduleMonthViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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
    private val scheduleViewModel by activityViewModels<ScheduleMangeViewModel>()
    private val hints = mutableListOf<HintCircle>()
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
        EventBus.getDefault().register(this)
        mcvCalendar = view.findViewById(R.id.mcvCalendar)
        initData()
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
        })
        scheduleMonthViewModel.scheduleList(DateTime.now())
        //addTaskHint(HintCircle(5, 3))
        //addTaskHints(listOf(HintCircle(9, 2), HintCircle(10, 1), HintCircle(13, 5)))
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
        removeTaskHints(hints)
        super.onDestroy()
        EventBus.getDefault().unregister(this)
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
     * 移除任务点
     */
    fun removeTaskHints(hints:List<HintCircle>){
        hints.forEach {
            val dateTime = it.dateTime
            if (CalendarUtils.getInstance(context).removeTaskHint(dateTime.year,dateTime.monthOfYear-1,it)) {
                if (mcvCalendar!!.currentMonthView != null) {
                    mcvCalendar!!.currentMonthView.invalidate()
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
        if (mcvCalendar!!.currentMonthView != null) {
            mcvCalendar!!.currentMonthView.invalidate()
        }
    }
}