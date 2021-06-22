package com.yyide.chatim.adapter.attendance;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.databinding.ItemWeekMonthStatisticsListBinding;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;
import com.yyide.chatim.utils.DateUtils;

import java.util.List;

/**
 * @Description: 考勤>周统计
 * @Author: liu tao
 * @CreateDate: 2021/5/29 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/29 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class WeekStatisticsListAdapter extends RecyclerView.Adapter<WeekStatisticsListAdapter.ViewHolder> {
    private Context context;
    private List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public WeekStatisticsListAdapter(Context context, List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> data) {
        this.context = context;
        this.data = data;

    }

    public void setData(List<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean> data){
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
        final AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean weekStatisticsBean = data.get(position);
        final String name = weekStatisticsBean.getName();
        if (SpData.getIdentityInfo().staffIdentity()) {
            holder.viewBinding.tvName.setText(name);
        }

        final String status = weekStatisticsBean.getStatus();
        //0正常、1缺勤、2迟到/3早退,4请假）
        switch (status) {
            case "0":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_normal));
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(weekStatisticsBean.getDeviceName())) {
                    holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_event_name));
                } else {
                    holder.viewBinding.tvEventTimeTitle.setText(weekStatisticsBean.getDeviceName());
                }
                final String time = DateUtils.formatTime(weekStatisticsBean.getTime(), null, "HH:mm");
                holder.viewBinding.tvEventTime.setText(time);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_normal));
                break;
            case "2":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_late));
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(weekStatisticsBean.getDeviceName())) {
                    holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_event_name));
                } else {
                    holder.viewBinding.tvEventTimeTitle.setText(weekStatisticsBean.getDeviceName());
                }
                final String time1 = DateUtils.formatTime(weekStatisticsBean.getTime(), null, "HH:mm");
                holder.viewBinding.tvEventTime.setText(time1);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_late));
                break;
            case "3":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_leave_early));
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(weekStatisticsBean.getDeviceName())) {
                    holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_event_name));
                } else {
                    holder.viewBinding.tvEventTimeTitle.setText(weekStatisticsBean.getDeviceName());
                }
                final String time2 = DateUtils.formatTime(weekStatisticsBean.getTime(), null, "HH:mm");
                holder.viewBinding.tvEventTime.setText(time2);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_late_early));
                break;
            case "1":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_absence));
                holder.viewBinding.gpEventTime.setVisibility(View.GONE);
                if (!SpData.getIdentityInfo().staffIdentity()){
                    final String subjectName = weekStatisticsBean.getSubjectName();
                    final String thingName = weekStatisticsBean.getThingName();
                    final String absenceDate = weekStatisticsBean.getTime();
                    final String date = DateUtils.formatTime(absenceDate, null, "MM.dd");
                    if (!TextUtils.isEmpty(thingName)){
                        holder.viewBinding.tvName.setText(date+" "+thingName);
                    }else {
                        if (!TextUtils.isEmpty(subjectName)){
                            holder.viewBinding.tvName.setText(date+" "+subjectName);
                        }else {
                            holder.viewBinding.tvName.setText(date);
                        }

                    }
                }
                break;
            case "4":
                holder.viewBinding.tvAttendanceStatus.setText(context.getString(R.string.attendance_ask_for_leave));
                holder.viewBinding.gpEventTime.setVisibility(View.VISIBLE);
                holder.viewBinding.tvEventTimeTitle.setText(context.getString(R.string.attendance_leave_time));
                final String data1 = DateUtils.formatTime(weekStatisticsBean.getStartDate(), null, "MM.dd HH:mm");
                final String data2 = DateUtils.formatTime(weekStatisticsBean.getEndDate(), null, "MM.dd HH:mm");
                holder.viewBinding.tvEventTime.setText(data1 + "-" + data2);
                holder.viewBinding.tvEventTime.setTextColor(context.getResources().getColor(R.color.attendance_time_leave));
                break;
            default:
                break;
        }
        //final String string = context.getString(R.string.attendance_time_format);
        //holder.tv_attendance_time.setText(String.format(string,weekStatisticsBean.getTime()));
//        holder.child_recyclerview.setLayoutManager(new LinearLayoutManager(context));
//        BaseQuickAdapter adapter = new BaseQuickAdapter<WeekStatisticsBean.DataBean, BaseViewHolder>(R.layout.item_statistics_child_list) {
//            @Override
//            protected void convert(@NotNull BaseViewHolder baseViewHolder, WeekStatisticsBean.DataBean dataBean) {
//                baseViewHolder
//                        .setText(R.id.tv_name, dataBean.getTime()+" "+dataBean.getTitle())
//                        .setText(R.id.tv_attendance_time, dataBean.getStatus());
//            }
//        };
//        holder.child_recyclerview.setAdapter(adapter);
//        adapter.setList(weekStatisticsBean.getDataBeanList());
//        holder.child_recyclerview.setVisibility(weekStatisticsBean.isChecked()?View.VISIBLE:View.GONE);
//        if (weekStatisticsBean.isChecked()) {
//            holder.iv_direction.setImageResource(R.drawable.icon_arrow_up);
//        }else {
//            holder.iv_direction.setImageResource(R.drawable.icon_arrow_down);
//        }
        //设置一级点击事件
//        holder.itemView.setOnClickListener(v -> {
//                onClickedListener.onClicked(position);
//        });
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
