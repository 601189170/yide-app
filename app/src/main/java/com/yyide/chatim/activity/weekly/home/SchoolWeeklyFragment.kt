package com.yyide.chatim.activity.weekly.home

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.WeeklyDetailsActivity
import com.yyide.chatim.activity.weekly.details.adapter.DateAdapter
import com.yyide.chatim.activity.weekly.home.viewmodel.SchoolViewModel
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentSchoolWeeklyBinding
import com.yyide.chatim.databinding.ItemWeeklyChartsVerticalBinding
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.model.*
import com.yyide.chatim.utils.DateUtils

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

    private val selectLists = mutableListOf<String>()
    private fun initView() {
        viewModel.schoolLiveData.observe(viewLifecycleOwner) {
            dismiss()
            val result = it.getOrNull()
            if (null != result) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData.visibility = View.GONE
                //本周小结
                setSummary(result.summary)
                //作业
                setWorkView(result.work)
                //考勤
                initAttendance(result.attend)
            } else {//接口返回空的情况处理
//                viewBinding.clContent.visibility = View.GONE
//                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }
        viewBinding.tvDescs.text = WeeklyUtil.getDesc()
        val dateTime = WeeklyUtil.getDateTime()
        if (dateTime != null) {
            loading()
            viewModel.requestSchoolWeekly(dateTime.startTime, dateTime.endTime)
            viewBinding.tvTime.text = getString(
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
        }
        val dateLists = WeeklyUtil.getDateTimes()
        val adapterDate = DateAdapter()
        if (dateLists.isNotEmpty()) {
            adapterDate.setList(dateLists)
        }
        viewBinding.tvTime.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterDate, "请选择时间")
            attendancePop.setOnSelectListener { index: Int ->
//                indexDate = index
                viewBinding.tvTime.text = getString(
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
                viewModel.requestSchoolWeekly(dateLists[index].startTime, dateLists[index].endTime)
            }
        }

        viewBinding.layoutWeeklySchoolAttendance.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(mActivity, WeeklyDetailsActivity.SCHOOL_ATTENDANCE_TYPE)
        }

        viewBinding.layoutWeeklyHomeworkSummary.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(mActivity, WeeklyDetailsActivity.SCHOOL_HOMEWORK_TYPE)
        }
    }

    private fun setSummary(summary: SchoolHomeSummary) {
        viewBinding.layoutWeeklySummary.tvWeeklyAttendance.text = summary.attend
        viewBinding.layoutWeeklySummary.tvWeeklyHomework.text = summary.work
        viewBinding.layoutWeeklySummary.tvWeeklyShopping.text = summary.expend
    }

    private fun initAttendance(attend: SchoolHomeAttendance) {
        //学生考勤
        viewBinding.layoutWeeklySchoolAttendance.layoutCharts.recyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.layoutWeeklySchoolAttendance.layoutCharts.recyclerview.adapter =
            adapterStudentAttendance
        adapterStudentAttendance.setList(attend.studentAttend)
        adapterStudentAttendance.setOnItemClickListener { adapter, view, position ->
            selectStudentPosition = if (selectStudentPosition != position) {
                position
            } else {
                -1
            }
            adapterStudentAttendance.notifyDataSetChanged()
        }

        //教师考勤
        viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.recyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.recyclerview.adapter =
            adapterTeacherAttendance
        adapterTeacherAttendance.setList(attend.teacherAttend)
        adapterTeacherAttendance.setOnItemClickListener { adapter, view, position ->
            selectTeacherPosition = if (selectTeacherPosition != position) {
                position
            } else {
                -1
            }
            adapterTeacherAttendance.notifyDataSetChanged()
        }
    }

    private fun setWorkView(work: SchoolHomeWork) {

    }

    /**
     * 学生考勤数据
     */
    private var selectStudentPosition = -1
    private val adapterStudentAttendance = object :
        BaseQuickAdapter<SchoolHomeStudentAttend, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
        override fun convert(holder: BaseViewHolder, item: SchoolHomeStudentAttend) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.tvProgress.text = "${item.value}%"
            bind.tvWeek.text = item.name
            setAnimation(bind.progressbar, if (item.value <= 0) 0 else item.value.toInt())
            bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            if (selectStudentPosition == holder.bindingAdapterPosition) {
                bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
            }
        }
    }

    fun setAnimation(view: ProgressBar, mProgressBar: Int) {
        val animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(600)
        animator.addUpdateListener { valueAnimator: ValueAnimator ->
            view.progress = valueAnimator.animatedValue as Int
        }
        animator.start()
    }

    /**
     * 教师考勤数据
     */
    private var selectTeacherPosition = -1
    private val adapterTeacherAttendance = object :
        BaseQuickAdapter<SchoolHomeTeacherAttend, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
        override fun convert(holder: BaseViewHolder, item: SchoolHomeTeacherAttend) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.tvProgress.text = "${item.value}%"
            bind.tvWeek.text = item.name
            setAnimation(bind.progressbar, if (item.value <= 0) 0 else item.value.toInt())
            bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            if (selectTeacherPosition == holder.bindingAdapterPosition) {
                bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
            }
        }
    }

}