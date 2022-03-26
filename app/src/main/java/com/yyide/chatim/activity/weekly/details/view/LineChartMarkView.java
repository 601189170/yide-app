package com.yyide.chatim.activity.weekly.details.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.yyide.chatim.R;

import java.text.DecimalFormat;
import java.util.List;

public class LineChartMarkView extends MarkerView {
    private TextView tv_name;
    private TextView tv_this_week;
    private TextView tv_last_week;
    private IAxisValueFormatter xAxisValueFormatter;
    DecimalFormat df = new DecimalFormat(".00");

    public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.dialog_weekly_chart_desc);
        this.xAxisValueFormatter = xAxisValueFormatter;

        tv_name = findViewById(R.id.tv_name);
        tv_this_week = findViewById(R.id.tv_this_week);
        tv_last_week = findViewById(R.id.tv_last_week);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //展示自定义X轴值 后的X轴内容
//        tv_name.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
//        tv_this_week.setText(df.format(e.getY()) + "%");
//        tv_last_week.setText(df.format(e.getY()) + "%");
        Chart chart = getChartView();
        if (chart instanceof LineChart) {
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            for (int i = 0; i < dataSetList.size(); i++) {
                LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                float y = dataSet.getValues().get((int) e.getX()).getY();
                if (i == 1) {
                    tv_last_week.setText(y + "%");
                }
                if (i == 2) {
                    tv_this_week.setText(y + "%");
                }
            }
            tv_name.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
