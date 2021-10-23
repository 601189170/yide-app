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

import androidx.constraintlayout.widget.ConstraintLayout;

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

    //    -->设置各区块的颜色
    public static final int[] PIE_COLORS2 = {
            Color.rgb(145, 147, 153),
            Color.rgb(246, 189, 22),
            Color.rgb(246, 108, 108),
            Color.rgb(55, 130, 255)
    };

    public List<AttendanceCheckRsp.DataBean.AttendancesFormBean> list = new ArrayList<>();

    public AttendanceAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    private AttendanceCheckRsp.DataBean.AttendancesFormBean getItem(int position) {
        return list.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(ViewGroup container, final int position) {
        AttendanceCheckRsp.DataBean.AttendancesFormBean.Students item = list.get(position).getStudents();
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.attce_item, null);
        TextView cd = view.findViewById(R.id.cd);
        TextView qq = view.findViewById(R.id.qq);
        TextView qj = view.findViewById(R.id.qj);
        TextView attendanceName = view.findViewById(R.id.tv_attendance_type);
        TextView number = view.findViewById(R.id.tv_attendance_number);
        TextView tv_desc = view.findViewById(R.id.tv_desc);
        TextView tv_absenteeism_text = view.findViewById(R.id.tv_absenteeism_text);
        TextView tv_leave_text = view.findViewById(R.id.tv_leave_text);

        tv_absenteeism_text.setText("1".equals(item.getGoOutStatus()) ? "未签退" : "缺勤");
        tv_leave_text.setText("1".equals(item.getGoOutStatus()) ? "早退" : "迟到");
        attendanceName.setText(item.getName());
        tv_desc.setText(TextUtils.isEmpty(item.getSubjectName()) ? item.getThingName() : item.getSubjectName());
        qq.setText(item.getAbsence() + " 人");
        if ("1".equals(item.getGoOutStatus())) {
            cd.setText(item.getLeaveEarly() + " 人");
        } else {
            cd.setText(item.getLate() + " 人");
        }
        qj.setText(item.getLeave() + " 人");
        number.setText(item.getNumber() + " 人");
        int absence = item.getAbsence();
        int leave = item.getLeave();
        int late = item.getLate();
        int applyNum = item.getApplyNum();
        absence = (absence + late + applyNum + leave) > 0 ? absence : 1;

        PieChart piechart = view.findViewById(R.id.piechart);
        piechart.setTouchEnabled(false);
        InitPieChart.InitPieChart(((Activity) container.getContext()), piechart);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(absence, "缺勤"));
        entries.add(new PieEntry(item.getLeave(), "请假"));
        entries.add(new PieEntry("1".equals(item.getGoOutStatus()) ? item.getLeaveEarly() : item.getLate(), "迟到"));
        entries.add(new PieEntry(item.getApplyNum(), "实到"));
        String desc = "1".equals(item.getGoOutStatus()) ? "签退率" : "出勤率";
        piechart.setCenterText((TextUtils.isEmpty(item.getRate()) ? 0 : item.getRate()) + "%\n" + desc);
        piechart.setCenterTextSize(12);
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0);//设置饼块之间的间隔
        dataSet.setColors(PIE_COLORS2);//设置饼块的颜色
        dataSet.setDrawValues(false);
        PieData pieData = new PieData(dataSet);
        piechart.setData(pieData);
        piechart.invalidate();

        view.setOnClickListener(v -> {
            AttendanceActivity.start(view.getContext(), position);
        });
        piechart.setOnClickListener(v -> AttendanceActivity.start(view.getContext(), position));
        return view;
    }

    @Override
    public int getRealCount() {
        return list != null ? list.size() : 0;
    }

    public void notifyData(List<AttendanceCheckRsp.DataBean.AttendancesFormBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
