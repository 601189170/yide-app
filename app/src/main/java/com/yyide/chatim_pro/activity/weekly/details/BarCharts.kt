package com.yyide.chatim_pro.activity.weekly.details

import android.graphics.Color
import android.graphics.Matrix
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.yyide.chatim_pro.model.SchoolHomeAttend
import java.util.ArrayList

class BarCharts {

    /**
     * 这个方法是初始化数据的
     */
    fun getBarData(numbers: ArrayList<SchoolHomeAttend>?): BarData {
        val yValues = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()
        if (numbers != null && numbers.size > 0 && numbers.size < 2) {
            numbers.add(SchoolHomeAttend("", "0", 0.0))
            numbers.add(SchoolHomeAttend("", "0", 0.0))
        }
        numbers?.forEachIndexed { index, item ->
            yValues.add(
                BarEntry(
                    index.toFloat(),
                    if (item.value != null) item.value.toFloat() else 0f
                )
            )
            colors.add(Color.parseColor("#11C685"))
        }
        // y轴的数据集合
        val barDataSet = BarDataSet(yValues, "")
        barDataSet.colors = colors
        barDataSet.valueTextSize = 12f
        barDataSet.valueFormatter =
            IValueFormatter { value, entry, dataSetIndex, viewPortHandler -> if (value > 0) "${value}%" else "" }
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

    fun showBarChart(
        barChart: BarChart,
        barData: BarData?,
        isSlither: Boolean
    ) {
        //设置值显示在柱状图的上边或者下边
        barChart.setDrawValueAboveBar(true)
        //设置绘制网格背景
        barChart.setDrawGridBackground(true)
        //设置双击缩放功能
        barChart.isDoubleTapToZoomEnabled = false
        //禁止点击柱状图（每个柱子）
        barChart.isHighlightPerTapEnabled = false
        barChart.isClickable = false
        //设置规模Y启用
        barChart.isScaleYEnabled = false
        //设置规模X启用
        barChart.isScaleXEnabled = false
        //启用设置阻力
        barChart.setScaleEnabled(false)
        //设置每个拖动启用的高亮显示
        barChart.isHighlightPerDragEnabled = false
        // 设置硬件加速功能
        barChart.setHardwareAccelerationEnabled(true)
        // 设置绘制标记视图
        barChart.setDrawMarkerViews(true)
        //隐藏右下角英文
        barChart.description.isEnabled = false
        // 设置启用日志
        barChart.isLogEnabled = true
        // 设置拖动减速功能
        barChart.isDragDecelerationEnabled = true
        // 数据描述
        //        barChart.setDescription("")
        barChart.setNoDataText("O__O …")
        // 是否显示表格颜色
        barChart.setDrawGridBackground(false)
        /**
         * 下面这几个属性你们可以试试 挺有意思的
         */
        // 设置是否可以触摸
        barChart.setTouchEnabled(isSlither)
        // 是否可以拖拽
        barChart.isDragEnabled = isSlither //放大可拖拽
        // 是否可以缩放
        barChart.setScaleEnabled(false)
        // 集双指缩放
        barChart.setPinchZoom(false)
        // 设置背景
        barChart.setBackgroundColor(Color.parseColor("#01000000"))
        // 如果打开，背景矩形将出现在已经画好的绘图区域的后边。
        //        barChart.setDrawGridBackground(false)
        // 集拉杆阴影
        barChart.setDrawBarShadow(false)
        // 图例
        barChart.legend.isEnabled = false
        // 设置数据
        barChart.data = barData
        // 隐藏右边的坐标轴 (就是右边的0 - 100 - 200 - 300 ... 和图表中横线)
        barChart.axisRight.isEnabled = false
        // 隐藏左边的左边轴 (同上)
        // 网格背景颜色
        barChart.setGridBackgroundColor(Color.parseColor("#E4E7ED"))
        // 是否显示表格颜色
        barChart.setDrawGridBackground(false)
        // 设置边框颜色
        barChart.setBorderColor(Color.parseColor("#E4E7ED"))
        // 拉杆阴影
        barChart.setDrawBarShadow(false)
        // 打开或关闭绘制的图表边框。（环绕图表的线）
        barChart.setDrawBorders(false)
        val mLegend: Legend = barChart.legend // 设置比例图标示
        // 设置窗体样式
//        mLegend.form = Legend.LegendForm.CIRCLE
        // 字体
        mLegend.formSize = 10f
        // 字体颜色
        mLegend.textColor = Color.parseColor("#909399")
        val axisLeft = barChart.axisLeft
        axisLeft.enableGridDashedLine(10f, 10f, 0f)
        axisLeft.textColor = Color.parseColor("#909399")
        axisLeft.textSize = 10f
        axisLeft.axisMinimum = 0f
        var maxSize = 10f
        //设置柱子的左侧数值
        if (barData != null && barData.dataSets != null) {
            barData.dataSets.forEach { item ->
                maxSize = item.yMax
            }
        }
        axisLeft.axisMaximum = if (maxSize <= 5) 10f else maxSize + 50
        axisLeft.setLabelCount(6, true)
        axisLeft.valueFormatter = IAxisValueFormatter { value, axis ->
            "${value.toInt()}"
        }
        if (isSlither) {
            //当为true时,放大图
            // 为了使 柱状图成为可滑动的,将水平方向 放大 2.5倍
            barChart.invalidate()
            val mMatrix = Matrix()
            mMatrix.postScale(2f, 1f)
            barChart.viewPortHandler.refresh(mMatrix, barChart, false)
            barChart.animateY(1000)
        } else {
            //当为false时 不对图进行操作
            barChart.animateY(1000)
        }
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
//        xAxis.granularity = 0f // only intervals of 1 day
        xAxis.labelCount = 4
        xAxis.mLabelHeight = 0
        xAxis.textColor = Color.parseColor("#909399")
        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
            return@IAxisValueFormatter when (value.toInt()) {
                1 -> {
                    "迟到"
                }
                2 -> {
                    "请假"
                }
                3 -> {
                    "早退"
                }
                else -> {
                    "缺勤"
                }
            }
        }
    }

