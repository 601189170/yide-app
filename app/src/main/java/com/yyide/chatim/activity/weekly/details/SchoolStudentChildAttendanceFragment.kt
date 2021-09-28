package com.yyide.chatim.activity.weekly.details

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.details.view.LineChartMarkView
import com.yyide.chatim.databinding.*
import com.yyide.chatim.model.DeptAttend
import com.yyide.chatim.model.Detail
import com.yyide.chatim.model.TeacherAttendance
import java.util.ArrayList

/**
 *
 * 校长查看子项教师考勤
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolStudentChildAttendanceFragment : Fragment() {

    private lateinit var viewBinding: FragmentSchoolStudentChildWeeklyAttendanceBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSchoolStudentChildWeeklyAttendanceBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(item: Detail) =
            SchoolStudentChildAttendanceFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", item)
                }
            }
    }

    private fun initView() {
        viewBinding.layoutCharts.recyclerview.layoutManager =
            GridLayoutManager(activity, 7)
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
        var teacherAttendances = mutableListOf<TeacherAttendance>()
        var subjects = mutableListOf<DeptAttend>()
        arguments?.apply {
            val detail = getSerializable("item") as Detail
            teacherAttendances = detail.teacherAttend as MutableList<TeacherAttendance>
            subjects = detail.deptAttend as MutableList<DeptAttend>
        }
        adapterAttendance.setList(teacherAttendances)
        initLineCharts(subjects)
    }

    private fun initLineCharts(subjects: List<DeptAttend>) {
        if (subjects.isEmpty()) return
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        viewBinding.lineChart.description.isEnabled = false
        viewBinding.lineChart.setDrawGridBackground(false)
        //是否可以拖动
        viewBinding.lineChart.isDragEnabled = false
        //是否绘制在图表里面
        /***折线图例 标签 设置***/
        val legend = viewBinding.lineChart.legend
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.form = Legend.LegendForm.LINE
        legend.textSize = 12f
        //大小设置
        legend.formSize = 0f
        //显示位置 左下方
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        //是否绘制在图表里面
        legend.setDrawInside(false)

        //是否有触摸事件
        viewBinding.lineChart.setTouchEnabled(true)
        viewBinding.lineChart.setBackgroundColor(Color.WHITE)
        //是否显示边界
        viewBinding.lineChart.setDrawBorders(false)
        val xAxis: XAxis = viewBinding.lineChart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)

        xAxis.valueFormatter =
            IAxisValueFormatter { value, axis ->
                subjects[value.toInt() % subjects.size].name
            }

        xAxis.granularity = 1f
        xAxis.position = XAxisPosition.BOTTOM
        //虚线
        xAxis.setDrawGridLines(false)

        val leftAxis: YAxis = viewBinding.lineChart.axisLeft
        leftAxis.setLabelCount(6, true)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.valueFormatter = IAxisValueFormatter { value, axis -> "${value.toInt()}%" }
        leftAxis.labelCount = 6

        val rightAxis: YAxis = viewBinding.lineChart.axisRight
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        // set data
        // set data
        viewBinding.lineChart.data = generateDataLine(subjects)
        rightAxis.setDrawGridLines(false)
        //设置X Y轴网格线为虚线（实体线长度、间隔距离、偏移量：通常使用 0）
        //目标效果图没有左右侧Y轴，所以去掉右侧Y轴
        rightAxis.isEnabled = false
        setMarkerView(xAxis)
        // do not forget to refresh the chart
        // viewBinding.lineChart.invalidate();
        // do not forget to refresh the chart
        viewBinding.lineChart.invalidate()
        viewBinding.lineChart.animateX(750)
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Line data
     */
    private fun generateDataLine(subjects: List<DeptAttend>): LineData {
        val values1 = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()

        subjects.forEachIndexed { index, deptAttend ->
            //上周数据
            values1.add(
                Entry(
                    index.toFloat(), deptAttend.thisWeek.toFloat()
                )
            )
            //本周数据
            values2.add(
                Entry(
                    index.toFloat(), (deptAttend.lastWeek).toFloat()
                )
            )

        }

        val d1 = LineDataSet(values1, "")
        d1.lineWidth = 2f
        d1.circleRadius = 4f
        //圆圈的大小
        d1.circleHoleRadius = 9f
        //设置颜色
        d1.highLightColor = Color.rgb(77, 205, 154)
        d1.color = Color.rgb(77, 205, 154)
        d1.setCircleColor(Color.rgb(77, 205, 154))

        //是否显示圆点处的数据
        d1.setDrawValues(false)
        val d2 = LineDataSet(values2, "")
        d2.lineWidth = 2f
        d2.circleRadius = 4f
        d2.circleHoleRadius = SizeUtils.dp2px(9f).toFloat()
        //设置颜色上周
        d2.highLightColor = Color.rgb(44, 138, 255)
        d2.setCircleColor(Color.rgb(44, 138, 255))
        d2.color = Color.rgb(44, 138, 255)
        //是否显示圆点处的数据
        d2.setDrawValues(false)
//        d2.color = ColorTemplate.VORDIPLOM_COLORS[0]
//        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0])
        val sets = ArrayList<ILineDataSet>()
        sets.add(d1)
        sets.add(d2)
        return LineData(sets)
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    private fun setMarkerView(xAxis: XAxis) {
        val mv = LineChartMarkView(activity, xAxis.valueFormatter)
        mv.chartView = viewBinding.lineChart
        viewBinding.lineChart.marker = mv
        viewBinding.lineChart.invalidate()
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
        BaseQuickAdapter<TeacherAttendance, BaseViewHolder>(R.layout.item_weekly_charts_school_attendance) {
        override fun convert(holder: BaseViewHolder, item: TeacherAttendance) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            bind.tvProgress.text = "${item.rate}%"
            bind.tvWeek.text = item.name
            setAnimation(bind.progressbar, if (item.rate <= 0) 0 else item.rate.toInt())
            bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
            if (selectPosition == holder.bindingAdapterPosition) {
                bind.constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
            }
        }
    }


}