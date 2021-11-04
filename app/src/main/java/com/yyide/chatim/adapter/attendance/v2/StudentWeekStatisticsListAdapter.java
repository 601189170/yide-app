package com.yyide.chatim.adapter.attendance.v2;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.adapter.attendance.WeekStatisticsListAdapter;
import com.yyide.chatim.databinding.ItemStatisticsChildListBinding;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;
import com.yyide.chatim.model.attendance.StudentAttendanceWeekMonthRsp;
import com.yyide.chatim.utils.DateUtils;

import java.util.List;

/**
 * @Description: 考勤>日统计详情
 * @Author: liu tao
 * @CreateDate: 2021/5/29 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/11/04 14:22
 * @UpdateRemark: 更新说明
 * @Version: v2
 */
public class StudentWeekStatisticsListAdapter extends RecyclerView.Adapter<StudentWeekStatisticsListAdapter.ViewHolder> {
    private Context context;
    private List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean> data;

    public void setOnClickedListener(WeekStatisticsListAdapter.OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private WeekStatisticsListAdapter.OnClickedListener onClickedListener;

    public StudentWeekStatisticsListAdapter(Context context, List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean> data) {
        this.context = context;
        this.data = data;

    }

    public void setData(List<StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_statistics_child_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final StudentAttendanceWeekMonthRsp.DataBean.WeeksMonthStatisticalFormBean.WeeksMonthListBean.EventFormBean weekStatisticsBean = data.get(position);
        String date = DateUtils.formatTime(weekStatisticsBean.getRequiredTime(), null, "MM.dd");
        if (!TextUtils.isEmpty(weekStatisticsBean.getSortName())) {
            holder.viewBinding.tvName.setText(date + " " + weekStatisticsBean.getSortName());
        } else {
            holder.viewBinding.tvName.setText(date + " " + weekStatisticsBean.getCourseInfo());
        }
        final String status = weekStatisticsBean.getAttendanceType();
        //0正常、1缺勤、2迟到/3早退,4请假）
        //0正常 1缺勤、2迟到 3早退 4 无效打卡 5 请假 6 未打卡
        switch (status) {
            case "0":
                holder.viewBinding.tvAttendanceStatus.setVisibility(View.VISIBLE);
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_normal));
                if (TextUtils.isEmpty(weekStatisticsBean.getClockName())) {
                    holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_event_name));
                } else {
                    holder.viewBinding.tvEventTimeTitle.setText(weekStatisticsBean.getClockName());
                }
                final String time = DateUtils.formatTime(weekStatisticsBean.getRequiredTime(), null, "HH:mm");
                holder.viewBinding.tvEventTime.setText(time);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_normal));
                break;
            case "2":
                holder.viewBinding.tvAttendanceStatus.setVisibility(View.GONE);
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(weekStatisticsBean.getClockName())) {
                    holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_event_name));
                } else {
                    holder.viewBinding.tvEventTimeTitle.setText(weekStatisticsBean.getClockName());
                }
                final String time1 = DateUtils.formatTime(weekStatisticsBean.getRequiredTime(), null, "HH:mm");
                holder.viewBinding.tvEventTime.setText(time1);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_late));
                break;
            case "3":
                holder.viewBinding.tvAttendanceStatus.setVisibility(View.GONE);
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(weekStatisticsBean.getClockName())) {
                    holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_event_name));
                } else {
                    holder.viewBinding.tvEventTimeTitle.setText(weekStatisticsBean.getClockName());
                }
                final String time2 = DateUtils.formatTime(weekStatisticsBean.getRequiredTime(), null, "HH:mm");
                holder.viewBinding.tvEventTime.setText(time2);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_late_early));
                break;
            case "1":
                holder.viewBinding.tvAttendanceStatus.setVisibility(View.VISIBLE);
                holder.viewBinding.gpEventTime.setVisibility(View.GONE);
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_no_clock_in));
                break;
            case "4":
                holder.viewBinding.tvAttendanceStatus.setVisibility(View.GONE);
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                //holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_ask_for_leave));
                holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_leave_time));
                final String data1 = DateUtils.formatTime(weekStatisticsBean.getLeaveStartTime(), null, "MM.dd HH:mm");
                final String data2 = DateUtils.formatTime(weekStatisticsBean.getLeaveEndTime(), null, "MM.dd HH:mm");
                holder.viewBinding.tvEventTime.setText(data1 + "-" + data2);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_leave));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemStatisticsChildListBinding viewBinding;

        public ViewHolder(View view) {
            super(view);
            viewBinding = ItemStatisticsChildListBinding.bind(view);
        }
    }

    public interface OnClickedListener {
        void onClicked(int position);
    }
}
