package com.yyide.chatim.adapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.ClassesHonorPhotoListActivity;
import com.yyide.chatim.model.ClassesBannerRsp;
import com.yyide.chatim.utils.InitPieChart;
import com.yyide.chatim.utils.VHUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/11/29.
 */

public class AttendanceAdapter extends LoopPagerAdapter {
    //    -->设置各区块的颜色
    public static final int[] PIE_COLORS2 = {
            Color.rgb(55, 130, 255), Color.rgb(224, 235, 255)
            , Color.rgb(115, 127, 255), Color.rgb(255, 106, 110)
    };
    public List<String> list = new ArrayList<>();

    public AttendanceAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    private String getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.attce_item, null);
        TextView cd = view.findViewById(R.id.cd);
        TextView qq = view.findViewById(R.id.qq);
        TextView qj = view.findViewById(R.id.qj);
        TextView tv_desc = view.findViewById(R.id.tv_desc);


        PieChart piechart = view.findViewById(R.id.piechart);
        InitPieChart.InitPieChart(((Activity) container.getContext()), piechart);
        //        总人数：50
//        考勤1: 出入校考勤-入校，出勤率92%，迟到2人，缺勤2人，请假2人
//        考勤2：课堂考勤-第1节语文，出勤率94%，迟到1人，缺勤1人，请假2人
//        int amOrPm = new GregorianCalendar().get(GregorianCalendar.AM_PM);
        List<PieEntry> entries = new ArrayList<>();
        if (position == 0) {
            cd.setText("迟到(2人)");
            qq.setText("缺勤(2人)");
            qj.setText("请假(2人)");
            entries.add(new PieEntry(44, "实到"));
            entries.add(new PieEntry(2, "请假"));
            entries.add(new PieEntry(2, "迟到"));
            entries.add(new PieEntry(2, "缺勤"));
            tv_desc.setText("出入校考勤-入校");
            piechart.setCenterText(92 + "%\n" + "考勤率");
        } else {
            cd.setText("迟到(1人)");
            qq.setText("缺勤(2人)");
            qj.setText("请假(1人)");
            entries.add(new PieEntry(46, "实到"));
            entries.add(new PieEntry(1, "请假"));
            entries.add(new PieEntry(1, "迟到"));
            entries.add(new PieEntry(2, "缺勤"));
            tv_desc.setText("课堂考勤-第1节语文");
            piechart.setCenterText(94 + "%\n" + "考勤率");
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0);//设置饼块之间的间隔
        dataSet.setColors(PIE_COLORS2);//设置饼块的颜色
        dataSet.setDrawValues(false);
        PieData pieData = new PieData(dataSet);
        piechart.setData(pieData);
        piechart.invalidate();
        return view;
    }

    @Override
    public int getRealCount() {
        return list.size();
    }

    public void notifyData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //-->设置饼图数据
    private void setPieChartData(PieChart piechart) {

    }
}
