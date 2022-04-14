package com.yyide.chatim_pro.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.activity.attendance.AttendanceActivity;
import com.yyide.chatim_pro.model.AttendanceRsp;
import com.yyide.chatim_pro.utils.InitPieChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hao on 2017/11/29.
 */

public class AttendanceAdapter extends LoopPagerAdapter {

    //    -->设置各区块的颜色
    public static final int[] PIE_COLORS2 = {
            Color.rgb(17, 198, 133),
            Color.rgb(255, 195, 40),
            Color.rgb(253, 79, 69),
            Color.rgb(102, 102, 102)
    };

    public List<AttendanceRsp.DataBean.AttendanceListBean> list = new ArrayList<>();

    public AttendanceAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    private AttendanceRsp.DataBean.AttendanceListBean getItem(int position) {
        return list.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(ViewGroup container, final int position) {
        AttendanceRsp.DataBean.AttendanceListBean item = list.get(position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.attce_item, null);
        TextView cd = view.findViewById(R.id.cd);
        TextView qq = view.findViewById(R.id.qq);
        TextView qj = view.findViewById(R.id.qj);
        TextView attendanceName = view.findViewById(R.id.tv_attendance_type);
        TextView number = view.findViewById(R.id.tv_attendance_number);
        TextView tv_desc = view.findViewById(R.id.tv_desc);
        TextView tv_absenteeism_text = view.findViewById(R.id.tv_absenteeism_text);
        TextView tv_leave_text = view.findViewById(R.id.tv_leave_text);

        tv_absenteeism_text.setText("1".equals(item.getAttendanceSignInOut()) ? "未签退" : "缺勤");
        tv_leave_text.setText("1".equals(item.getAttendanceSignInOut()) ? "早退" : "迟到");
        attendanceName.setText(item.getTheme());
        tv_desc.setText(item.getEventName());
        qq.setText(item.getAbsenteeism() + " 人");
        cd.setText(("1".equals(item.getAttendanceSignInOut()) ? item.getEarly() : item.getLate()) + " 人");
        qj.setText(item.getLeave() + " 人");
        number.setText(item.getTotalNumber() + " 人");
        int absence = item.getAbsenteeism();
        int leave = item.getLeave();
        int late = item.getLate();
        int applyNum = item.getNormal();
        absence = (absence + late + applyNum + leave) > 0 ? absence : 1;

        PieChart piechart = view.findViewById(R.id.piechart);
        piechart.setTouchEnabled(false);
        InitPieChart.InitPieChart(((Activity) container.getContext()), piechart);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(absence, "缺勤"));
        entries.add(new PieEntry(item.getLeave(), "请假"));
        entries.add(new PieEntry("1".equals(item.getAttendanceSignInOut()) ? item.getEarly() : item.getLate(), "迟到"));
        entries.add(new PieEntry(item.getNormal(), "实到"));
        String desc = "1".equals(item.getAttendanceSignInOut()) ? "签退率" : "出勤率";
        piechart.setCenterText((TextUtils.isEmpty(item.getAttendanceSignInOut()) ? 0 : item.getSignInOutRate()) + "%\n" + desc);
        piechart.setCenterTextSize(12);
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0);//设置饼块之间的间隔
        dataSet.setColors(PIE_COLORS2);//设置饼块的颜色
        dataSet.setDrawValues(false);
        PieData pieData = new PieData(dataSet);
        piechart.setData(pieData);
        piechart.invalidate();

        view.setOnClickListener(v -> {
            AttendanceActivity.start(view.getContext(), item);
        });
        piechart.setOnClickListener(v -> AttendanceActivity.start(view.getContext(), item));
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
