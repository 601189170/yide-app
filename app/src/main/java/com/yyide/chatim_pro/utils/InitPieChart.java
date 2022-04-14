package com.yyide.chatim_pro.utils;

import android.app.Activity;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.yyide.chatim_pro.R;


public class InitPieChart {

    public static void InitPieChart(Activity activity, PieChart pieChart) {
        pieChart.setUsePercentValues(false);//设置使用百分比（后续有详细介绍）
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setExtraOffsets(0, 0, 0, 0); //设置边距
        pieChart.setHoleRadius(70);
        pieChart.setDragDecelerationFrictionCoef(0.95f);//设置摩擦系数（值越小摩擦系数越大）
        pieChart.setCenterTextColor(activity.getResources().getColor(R.color.black3));
        pieChart.setRotationEnabled(false);//是否可以旋转
        pieChart.setHighlightPerTapEnabled(false);//点击是否放大
        pieChart.setCenterTextSize(9f);//设置环中文字的大小
        pieChart.setDrawCenterText(true);//设置绘制环中文字
        pieChart.setRotationAngle(0);//设置旋转角度
        pieChart.setTransparentCircleRadius(0);//设置半透明圆环的半径,看着就有一种立体的感觉
        //这个方法为true就是环形图，为false就是饼图
        pieChart.setDrawHoleEnabled(true);
        //设置环形中间空白颜色是白色
        pieChart.setHoleColor(Color.WHITE);
        //设置半透明圆环的颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        //设置半透明圆环的透明度
        pieChart.setTransparentCircleAlpha(100);

        //图例设置
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        //设置初始旋转角度
        pieChart.setRotationAngle(0);
        pieChart.setDrawSliceText(false);
        pieChart.highlightValues(null);

        pieChart.highlightValues(null);
        //设置饼图数据
        pieChart.animateX(1500, Easing.EasingOption.EaseInOutQuad);//数据显示动画
    }
}
