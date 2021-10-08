package com.yyide.chatim.activity.weekly.home

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.weekly.WeeklyDetailsActivity
import com.yyide.chatim.activity.weekly.details.adapter.ClassAdapter
import com.yyide.chatim.activity.weekly.details.adapter.DateAdapter
import com.yyide.chatim.activity.weekly.home.viewmodel.ParentsViewModel
import com.yyide.chatim.activity.weekly.home.viewmodel.TeacherViewModel
import com.yyide.chatim.adapter.AttendanceAdapter
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentParentsWeeklyBinding
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.model.*
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.InitPieChart
import java.util.*

/**
 *
 * 家长周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
open class ParentsWeeklyFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentParentsWeeklyBinding
    private val viewModel: ParentsViewModel by viewModels()
    private var studentId: String = ""
    private lateinit var dateTime: WeeklyDateBean.DataBean.TimeBean

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentParentsWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ParentsWeeklyFragment().apply {}
    }

    private fun initView() {
        if (SpData.getClassInfo() != null && !TextUtils.isEmpty(SpData.getClassInfo().studentId)) {
            studentId = SpData.getClassInfo().studentId
        }
        viewModel.parentsLiveData.observe(viewLifecycleOwner) {
            val result = it.getOrNull()
            if (null != result) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData.visibility = View.GONE
                setSummary(result.summary)
                //考勤统计
                if (result.attend.isNotEmpty()) {
                    initAttendance(result.attend[0])
                    adapterAttendance.setList(result.attend)
                }
                //教师评语
                viewBinding.comments.tvWeeklyAttendance.text = result.eval
            } else {//接口返回空的情况处理
                viewBinding.clContent.visibility = View.GONE
                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }
        //作业统计
        initStatistical()
        //教师评语
        initComments()
        initDate()
        initClassMenu()
    }

    private fun request() {
        viewModel.requestParentsWeekly(studentId, dateTime.startTime, dateTime.endTime)
    }

    private fun setSummary(summary: WeeklyParentsSummary?) {
        if (summary != null) {
            viewBinding.summary.tvWeeklyAttendance.text = summary.attend
            viewBinding.summary.tvWeeklyHomework.text = summary.work
            viewBinding.summary.tvWeeklyShopping.text = summary.expend
        }
    }

    private fun initDate() {
        //获取日期时间
        dateTime = WeeklyUtil.getDateTime()!!
        if (dateTime != null) {
            val time = getString(
                R.string.startTime_endTime, DateUtils.formatTime(
                    dateTime.startTime,
                    "yyyy-MM-dd HH:mm:ss",
                    "MM/dd"
                ), DateUtils.formatTime(
                    dateTime.endTime,
                    "yyyy-MM-dd HH:mm:ss",
                    "MM/dd"
                )
            )
            viewBinding.tvTime.text = time
            viewBinding.attendance.tvAttendanceTime.text = time
        }
        request()
        val dateLists = WeeklyUtil.getDateTimes()
        val adapterDate = DateAdapter()
        if (dateLists.isNotEmpty()) {
            adapterDate.setList(dateLists)
        }
        viewBinding.tvTime.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterDate, "请选择时间")
            attendancePop.setOnSelectListener { index: Int ->
                //                indexDate = index
                val time = getString(
                    R.string.startTime_endTime, DateUtils.formatTime(
                        dateLists[index].startTime,
                        "yyyy-MM-dd HH:mm:ss",
                        "MM/dd"
                    ), DateUtils.formatTime(
                        dateLists[index].endTime,
                        "yyyy-MM-dd HH:mm:ss",
                        "MM/dd"
                    )
                )
                viewBinding.tvTime.text = time
                viewBinding.attendance.tvAttendanceTime.text = time
                request()
            }
        }
    }

    private fun initClassMenu() {
        if (SpData.getClassInfo() != null && !TextUtils.isEmpty(SpData.getClassInfo().studentName)) {
            viewBinding.tvEvent.text = SpData.getClassInfo().studentName
        }
        val classList = SpData.getClassList()
        if (classList != null) {
            if (classList.size > 1) {
                viewBinding.tvEvent.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    resources.getDrawable(R.mipmap.icon_down),
                    null
                )
                viewBinding.tvEvent.setOnClickListener {
                    val attendancePop = AttendancePop(activity, adapterEvent, "请选择班级")
                    attendancePop.setOnSelectListener { index: Int ->
                        viewBinding.tvEvent.text = adapterEvent.getItem(index).studentName
                        studentId = adapterEvent.getItem(index).studentId
                        request()
                    }

                }
            } else {
                viewBinding.tvEvent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            adapterEvent.setList(classList)
        }
    }

    private fun initAttendance(attend: WeeklyParentsAttend?) {
        if (attend != null) {
            val piechart: PieChart = viewBinding.attendance.piechart
            piechart.setTouchEnabled(false)
            InitPieChart.InitPieChart(activity, piechart)
            val entries: MutableList<PieEntry> = ArrayList()
            entries.add(PieEntry(attend.earlyNumber.toFloat(), "早退"))
            entries.add(PieEntry(attend.leaveNumber.toFloat(), "请假"))
            entries.add(PieEntry(attend.lateNumber.toFloat(), "迟到"))
            entries.add(PieEntry(attend.abNumber.toFloat(), "缺勤"))
            piechart.centerText = "${attend.rate}%\n出勤率"
            piechart.setCenterTextSize(12f)
            val dataSet = PieDataSet(entries, "")
            dataSet.sliceSpace = 0f //设置饼块之间的间隔

            dataSet.setColors(*PIE_COLORS2) //设置饼块的颜色

            dataSet.setDrawValues(false)
            val pieData = PieData(dataSet)
            piechart.data = pieData
            piechart.invalidate()
            viewBinding.attendance.cd.text = "${attend.lateNumber}次"
            viewBinding.attendance.qq.text = "${attend.abNumber}次"
            viewBinding.attendance.qj.text = "${attend.leaveNumber}次"
            viewBinding.attendance.zt.text = "${attend.earlyNumber}次"
            val desc =
                if (attend.lateNumber <= 0 && attend.abNumber <= 0 && attend.leaveNumber <= 0 && attend.earlyNumber <= 0) {
                    "无"
                } else {
                    "有"
                }
            viewBinding.attendance.tcAttendDescs.text =
                getString(R.string.weekly_attend_desc, attend.name, desc)
            viewBinding.attendance.tvEventName.text = attend.name
            viewBinding.attendance.tvEventName.setOnClickListener {
                val attendancePop = AttendancePop(activity, adapterAttendance, "请选择考勤事件")
                attendancePop.setOnSelectListener { index: Int ->
                    indexAttendance = index
                    val item = adapterAttendance.getItem(index)
                    viewBinding.attendance.tvEventName.text = item.name
                    initAttendance(item)
                    adapterAttendance.notifyDataSetChanged()
                }
            }
            viewBinding.attendance.cardView.setOnClickListener {
                WeeklyDetailsActivity.jump(
                    mActivity,
                    WeeklyDetailsActivity.PARENT_ATTENDANCE_TYPE,
                    studentId,
                    viewBinding.tvEvent.text.toString().trim()
                )
            }
        }
    }

    //    -->设置各区块的颜色
    val PIE_COLORS2 = intArrayOf(
        Color.rgb(61, 189, 134),
        Color.rgb(246, 189, 22),
        Color.rgb(246, 108, 108),
        Color.rgb(44, 138, 255)
    )

    //    -->设置各区块的颜色
    val PIE_COLORS = intArrayOf(
        Color.rgb(44, 138, 255),
        Color.rgb(115, 222, 179)
    )

    val PIE_COLORS3 = intArrayOf(
        Color.rgb(44, 138, 255),
        Color.rgb(115, 222, 179),
        Color.rgb(98, 112, 146)
    )

    private fun initStatistical() {
        val piechart: PieChart = viewBinding.homeworkStatistical.piechart
        piechart.setTouchEnabled(false)
        InitPieChart.InitPieChart(activity, piechart)
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(30f, "已完成"))
        entries.add(PieEntry(20f, "未完成"))
        piechart.centerText = "完成情况"
        piechart.setCenterTextSize(11f)
        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 0f //设置饼块之间的间隔

        dataSet.setColors(*PIE_COLORS) //设置饼块的颜色

        dataSet.setDrawValues(false)
        val pieData = PieData(dataSet)
        piechart.data = pieData
        piechart.invalidate()
        viewBinding.homeworkStatistical.tvComplete.text = "30"
        viewBinding.homeworkStatistical.tvUnComplete.text = "20"

        val piechart2: PieChart = viewBinding.homeworkStatistical.piechart2
        piechart2.setTouchEnabled(false)
        InitPieChart.InitPieChart(activity, piechart2)
        val entries2: MutableList<PieEntry> = ArrayList()
        entries2.add(PieEntry(10f, "很简单"))
        entries2.add(PieEntry(20f, "一般"))
        entries2.add(PieEntry(30f, "有难度"))
        piechart2.centerText = "难易程度"
        piechart2.setCenterTextSize(11f)
        val dataSet2 = PieDataSet(entries2, "")
        dataSet2.sliceSpace = 0f //设置饼块之间的间隔
        dataSet2.setColors(*PIE_COLORS3) //设置饼块的颜色
        dataSet2.setDrawValues(false)
        val pieData2 = PieData(dataSet2)
        piechart2.data = pieData2
        piechart2.invalidate()
        viewBinding.homeworkStatistical.tvSimple.text = "10"
        viewBinding.homeworkStatistical.tvGeneral.text = "20"
        viewBinding.homeworkStatistical.tvHaveDifficulty.text = "30"
    }

    private fun initComments() {
        viewBinding.comments.recyclerview.layoutManager = LinearLayoutManager(activity)
        viewBinding.comments.recyclerview.adapter = commentsAdapter
    }

    private val commentsAdapter = object :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_weekly_teacher_comments) {
        override fun convert(holder: BaseViewHolder, item: String) {

        }
    }

    private var index = 0
    private val adapterEvent = object :
        BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(holder: BaseViewHolder, item: GetUserSchoolRsp.DataBean.FormBean) {
            holder.setText(R.id.className, item.studentName)
            holder.getView<View>(R.id.select).visibility =
                if (index == holder.adapterPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.adapterPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
            }
        }
    }

    private var indexAttendance = 0
    private val adapterAttendance = object :
        BaseQuickAdapter<WeeklyParentsAttend, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(holder: BaseViewHolder, item: WeeklyParentsAttend) {
            holder.setText(R.id.className, item.name)
            holder.getView<View>(R.id.select).visibility =
                if (indexAttendance == holder.absoluteAdapterPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.absoluteAdapterPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
            }
        }
    }
}