    fun showBarChart2(
        barChart: BarChart,
        barData: BarData?,
        datas: List<SchoolHomeAttend>?,
        isSlither: Boolean
    ) {
        //设置值显示在柱状图的上边或者下边
        barChart.setDrawValueAboveBar(true)
        //设置绘制网格背景
        barChart.setDrawGridBackground(true)
        //设置双击缩放功能
        barChart.isDoubleTapToZoomEnabled = false
        //禁止点击柱状图（每个柱子）
        barChart.isHighlightPerTapEnabled = false
        //设置规模Y启用
        barChart.isScaleYEnabled = false
        //设置规模X启用
        barChart.isScaleXEnabled = false
        //启用设置阻力
        barChart.setScaleEnabled(false)
        //设置每个拖动启用的高亮显示
        barChart.isHighlightPerDragEnabled = false
        // 设置硬件加速功能
        barChart.setHardwareAccelerationEnabled(true)
        // 设置绘制标记视图
        barChart.setDrawMarkerViews(true)
        //隐藏右下角英文
        barChart.description.isEnabled = false
        // 设置启用日志
        barChart.isLogEnabled = true
        // 设置拖动减速功能
        barChart.isDragDecelerationEnabled = true
        // 数据描述
        //        barChart.setDescription("")
        barChart.setNoDataText("O__O …")
        // 是否显示表格颜色
        barChart.setDrawGridBackground(false)
        /**
         * 下面这几个属性你们可以试试 挺有意思的
         */
        // 设置是否可以触摸
        barChart.setTouchEnabled(isSlither)
        // 是否可以拖拽
        barChart.isDragEnabled = isSlither //放大可拖拽
        // 是否可以缩放
        barChart.setScaleEnabled(false)
        // 集双指缩放
        barChart.setPinchZoom(false)
        // 设置背景
        barChart.setBackgroundColor(Color.parseColor("#01000000"))
        // 如果打开，背景矩形将出现在已经画好的绘图区域的后边。
        //        barChart.setDrawGridBackground(false)
        // 集拉杆阴影
        barChart.setDrawBarShadow(false)
        // 图例
        barChart.legend.isEnabled = false
        // 设置数据
        barChart.data = barData
        // 隐藏右边的坐标轴 (就是右边的0 - 100 - 200 - 300 ... 和图表中横线)
        barChart.axisRight.isEnabled = false
        // 隐藏左边的左边轴 (同上)
        // 网格背景颜色
        barChart.setGridBackgroundColor(Color.parseColor("#E4E7ED"))
        // 是否显示表格颜色
        barChart.setDrawGridBackground(false)
        // 设置边框颜色
        barChart.setBorderColor(Color.parseColor("#E4E7ED"))
        // 拉杆阴影
        barChart.setDrawBarShadow(false)
        // 打开或关闭绘制的图表边框。（环绕图表的线）
        barChart.setDrawBorders(false)
        val mLegend: Legend = barChart.legend // 设置比例图标示
        // 设置窗体样式
//        mLegend.form = Legend.LegendForm.CIRCLE
        // 字体
        mLegend.formSize = 10f
        // 字体颜色
        mLegend.textColor = Color.parseColor("#909399")
        val axisLeft = barChart.axisLeft
        axisLeft.enableGridDashedLine(10f, 10f, 0f)
        axisLeft.textColor = Color.parseColor("#909399")
        axisLeft.textSize = 10f
        axisLeft.axisMinimum = 0f
        axisLeft.axisMaximum = 100f
//        axisLeft.setDrawGridLines(false)
        axisLeft.setLabelCount(6, true)
        axisLeft.enableGridDashedLine(10f, 10f, 0f)
        axisLeft.valueFormatter = IAxisValueFormatter { value, axis ->
            "${value.toInt()}%"
        }
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
//        xAxis.mLabelHeight = 0
        xAxis.textColor = Color.parseColor("#909399")
        if (datas != null) {
            xAxis.labelCount = if (datas.size > 6) datas.size else 4
            xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
                if (value > -1 && value < datas.size) {
                    return@IAxisValueFormatter datas[value.toInt()].name
                } else {
                    return@IAxisValueFormatter ""
                }
            }
            if (isSlither && datas.size > 4) {
                //当为true时,放大图
                // 为了使 柱状图成为可滑动的,将水平方向 放大 2.5倍
                barChart.invalidate()
                val mMatrix = Matrix()
                mMatrix.postScale(2f, 1f)
                barChart.viewPortHandler.refresh(mMatrix, barChart, false)
                barChart.animateY(1000)
            }
        }
        barChart.invalidate()
    }

}