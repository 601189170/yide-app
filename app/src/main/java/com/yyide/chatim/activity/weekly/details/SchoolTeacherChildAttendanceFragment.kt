package com.yyide.chatim.activity.weekly.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.home.WeeklyUtil
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentSchoolTeacherChildWeeklyAttendanceBinding
import com.yyide.chatim.databinding.ItemWeeklyChartsVerticalBinding
import com.yyide.chatim.databinding.ItemWeeklyProgressHBinding
import com.yyide.chatim.model.DeptAttend
import com.yyide.chatim.model.Detail
import com.yyide.chatim.model.TeacherAttendance

/**
 *
 * 校长查看子项教师考勤
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolTeacherChildAttendanceFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentSchoolTeacherChildWeeklyAttendanceBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSchoolTeacherChildWeeklyAttendanceBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(item: Detail?, tabTitle: String) =
            SchoolTeacherChildAttendanceFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", item)
                    putString("tabTitle", tabTitle)
                }
            }
    }

    private fun initView() {
        //教职工出入校考勤统计
        viewBinding.layoutCharts.recyclerview.layoutManager = GridLayoutManager(activity, 7)
        viewBinding.layoutCharts.recyclerview.adapter =
            adapterAttendance
        adapterAttendance.setOnItemClickListener { adapter, view, position ->
            selectPosition = if (selectPosition != position) {
                position
            } else {
                -1
            }
            adapterAttendance.notifyDataSetChanged()
        }

        //教职工周统计横版
        viewBinding.recyclerviewDept.layoutManager = LinearLayoutManager(activity)
        viewBinding.recyclerviewDept.adapter =
            adapterAttendanceDept
        adapterAttendanceDept.setOnItemClickListener { adapter, view, position ->
            selectPosition2 = if (selectPosition2 != position) {
                position
            } else {
                -1
            }
            adapterAttendanceDept.notifyDataSetChanged()
        }

        var teacherAttends = mutableListOf<TeacherAttendance>()
        var deptattends = mutableListOf<DeptAttend>()
        arguments?.apply {
            val detail = getSerializable("item") as Detail
            teacherAttends = detail.teacherAttend as MutableList<TeacherAttendance>
            deptattends = detail.deptAttend as MutableList<DeptAttend>
            viewBinding.textView.text = "全校教职工每天${getString("tabTitle", "")}考勤率"
            viewBinding.tvFaculty.text = "教职工各部门${getString("tabTitle", "")}考勤率"
        }
        adapterAttendance.setList(teacherAttends)
        adapterAttendanceDept.setList(deptattends)
    }

    /**
     * 考勤数据
     */
    private var selectPosition = -1
    private val adapterAttendance = object :
        BaseQuickAdapter<TeacherAttendance, BaseViewHolder>(R.layout.item_weekly_charts_school_attendance) {
        override fun convert(holder: BaseViewHolder, item: TeacherAttendance) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.tvProgress.text = "${item.rate}%"
            bind.tvWeek.text = item.name
            WeeklyUtil.setAnimation(bind.progressbar, if (item.rate <= 0) 0 else item.rate.toInt())
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
    private val adapterAttendanceDept = object :
        BaseQuickAdapter<DeptAttend, BaseViewHolder>(R.layout.item_weekly_progress_h) {
        override fun convert(holder: BaseViewHolder, item: DeptAttend) {
            val bind = ItemWeeklyProgressHBinding.bind(holder.itemView)
            bind.tvEventName.text = item.name
            WeeklyUtil.setAnimation(
                bind.progressbarLast,
                if (item.lastWeek <= 0) 0 else item.lastWeek.toInt()
            )
            WeeklyUtil.setAnimation(
                bind.progressbarThis,
                if (item.thisWeek <= 0) 0 else item.thisWeek.toInt()
            )
            if (item.lastWeek <= 0) {
                bind.progressbarLast.visibility = View.INVISIBLE
            }
            if (item.thisWeek <= 0) {
                bind.progressbarThis.visibility = View.INVISIBLE
            }
            bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            if (selectPosition2 == holder.bindingAdapterPosition) {
                bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
            }
        }
    }

}