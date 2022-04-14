package com.yyide.chatim_pro.activity.weekly.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.activity.weekly.WeeklyDetailsActivity
import com.yyide.chatim_pro.activity.weekly.home.viewmodel.ParentsViewModel
import com.yyide.chatim_pro.base.BaseFragment
import com.yyide.chatim_pro.databinding.FragmentParentsWeeklyBinding
import com.yyide.chatim_pro.databinding.ItemWeeklyAttendanceBinding
import com.yyide.chatim_pro.dialog.AttendancePop
import com.yyide.chatim_pro.model.*
import com.yyide.chatim_pro.utils.DateUtils
import com.yyide.chatim_pro.utils.InitPieChart
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
    private var classInfo = SpData.getClassInfo()

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
        if (classInfo != null && !TextUtils.isEmpty(classInfo.studentId)) {
            studentId = classInfo.studentId
        }
        viewBinding.clContent.visibility = View.GONE
        viewModel.parentsLiveData.observe(viewLifecycleOwner) {
            dismiss()
            val result = it.getOrNull()
            if (null != result) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData.visibility = View.GONE
                viewBinding.summary.tvNoData.visibility = View.GONE
                setSummary(result.summary)
                //考勤统计
                if (result.attend != null && result.attend.isNotEmpty()) {
                    initAttendance(result.attend[0])
                    adapterAttendance.setList(result.attend)
                    viewBinding.attendance.tvEventName.visibility = View.VISIBLE
                    viewBinding.attendance.clAttend.visibility = View.VISIBLE
                    viewBinding.attendance.ivNoData.visibility = View.GONE
                } else {
                    viewBinding.attendance.tcAttendDescs.text = "你的本周考勤无异常记录，太棒啦～"
                    viewBinding.attendance.tvEventName.visibility = View.INVISIBLE
                    viewBinding.attendance.clAttend.visibility = View.GONE
                    viewBinding.attendance.ivNoData.visibility = View.VISIBLE
                }

                if (result.eval != null) {
                    setTeacherComments(result.eval)
                }

                if (!viewBinding.attendance.clAttend.isShown
                    && !viewBinding.summary.root.isShown
                    && !viewBinding.comments.root.isShown
                ) {
                    viewBinding.attendance.root.visibility = View.GONE
                    viewBinding.cardViewNoData.visibility = View.VISIBLE
                } else {
                    viewBinding.attendance.root.visibility = View.VISIBLE
                    viewBinding.cardViewNoData.visibility = View.GONE
                }
            } else {//接口返回空的情况处理
                viewBinding.clContent.visibility = View.GONE
                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }

        viewBinding.homeworkStatistical.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(
                mActivity,
                WeeklyDetailsActivity.PARENT_HOMEWORK_TYPE,
                "",
                "",
                dateTime,
                classInfo
            )
        }

        viewBinding.comments.root.setOnClickListener {
            WeeklyDetailsActivity.jump(
                mActivity,
                WeeklyDetailsActivity.PARENT_ATTENDANCE_TYPE,
                studentId,
                viewBinding.tvEvent.text.toString().trim(),
                dateTime,
                classInfo
            )
        }

        //作业统计
        initStatistical()
        //教师评语
        initComments()
        initDate()
        initClassMenu()
    }

    private fun request() {
        setTime()
        loading()
        viewModel.requestParentsWeekly(studentId, dateTime.startTime, dateTime.endTime)
    }

    @SuppressLint("SetTextI18n")
    private fun setTime() {
        viewBinding.tvStartTime.text = DateUtils.formatTime(
            dateTime.startTime,
            "yyyy-MM-dd HH:mm:ss",
            "MM/dd"
        )
        viewBinding.tvEndTime.text = DateUtils.formatTime(
            dateTime.endTime,
            "yyyy-MM-dd HH:mm:ss",
            "MM/dd"
        )
        viewBinding.attendance.tvAttendanceTime.text = DateUtils.formatTime(
            dateTime.startTime,
            "yyyy-MM-dd HH:mm:ss",
            "MM/dd"
        ) + "-" +
                DateUtils.formatTime(
                    dateTime.endTime,
                    "yyyy-MM-dd HH:mm:ss",
                    "MM/dd"
                )
    }

    private fun setTeacherComments(eval: Eval?) {
        //教师评语
        viewBinding.comments.root.visibility = View.VISIBLE
        if (eval != null) {
            viewBinding.comments.tvWeeklyAttendance.text = eval.body
            when (eval.level) {//1:非常优秀、2:比较优秀、3：较好
                1 -> {
                    viewBinding.comments.ivBg.setBackgroundResource(R.mipmap.icon_weekly_teacher_commnets)
                }
                2 -> {
                    viewBinding.comments.ivBg.setBackgroundResource(R.mipmap.icon_weekly_teacher_commnets_good)
                }
                else -> {
                    viewBinding.comments.ivBg.setBackgroundResource(R.mipmap.icon_weekly_teacher_commnets_general)
                }
            }
        }

        if (eval != null && TextUtils.isEmpty(eval.body)) {
            viewBinding.comments.root.visibility = View.GONE
        }
    }

    private fun setSummary(summary: WeeklyParentsSummary?) {
        if (summary != null) {
            viewBinding.summary.root.visibility = View.VISIBLE
            viewBinding.summary.tvWeeklyAttendance.text = summary.attend
            viewBinding.summary.tvWeeklyHomework.text = summary.work
            viewBinding.summary.tvWeeklyShopping.text = summary.expend
        }
        if (summary != null
            && TextUtils.isEmpty(summary.attend)
            && TextUtils.isEmpty(summary.expend)
            && TextUtils.isEmpty(summary.work)
        ) {
            viewBinding.summary.root.visibility = View.GONE
        }
    }

    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
    private var timePosition = -1
    private fun initDate() {
        //获取日期时间
        val dateLists = WeeklyUtil.getDateTimes()
        if (dateLists.isNotEmpty()) {
            timePosition = dateLists.size - 1
            dateTime = dateLists[dateLists.size - 1]
            request()
            viewBinding.tvStartTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition > 0) {
                        timePosition -= 1
                        dateTime = dateLists[timePosition]
                        request()
                    }
                }
            }
            viewBinding.tvEndTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition < (dateLists.size - 1)) {
                        timePosition += 1
                        dateTime = dateLists[timePosition]
                        request()
                    }
                }
            }
        }
    }

    private fun initClassMenu() {
        if (SpData.getClassInfo() != null && !TextUtils.isEmpty(SpData.getClassInfo().studentName)) {
            viewBinding.tvEvent.text = SpData.getClassInfo().studentName + "的周报"
            studentName = SpData.getClassInfo().studentName
        } else {
            viewBinding.tvEvent.text = "暂无周报"
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
                    val attendancePop = AttendancePop(activity, adapterEvent, "请选择学生")
                    attendancePop.setOnSelectListener { index: Int ->
                        viewBinding.tvEvent.text = adapterEvent.getItem(index).studentName + "的周报"
                        classInfo = adapterEvent.getItem(index)
                        studentName = adapterEvent.getItem(index).studentName
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
            entries.add(PieEntry(attend.lateNumber.toFloat(), "迟到"))
            entries.add(PieEntry(attend.earlyNumber.toFloat(), "早退"))
            entries.add(PieEntry(attend.leaveNumber.toFloat(), "请假"))
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
                    viewBinding.tvEvent.text.toString().trim(),
                    dateTime,
                    classInfo
                )
            }
        }
    }

    //    -->设置各区块的颜色
    val PIE_COLORS2 = intArrayOf(
        Color.rgb(246, 108, 108),//迟到
        Color.rgb(31, 193, 195),//早退
        Color.rgb(143, 129, 254),//请假
        Color.rgb(246, 205, 87)//缺勤
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
        //最多作业条目
        viewBinding.homeworkStatistical.statisticsRecyclerview.layoutManager =
            GridLayoutManager(activity, 3)
        viewBinding.homeworkStatistical.statisticsRecyclerview.adapter = mostAdapter
        val datas = mutableListOf<SchoolHomeWork>()
        datas.add(SchoolHomeWork("50", "本周总作业数"))
        datas.add(SchoolHomeWork("3.5", "作业最多科目"))
        datas.add(SchoolHomeWork("30", "作业最少科目"))
        mostAdapter.setList(datas)

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

    private var studentName = ""
    private val adapterEvent = object :
        BaseQuickAdapter<GetUserSchoolRsp.DataBean.FormBean, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(holder: BaseViewHolder, item: GetUserSchoolRsp.DataBean.FormBean) {
            holder.setText(R.id.className, item.studentName)
            holder.getView<View>(R.id.select).visibility =
                if (studentName == item.studentName) View.VISIBLE else View.GONE
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

    private val mostAdapter =
        object :
            BaseQuickAdapter<SchoolHomeWork, BaseViewHolder>(R.layout.item_weekly_attendance) {
            override fun convert(holder: BaseViewHolder, item: SchoolHomeWork) {
                val viewBind = ItemWeeklyAttendanceBinding.bind(holder.itemView)
                viewBind.tvEventName.text = item.value
                viewBind.tvAttendance.text = item.name
                viewBind.viewLine.visibility =
                    if (holder.bindingAdapterPosition == 0) View.GONE else View.VISIBLE
            }

        }
}