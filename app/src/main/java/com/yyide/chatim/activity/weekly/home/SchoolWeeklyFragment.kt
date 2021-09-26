package com.yyide.chatim.activity.weekly.home

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.WeeklyDetailsActivity
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
    private val dateLists = mutableListOf<String>()
    private fun initView() {
        viewModel.schoolLiveData.observe(viewLifecycleOwner) {
            val video = it.getOrNull()
            if (null != video) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData.visibility = View.GONE
                //本周小结
                setSummary(video.summary)
                //作业
                setWorkView(video.work)
                //考勤
                initAttendance(video.attend)
            } else {//接口返回空的情况处理
//                viewBinding.clContent.visibility = View.GONE
//                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }
        val startData = DateUtils.getDate(DateUtils.getBeginDayOfWeek().time)
        val endData = DateUtils.getDate(System.currentTimeMillis())
        viewModel.requestSchoolWeekly(startData, endData)

        viewBinding.tvDescs.text = WeeklyUtil.getDesc()
        viewBinding.tvTime.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterDate, "请选择时间")
            attendancePop.setOnSelectListener { index: Int ->
                indexDate = index
                viewBinding.tvTime.text = dateLists[index]
            }
        }

        viewBinding.layoutWeeklySchoolAttendance.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(mActivity, WeeklyDetailsActivity.SCHOOL_ATTENDANCE_TYPE)
        }

        viewBinding.layoutWeeklyHomeworkSummary.cardView.setOnClickListener {
            WeeklyDetailsActivity.jump(mActivity, WeeklyDetailsActivity.SCHOOL_HOMEWORK_TYPE)
        }

        for (item in 1..10) {
            selectLists.add("$item + 事件")
            dateLists.add("$item + 9:23-16:32")
        }
        viewBinding.tvTime.text = dateLists[0]
        adapterEvent.setList(selectLists)
        adapterDate.setList(dateLists)
    }

    private fun setSummary(summary: Summary) {
        viewBinding.layoutWeeklySummary.tvWeeklyAttendance.text = summary.attend
        viewBinding.layoutWeeklySummary.tvWeeklyHomework.text = summary.work
        viewBinding.layoutWeeklySummary.tvWeeklyShopping.text = summary.expend
    }

    private fun initAttendance(attend: Attend) {
        viewBinding.layoutWeeklySchoolAttendance.layoutCharts.recyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.layoutWeeklySchoolAttendance.layoutCharts.recyclerview.adapter =
            adapterAttendance
        viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.recyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.layoutWeeklySchoolAttendance.layoutCharts2.recyclerview.adapter =
            adapterAttendance2

        //学生考勤
        adapterAttendance.setList(attend.studentAttend)
        adapterAttendance.setOnItemClickListener { adapter, view, position ->
            selectPosition = if (selectPosition != position) {
                position
            } else {
                -1
            }
            adapterAttendance.notifyDataSetChanged()
        }
        //教师考勤
        adapterAttendance2.setList(attend.teacherAttend)
        adapterAttendance2.setOnItemClickListener { adapter, view, position ->
            selectPosition2 = if (selectPosition2 != position) {
                position
            } else {
                -1
            }
            adapterAttendance2.notifyDataSetChanged()
        }
    }

    private fun setWorkView(work: Work) {

    }

    /**
     * 考勤数据
     */
    private var selectPosition = -1
    private val adapterAttendance = object :
        BaseQuickAdapter<StudentAttend, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
        override fun convert(holder: BaseViewHolder, item: StudentAttend) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.tvProgress.text = "${item.value}%"
            bind.tvWeek.text = item.name
            bind.progressbar.progress = if (item.value <= 0) 0 else item.value.toInt()
            bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            if (selectPosition == holder.bindingAdapterPosition) {
                bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
            }
        }
    }

    /**
     * 考勤数据
     */
    private var selectPosition2 = -1
    private val adapterAttendance2 = object :
        BaseQuickAdapter<TeacherAttend, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
        override fun convert(holder: BaseViewHolder, item: TeacherAttend) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.tvProgress.text = "${item.value}%"
            bind.tvWeek.text = item.name
            bind.progressbar.progress = if (item.value <= 0) 0 else item.value.toInt()
            bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            if (selectPosition2 == holder.bindingAdapterPosition) {
                bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
            }
        }
    }

    private var indexDate = 0
    private val adapterDate = object :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.className, item)
            holder.getView<View>(R.id.select).visibility =
                if (indexDate == holder.adapterPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.adapterPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
            }
        }
    }

    private var index = 0
    private val adapterEvent = object :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.className, item)
            holder.getView<View>(R.id.select).visibility =
                if (indexDate == holder.adapterPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.adapterPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
            }
        }
    }
}