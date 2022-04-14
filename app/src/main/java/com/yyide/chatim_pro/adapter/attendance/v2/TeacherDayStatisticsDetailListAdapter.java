package com.yyide.chatim_pro.adapter.attendance.v2;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.adapter.attendance.WeekStatisticsListAdapter;
import com.yyide.chatim_pro.databinding.ItemWeekMonthStatisticsListBinding;
import com.yyide.chatim_pro.model.AttendanceWeekStatsRsp;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceDayRsp;
import com.yyide.chatim_pro.utils.DateUtils;

import java.util.List;

/**
 * @Description: 考勤>日统计详情
 * @Author: liu tao
 * @CreateDate: 2021/5/29 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/29 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TeacherDayStatisticsDetailListAdapter extends RecyclerView.Adapter<TeacherDayStatisticsDetailListAdapter.ViewHolder> {
    private Context context;
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> data;

    public void setOnClickedListener(WeekStatisticsListAdapter.OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private WeekStatisticsListAdapter.OnClickedListener onClickedListener;

    public TeacherDayStatisticsDetailListAdapter(Context context, List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> data) {
        this.context = context;
        this.data = data;

    }

    public void setData(List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_week_month_statistics_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean.EventBean dayStatisticsBean = data.get(position);
        final String name = dayStatisticsBean.getUserName();
        holder.viewBinding.tvName.setText(name);

        final String status = dayStatisticsBean.getAttendanceType();
        //0正常、1缺勤、2迟到/3早退,4请假）
        //考勤类型 0正常 1缺勤、2迟到 3早退 4 无效打卡 5 请假 6 未打卡
        switch (status) {
            case "0":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_normal));
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(dayStatisticsBean.getClockName())) {
                    holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_event_name));
                } else {
                    holder.viewBinding.tvEventTimeTitle.setText(dayStatisticsBean.getClockName());
                }
                final String time = DateUtils.formatTime(dayStatisticsBean.getSignInTime(), null, "HH:mm");
                holder.viewBinding.tvEventTime.setText(time);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_normal));
                break;
            case "2":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_late));
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(dayStatisticsBean.getClockName())) {
                    holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_event_name));
                } else {
                    holder.viewBinding.tvEventTimeTitle.setText(dayStatisticsBean.getClockName());
                }
                final String time1 = DateUtils.formatTime(dayStatisticsBean.getSignInTime(), null, "HH:mm");
                holder.viewBinding.tvEventTime.setText(time1);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_late));
                break;
            case "3":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_leave_early));
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(dayStatisticsBean.getClockName())) {
                    holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_event_name));
                } else {
                    holder.viewBinding.tvEventTimeTitle.setText(dayStatisticsBean.getClockName());
                }
                final String time2 = DateUtils.formatTime(dayStatisticsBean.getSignInTime(), null, "HH:mm");
                holder.viewBinding.tvEventTime.setText(time2);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_leave_early));
                break;
            case "1":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_absence));
                holder.viewBinding.gpEventTime.setVisibility(View.GONE);
                holder.viewBinding.tvName.setText(dayStatisticsBean.getUserName());
                break;
            case "6":
                final String goOutStatus1 = dayStatisticsBean.getAttendanceSignInOut();
                if ("1".equals(goOutStatus1)) {
                    holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_no_logout));
                } else {
                    holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_no_sign_in));
                }
                //holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_absence));
                holder.viewBinding.gpEventTime.setVisibility(View.GONE);
                holder.viewBinding.tvName.setText(dayStatisticsBean.getUserName());
                break;
            case "5":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_ask_for_leave));
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_leave_time));
                final String data1 = DateUtils.formatTime(dayStatisticsBean.getLeaveStartTime(), null, "MM.dd HH:mm");
                final String data2 = DateUtils.formatTime(dayStatisticsBean.getLeaveEndTime(), null, "MM.dd HH:mm");
                holder.viewBinding.tvEventTime.setText(data1 + "-" + data2);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_leave));
                holder.viewBinding.tvName.setText(dayStatisticsBean.getUserName());
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

        private final ItemWeekMonthStatisticsListBinding viewBinding;

        public ViewHolder(View view) {
            super(view);
            viewBinding = ItemWeekMonthStatisticsListBinding.bind(view);
        }
    }

    public interface OnClickedListener {
        void onClicked(int position);
    }
}
