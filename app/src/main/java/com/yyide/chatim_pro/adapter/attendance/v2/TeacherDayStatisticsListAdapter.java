package com.yyide.chatim_pro.adapter.attendance.v2;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.databinding.ItemAttendanceDayStatisticsBinding;
import com.yyide.chatim_pro.model.AttendanceDayStatsRsp;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceDayRsp;
import com.yyide.chatim_pro.utils.DateUtils;

import java.util.List;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/5/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TeacherDayStatisticsListAdapter extends RecyclerView.Adapter<TeacherDayStatisticsListAdapter.ViewHolder> {
    private Context context;
    private List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public TeacherDayStatisticsListAdapter(Context context, List<TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance_day_statistics,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TeacherAttendanceDayRsp.DataBean.EventBasicVoListBean dayStatisticsBean = data.get(position);
        holder.binding.tvEventName.setText(dayStatisticsBean.getEventName());
        if (dayStatisticsBean.getType() == 2) {
            final String attendanceTime = String.format(context.getString(R.string.attendance_time_text), DateUtils.formatTime(dayStatisticsBean.getCourseTime(), "", "HH:mm"));
            holder.binding.tvAttendanceTime.setText(attendanceTime);
        } else {
            final String attendanceTime = String.format(context.getString(R.string.attendance_time_text), DateUtils.formatTime(dayStatisticsBean.getRequiredTime(), "", "HH:mm"));
            holder.binding.tvAttendanceTime.setText(attendanceTime);
        }

        holder.binding.tvAttendanceRate.setText(dayStatisticsBean.getSignInOutRate());
        holder.binding.tvDueNum.setText(String.valueOf(dayStatisticsBean.getTotalNumber()));
        holder.binding.tvNormalNum.setText(String.valueOf(dayStatisticsBean.getNormal()));
        holder.binding.tvAbsenceNum.setText(String.valueOf(dayStatisticsBean.getAbsenteeism()));
        holder.binding.tvAskForLeaveNum.setText(String.valueOf(dayStatisticsBean.getLeave()));
        if ("1".equals(dayStatisticsBean.getAttendanceSignInOut())){
            //签退
            holder.binding.tvLate.setText(context.getString(R.string.attendance_leave_early));
            holder.binding.tvAttendanceRateTitle.setText(context.getString(R.string.attendance_sign_out_rate));
            //holder.binding.tvAbsence.setText(context.getString(R.string.attendance_no_logout));
            holder.binding.tvAbsence.setText(context.getString(R.string.attendance_absence));
            holder.binding.tvLateNum.setText(String.valueOf(dayStatisticsBean.getEarly()));
        }else {
            holder.binding.tvLate.setText(context.getString(R.string.attendance_late));
            holder.binding.tvAbsence.setText(context.getString(R.string.attendance_absence));
            holder.binding.tvAttendanceRateTitle.setText(context.getString(R.string.attendance_rate));
            holder.binding.tvLateNum.setText(String.valueOf(dayStatisticsBean.getLate()));
        }


        holder.itemView.setOnClickListener(v ->
                onClickedListener.onClicked(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAttendanceDayStatisticsBinding binding;
        public ViewHolder(View view) {
            super(view);
            binding = ItemAttendanceDayStatisticsBinding.bind(view);
        }
    }

    public interface OnClickedListener{
        void onClicked(int position);
    }
}
