package com.yyide.chatim.activity.weekly.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.details.adapter.TeacherAttendanceAdapter
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentTeacherChildWeeklyAttendanceBinding
import com.yyide.chatim.model.*
import com.yyide.chatim.widget.treeview.adapter.SingleLayoutTreeAdapter
import com.yyide.chatim.widget.treeview.model.TreeNode
import java.util.*

/**
 * Child
 * 班主任查看教师考勤周报
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherAttendanceTeacherChildFragment : BaseFragment() {

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
        fun newInstance(detail: WeeklyTeacherDetail, title: String) =
            TeacherAttendanceTeacherChildFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", detail)
                    putString("tabTitle", title)

                }
            }
    }

    private lateinit var detail: WeeklyTeacherDetail

    private fun initView() {
        arguments?.apply {
            detail = getSerializable("item") as WeeklyTeacherDetail
            viewBinding.textView.text =
                getString(R.string.weekly_desc_name, getString("tabTitle", ""))
            viewBinding.tvAbsenteeism.isChecked = true
            viewBinding.tvAbsenteeism.setTextColor(resources.getColor(R.color.white))
            if (detail.abnormalDetails.absenteeism != null && detail.abnormalDetails.absenteeism.isNotEmpty()) {
                initAttendanceList(detail.abnormalDetails.absenteeism[0])
            } else {
                initAttendanceList(ValueChild("", "", "", "", null))
            }
            initBarCharts(detail.numbers)
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
            IValueFormatter { value, entry, dataSetIndex, viewPortHandler -> if (value > 0) "${value.toInt()}" else "" }
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
            if (detail.abnormalDetails.late != null && detail.abnormalDetails.late.isNotEmpty()) {
                initAttendanceList(detail.abnormalDetails.late[0])
            } else {
                initAttendanceList(ValueChild("", "", "", "", null))
            }
        }
        viewBinding.tvAbsenteeism.setOnClickListener {
            setButton()
            viewBinding.tvAbsenteeism.isChecked = true
            viewBinding.tvAbsenteeism.setTextColor(resources.getColor(R.color.white))
            if (detail.abnormalDetails.absenteeism != null && detail.abnormalDetails.absenteeism.isNotEmpty()) {
                initAttendanceList(detail.abnormalDetails.absenteeism[0])
            } else {
                initAttendanceList(ValueChild("", "", "", "", null))
            }
        }
        viewBinding.tvLeave.setOnClickListener {
            setButton()
            viewBinding.tvLeave.setTextColor(resources.getColor(R.color.white))
            viewBinding.tvLeave.isChecked = true
            if (detail.abnormalDetails.leave != null && detail.abnormalDetails.leave.isNotEmpty()) {
                initAttendanceList(detail.abnormalDetails.leave[0])
            } else {
                initAttendanceList(ValueChild("", "", "", "", null))
            }
        }
        viewBinding.tvEarly.setOnClickListener {
            setButton()
            viewBinding.tvEarly.setTextColor(resources.getColor(R.color.white))
            viewBinding.tvEarly.isChecked = true
            if (detail.abnormalDetails.LeaveEarly != null && detail.abnormalDetails.LeaveEarly.isNotEmpty()) {
                initAttendanceList(detail.abnormalDetails.LeaveEarly[0])
            } else {
                initAttendanceList(ValueChild("", "", "", "", null))
            }
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

    private val dataToBind = mutableListOf<TreeNode<ValueChild>>()
    private fun initAttendanceList(data: ValueChild) {
        dataToBind.clear()
        dataToBind.addAll(convertDataToTreeNode(data))
        val adapter =
            TeacherAttendanceAdapter(R.layout.item_attendance_parents_status, dataToBind)
        viewBinding.recyclerviewAttend.layoutManager = LinearLayoutManager(
            context
        )
        viewBinding.recyclerviewAttend.adapter = adapter
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
        data: ValueChild
    ): List<TreeNode<ValueChild>> {
        val nodes: MutableList<TreeNode<ValueChild>> = ArrayList()
        val childs = mutableListOf<TreeNode<ValueChild>>()
        val item = ValueChild(data.clockName, data.time, data.name, data.type, mutableListOf())
        val treeNode: TreeNode<ValueChild> = TreeNode(item, -1)
        if (data.value != null) {
            for (childItem in data.value!!) {
                childItem.type = data.type
                val child: TreeNode<ValueChild> = TreeNode(childItem, -1)
                child.parent = treeNode
                childs.add(child)
            }
            treeNode.children = childs
        }
        nodes.add(treeNode)
        return nodes
    }

}