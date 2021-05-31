package com.yyide.chatim.adapter.attendance;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.databinding.ItemStudentAttendanceDayStatisticsBinding;
import com.yyide.chatim.model.DayStatisticsBean;
import com.yyide.chatim.model.StudentDayStatisticsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/5/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class StudentDayStatisticsListAdapter extends RecyclerView.Adapter<StudentDayStatisticsListAdapter.ViewHolder> {
    private Context context;
    private List<StudentDayStatisticsBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public StudentDayStatisticsListAdapter(Context context, List<StudentDayStatisticsBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_attendance_day_statistics,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final StudentDayStatisticsBean dayStatisticsBean = data.get(position);
        final String attendanceTime = String.format(context.getString(R.string.attendance_time_text), dayStatisticsBean.getTime());
        holder.mViewBanding.tvAttendanceTime.setText(attendanceTime);
        holder.mViewBanding.tvEventName.setText(dayStatisticsBean.getEventName());
        final String eventTime = dayStatisticsBean.getEventTime();
        holder.mViewBanding.tvEventTime.setText(eventTime);
        //考勤状态 1 正常，2迟到，3早退，4，未签到，5请假
        switch (dayStatisticsBean.getEventStatus()){
            case 1:
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_normal));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_normal));
                holder.mViewBanding.ivEventStatus.setImageResource(R.drawable.icon_attendance_normal);
                holder.mViewBanding.ivEventFaceRecognize.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_late));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_late));
                holder.mViewBanding.ivEventStatus.setImageResource(R.drawable.icon_attendance_late);
                break;
            case 3:
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_leave_early));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_leave_early));
                break;
            case 4:
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_no_sign_in));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_no_sign_in));
                holder.mViewBanding.tvEventTime.setVisibility(View.GONE);
                holder.mViewBanding.ivEventFaceRecognize.setVisibility(View.GONE);
                break;
            case 5:
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_ask_for_leave));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_ask_for_leave));
                holder.mViewBanding.ivEventFaceRecognize.setVisibility(View.GONE);
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
        public ItemStudentAttendanceDayStatisticsBinding mViewBanding;
        public ViewHolder(View view) {
            super(view);
            //ButterKnife.bind(this, view);
            mViewBanding = ItemStudentAttendanceDayStatisticsBinding.bind(view);
        }
    }

    public interface OnClickedListener{
        void onClicked(int position);
    }
}
