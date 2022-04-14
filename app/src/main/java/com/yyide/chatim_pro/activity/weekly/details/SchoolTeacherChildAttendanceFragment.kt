package com.yyide.chatim_pro.activity.weekly.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.weekly.home.WeeklyUtil
import com.yyide.chatim_pro.base.BaseFragment
import com.yyide.chatim_pro.databinding.FragmentSchoolTeacherChildWeeklyAttendanceBinding
import com.yyide.chatim_pro.databinding.ItemWeeklyProgressHBinding
import com.yyide.chatim_pro.model.DeptAttend
import com.yyide.chatim_pro.model.Detail
import com.yyide.chatim_pro.model.SchoolHomeAttend

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
        //教职工周统计横版
        viewBinding.recyclerviewDept.layoutManager = LinearLayoutManager(activity)
        viewBinding.recyclerviewDept.adapter =
            adapterAttendanceDept

        var teacherAttends = ArrayList<SchoolHomeAttend>()
        var deptattends = mutableListOf<DeptAttend>()
        arguments?.apply {
            val detail = getSerializable("item") as Detail
            teacherAttends = detail.teacherAttend as ArrayList<SchoolHomeAttend>
            deptattends = detail.deptAttend as MutableList<DeptAttend>
            viewBinding.textView.text = "全校教职工每天${getString("tabTitle", "")}考勤率"
            viewBinding.tvFaculty.text = "教职工各部门${getString("tabTitle", "")}考勤率"
        }

        //教职工出入校考勤统计
        val mBarCharts = BarCharts()
        mBarCharts.showBarChart2(
            viewBinding.layoutCharts.barChart,
            mBarCharts.getBarData(teacherAttends),
            teacherAttends,
            true
        )
        adapterAttendanceDept.setList(deptattends)
    }
    
    /**
     * 考勤数据
     */
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
            } else {
                bind.progressbarLast.setProgress(item.lastWeek)
            }
            if (item.thisWeek <= 0) {
                bind.progressbarThis.visibility = View.INVISIBLE
            } else {
                bind.progressbarThis.setProgress(item.thisWeek)
            }
        }
    }


}