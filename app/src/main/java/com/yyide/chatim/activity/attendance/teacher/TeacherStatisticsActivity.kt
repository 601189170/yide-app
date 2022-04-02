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
import com.yyide.chatim.utils.TimeUtil
import com.yyide.chatim.utils.asColor
import com.yyide.chatim.utils.hide
import com.yyide.chatim.utils.logd
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
        val bean = MonthDayBean()
        bean.year = date.get(java.util.Calendar.YEAR).toString()
        bean.month = (date.get(java.util.Calendar.MONTH) + 1).toString()
        bean.day = date.get(java.util.Calendar.DAY_OF_MONTH).toString()
        viewModel.setDate(bean)
        timePopUp.setMillisecond(date.timeInMillis)

        viewModel.requestClockRecordByMonth("${bean.year}-${bean.month}")


        punchAdapter = object :
            BaseQuickAdapter<SignTimeItem, BaseViewHolder>(R.layout.item_punch_list) {
            override fun convert(holder: BaseViewHolder, item: SignTimeItem) {
                val viewBind = ItemPunchListBinding.bind(holder.itemView)
                if (holder.absoluteAdapterPosition == 0) {
                    viewBind.lineTop.hide()
                }
                if (holder.absoluteAdapterPosition == punchAdapter.data.size - 1) {
                    viewBind.lineBottom.hide()
                }
                viewBind.tvItemPunchTitle.text = "${item.signTime}${item.signResult}"
                viewBind.tvItemPunchSub.text = item.address
            }
        }

        binding.rvTeacherAttendancePunchList.layoutManager = LinearLayoutManager(this)
        binding.rvTeacherAttendancePunchList.adapter = punchAdapter

        viewModel.date.observe(this) {

            binding.tvTeacherAttendanceStatisticsTime.text = "${it.year}年${it.month}月"
            binding.tvTeacherAttendanceStatisticsTimeTitle.text =
                String.format(getString(R.string.statistics_time_pool), "${it.month}月")
        }

        viewModel.monthRecordData.observe(this) {
            val monthData = it.getOrNull()
            monthData?.let { monthInfo ->
                binding.tvTeacherAttendanceAbsence.text = monthInfo.absenceFromWorkCount.toString()
                binding.tvTeacherAttendanceLate.text = monthInfo.beLateCount.toString()
                binding.tvTeacherAttendanceLeaveEarly.text = monthInfo.leaveEarlyCount.toString()
                binding.tvTeacherAttendanceLeave.text = monthInfo.askForLeaveCount.toString()
                parseSchemeDate(monthInfo.dailyRecord)
            }

        }


        viewModel.dayRecordData.observe(this) {
            val dayData = it.getOrNull()
            dayData?.let { dayInfo ->
                binding.tvTeacherAttendanceRule.text =
                    "规则: ${dayInfo.attendanceSchedulingDescription}"

                punchAdapter.setList(dayInfo.signRecordList)
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

        timePopUp.setSubmitCallBack(object : TimePopUp.SubmitCallBack {
            override fun getSubmitData(data: MonthDayBean) {
                viewModel.setDate(data)
            }
        })


        binding.ivTeacherAttendanceCalendarExpand.setOnClickListener {
            if (binding.calendarLayout.isExpand) {
                binding.calendarLayout.shrink()
            } else {
                binding.calendarLayout.expand()
            }
        }

        binding.calendarView.setOnViewChangeListener {
            if (it) {
                binding.ivTeacherAttendanceCalendarExpand.setImageResource(R.drawable.calendar_up_icon)
            } else {
                binding.ivTeacherAttendanceCalendarExpand.setImageResource(R.drawable.calendar_down_icon)
            }
        }

        binding.calendarView.setOnCalendarSelectListener(object : CalendarView.OnCalendarSelectListener{
            override fun onCalendarOutOfRange(calendar: Calendar?) {
                logd("out of range")
            }

            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                calendar?.let {
                    if (isClick){
                        val requestStr = "${calendar.year}-${calendar.month}-${calendar.day}"
                        viewModel.requestClockRecordByDay(requestStr)
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
            if (record.restDay) {
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
        if (dailyRecord.hasAbsenceFromWork) {
            return R.color.not_punch_color.asColor()
        }

        if (!dailyRecord.hasRecord) {
            return R.color.not_punch_color.asColor()
        }

        if (dailyRecord.hasAskForLeave) {
            return R.color.not_punch_color.asColor()
        }

        if (dailyRecord.hasLeaveEarly) {
            return R.color.leave_early.asColor()
        }

        if (dailyRecord.hasBeLate) {
            return R.color.late.asColor()
        }

        return R.color.punch_normal.asColor()
    }

    override fun onDestroy() {
        super.onDestroy()
        timePopUp.setSubmitCallBack(null)
    }

}