package com.yyide.chatim_pro.activity.operation.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.SizeUtils
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.yyide.chatim_pro.activity.operation.fragment.view.OperationLineChartMarkView
import com.yyide.chatim_pro.activity.operation.viewmodel.OperationChartViewModel
import com.yyide.chatim_pro.base.KTBaseFragment
import com.yyide.chatim_pro.databinding.OperationChartFragmentBinding
import com.yyide.chatim_pro.model.DeptAttend
import com.yyide.chatim_pro.utils.InitPieChart

/**
 * 作业图表
 * @author lrz
 * @Date 2022年2月21日
 */
class OperationChartFragment :
    KTBaseFragment<OperationChartFragmentBinding>(OperationChartFragmentBinding::inflate) {

    companion object {
        fun newInstance() = OperationChartFragment()
    }

    private lateinit var viewModel: OperationChartViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationChartViewModel::class.java)
    }

    /**
     * 作业统计数据
     */
    private fun setStatistics() {
        binding.tvTotal.text = ""
        binding.tvDay.text = ""
        binding.tvSubmit.text = ""
        binding.tvUnSubmit.text = ""
    }

    //    -->设置各区块的颜色
    val PIE_COLORS = intArrayOf(
        Color.rgb(44, 138, 255),
        Color.rgb(115, 222, 179)
    )

    val PIE_COLORS3 = intArrayOf(
        Color.rgb(44, 138, 255),
        Color.rgb(115, 222, 179),
        Color.rgb(98, 112, 146)
    )

    /**
     * 反馈图表数据设置
     */
    private fun setFeedBack() {
        //完成情况
        val piechart: PieChart = binding.pc
        piechart.setTouchEnabled(false)
        InitPieChart.InitPieChart(activity, piechart)
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(30f, "已完成"))
        entries.add(PieEntry(20f, "未完成"))
        piechart.centerText = "完成情况"
        piechart.setCenterTextSize(11f)
        val dataSet = PieDataSet(entries, "")
        dataSet.sliceSpace = 0f //设置饼块之间的间隔
        dataSet.setColors(*PIE_COLORS) //设置饼块的颜色
        dataSet.setDrawValues(false)
        val pieData = PieData(dataSet)
        piechart.data = pieData
        piechart.invalidate()

        //难易程度
        val piechart2: PieChart = binding.pd
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
    }

    /**
     * 配置反馈图表
     */
    private fun initLineCharts(subjects: List<DeptAttend>) {
        if (subjects.isEmpty()) return
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setDrawGridBackground(false)
        //是否可以拖动
        binding.lineChart.isDragEnabled = true
        //是否绘制在图表里面
        /***折线图例 标签 设置***/
        val legend = binding.lineChart.legend
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
        binding.lineChart.setTouchEnabled(true)
        binding.lineChart.setBackgroundColor(Color.WHITE)
        //设置双击缩放功能
        binding.lineChart.isDoubleTapToZoomEnabled = false
        //是否显示边界
        binding.lineChart.setDrawBorders(false)
        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)

        xAxis.valueFormatter =
            IAxisValueFormatter { value, axis ->
                if (value > -1 && value < subjects.size) {
                    return@IAxisValueFormatter subjects[value.toInt() % subjects.size].name
                } else {
                    return@IAxisValueFormatter ""
                }
            }

        xAxis.granularity = 1f
        xAxis.labelCount = subjects.size
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //虚线
        xAxis.setDrawGridLines(false)

        val leftAxis: YAxis = binding.lineChart.axisLeft
        leftAxis.labelCount = 6
        leftAxis.setLabelCount(6, true)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.valueFormatter = IAxisValueFormatter { value, axis -> "${value.toInt()}" }

        val rightAxis: YAxis = binding.lineChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        // set data
        // set data

        binding.lineChart.data = generateDataLine(subjects)
        rightAxis.setDrawGridLines(false)
        //设置X Y轴网格线为虚线（实体线长度、间隔距离、偏移量：通常使用 0）
        //目标效果图没有左右侧Y轴，所以去掉右侧Y轴
        rightAxis.isEnabled = false
        setMarkerView(xAxis)
        // do not forget to refresh the chart
        // viewBinding.lineChart.invalidate();
        // do not forget to refresh the chart
        binding.lineChart.invalidate()
        binding.lineChart.animateX(750)
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Line data
     */
    private fun generateDataLine(subjects: List<DeptAttend>): LineData {
        val values1 = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()
        val values = ArrayList<Entry>()
        subjects.forEachIndexed { index, deptAttend ->
            values.add(
                Entry(
                    index.toFloat(), 91f
                )
            )
            //上周数据
            values1.add(
                Entry(
                    index.toFloat(), deptAttend.lastWeek.toFloat()
                )
            )
            //本周数据
            values2.add(
                Entry(
                    index.toFloat(), (deptAttend.thisWeek).toFloat()
                )
            )

        }

        val d = LineDataSet(values, "")
        d.highLightColor = Color.argb(0, 0, 0, 0)
        d.setCircleColor(Color.argb(0, 0, 0, 0))
        d.color = Color.argb(0, 0, 0, 0)
        //圆圈的大小
        d.circleHoleRadius = 0f
        //是否显示圆点处的数据
        d.setDrawValues(false)
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
        sets.add(d)
        sets.add(d1)
        sets.add(d2)
        return LineData(sets)
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    private fun setMarkerView(xAxis: XAxis) {
        val mv = OperationLineChartMarkView(activity, xAxis.valueFormatter)
        mv.chartView = binding.lineChart
        binding.lineChart.marker = mv
        binding.lineChart.invalidate()
    }

}