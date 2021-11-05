package com.yyide.chatim.activity.weekly.home

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.WeeklyDetailsActivity
import com.yyide.chatim.activity.weekly.details.BarCharts
import com.yyide.chatim.activity.weekly.home.viewmodel.SchoolViewModel
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.DialogWeekMessgeBinding
import com.yyide.chatim.databinding.FragmentSchoolWeeklyBinding
import com.yyide.chatim.databinding.ItemWeeklyChartsVerticalBinding
import com.yyide.chatim.model.*
import com.yyide.chatim.utils.DateUtils
import java.math.BigDecimal
import java.util.ArrayList
import com.github.mikephil.charting.components.XAxis


/**
 *
 * 校长周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolWeeklyFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentSchoolWeeklyBinding
    private val viewModel: SchoolViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSchoolWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SchoolWeeklyFragment().apply {}
    }

    private fun initView() {
        viewModel.schoolLiveData.observe(viewLifecycleOwner) {
            dismiss()
            val result = it.getOrNull()
            if (null != result) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData.visibility = View.GONE
                //本周小结
                setSummary(result.summary)
                //考勤
                initAttendance(result.attend)
                //作业
//                setWorkView(result.work)
//                if (!viewBinding.layoutWeeklySummary.root.isShown && !viewBinding.layoutWeeklySchoolAttendance.root.isShown) {
//                    viewBinding.clContent.visibility = View.GONE
//                    viewBinding.cardViewNoData.visibility = View.VISIBLE
//                }
            } else {//接口返回空的情况处理
                viewBinding.clContent.visibility = View.GONE
                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }
        viewBinding.tvDescs.text = WeeklyUtil.getDesc()
        viewBinding.layoutWeeklySchoolAttendance.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(
                mActivity,
                WeeklyDetailsActivity.SCHOOL_ATTENDANCE_TYPE,
                "",
                "",
                dateTime,
                null
            )
        }

        viewBinding.layoutWeeklyHomeworkSummary.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(
                mActivity,
                WeeklyDetailsActivity.SCHOOL_HOMEWORK_TYPE,
                "",
                "",
                dateTime,
                null
            )
        }
        initDate()
    }

    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
    private var timePosition = -1
    private fun initDate() {
        //获取日期时间
        val dateLists = WeeklyUtil.getDateTimes()
        if (dateLists.isNotEmpty()) {
            timePosition = dateLists.size - 1
            dateTime = dateLists[dateLists.size - 1]
            request(dateTime)
            viewBinding.tvStartTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition > 0) {
                        timePosition -= 1
                        dateTime = dateLists[timePosition]
                        request(dateTime)
                    }
                }
            }
            viewBinding.tvEndTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition < (dateLists.size - 1)) {
                        timePosition += 1
                        dateTime = dateLists[timePosition]
                        request(dateTime)
                    }
                }
            }
        }
    }

    private fun request(dateTime: WeeklyDateBean.DataBean.TimesBean) {
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
        loading()
        viewModel.requestSchoolWeekly(dateTime.startTime, dateTime.endTime)
    }

    private fun setSummary(summary: SchoolHomeSummary?) {
        viewBinding.layoutWeeklySummary.root.visibility = View.VISIBLE
        if (summary != null) {
            viewBinding.layoutWeeklySummary.tvWeeklyAttendance.text = summary.attend
            viewBinding.layoutWeeklySummary.tvWeeklyHomework.text = summary.work
            viewBinding.layoutWeeklySummary.tvWeeklyShopping.text = summary.expend
        }
        if (summary != null
            && TextUtils.isEmpty(summary.attend)
            && TextUtils.isEmpty(summary.expend)
            && TextUtils.isEmpty(summary.work)
        ) {
            viewBinding.layoutWeeklySummary.root.visibility = View.GONE
        }
    }

    private fun initAttendance(attend: SchoolHomeAttendance?) {
        if (attend != null) {
            viewBinding.layoutWeeklySchoolAttendance.cardViewNoData.visibility = View.GONE
            viewBinding.layoutWeeklySchoolAttendance.cardView.visibility = View.VISIBLE
            viewBinding.layoutWeeklySchoolAttendance.textView1.visibility = View.VISIBLE
            viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.root.visibility =
                View.VISIBLE
            viewBinding.layoutWeeklySchoolAttendance.textView.visibility = View.VISIBLE
            viewBinding.layoutWeeklySchoolAttendance.layoutCharts.root.visibility = View.VISIBLE
            //学生考勤
            if (attend.studentAttend != null) {

                val mBarCharts = BarCharts()
                mBarCharts.showBarChart2(
                    viewBinding.layoutWeeklySchoolAttendance.layoutCharts.barChart,
                    mBarCharts.getBarData(attend.studentAttend),
                    attend.studentAttend,
                    true
                )
            } else {
                viewBinding.layoutWeeklySchoolAttendance.textView.visibility = View.GONE
                viewBinding.layoutWeeklySchoolAttendance.layoutCharts.root.visibility = View.GONE
            }

            //教师考勤
            if (attend.teacherAttend != null) {
                val mBarCharts = BarCharts()
                mBarCharts.showBarChart2(
                    viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.barChart,
                    mBarCharts.getBarData(attend.teacherAttend),
                    attend.teacherAttend,
                    true
                )
            } else {
                viewBinding.layoutWeeklySchoolAttendance.textView1.visibility = View.GONE
                viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.root.visibility =
                    View.GONE
            }
            if (attend.teacherAttend != null && attend.studentAttend != null && attend.teacherAttend!!.isEmpty() && attend.studentAttend!!.isEmpty()) {
                viewBinding.layoutWeeklySchoolAttendance.cardViewNoData.visibility = View.VISIBLE
                viewBinding.layoutWeeklySchoolAttendance.cardView.visibility = View.GONE
            }
        } else {
            viewBinding.layoutWeeklySchoolAttendance.cardViewNoData.visibility = View.VISIBLE
            viewBinding.layoutWeeklySchoolAttendance.cardView.visibility = View.GONE
        }
    }

    private fun setWorkView(work: SchoolHomeWork?) {

    }

}