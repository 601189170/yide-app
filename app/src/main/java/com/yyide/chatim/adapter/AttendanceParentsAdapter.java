package com.yyide.chatim.adapter;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.attendance.StatisticsActivity;
import com.yyide.chatim.model.AttendanceRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/11/29.
 */

public class AttendanceParentsAdapter extends LoopPagerAdapter {

    public List<AttendanceRsp.DataBean.AttendanceListBean> list = new ArrayList<>();

    public AttendanceParentsAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    private AttendanceRsp.DataBean.AttendanceListBean getItem(int position) {
        return list.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_attendance_parents, null);
        AttendanceRsp.DataBean.AttendanceListBean item = list.get(position);
        if (item != null) {
            TextView attendanceName = view.findViewById(R.id.tv_attendance_type);
            TextView tv_desc = view.findViewById(R.id.tv_desc);
            TextView tv_attendance_time = view.findViewById(R.id.tv_attendance_time);
            TextView tv_attendance_status = view.findViewById(R.id.tv_attendance_status);
            ImageView img = view.findViewById(R.id.img);
            attendanceName.setText(item.getTheme());
            tv_desc.setText(item.getEventName());
            if (!TextUtils.isEmpty(item.getAttendanceType())) {
                switch (item.getAttendanceType()) {//	考勤类型 0正常 1缺勤、2迟到 3早退 4 无效打卡 5 请假 6 未打卡
                    case "0":
                    case "2":
                    case "3":
                        tv_attendance_time.setText("打卡时间：" + item.getSignInTime());
                        img.setImageResource(R.mipmap.icon_home_attendance_normal);
                        tv_attendance_status.setTextColor(Color.parseColor("#2C8AFF"));
                        tv_attendance_status.setText("已打卡");
                        break;
                    case "1":
                    case "4":
                    case "5":
                    case "6":
                        tv_attendance_status.setTextColor(Color.parseColor("#919399"));
                        img.setImageResource(R.mipmap.icon_home_attendance_absenteeism);
                        tv_attendance_status.setText("未打卡");
                        break;
                    default:
                        break;
                }
            }
            view.setOnClickListener(v -> {
                Intent intent = new Intent(container.getContext(), StatisticsActivity.class);
                intent.putExtra("theme", item.getTheme());
                container.getContext().startActivity(intent);
            });
        }
        return view;
    }

    @Override
    public int getRealCount() {
        return list != null ? list.size() : 0;
    }

    public void notifyData(List<AttendanceRsp.DataBean.AttendanceListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
