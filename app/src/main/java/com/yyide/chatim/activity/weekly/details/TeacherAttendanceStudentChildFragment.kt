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
import com.yyide.chatim.activity.weekly.details.adapter.StudentAttendanceAdapter
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentTeacherChildWeeklyAttendanceBinding
import com.yyide.chatim.model.AttendItem
import com.yyide.chatim.model.StudentDetail
import com.yyide.chatim.model.StudentNumber
import com.yyide.chatim.model.StudentValue
import com.yyide.chatim.widget.treeview.adapter.SingleLayoutTreeAdapter
import com.yyide.chatim.widget.treeview.model.TreeNode
import java.util.*


/**
 * Child
 * 班主任查看学生考勤周报 tab
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherAttendanceStudentChildFragment : BaseFragment() {

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
        fun newInstance(detail: StudentDetail, title: String) =
            TeacherAttendanceStudentChildFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", detail)
                    putString("tabTitle", title)
                }
            }
    }

    private lateinit var detail: StudentDetail

    private fun initView() {
        arguments?.apply {
            detail = getSerializable("item") as StudentDetail
            viewBinding.textView.text =
                getString(R.string.weekly_desc_name, getString("tabTitle", ""))
            viewBinding.tvAbsenteeism.isChecked = true
            viewBinding.tvAbsenteeism.setTextColor(resources.getColor(R.color.white))
            initAttendanceList(detail.details.absenteeism)
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
        barDataSet.valueTextSize = 12f
        barDataSet.valueFormatter =
            IValueFormatter { value, entry, dataSetIndex, viewPortHandler -> "${value.toInt()}" }
        // 设置栏阴影颜色
//        barDataSet.barShadowColor = Color.parseColor("#01000000")
        val barDataSets = mutableListOf<BarDataSet>()
        barDataSet.valueTextColor = Color.parseColor("#909399")
        barDataSet.valueTextSize = 10f
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
            initAttendanceList(detail.details.late)
        }
        viewBinding.tvAbsenteeism.setOnClickListener {
            setButton()
            viewBinding.tvAbsenteeism.isChecked = true
            viewBinding.tvAbsenteeism.setTextColor(resources.getColor(R.color.white))
            initAttendanceList(detail.details.absenteeism)
        }
        viewBinding.tvLeave.setOnClickListener {
            setButton()
            viewBinding.tvLeave.setTextColor(resources.getColor(R.color.white))
            viewBinding.tvLeave.isChecked = true
            initAttendanceList(detail.details.leave)
        }
        viewBinding.tvEarly.setOnClickListener {
            setButton()
            viewBinding.tvEarly.setTextColor(resources.getColor(R.color.white))
            viewBinding.tvEarly.isChecked = true
            initAttendanceList(detail.details.LeaveEarly)
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

    private val dataToBind = mutableListOf<TreeNode<StudentValue>>()
    private fun initAttendanceList(datas: List<StudentValue>) {
        dataToBind.clear()
        if (datas.isNotEmpty()) {
            dataToBind.addAll(convertDataToTreeNode(datas))
        }
        val adapter =
            StudentAttendanceAdapter(R.layout.item_attendance_parents_status, dataToBind)
        viewBinding.recyclerviewAttend.layoutManager = LinearLayoutManager(
            context
        )
        viewBinding.recyclerviewAttend.adapter = adapter
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
        datas: List<StudentValue>
    ): List<TreeNode<StudentValue>> {
        val nodes: MutableList<TreeNode<StudentValue>> = ArrayList()
        for (item in datas) {
            val childs = mutableListOf<TreeNode<StudentValue>>()
            val itemChild =
                StudentValue("", item.type, item.name, "", "", "", "", "", mutableListOf())
            val treeNode: TreeNode<StudentValue> = TreeNode(itemChild, -1)
            for (childItem in item.value) {
                childItem.type = item.type
                val child: TreeNode<StudentValue> = TreeNode(childItem, -1)
                child.parent = treeNode
                childs.add(child)
            }
            treeNode.children = childs
            nodes.add(treeNode)
        }
        return nodes
    }
}