package com.yyide.chatim.activity.weekly.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.home.viewmodel.ParentsViewModel
import com.yyide.chatim.activity.weekly.home.viewmodel.TeacherViewModel
import com.yyide.chatim.adapter.AttendanceAdapter
import com.yyide.chatim.databinding.FragmentParentsWeeklyBinding
import com.yyide.chatim.dialog.AttendancePop
import com.yyide.chatim.utils.InitPieChart
import java.util.*

/**
 *
 * 家长周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
open class ParentsWeeklyFragment : Fragment() {

    private lateinit var viewBinding: FragmentParentsWeeklyBinding
    private val viewModel: ParentsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentParentsWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ParentsWeeklyFragment().apply {}
    }

    private val selectLists = mutableListOf<String>()
    private val dateLists = mutableListOf<String>()
    private fun initView() {

        viewModel.parentsLiveData.observe(viewLifecycleOwner){
            val bean = it.getOrNull()
            if (null != bean) {
                viewBinding.clContent.visibility = View.VISIBLE
                viewBinding.cardViewNoData.visibility = View.GONE
            } else {//接口返回空的情况处理
                viewBinding.clContent.visibility = View.GONE
                viewBinding.cardViewNoData.visibility = View.VISIBLE
            }
        }

        viewBinding.tvEvent.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterEvent, "请选择考勤事件")
            attendancePop.setOnSelectListener { index: Int ->
                this.index = index
                viewBinding.tvEvent.text = selectLists[index]
            }
        }
        viewBinding.tvDescs.text = WeeklyUtil.getDesc()
        viewBinding.tvTime.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterDate, "请选择时间")
            attendancePop.setOnSelectListener { index: Int ->
                indexDate = index
                viewBinding.tvTime.text = dateLists[index]
            }
        }

        for (item in 1..10) {
            selectLists.add("$item + 事件")
            dateLists.add("$item + 2021年9月23日16:32:55")
        }
        viewBinding.tvEvent.text = selectLists[0]
        viewBinding.tvTime.text = dateLists[0]
        adapterEvent.setList(selectLists)
        adapterDate.setList(dateLists)
        //本周小结

        //考勤统计
        initAttendance()
        //作业统计
        initStatistical()
        //教师评语
        initComments()
    }

    private fun initAttendance() {
        val piechart: PieChart = viewBinding.attendance.piechart
        piechart.setTouchEnabled(false)
        InitPieChart.InitPieChart(activity, piechart)
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(2f, "缺勤"))
        entries.add(PieEntry(1f, "请假"))
        entries.add(PieEntry(3f, "迟到"))
        entries.add(PieEntry(10f, "实到"))
        piechart.centerText = "签到率"
        piechart.setCenterTextSize(12f)
        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 0f //设置饼块之间的间隔

        dataSet.setColors(*AttendanceAdapter.PIE_COLORS2) //设置饼块的颜色

        dataSet.setDrawValues(false)
        val pieData = PieData(dataSet)
        piechart.data = pieData
        piechart.invalidate()

        viewBinding.attendance.cd.text = "3次"
        viewBinding.attendance.qq.text = "2次"
        viewBinding.attendance.qj.text = "1次"
        viewBinding.attendance.zt.text = "0次"

        viewBinding.attendance.textView.text = "学生出入校"
        viewBinding.attendance.textView.setOnClickListener {
            val attendancePop = AttendancePop(activity, adapterAttendance, "请选择考勤事件")
            attendancePop.setOnSelectListener { index: Int ->
                indexAttendance = index
                viewBinding.attendance.textView.text = dateLists[index]
                setAttendanceData()
                adapterAttendance.notifyDataSetChanged()
            }
            adapterAttendance.setList(selectLists)
        }
    }

    private fun setAttendanceData() {

    }

    //    -->设置各区块的颜色
    val PIE_COLORS2 = intArrayOf(
        Color.rgb(44, 138, 255),
        Color.rgb(115, 222, 179)
    )
    val PIE_COLORS3 = intArrayOf(
        Color.rgb(44, 138, 255),
        Color.rgb(115, 222, 179),
        Color.rgb(98, 112, 146)
    )

    private fun initStatistical() {
        val piechart: PieChart = viewBinding.homeworkStatistical.piechart
        piechart.setTouchEnabled(false)
        InitPieChart.InitPieChart(activity, piechart)
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(30f, "已完成"))
        entries.add(PieEntry(20f, "未完成"))
        piechart.centerText = "完成情况"
        piechart.setCenterTextSize(11f)
        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 0f //设置饼块之间的间隔

        dataSet.setColors(*PIE_COLORS2) //设置饼块的颜色

        dataSet.setDrawValues(false)
        val pieData = PieData(dataSet)
        piechart.data = pieData
        piechart.invalidate()
        viewBinding.homeworkStatistical.tvComplete.text = "30"
        viewBinding.homeworkStatistical.tvUnComplete.text = "20"

        val piechart2: PieChart = viewBinding.homeworkStatistical.piechart2
        piechart2.setTouchEnabled(false)
        InitPieChart.InitPieChart(activity, piechart2)
        val entries2: MutableList<PieEntry> = ArrayList()
        entries2.add(PieEntry(10f, "很简单"))
        entries2.add(PieEntry(20f, "一般"))
        entries2.add(PieEntry(30f, "有难度"))
        piechart2.centerText = "难易程度"
        piechart2.setCenterTextSize(11f)
        val dataSet2 = PieDataSet(entries2, "")
        dataSet2.sliceSpace = 0f //设置饼块之间的间隔
        dataSet2.setColors(*PIE_COLORS3) //设置饼块的颜色
        dataSet2.setDrawValues(false)
        val pieData2 = PieData(dataSet2)
        piechart2.data = pieData2
        piechart2.invalidate()
        viewBinding.homeworkStatistical.tvSimple.text = "10"
        viewBinding.homeworkStatistical.tvGeneral.text = "20"
        viewBinding.homeworkStatistical.tvHaveDifficulty.text = "30"
    }

    private fun initComments() {
        viewBinding.comments.recyclerview.layoutManager = LinearLayoutManager(activity)
        viewBinding.comments.recyclerview.adapter = commentsAdapter
    }

    private val commentsAdapter = object :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_weekly_teacher_comments) {
        override fun convert(holder: BaseViewHolder, item: String) {

        }
    }

    private var index = 0
    private val adapterEvent = object :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.swich_class_item) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.className, item)
            holder.getView<View>(R.id.select).visibility =
                if (index == holder.adapterPosition) View.VISIBLE else View.GONE
            if (this.itemCount - 1 == holder.adapterPosition) {
                holder.getView<View>(R.id.view_line).visibility = View.GONE
            } else {
                holder.getView<View>(R.id.view_line).visibility = View.VISIBLE
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

    private var indexAttendance = 0
    private val adapterAttendance = object :
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