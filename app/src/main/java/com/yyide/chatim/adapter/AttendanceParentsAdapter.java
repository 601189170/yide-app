package com.yyide.chatim.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.yyide.chatim.utils.InitPieChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/11/29.
 */

public class AttendanceParentsAdapter extends LoopPagerAdapter {

    public List<AttendanceCheckRsp.DataBean.AttendancesFormBean> list = new ArrayList<>();

    public AttendanceParentsAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    private AttendanceCheckRsp.DataBean.AttendancesFormBean getItem(int position) {
        return list.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_attendance_parents, null);
        AttendanceCheckRsp.DataBean.AttendancesFormBean item = list.get(position);
        if(item != null && item.getStudents() != null){
            AttendanceCheckRsp.DataBean.AttendancesFormBean.Students students = item.getStudents();
            TextView attendanceName = view.findViewById(R.id.tv_attendance_type);
            TextView tv_attendance_time = view.findViewById(R.id.tv_attendance_time);
            TextView tv_attendance_status = view.findViewById(R.id.tv_attendance_status);
            attendanceName.setText(students.getName());
            tv_attendance_status.setText(students.getStatusType());
            tv_attendance_time.setText("打卡时间：" + students.getApplyDate());
            view.setOnClickListener(v -> {
                AttendanceActivity.start(view.getContext(), item.getPeopleType(), position);
            });
        }
        return view;
    }

    @Override
    public int getRealCount() {
        return list.size();
    }

    public void notifyData(List<AttendanceCheckRsp.DataBean.AttendancesFormBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
