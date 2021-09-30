package com.yyide.chatim.activity.weekly.details

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.details.adapter.ParentsAttendanceAdapter
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentParentsChildWeeklyAttendanceBinding
import com.yyide.chatim.databinding.FragmentTeacherChildWeeklyAttendanceBinding
import com.yyide.chatim.databinding.ItemWeeklyChartsVerticalBinding
import com.yyide.chatim.model.*
import com.yyide.chatim.widget.treeview.adapter.SingleLayoutTreeAdapter
import com.yyide.chatim.widget.treeview.model.TreeNode
import java.util.ArrayList

/**
 * Child
 * 班主任查看教师考勤周报
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class ParentsAttendanceChildFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentParentsChildWeeklyAttendanceBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentParentsChildWeeklyAttendanceBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(detail: AttendDetail) =
            ParentsAttendanceChildFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", detail)
                }
            }
    }

    private lateinit var detail: AttendDetail
    private fun initView() {
        arguments?.apply {
            detail = getSerializable("item") as AttendDetail
            viewBinding.progressRecyclerview.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            viewBinding.progressRecyclerview.adapter = adapterAttendance
            adapterAttendance.setOnItemClickListener { adapter, view, position ->
                selectPosition = if (selectPosition != position) {
                    position
                } else {
                    -1
                }
                adapterAttendance.notifyDataSetChanged()
            }
//            adapterAttendance.setList(detail)
            viewBinding.tvAbsenteeism.isChecked = true
            viewBinding.tvAbsenteeism.setTextColor(resources.getColor(R.color.white))
            setAttendanceList(detail.absence, "缺勤")
        }
        initClick()
    }

    private fun initClick() {
        viewBinding.tvLate.setOnClickListener {
            setButton()
            viewBinding.tvLate.isChecked = true
            setAttendanceList(detail.late, "迟到")

        }
        viewBinding.tvAbsenteeism.setOnClickListener {
            setButton()
            viewBinding.tvAbsenteeism.isChecked = true
            setAttendanceList(detail.absence, "缺勤")
        }
        viewBinding.tvEarly.setOnClickListener {
            setButton()
            viewBinding.tvEarly.isChecked = true
            setAttendanceList(detail.early, "早退")
        }
        viewBinding.tvLeave.setOnClickListener {
            setButton()
            viewBinding.tvLeave.isChecked = true
            setAttendanceList(detail.leave, "请假")
        }
    }

    private fun setButton() {
        viewBinding.tvAbsenteeism.isChecked = false
        viewBinding.tvLeave.isChecked = false
        viewBinding.tvLate.isChecked = false
        viewBinding.tvEarly.isChecked = false
        viewBinding.tvAbsenteeism.setTextColor(resources.getColor(R.color.text_1E1E1E))
        viewBinding.tvLeave.setTextColor(resources.getColor(R.color.text_1E1E1E))
        viewBinding.tvLate.setTextColor(resources.getColor(R.color.text_1E1E1E))
        viewBinding.tvEarly.setTextColor(resources.getColor(R.color.text_1E1E1E))
    }

    private val dataToBind = mutableListOf<TreeNode<AttendItem>>()

    private fun setAttendanceList(datas: List<AttendItem>, desc: String) {
        //mViewBinding.tvNum.setText((teachers.getAbsencePeople() != null ? teachers.getAbsencePeople().size() : 0) + "人");
        dataToBind.addAll(convertDataToTreeNode(datas, desc))
        val adapter =
            ParentsAttendanceAdapter(R.layout.item_attendance_school_teacher, dataToBind)
        viewBinding.recyclerview.layoutManager = LinearLayoutManager(
            context
        )
        viewBinding.recyclerview.adapter = adapter
        adapter.setEmptyView(R.layout.empty_top)
        adapter.setOnTreeClickedListener(object :
            SingleLayoutTreeAdapter.OnTreeClickedListener<AttendItem> {
            override fun onNodeClicked(view: View, node: TreeNode<AttendItem>, position: Int) {
                val tvStatus = view.findViewById<TextView>(R.id.tv_status)
                if (node.isExpand) {
                    tvStatus.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        context!!.resources.getDrawable(R.mipmap.icon_up),
                        null
                    )
                } else {
                    tvStatus.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        context!!.resources.getDrawable(R.mipmap.icon_down),
                        null
                    )
                }
            }

            override fun onLeafClicked(view: View?, node: TreeNode<AttendItem>?, position: Int) {
            }

        })
    }

    private fun convertDataToTreeNode(
        datas: List<AttendItem>,
        desc: String
    ): List<TreeNode<AttendItem>> {
        val nodes: MutableList<TreeNode<AttendItem>> = ArrayList()
        val childs = mutableListOf<TreeNode<AttendItem>>()
        val item = AttendItem(desc, 1, datas)
        val treeNode: TreeNode<AttendItem> = TreeNode(item, -1)
        for (childItem in item.list) {
            val child: TreeNode<AttendItem> = TreeNode(childItem, -1)
            child.parent = treeNode
            childs.add(child)
        }
        treeNode.children = childs
        nodes.add(treeNode)
        return nodes
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
        BaseQuickAdapter<AttendDetail, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
        override fun convert(holder: BaseViewHolder, item: AttendDetail) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            if (selectPosition == holder.bindingAdapterPosition) {
                bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
            }
            bind.tvWeek.text = item.name
            when (item.name) {
                "0" -> {
                    bind.progressbar.progressDrawable =
                        activity?.resources?.getDrawable(R.drawable.progress_bg_vertical_gary)
                    bind.tvProgress.text = "${item.abNumber}%"
                    setAnimation(bind.progressbar, if (item.abNumber <= 0) 0 else item.abNumber)
                }
                "1" -> {
                    bind.progressbar.progressDrawable =
                        activity?.resources?.getDrawable(R.drawable.progress_bg_vertical_red)
                    bind.tvProgress.text = "${item.lateNumber}%"
                    setAnimation(bind.progressbar, if (item.lateNumber <= 0) 0 else item.lateNumber)
                }
                "2" -> {
                    bind.progressbar.progressDrawable =
                        activity?.resources?.getDrawable(R.drawable.progress_bg_vertical_yellow)
                    bind.tvProgress.text = "${item.leaveNumber}%"
                    setAnimation(
                        bind.progressbar,
                        if (item.leaveNumber <= 0) 0 else item.leaveNumber
                    )
                }
                "3" -> {
                    bind.progressbar.progressDrawable =
                        activity?.resources?.getDrawable(R.drawable.progress_bg_vertical_grenn)
                    bind.tvProgress.text = "${item.earlyNumber}%"
                    setAnimation(
                        bind.progressbar,
                        if (item.earlyNumber <= 0) 0 else item.earlyNumber
                    )
                }
            }
        }
    }
}