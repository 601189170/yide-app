package com.yyide.chatim.homemodel;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseFragment;
import com.yyide.chatim.utils.InitPieChart;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;


public class AttenceFragment extends BaseFragment {

    @BindView(R.id.cd)
    TextView cd;
    @BindView(R.id.qq)
    TextView qq;
    @BindView(R.id.qj)
    TextView qj;
    @BindView(R.id.piechart)
    PieChart piechart;
    private View mBaseView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.home_attence_fragmnet, container, false);


        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mvpPresenter.getMyData();

        InitPieChart.InitPieChart(mActivity, piechart);

        setPieChartData();
    }

    //-->设置饼图数据
    private void setPieChartData() {

//        int amOrPm = new GregorianCalendar().get(GregorianCalendar.AM_PM);
            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(1, "实到"));
            entries.add(new PieEntry(2, "请假"));
            entries.add(new PieEntry(3, "迟到"));
            entries.add(new PieEntry(4, "缺勤"));
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(0);//设置饼块之间的间隔
            dataSet.setColors(PIE_COLORS2);//设置饼块的颜色
            dataSet.setDrawValues(false);
            PieData pieData = new PieData(dataSet);
            piechart.setData(pieData);
            piechart.setCenterText(1 + "%\n" + "考勤率");
            piechart.invalidate();
//            tardyNum.setText(setStyle("实到 ", rsp.pmAttendance.pmNomalNum));
//            leaveNum.setText(setStyle("请假 ", rsp.pmAttendance.pmLeaveNum));
//            notComeNum.setText(setStyle("迟到 ", rsp.pmAttendance.pmLateNum));
//            leaveEarlyNum.setText(setStyle("缺勤 ", rsp.pmAttendance.pmNotComeNum));

    }


    //    -->设置各区块的颜色
    public static final int[] PIE_COLORS2 = {
            Color.rgb(55, 130, 255), Color.rgb(224, 235, 255)
            , Color.rgb(115, 127, 255), Color.rgb(255, 106, 110)
    };
}
