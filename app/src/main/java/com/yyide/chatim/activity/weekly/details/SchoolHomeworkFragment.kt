package com.yyide.chatim.activity.weekly.details

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.yyide.chatim.R
import com.yyide.chatim.activity.weekly.details.view.LineChartMarkView
import com.yyide.chatim.activity.weekly.home.WeeklyUtil
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.FragmentSchoolHomeworkWeeklyDetailsBinding
import com.yyide.chatim.databinding.ItemWeeklyAttendanceBinding
import com.yyide.chatim.databinding.ItemWeeklyChartsVerticalBinding
import com.yyide.chatim.model.*
import com.yyide.chatim.utils.DateUtils
import java.util.ArrayList

/**
 *
 * 校长查看作业统计详情
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class SchoolHomeworkFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentSchoolHomeworkWeeklyDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSchoolHomeworkWeeklyDetailsBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    companion object {
        @JvmStatic
        fun newInstance(dateTime: WeeklyDateBean.DataBean.TimesBean) =
            SchoolHomeworkFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", dateTime)
                }
            }
    }

    private fun initView() {
        initDate()
        //最多作业条目
        viewBinding.recyclerview.layoutManager = GridLayoutManager(activity, 4)
        viewBinding.recyclerview.adapter = mostAdapter
        val datas = mutableListOf<SchoolHomeWork>()
        datas.add(SchoolHomeWork("高三12班", "作业最多的班"))
        datas.add(SchoolHomeWork("一年级1班", "作业最多的班"))
        datas.add(SchoolHomeWork("高三", "作业最多的班"))
        datas.add(SchoolHomeWork("一年级", "作业最多的班"))
        mostAdapter.setList(datas)

        //各年级作业数统计
        val charts = mutableListOf<DeptAttend>()
        charts.add(DeptAttend(40.0, "一年级", 50.3))
        charts.add(DeptAttend(50.0, "二年级", 70.3))
        charts.add(DeptAttend(20.0, "三年级", 80.3))
        charts.add(DeptAttend(10.0, "四年级", 30.3))
        charts.add(DeptAttend(50.0, "五年级", 77.3))
        charts.add(DeptAttend(24.0, "六年级", 88.3))
        initLineCharts(charts)

        //科目作业柱状图
        viewBinding.layoutCharts.recyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.layoutCharts.recyclerview.adapter =
            adapterAttendance
        val datas1 = mutableListOf<SchoolAttendance>()
        datas1.add(SchoolAttendance("语文", 50.3))
        datas1.add(SchoolAttendance("数学", 30.3))
        datas1.add(SchoolAttendance("英语", 40.3))
        datas1.add(SchoolAttendance("物理", 50.3))
        datas1.add(SchoolAttendance("化学", 25.3))
        datas1.add(SchoolAttendance("历史", 20.3))
        datas1.add(SchoolAttendance("政治", 35.3))
        adapterAttendance.setList(datas1)
    }

    private var timePosition = -1
    private lateinit var dateTime: WeeklyDateBean.DataBean.TimesBean
    private fun initDate() {
        arguments?.apply {
            dateTime = getSerializable("item") as WeeklyDateBean.DataBean.TimesBean
        }
        //获取日期时间
        val dateLists = WeeklyUtil.getDateTimes()
        if (dateLists.isNotEmpty()) {
            timePosition = WeeklyUtil.getTimePosition(dateTime, dateLists)
            //dateTime = dateLists[dateLists.size - 1]
            request()
            viewBinding.tvStartTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition > 0) {
                        timePosition -= 1
                        dateTime = dateLists[timePosition]
                        request()
                    }
                }
            }
            viewBinding.tvEndTime.setOnClickListener {
                if (dateLists.isNotEmpty()) {
                    if (timePosition < (dateLists.size - 1)) {
                        timePosition += 1
                        dateTime = dateLists[timePosition]
                        request()
                    }
                }
            }
        }
    }

    private fun request() {
        if (dateTime != null) {
            viewBinding.tvStartTime.text = DateUtils.formatTime(
                dateTime.startTime,
                "yyyy-MM-dd HH:mm:ss",
                "MM/dd"
            )
            viewBinding.tvEndTime.text = DateUtils.formatTime(
                dateTime.endTime,
                "yyyy-MM-dd HH:mm:ss",
                "MM/dd"
            )
            //loading()
            //viewModel.requestSchoolAttendance(dateTime.startTime, dateTime.endTime)
        }
    }

    private fun initLineCharts(subjects: List<DeptAttend>) {
        if (subjects.isEmpty()) return
        // apply styling
        // holder.chart.setValueTypeface(mTf);
        viewBinding.lineChart.description.isEnabled = false
        viewBinding.lineChart.setDrawGridBackground(false)
        //是否可以拖动
        viewBinding.lineChart.isDragEnabled = true
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
        //设置双击缩放功能
        viewBinding.lineChart.isDoubleTapToZoomEnabled = false
        //是否显示边界
        viewBinding.lineChart.setDrawBorders(false)
        val xAxis: XAxis = viewBinding.lineChart.xAxis
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

        val leftAxis: YAxis = viewBinding.lineChart.axisLeft
        leftAxis.labelCount = 6
        leftAxis.setLabelCount(6, true)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.valueFormatter = IAxisValueFormatter { value, axis -> "${value.toInt()}%" }

        val rightAxis: YAxis = viewBinding.lineChart.axisRight
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
        val mv = LineChartMarkView(activity, xAxis.valueFormatter)
        mv.chartView = viewBinding.lineChart
        viewBinding.lineChart.marker = mv
        viewBinding.lineChart.invalidate()
    }

    /**
     * 全校各科目成绩
     */
    private val mostAdapter =
        object :
            BaseQuickAdapter<SchoolHomeWork, BaseViewHolder>(R.layout.item_weekly_attendance) {
            override fun convert(holder: BaseViewHolder, item: SchoolHomeWork) {
                val viewBind = ItemWeeklyAttendanceBinding.bind(holder.itemView)
                viewBind.tvEventName.text = item.value
                viewBind.tvAttendance.text = item.name
                viewBind.viewLine.visibility =
                    if (holder.bindingAdapterPosition == 0) View.GONE else View.VISIBLE
            }

        }

    /**
     * 全校各年级成绩
     */
    private val adapterAttendance = object :
        BaseQuickAdapter<SchoolAttendance, BaseViewHolder>(R.layout.item_weekly_charts_school_attendance) {
        override fun convert(holder: BaseViewHolder, item: SchoolAttendance) {
            val bind = ItemWeeklyChartsVerticalBinding.bind(holder.itemView)
            if (item.value > 0) {
                bind.tvProgress.text = "${item.value}%"
            }
            bind.tvWeek.text = item.name
            WeeklyUtil.setAnimation(
                bind.progressbar,
                if (item.value <= 0) 0 else item.value.toInt()
            )
        }
    }
}