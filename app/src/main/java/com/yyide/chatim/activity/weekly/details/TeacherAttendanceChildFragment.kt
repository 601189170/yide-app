package com.yyide.chatim.activity.weekly.details

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentSchoolTeacherWeeklyAttendanceBinding
import com.yyide.chatim.databinding.FragmentTeacherChildWeeklyAttendanceBinding
import com.yyide.chatim.databinding.ItemWeeklyChartsVerticalBinding
import com.yyide.chatim.model.*

/**
 * Child
 * 班主任查看教师考勤周报
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherAttendanceChildFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentTeacherChildWeeklyAttendanceBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherChildWeeklyAttendanceBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(detail: WeeklyTeacherDetail) =
            TeacherAttendanceChildFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", detail)
                }
            }
    }

    private fun initView() {
        arguments?.apply {
            val detail = getSerializable("item") as WeeklyTeacherDetail
            viewBinding.layoutCharts.recyclerview.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            viewBinding.layoutCharts.recyclerview.adapter = adapterAttendance
            adapterAttendance.setOnItemClickListener { adapter, view, position ->
                selectPosition = if (selectPosition != position) {
                    position
                } else {
                    -1
                }
                adapterAttendance.notifyDataSetChanged()
            }
            adapterAttendance.setList(detail.numbers)
        }
        initAttendanceList()
    }

    private fun initAttendanceList() {
        viewBinding.recyclerview.layoutManager = LinearLayoutManager(activity)
//        viewBinding.recyclerview.adapter =
    }

    fun setAnimation(view: ProgressBar, mProgressBar: Int) {
        val animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(600)
        animator.addUpdateListener { valueAnimator: ValueAnimator ->
            view.progress = valueAnimator.animatedValue as Int
        }
        animator.start()
    }

    /**
     * 考勤数据
     */
    private var selectPosition = -1
    private val adapterAttendance = object :
        BaseQuickAdapter<WeeklyTeacherNumber, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
        override fun convert(holder: BaseViewHolder, item: WeeklyTeacherNumber) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.tvProgress.text = "${item.value}%"
            bind.tvWeek.text = item.name
            setAnimation(bind.progressbar, if (item.value <= 0) 0 else item.value.toInt())
            bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            if (selectPosition == holder.bindingAdapterPosition) {
                bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
            }

            when (item.name) {
                "缺勤" -> {
                    bind.progressbar.progressDrawable =
                        activity?.resources?.getDrawable(R.drawable.progress_bg_vertical_gary)
                }
                "迟到" -> {
                    bind.progressbar.progressDrawable =
                        activity?.resources?.getDrawable(R.drawable.progress_bg_vertical_red)
                }
                "请假" -> {
                    bind.progressbar.progressDrawable =
                        activity?.resources?.getDrawable(R.drawable.progress_bg_vertical_yellow)
                }
                "早退" -> {
                    bind.progressbar.progressDrawable =
                        activity?.resources?.getDrawable(R.drawable.progress_bg_vertical_grenn)
                }
                else -> {
                    bind.progressbar.progressDrawable =
                        activity?.resources?.getDrawable(R.drawable.progress_bg_vertical)
                }
            }
        }
    }
}