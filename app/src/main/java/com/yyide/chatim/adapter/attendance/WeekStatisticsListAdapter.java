package com.yyide.chatim.adapter.attendance;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.databinding.ItemStatisticsListBinding;
import com.yyide.chatim.databinding.ItemWeekMonthStatisticsListBinding;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;
import com.yyide.chatim.model.WeekStatisticsBean;
import com.yyide.chatim.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_statistics_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean weekStatisticsBean = data.get(position);
        final String name = weekStatisticsBean.getName();
        if (SpData.getIdentityInfo().staffIdentity()) {
            holder.viewBinding.tvName.setText(name);
        }else {
            final String time = weekStatisticsBean.getTime();
            String date = DateUtils.formatTime(time, null, "MM.dd");
            final String subjectName = weekStatisticsBean.getSubjectName();
            if (TextUtils.isEmpty(subjectName)){
                holder.viewBinding.tvName.setText(date);
            }else {
                holder.viewBinding.tvName.setText(subjectName);
            }
        }

        final String string = context.getString(R.string.attendance_time_format);
        holder.viewBinding.tvAttendanceTime.setText(String.format(string,weekStatisticsBean.getSpecialCount()));
        //holder.viewBinding.tvName.setText(weekStatisticsBean.getName());
        holder.viewBinding.childRecyclerview.setLayoutManager(new LinearLayoutManager(context));
        BaseQuickAdapter adapter = new BaseQuickAdapter<AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean, BaseViewHolder>(R.layout.item_statistics_child_list) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, AttendanceWeekStatsRsp.DataBean.AttendancesFormBean.StudentsBean.PeopleBean dataBean) {
                String date = DateUtils.formatTime(dataBean.getTime(), null, "MM.dd");
                if (!TextUtils.isEmpty(dataBean.getThingName())){
                    baseViewHolder.setText(R.id.tv_name, date+" "+dataBean.getThingName());
                }else if (!TextUtils.isEmpty(dataBean.getSubjectName())){
                    if (TextUtils.isEmpty(date)){
                        date = DateUtils.formatTime(dataBean.getStartDate(), null, "MM.dd");
                    }
                    if (!"4".equals(dataBean.getStatus())){
                        final int section = dataBean.getSection();
                        String sectionUppercase = DateUtils.sectionDesc(context,section);
                        baseViewHolder.setText(R.id.tv_name, date+" "+sectionUppercase+" "+dataBean.getSubjectName());
                    }else {
                        baseViewHolder.setText(R.id.tv_name, date+" "+dataBean.getSubjectName());
                    }
                }else {
                    final int section = dataBean.getSection();
                    String sectionUppercase = DateUtils.sectionDesc(context,section);
                    if (TextUtils.isEmpty(date)){
                        date = DateUtils.formatTime(dataBean.getStartDate(), null, "MM.dd");
                    }
                    baseViewHolder.setText(R.id.tv_name, date+" "+sectionUppercase);
                }

                String status = dataBean.getStatus();
                if (status == null){
                    status = "";
                }
                //0正常、1缺勤、2迟到/3早退,4请假）
                switch (status) {
                    case "0":
                        baseViewHolder.setVisible(R.id.tv_attendance_status,true);
                        baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_normal));
                        break;
                    case "1":
                        baseViewHolder.setVisible(R.id.tv_attendance_status,true);
                        baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_no_clock_in));
                        baseViewHolder.setVisible(R.id.gp_event_time,false);
                        break;
                    case "2":
                        baseViewHolder.setVisible(R.id.tv_attendance_status,false);
                        baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_late));
                        baseViewHolder.setVisible(R.id.gp_event_time,true);
                        if (!TextUtils.isEmpty(dataBean.getDeviceName())){
                            baseViewHolder.setText(R.id.tv_event_time_title,dataBean.getDeviceName());
                        }else {
                            baseViewHolder.setText(R.id.tv_event_time_title,context.getString(R.string.attendance_event_name));
                        }
                        final String date1 = DateUtils.formatTime(dataBean.getTime(), null, "HH:mm");
                        baseViewHolder.setText(R.id.tv_event_time,date1);
                        baseViewHolder.setTextColor(R.id.tv_event_time,context.getResources().getColor(R.color.attendance_time_late));
                        break;
                    case "3":
                        baseViewHolder.setVisible(R.id.tv_attendance_status,false);
                        baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_leave_early));
                        baseViewHolder.setVisible(R.id.gp_event_time,true);
                        if (!TextUtils.isEmpty(dataBean.getDeviceName())){
                            baseViewHolder.setText(R.id.tv_event_time_title,dataBean.getDeviceName());
                        }else {
                            baseViewHolder.setText(R.id.tv_event_time_title,context.getString(R.string.attendance_event_name));
                        }
                        final String date2 = DateUtils.formatTime(dataBean.getTime(), null, "HH:mm");
                        baseViewHolder.setText(R.id.tv_event_time,date2);
                        baseViewHolder.setTextColor(R.id.tv_event_time,context.getResources().getColor(R.color.attendance_time_late_early));
                        break;
                    case "4":
                        baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_ask_for_leave));
                        baseViewHolder.setVisible(R.id.tv_attendance_status,false);
                        baseViewHolder.setVisible(R.id.gp_event_time,true);
                        baseViewHolder.setText(R.id.tv_event_time_title,context.getString(R.string.attendance_leave_time));
                        baseViewHolder.setText(R.id.tv_event_time,"");
                        baseViewHolder.setTextColor(R.id.tv_event_time,context.getResources().getColor(R.color.attendance_time_leave));
                        final String data1 = DateUtils.formatTime(weekStatisticsBean.getStartDate(), null, "MM.dd HH:mm");
                        final String data2 = DateUtils.formatTime(weekStatisticsBean.getEndDate(), null, "MM.dd HH:mm");
                        baseViewHolder.setText(R.id.tv_event_time,data1+"-"+data2);
                        break;

                    default:
                        baseViewHolder.setVisible(R.id.tv_attendance_status,false);
                        baseViewHolder.setVisible(R.id.gp_event_time,false);
                        break;
                }
            }
        };
        holder.viewBinding.childRecyclerview.setAdapter(adapter);
        adapter.setList(weekStatisticsBean.getSpecialPeople());
        holder.viewBinding.childRecyclerview.setVisibility(weekStatisticsBean.isChecked()?View.VISIBLE:View.GONE);
        if (weekStatisticsBean.isChecked()) {
            holder.viewBinding.ivDirection.setImageResource(R.drawable.icon_arrow_up);
        }else {
            holder.viewBinding.ivDirection.setImageResource(R.drawable.icon_arrow_down);
        }
        //设置一级点击事件
        holder.itemView.setOnClickListener(v -> {
            onClickedListener.onClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemStatisticsListBinding viewBinding;

        public ViewHolder(View view) {
            super(view);
            viewBinding = ItemStatisticsListBinding.bind(view);
        }
    }

    public interface OnClickedListener {
        void onClicked(int position);
    }
}
