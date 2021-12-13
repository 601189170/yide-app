package com.yyide.chatim.activity.schedule

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ToastUtils
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.yide.calendar.CalendarUtils
import com.yide.calendar.HintCircle
import com.yide.calendar.OnCalendarClickListener
import com.yide.calendar.schedule.CalendarComposeLayout
import com.yyide.chatim.R
import com.yyide.chatim.adapter.schedule.SchoolCalendarAdapter
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.toDateTime
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.ActivitySchoolCalendarBinding
import com.yyide.chatim.dialog.DeptSelectPop
import com.yyide.chatim.model.LeaveDeptRsp
import com.yyide.chatim.model.schedule.SchoolCalendarRsp
import com.yyide.chatim.model.schedule.SchoolSemesterRsp
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.ScheduleRepetitionRuleUtil.simplifiedDataTime
import com.yyide.chatim.utils.logd
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.viewmodel.SchoolCalendarViewModel
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.ArrayList

class SchoolCalendarActivity : BaseActivity(), OnCalendarClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    lateinit var schoolCalendarBinding: ActivitySchoolCalendarBinding
    private val schoolCalendarViewModel: SchoolCalendarViewModel by viewModels()
    private val eventList = mutableListOf<LeaveDeptRsp.DataBean>()
    private val schoolCalendarList = mutableListOf<SchoolCalendarRsp.DataBean>()
    private var curSemesterId: String? = null
    private lateinit var curDateTime:DateTime
    private lateinit var rvScheduleList: SwipeRecyclerView
    private lateinit var blankPage: ConstraintLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var calendarComposeLayout: CalendarComposeLayout
    private lateinit var schoolCalendarAdapter:SchoolCalendarAdapter
    private var refresh = false
    private val hints = mutableListOf<HintCircle>()
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schoolCalendarBinding = ActivitySchoolCalendarBinding.inflate(layoutInflater)
        setContentView(schoolCalendarBinding.root)
        initView()
        schoolCalendarViewModel.semesterList.observe(this) {
            if (it.isEmpty()) {
                ToastUtils.showShort("查询校历学年学期失败")
                blankPage.visibility = View.VISIBLE
                return@observe
            }
            blankPage.visibility = View.GONE
            initSemesterData(it)
            initSemesterView()
        }
        //校历备注列表
        schoolCalendarViewModel.schoolCalendarList.observe(this){
            if (refresh){
                refresh = false
                swipeRefreshLayout.isRefreshing = false
            }
            schoolCalendarList.clear()
            removeTaskHints(hints)
            schoolCalendarList.addAll(it)
            schoolCalendarList.sort()
            if (it.isEmpty()) {
                blankPage.visibility = View.VISIBLE
            } else {
                blankPage.visibility = View.GONE
                val dataBean = SchoolCalendarRsp.DataBean()
                dataBean.type = 1
                dataBean.startTime = curDateTime.toStringTime("yyyy-MM").plus("-01")
                dataBean.endTime = curDateTime.toStringTime("yyyy-MM").plus("-01")
                schoolCalendarList.add(0,dataBean)
            }

            schoolCalendarAdapter.setList(schoolCalendarList)
            schoolCalendarList.forEach {
                if (it.type == 1){
                    return@forEach
                }
                if (it.moreDay == 1) {
                    //跨天计算
                    val startTime = toDateTime(it.startTime?:"", "yyyy-MM-dd")
                    val endTime = toDateTime(it.endTime?:"", "yyyy-MM-dd")

                    val daysBetween = Days.daysBetween(startTime, endTime).days
                    for (index in 0..daysBetween) {
                        val dataBean:SchoolCalendarRsp.DataBean = it.copy()
                        val allDay = toDateTime(dataBean.startTime?:"","yyyy-MM-dd").simplifiedDataTime().plusDays(index)
                        //dataBean.startTime = allDay.toStringTime("yyyy-MM-dd")
                        //dataBean.endTime = allDay.toStringTime("yyyy-MM-dd")
                        if (startTime.year != allDay.year || startTime.monthOfYear != allDay.monthOfYear){
                            break
                        }
                        val hintCircle = HintCircle(allDay, allDay.dayOfMonth, 1)
                        hints.add(hintCircle)
                        addTaskHint(hintCircle)
                    }
                } else {
                    it.startTime?.let {
                        val dateTime = toDateTime(it, "yyyy-MM-dd")
                        val hintCircle = HintCircle(dateTime, dateTime.dayOfMonth, 1)
                        hints.add(hintCircle)
                        addTaskHint(hintCircle)
                    }
                }

            }
        }
    }
    /**
     * 移除任务点 #FFFF4140
     */
    fun removeTaskHints(hints:List<HintCircle>){
        hints.forEach {
            val dateTime = it.dateTime
            if (CalendarUtils.getInstance(this).removeTaskHint(dateTime.year,dateTime.monthOfYear-1,it)) {
                if (calendarComposeLayout.monthCalendarView.currentMonthView != null) {
                    calendarComposeLayout.monthCalendarView.currentMonthView.invalidate()
                }
                if (calendarComposeLayout.weekCalendarView.currentWeekView != null){
                    calendarComposeLayout.weekCalendarView.currentWeekView.invalidate()
                }
            }
        }
        this.hints.clear()
    }

    /**
     * 添加一个圆点提示
     *
     * @param day
     */
    fun addTaskHint(day: HintCircle) {
        val mCurrentSelectYear = day.dateTime.year
        val mCurrentSelectMonth = day.dateTime.monthOfYear -1
        CalendarUtils.getInstance(this).addTaskHint(mCurrentSelectYear, mCurrentSelectMonth, day)
        if (calendarComposeLayout.monthCalendarView.currentMonthView != null) {
            calendarComposeLayout.monthCalendarView.currentMonthView.invalidate()
        }
        if (calendarComposeLayout.weekCalendarView.currentWeekView != null){
            calendarComposeLayout.weekCalendarView.currentWeekView.invalidate()
        }
    }
    override fun onResume() {
        super.onResume()
        schoolCalendarViewModel.selectSemester()
    }

    /**
     * 初始化学期列表数据
     */
    fun initSemesterData(semesterList: List<SchoolSemesterRsp.DataBean>) {
        eventList.clear()
        semesterList.forEach {
            val dataBean = LeaveDeptRsp.DataBean()
            dataBean.deptId = it.id
            dataBean.deptName = it.schoolYear.plus(" ").plus(it.name)
            dataBean.isDefault = 0
            eventList.add(dataBean)
        }

        if (eventList.isNotEmpty()) {
            eventList[0].isDefault = 1
            schoolCalendarBinding.clSemester.visibility = View.VISIBLE
        } else {
            schoolCalendarBinding.clSemester.visibility = View.INVISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun initSemesterView() {
        val eventOptional = eventList.filter { it.isDefault == 1 }
        if (eventOptional.isNotEmpty()) {
            val dataBean = eventOptional[0]
            curSemesterId = dataBean.deptId
            selectSchoolCalendar()
            schoolCalendarBinding.tvSemester.text = dataBean.deptName
            if (eventList.size <= 1) {
                schoolCalendarBinding.tvSemester.setCompoundDrawables(null, null, null, null)
            } else {
                val drawable = ContextCompat.getDrawable(
                    this@SchoolCalendarActivity,
                    R.drawable.icon_gray_arrow_down
                )?.apply {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
                schoolCalendarBinding.tvSemester.setCompoundDrawables(null, null, drawable, null)
                schoolCalendarBinding.tvSemester.setOnClickListener {
                    val deptSelectPop = DeptSelectPop(this, 5, eventList)
                    deptSelectPop.setOnCheckedListener { dataBean ->
                        loge("选择的学期：$dataBean")
                        curSemesterId = dataBean.deptId
                        schoolCalendarBinding.tvSemester.text = dataBean.deptName
                        selectSchoolCalendar()
                    }
                }
            }
        }
    }

    private fun initView() {
        schoolCalendarBinding.top.title.text = "校历"
        schoolCalendarBinding.top.backLayout.setOnClickListener {
            finish()
        }
        schoolCalendarBinding.top.flTitle.setBackgroundColor(resources.getColor(R.color.school_calendar_bg_color))

        calendarComposeLayout = findViewById(R.id.calendarComposeLayout)
        rvScheduleList = calendarComposeLayout.rvScheduleList
        blankPage = calendarComposeLayout.blankPage
        swipeRefreshLayout = calendarComposeLayout.swipeRefreshLayout
        calendarComposeLayout.setOnCalendarClickListener(this)

        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        curDateTime = DateTime.now().simplifiedDataTime()
        schoolCalendarBinding.tvMonth.text = curDateTime.monthOfYear.toString().plus("月")

        schoolCalendarBinding.ivDateMinus.setOnClickListener {
            loge("月份减少")
            curDateTime = curDateTime.minusMonths(1)
            calendarComposeLayout.setSelectedData(curDateTime.year,curDateTime.monthOfYear-1,1)
        }

        schoolCalendarBinding.ivDateAdd.setOnClickListener {
            loge("月份增加")
            curDateTime = curDateTime.plusMonths(1)
            calendarComposeLayout.setSelectedData(curDateTime.year,curDateTime.monthOfYear-1,1)
        }

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvScheduleList.layoutManager = linearLayoutManager
        rvScheduleList.addItemDecoration(SpaceItemDecoration(DisplayUtils.dip2px(this,10f)))
        schoolCalendarAdapter = SchoolCalendarAdapter(schoolCalendarList)
        rvScheduleList.adapter = schoolCalendarAdapter
    }

    override fun getContentViewID(): Int = R.layout.activity_school_calendar

    override fun onClickDate(year: Int, month: Int, day: Int) {
        loge("onClickDate year=$year,month=$month,day=$day")
        scrollToPosition(year, month, day)
    }

    override fun onPageChange(year: Int, month: Int, day: Int) {
        loge("onPageChange year=$year,month=$month,day=$day")
        curDateTime = DateTime(year, month + 1, day, 0, 0, 0).simplifiedDataTime()
        update()
    }

    private fun update() {
        loge("curDateTime=${curDateTime.toStringTime("yyyy-MM-dd")}")
        val monthOfYear = "${curDateTime.monthOfYear}"
        schoolCalendarBinding.tvMonth.text = monthOfYear.plus("月")
        selectSchoolCalendar()
    }

    override fun onRefresh() {
        refresh = true
        selectSchoolCalendar()
    }

    /**
     * 请求校历数据列表
     */
    private fun selectSchoolCalendar(){
        schoolCalendarViewModel.selectSchoolCalendar(curSemesterId,curDateTime.monthOfYear.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        removeTaskHints(hints)
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
        dateTime.let {
            for (i in 0 until schoolCalendarList.size - 1) {
                val scheduleData1 = schoolCalendarList[i]
                val scheduleData2 = schoolCalendarList[i + 1]
                val dateTime1 = toDateTime((scheduleData1.startTime ?: "").plus(" 00:00:00")).simplifiedDataTime()
                val dateTime2 = toDateTime((scheduleData2.startTime ?: "").plus(" 00:00:00")).simplifiedDataTime()
                if (i != 0 && i != schoolCalendarList.size - 1 && it in dateTime1..dateTime2) {
                    loge("----找到定位的位置----")
                    if (scheduleData1.type != 1) {
                        scrollOuter = i
                        return@let
                    }
                    return@let
                }
            }
        }
        loge("需要滚动到 scrollOuter=$scrollOuter,dateTime=$dateTime")
        moveToPosition(scrollOuter, rvScheduleList)
    }
}