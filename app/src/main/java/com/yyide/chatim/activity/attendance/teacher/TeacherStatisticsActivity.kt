package com.yyide.chatim.activity.attendance.teacher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.yyide.chatim.R
import com.yyide.chatim.activity.attendance.teacher.viewmodel.TeacherStatisticsViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityAttendanceV2StatisticsBinding
import com.yyide.chatim.databinding.ItemPunchListBinding
import com.yyide.chatim.dialog.TimePopUp
import com.yyide.chatim.model.attendance.teacher.DailyRecordItem
import com.yyide.chatim.model.attendance.teacher.MonthDayBean
import com.yyide.chatim.model.attendance.teacher.SignTimeItem
import com.yyide.chatim.utils.*
import razerdp.basepopup.BasePopupWindow


/**
 *
 * @author shenzhibin
 * @date 2022/3/28 20:19
 * @description 描述
 */
class TeacherStatisticsActivity :
    KTBaseActivity<ActivityAttendanceV2StatisticsBinding>(ActivityAttendanceV2StatisticsBinding::inflate) {

    private lateinit var punchAdapter: BaseQuickAdapter<SignTimeItem, BaseViewHolder>

    private val viewModel by viewModels<TeacherStatisticsViewModel>()

    companion object {
        fun startGo(context: Context) {
            val intent = Intent(context, TeacherStatisticsActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val timePopUp: TimePopUp by lazy {
        TimePopUp(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()

    }

    override fun initView() {
        super.initView()

        binding.teacherAttendanceStatisticsTop.title.text = "统计"


        val date = java.util.Calendar.getInstance()
        val currentBean = MonthDayBean()
        currentBean.year = date.get(java.util.Calendar.YEAR)
        currentBean.month = (date.get(java.util.Calendar.MONTH) + 1)
        currentBean.day = date.get(java.util.Calendar.DAY_OF_MONTH)
        viewModel.setDate(currentBean)
        timePopUp.setMillisecond(date.timeInMillis)

        binding.calendarView.setWeekStarWithMon()


        punchAdapter = object :
            BaseQuickAdapter<SignTimeItem, BaseViewHolder>(R.layout.item_punch_list) {
            override fun convert(holder: BaseViewHolder, item: SignTimeItem) {
                val viewBind = ItemPunchListBinding.bind(holder.itemView)
                if (holder.absoluteAdapterPosition == 0) {
                    viewBind.lineTop.hide()
                }else{
                    viewBind.lineTop.show()
                }
                if (holder.absoluteAdapterPosition == punchAdapter.data.size - 1) {
                    viewBind.lineBottom.hide()
                }else{
                    viewBind.lineBottom.show()
                }
                val signType = if (item.signType == 0) getString(R.string.sign_in) else getString(R.string.sign_out)
                val signStr = if (item.signResult == "未打卡") "$signType${getString(R.string.not_punched)}" else "已打卡"
                viewBind.tvItemPunchTitle.text = "${item.signTime} $signStr"

                when {
                    item.signResult == "迟到" || item.signResult == "早退" -> {
                        viewBind.tvItemPunchState.text = item.signResult
                        viewBind.tvItemPunchState.setTextColor(R.color.late.asColor())
                        viewBind.tvItemPunchState.setBackgroundResource(R.drawable.bg_yellow_2)
                    }
                    item.signInOutside -> {
                        viewBind.tvItemPunchState.text = "外勤"
                        viewBind.tvItemPunchState.setTextColor(R.color.white.asColor())
                        viewBind.tvItemPunchState.setBackgroundResource(R.mipmap.attendance_outside_bg)
                    }
                    /*item.signResult == "早退" -> {
                        viewBind.tvItemPunchState.text = "早退"
                        viewBind.tvItemPunchState.setTextColor(R.color.leave_early.asColor())
                        viewBind.tvItemPunchState.setBackgroundResource(R.drawable.bg_rad_2)
                    }*/
                    item.signResult == "未打卡" -> {
                        viewBind.tvItemPunchState.text = "未打卡"
                        viewBind.tvItemPunchState.setTextColor(R.color.not_punch_color.asColor())
                        viewBind.tvItemPunchState.setBackgroundResource(R.drawable.bg_black_2)
                    }
                    else -> {
                        viewBind.tvItemPunchState.text = ""
                        viewBind.tvItemPunchState.background = null
                    }
                }

                if (item.address.isNotEmpty()) {
                    viewBind.tvItemPunchSub.show()
                    viewBind.tvItemPunchSub.text = item.address
                } else {
                    viewBind.tvItemPunchSub.hide()
                }

            }
        }

        binding.rvTeacherAttendancePunchList.layoutManager = LinearLayoutManager(this)
        binding.rvTeacherAttendancePunchList.adapter = punchAdapter

        viewModel.date.observe(this) {
            val requestStr = "${it.year}年${it.month}月"
            binding.tvTeacherAttendanceStatisticsTime.text = requestStr
            binding.tvTeacherAttendanceStatisticsTimeTitle.text =
                String.format(getString(R.string.statistics_time_pool), "${it.month}月")
            showLoading()
            viewModel.requestClockRecordByMonth("${it.year}-${it.month}")
        }

        viewModel.monthRecordData.observe(this) {
            hideLoading()
            val monthData = it.getOrNull() ?: return@observe

            binding.tvTeacherAttendanceAbsence.text = monthData.absenceFromWorkCount.toString()
            binding.tvTeacherAttendanceLate.text = monthData.beLateCount.toString()
            binding.tvTeacherAttendanceLeaveEarly.text = monthData.leaveEarlyCount.toString()
            binding.tvTeacherAttendanceLeave.text = monthData.askForLeaveCount.toString()

            viewModel.dailyRecordList.clear()
            viewModel.dailyRecordList.addAll(monthData.dailyRecord)

            viewModel.date.value?.let { bean ->
                val monthStr = DateUtils.judgeIsNeedAddZero(bean.month.toString())
                val dayStr = DateUtils.judgeIsNeedAddZero(bean.day.toString())
                val requestStr = "${bean.year}-${monthStr}-${dayStr}"
                val todayRecord = monthData.dailyRecord.find { recordItem -> recordItem.aboutDate == requestStr }
                binding.teacherAttendanceEmpty.tvDesc.setTextColor(R.color.text_A60.asColor())
                if (todayRecord == null) {
                    binding.rvTeacherAttendancePunchList.hide()
                    binding.teacherAttendanceEmpty.root.show()
                    binding.teacherAttendanceEmpty.tvDesc.text = "暂无数据"
                    binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.icon_no_data)
                    return@let
                }
                binding.tvTeacherAttendanceRule.text = todayRecord.dailyRuleDescription
                if (!todayRecord.hasScheduling) {
                    binding.rvTeacherAttendancePunchList.hide()
                    binding.teacherAttendanceEmpty.root.show()
                    binding.teacherAttendanceEmpty.tvDesc.text = "当天无排班"
                    binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.attendance_off_day)
                } else if (!todayRecord.hasRecord) {
                    binding.rvTeacherAttendancePunchList.hide()
                    binding.teacherAttendanceEmpty.root.show()
                    when {
                        todayRecord.restDay -> {
                            binding.teacherAttendanceEmpty.tvDesc.text = "今天休息日"
                            binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.attendance_off_day)
                        }
                        else -> {
                            binding.teacherAttendanceEmpty.tvDesc.text = "当天无打卡记录"
                            binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.attendance_not_punch)
                        }
                    }
                } else {
                    binding.rvTeacherAttendancePunchList.show()
                    binding.teacherAttendanceEmpty.root.hide()
                    punchAdapter.setList(todayRecord.signTime)
                }
            }
            parseSchemeDate(monthData.dailyRecord)
        }


        viewModel.dayRecordData.observe(this) {
            val dayData = it.getOrNull()
            binding.teacherAttendanceEmpty.tvDesc.setTextColor(R.color.text_A60.asColor())
            if (dayData == null) {
                binding.rvTeacherAttendancePunchList.hide()
                binding.teacherAttendanceEmpty.root.show()
                binding.teacherAttendanceEmpty.tvDesc.text = "暂无数据"
                binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.icon_no_data)
                return@observe
            }
            binding.tvTeacherAttendanceRule.text = dayData.dailyRuleDescription
            binding.tvTeacherAttendanceRule.text = dayData.dailyRuleDescription
            if (!dayData.hasScheduling) {
                binding.rvTeacherAttendancePunchList.hide()
                binding.teacherAttendanceEmpty.root.show()
                binding.teacherAttendanceEmpty.tvDesc.text = "当天无排班"
                binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.attendance_off_day)
            } else if (!dayData.hasRecord) {
                binding.rvTeacherAttendancePunchList.hide()
                binding.teacherAttendanceEmpty.root.show()
                when {
                    dayData.restDay -> {
                        binding.teacherAttendanceEmpty.tvDesc.text = "今天休息日"
                        binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.attendance_off_day)
                    }
                    else -> {
                        binding.teacherAttendanceEmpty.tvDesc.text = "当天无打卡记录"
                        binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.attendance_not_punch)
                    }
                }
            } else {
                binding.rvTeacherAttendancePunchList.show()
                binding.teacherAttendanceEmpty.root.hide()
                punchAdapter.setList(dayData.signTime)
            }

        }


    }

    private fun initListener() {
        binding.teacherAttendanceStatisticsTop.backLayout.setOnClickListener {
            finish()
        }

        binding.tvTeacherAttendanceStatisticsTime.setOnClickListener {
            if (timePopUp.isShowing) {
                timePopUp.dismiss()
            } else {
                binding.tvTeacherAttendanceStatisticsTime.setTextColor(R.color.colorAccent.asColor())
                binding.ivTeacherAttendanceStatisticsTimeLogo.setImageResource(R.mipmap.table_week_button_pack)
                timePopUp.showPopupWindow(it)
            }
        }

        binding.ivTeacherAttendanceStatisticsTimeLogo.setOnClickListener {
            if (timePopUp.isShowing) {
                timePopUp.dismiss()
            } else {
                binding.tvTeacherAttendanceStatisticsTime.setTextColor(R.color.colorAccent.asColor())
                binding.ivTeacherAttendanceStatisticsTimeLogo.setImageResource(R.mipmap.table_week_button_pack)
                timePopUp.showPopupWindow(it)
            }

        }

        timePopUp.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                binding.tvTeacherAttendanceStatisticsTime.setTextColor(R.color.black10.asColor())
                binding.ivTeacherAttendanceStatisticsTimeLogo.setImageResource(R.mipmap.table_week_button)
            }
        }



        binding.ivTeacherAttendanceCalendarExpand.setOnClickListener {
            if (binding.calendarLayout.isExpand) {
                binding.calendarLayout.shrink()
            } else {
                binding.calendarLayout.expand()
            }
        }

        timePopUp.setSubmitCallBack(object : TimePopUp.SubmitCallBack {
            override fun getSubmitData(data: MonthDayBean) {
                var day = 1
                if (data.month == binding.calendarView.curMonth) {
                    day = binding.calendarView.curDay
                }
                data.day = day
                binding.calendarView.scrollToCalendar(data.year, data.month, data.day)
            }
        })

        binding.calendarView.setOnMonthChangeListener { year, month ->
            var day = 1
            if (month == binding.calendarView.curMonth) {
                day = binding.calendarView.curDay
            }
            val data = MonthDayBean(
                year, month,
                day
            )
            viewModel.setDate(data)
        }

        binding.calendarView.setOnViewChangeListener {
            if (it) {
                binding.ivTeacherAttendanceCalendarExpand.setImageResource(R.drawable.calendar_up_icon)
            } else {
                binding.ivTeacherAttendanceCalendarExpand.setImageResource(R.drawable.calendar_down_icon)
            }
        }

        binding.calendarView.setOnCalendarSelectListener(object :
            CalendarView.OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar?) {
                logd("out of range")
            }

            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                calendar?.let {
                    logd("onCalendarSelect ${calendar.day}")
                    if (isClick) {
                        val monthStr = DateUtils.judgeIsNeedAddZero(calendar.month.toString())
                        val dayStr = DateUtils.judgeIsNeedAddZero(calendar.day.toString())
                        val requestStr = "${calendar.year}-${monthStr}-${dayStr}"
                        val todayRecord = viewModel.dailyRecordList.find { recordItem -> recordItem.aboutDate == requestStr }
                        binding.teacherAttendanceEmpty.tvDesc.setTextColor(R.color.text_A60.asColor())
                        if (todayRecord == null) {
                            binding.rvTeacherAttendancePunchList.hide()
                            binding.teacherAttendanceEmpty.root.show()
                            binding.teacherAttendanceEmpty.tvDesc.text = "暂无数据"
                            binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.icon_no_data)
                            return@let
                        }
                        binding.tvTeacherAttendanceRule.text = todayRecord.dailyRuleDescription
                        if (!todayRecord.hasScheduling) {
                            binding.rvTeacherAttendancePunchList.hide()
                            binding.teacherAttendanceEmpty.root.show()
                            binding.teacherAttendanceEmpty.tvDesc.text = "当天无排班"
                            binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.attendance_off_day)
                        } else if (!todayRecord.hasRecord) {
                            binding.rvTeacherAttendancePunchList.hide()
                            binding.teacherAttendanceEmpty.root.show()
                            when {
                                todayRecord.restDay -> {
                                    binding.teacherAttendanceEmpty.tvDesc.text = "今天休息日"
                                    binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.attendance_off_day)
                                }
                                else -> {
                                    binding.teacherAttendanceEmpty.tvDesc.text = "当天无打卡记录"
                                    binding.teacherAttendanceEmpty.imageView2.setImageResource(R.mipmap.attendance_not_punch)
                                }
                            }
                        } else {
                            binding.rvTeacherAttendancePunchList.show()
                            binding.teacherAttendanceEmpty.root.hide()
                            punchAdapter.setList(todayRecord.signTime)
                        }
                        //viewModel.requestClockRecordByDay(requestStr)
                    }
                }
            }

        })

    }

    /**
     * 分析每月的标志事件
     * @param dailyRecord List<DailyRecordItem>?
     */
    private fun parseSchemeDate(dailyRecord: List<DailyRecordItem>?) {
        if (dailyRecord == null) {
            return
        }
        val map: MutableMap<String, Calendar> = HashMap()
        for (record in dailyRecord) {

            if (!record.hasScheduling ||
                (record.restDay && !record.hasRecord) ||
                DateUtils.compareDate(record.aboutDate)
            ) {
                continue
            }

            val longTime = TimeUtil.getTime10(record.aboutDate)
            val calendar = java.util.Calendar.getInstance()
            calendar.timeInMillis = longTime

            val color = judgeShowColor(record)

            val schemeCal = getSchemeCalendar(
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH) + 1,
                calendar.get(java.util.Calendar.DAY_OF_MONTH),
                color
            )
            map[schemeCal.toString()] = schemeCal
        }
        binding.calendarView.setSchemeDate(map)
    }

    private fun getSchemeCalendar(
        year: Int,
        month: Int,
        day: Int,
        color: Int,
        text: String = ""
    ): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        return calendar
    }

    private fun judgeShowColor(dailyRecord: DailyRecordItem): Int {


        if (dailyRecord.hasAbnormal || dailyRecord.hasBeLate || dailyRecord.hasLeaveEarly) {
            return R.color.late.asColor()
        }

        if (dailyRecord.hasAskForLeave) {
            return R.color.not_punch_color.asColor()
        }

        if (dailyRecord.hasAbsenceFromWork) {
            return R.color.leave_early.asColor()
        }

        return R.color.punch_normal.asColor()
    }

    override fun onDestroy() {
        super.onDestroy()
        timePopUp.setSubmitCallBack(null)
    }

}