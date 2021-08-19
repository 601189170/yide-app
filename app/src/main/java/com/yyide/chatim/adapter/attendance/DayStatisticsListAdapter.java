package com.yyide.chatim.adapter.attendance;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.databinding.ItemAttendanceDayStatisticsBinding;
import com.yyide.chatim.model.AttendanceDayStatsRsp;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;
import com.yyide.chatim.utils.DateUtils;

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
public class DayStatisticsListAdapter extends RecyclerView.Adapter<DayStatisticsListAdapter.ViewHolder> {
    private Context context;
    private List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public DayStatisticsListAdapter(Context context, List<AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean> data) {
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
        final AttendanceDayStatsRsp.DataBean.AttendancesFormBean.StudentListsBean dayStatisticsBean = data.get(position);
        final String thingName = dayStatisticsBean.getThingName();
        if (!TextUtils.isEmpty(thingName)) {
            holder.binding.tvEventName.setText(dayStatisticsBean.getThingName());
        }else {
            final int section = dayStatisticsBean.getSection();
            String sectionUppercase = DateUtils.sectionDesc(context,section);
            holder.binding.tvEventName.setText(sectionUppercase+" "+ dayStatisticsBean.getSubjectName());
        }
        final String attendanceTime = String.format(context.getString(R.string.attendance_time_text), dayStatisticsBean.getApplyDate());
        holder.binding.tvAttendanceTime.setText(attendanceTime);
        holder.binding.tvAttendanceRate.setText(dayStatisticsBean.getRate());
        holder.binding.tvDueNum.setText(String.valueOf(dayStatisticsBean.getNumber()));
        holder.binding.tvNormalNum.setText(String.valueOf(dayStatisticsBean.getApplyNum()));
        holder.binding.tvAbsenceNum.setText(String.valueOf(dayStatisticsBean.getAbsence()));
        holder.binding.tvAskForLeaveNum.setText(String.valueOf(dayStatisticsBean.getLeave()));
        if (dayStatisticsBean.getGoOutStatus() == 1){
            //签退
            holder.binding.tvLate.setText("早退");
            holder.binding.tvAttendanceRateTitle.setText("签退率");
            holder.binding.tvAbsence.setText("未签退");
            holder.binding.tvLateNum.setText(String.valueOf(dayStatisticsBean.getLeaveEarly()));
        }else {
            holder.binding.tvLate.setText("迟到");
            holder.binding.tvAbsence.setText("缺勤");
            holder.binding.tvAttendanceRateTitle.setText("出勤率");
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
