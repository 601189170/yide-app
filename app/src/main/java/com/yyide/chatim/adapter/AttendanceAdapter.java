package com.yyide.chatim.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.AttendanceActivity;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.HomeAttendanceRsp;
import com.yyide.chatim.utils.InitPieChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/11/29.
 */

public class AttendanceAdapter extends LoopPagerAdapter {

    private boolean isSchool = false;
    //    -->设置各区块的颜色
    public static final int[] PIE_COLORS2 = {
            Color.rgb(145, 147, 153), Color.rgb(246, 189, 22)
            , Color.rgb(246, 108, 108), Color.rgb(55, 130, 255)
    };
    public List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean> list = new ArrayList<>();

    public AttendanceAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    private AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean getItem(int position) {
        return list.get(position);
    }

    public void setSchool(boolean isSchool) {
        this.isSchool = isSchool;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.attce_item, null);
        TextView cd = view.findViewById(R.id.cd);
        TextView qq = view.findViewById(R.id.qq);
        TextView qj = view.findViewById(R.id.qj);
        TextView attendanceName = view.findViewById(R.id.tv_attendance_type);
        TextView number = view.findViewById(R.id.tv_attendance_number);
        TextView tv_desc = view.findViewById(R.id.tv_desc);
        LinearLayout LinearLayout = view.findViewById(R.id.ll_desc);

        AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean item = list.get(position);
        attendanceName.setText(item.getAttName());

//        tv_desc.setText("0".equals(item.getAttendanceType()) ? "已签到" : "未签到");
        if (isSchool) {
            LinearLayout.setVisibility(View.GONE);
        } else {
            LinearLayout.setVisibility(View.VISIBLE);
            qq.setText(item.getAbsence() + " 人");
            cd.setText(item.getLate() + " 人");
            qj.setText(item.getLeave() + " 人");
            number.setText(item.getNumber() + " 人");
        }
        PieChart piechart = view.findViewById(R.id.piechart);
        InitPieChart.InitPieChart(((Activity) container.getContext()), piechart);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(item.getAbsence(), "缺勤"));
        entries.add(new PieEntry(item.getLeave(), "请假"));
        entries.add(new PieEntry(item.getLate(), "迟到"));
        entries.add(new PieEntry(item.getApplyNum(), "实到"));
        piechart.setCenterText((TextUtils.isEmpty(item.getRate()) ? 0 : item.getRate()) + "%\n" + "考勤率");
        piechart.setCenterTextSize(12);
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0);//设置饼块之间的间隔
        dataSet.setColors(PIE_COLORS2);//设置饼块的颜色
        dataSet.setDrawValues(false);
        PieData pieData = new PieData(dataSet);
        piechart.setData(pieData);
        piechart.invalidate();
        view.setOnClickListener(v -> {
            AttendanceActivity.start(view.getContext(), item.getPeopleType());
        });
        return view;
    }

    @Override
    public int getRealCount() {
        return list.size();
    }

    public void notifyData(List<AttendanceCheckRsp.DataBean.SchoolPeopleAllFormBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
