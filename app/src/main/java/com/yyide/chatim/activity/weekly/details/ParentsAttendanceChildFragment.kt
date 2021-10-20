package com.yyide.chatim.activity.weekly.details

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.details.adapter.ParentsAttendanceAdapter
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentParentsChildWeeklyAttendanceBinding
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
        fun newInstance(detail: AttendDetail, studentName: String, title: String) =
            ParentsAttendanceChildFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", detail)
                    putString("studentName", studentName)
                    putString("tabTitle", title)
                }
            }
    }

    private lateinit var detail: AttendDetail
    private lateinit var studentName: String
    private fun initView() {
        arguments?.apply {
            detail = getSerializable("item") as AttendDetail
            studentName = getString("studentName", "")
            viewBinding.textView.text =
                getString(R.string.weekly_desc_name, getString("tabTitle", ""))

            val numbers = StudentNumber(
                detail.abNumber,
                detail.lateNumber,
                detail.leaveNumber,
                detail.earlyNumber
            )
            viewBinding.tvAbsenteeism.isChecked = true
            viewBinding.tvAbsenteeism.setTextColor(resources.getColor(R.color.white))
            setAttendanceList(detail.absence)
            initBarCharts(numbers)
        }
        initClick()
    }

    private fun initBarCharts(numbers: StudentNumber) {
        val mBarCharts = BarCharts()
        mBarCharts.showBarChart(viewBinding.chart, getBarData(numbers), false)
    }

    /**
     * 这个方法是初始化数据的
     *
     * @param count X 轴的个数
     * @param range Y 轴的数据
     */
    private var color: Array<String> = arrayOf("#919399", "#F66C6C", "#FFC000", "#63DAAB")
    private fun getBarData(numbers: StudentNumber): BarData {
//        val xValues = ArrayList<String>()
//        for (i in numbers) {
//            xValues.add("" + (i) + "") // 设置每个柱壮图的文字描述
//        }
        val yValues = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()
        for (i in 0 until 4) {
            when (i) {
                0 -> {
                    yValues.add(BarEntry(i.toFloat(), numbers.abNumber.toFloat()))
                }
                1 -> {
                    yValues.add(BarEntry(i.toFloat(), numbers.lateNumber.toFloat()))
                }
                2 -> {
                    yValues.add(BarEntry(i.toFloat(), numbers.leaveNumber.toFloat()))
                }
                3 -> {
                    yValues.add(BarEntry(i.toFloat(), numbers.earlyNumber.toFloat()))
                }
            }
            colors.add(Color.parseColor(color[i]))
        }
        // y轴的数据集合
        val barDataSet = BarDataSet(yValues, "")
        barDataSet.colors = colors
        // 设置栏阴影颜色
//        barDataSet.barShadowColor = Color.parseColor("#01000000")
        val barDataSets = mutableListOf<BarDataSet>()
        barDataSet.valueTextColor = Color.parseColor("#909399")
        barDataSet.valueTextSize = 10f
        barDataSet.valueFormatter =
            IValueFormatter { value, entry, dataSetIndex, viewPortHandler -> "${value.toInt()}" }
        barDataSets.add(barDataSet)
        // 绘制值
        barDataSet.setDrawValues(true)
        val barData = BarData(barDataSets as List<IBarDataSet>?)
        barData.barWidth = 0.3f
        return barData
    }


    private fun initClick() {
        viewBinding.tvLate.setOnClickListener {
            setButton()
            viewBinding.tvLate.isChecked = true
            viewBinding.tvLate.setTextColor(resources.getColor(R.color.white))
            setAttendanceList(detail.late)

        }
        viewBinding.tvAbsenteeism.setOnClickListener {
            setButton()
            viewBinding.tvAbsenteeism.isChecked = true
            viewBinding.tvAbsenteeism.setTextColor(resources.getColor(R.color.white))
            setAttendanceList(detail.absence)
        }
        viewBinding.tvEarly.setOnClickListener {
            setButton()
            viewBinding.tvEarly.isChecked = true
            viewBinding.tvEarly.setTextColor(resources.getColor(R.color.white))
            setAttendanceList(detail.early)
        }
        viewBinding.tvLeave.setOnClickListener {
            setButton()
            viewBinding.tvLeave.setTextColor(resources.getColor(R.color.white))
            viewBinding.tvLeave.isChecked = true
            setAttendanceList(detail.leave)
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

    private fun setAttendanceList(datas: List<AttendItem>) {
        dataToBind.clear()
        dataToBind.addAll(convertDataToTreeNode(datas))
        val adapter =
            ParentsAttendanceAdapter(R.layout.item_attendance_parents_status, dataToBind)
        viewBinding.recyclerview.layoutManager = LinearLayoutManager(
            context
        )
        viewBinding.recyclerview.adapter = adapter
//        adapter.setEmptyView(R.layout.empty_top)
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
        datas: List<AttendItem>
    ): List<TreeNode<AttendItem>> {
        val nodes: MutableList<TreeNode<AttendItem>> = ArrayList()
        val childs = mutableListOf<TreeNode<AttendItem>>()
        val item = AttendItem(studentName, 1, datas)
        val treeNode: TreeNode<AttendItem> = TreeNode(item, -1)
        if (datas != null) {
            for (childItem in item.list) {
                val child: TreeNode<AttendItem> = TreeNode(childItem, -1)
                child.parent = treeNode
                childs.add(child)
            }
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

}