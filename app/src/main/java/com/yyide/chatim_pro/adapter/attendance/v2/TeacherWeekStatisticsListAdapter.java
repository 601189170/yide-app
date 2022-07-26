package com.yyide.chatim_pro.adapter.attendance.v2;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.databinding.ItemStatisticsListBinding;
import com.yyide.chatim_pro.model.AttendanceWeekStatsRsp;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceWeekMonthRsp;
import com.yyide.chatim_pro.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 考勤>周统计
 * @Author: liu tao
 * @CreateDate: 2021/5/29 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/29 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TeacherWeekStatisticsListAdapter extends RecyclerView.Adapter<TeacherWeekStatisticsListAdapter.ViewHolder> {
    private Context context;
    private List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public TeacherWeekStatisticsListAdapter(Context context, List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> data) {
        this.context = context;
        this.data = data;

    }

    public void setData(List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> data){
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
        Log.e("TeacherWeek", "onBindViewHolder: "+position );
        final TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean weekStatisticsBean = data.get(position);
        final String name = weekStatisticsBean.getUserName();
        holder.viewBinding.tvName.setText(name);

        final String string = context.getString(R.string.attendance_time_format);
        holder.viewBinding.tvAttendanceTime.setText(String.format(string,weekStatisticsBean.getSectionNumber()));
        if (weekStatisticsBean.getChecked()){
//            final int[] index = {0};
//            int page = 50;
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context);
            holder.viewBinding.childRecyclerview.setLayoutManager(mLinearLayoutManager);
            BaseQuickAdapter adapter = new BaseQuickAdapter<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean.CourseInfoFormListBean, BaseViewHolder>(R.layout.item_statistics_child_list) {
                @Override
                protected void convert(@NotNull BaseViewHolder baseViewHolder, TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean.CourseInfoFormListBean dataBean) {
//                    baseViewHolder.setIsRecyclable(false);
                    if (!TextUtils.isEmpty(dataBean.getSortName())) {
                        String date = DateUtils.formatTime(dataBean.getRequiredTime(), null, "MM.dd");
                        baseViewHolder.setText(R.id.tv_name, date + " " + dataBean.getSortName());
                    } else if(TextUtils.isEmpty(dataBean.getSortName()) && !TextUtils.isEmpty(dataBean.getEventName())){
                        String date = DateUtils.formatTime(dataBean.getRequiredTime(), null, "MM.dd");
                        baseViewHolder.setText(R.id.tv_name, date + " " + dataBean.getEventName());
                    } else {
                        String date = DateUtils.formatTime(dataBean.getCourseStartTime(), null, "MM.dd");
                        if (Objects.equals(dataBean.getAttendanceType(), "1") || "6".equals(dataBean.getAttendanceType())) {
                            baseViewHolder.setText(R.id.tv_name, date + " " + dataBean.getCourseInfo());
                            baseViewHolder.setVisible(R.id.tv_course_name,true);
                            baseViewHolder.setText(R.id.tv_course_name,dataBean.getCourseName());
                        } else {
                            baseViewHolder.setVisible(R.id.tv_course_name,true);
                            baseViewHolder.setText(R.id.tv_name, date + " " + dataBean.getCourseInfo());
                            baseViewHolder.setText(R.id.tv_course_name,dataBean.getCourseName());
                        }

                    }

                    String status = dataBean.getAttendanceType();
                    if (status == null){
                        status = "";
                    }
                    //0正常、1缺勤、2迟到/3早退,4请假）
                    //考勤类型 0正常 1缺勤、2迟到 3早退 4 无效打卡 5 请假 6 未打卡
                    switch (status) {
                        case "0":
                            baseViewHolder.setVisible(R.id.tv_attendance_status,true);
                            baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_normal));
                            baseViewHolder.getView(R.id.gp_event_time).setVisibility(View.GONE);
                            baseViewHolder.getView(R.id.gp_leave_event_time).setVisibility(View.GONE);
                            break;
                        case "1":
                            baseViewHolder.setVisible(R.id.tv_attendance_status,true);
                            baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_absence));
                            baseViewHolder.getView(R.id.gp_event_time).setVisibility(View.GONE);
                            baseViewHolder.getView(R.id.gp_leave_event_time).setVisibility(View.GONE);
                            break;
                        case "6":
                            final String attendanceSignInOut = dataBean.getAttendanceSignInOut();
                            if ("1".equals(attendanceSignInOut)) {
                                baseViewHolder.setText(R.id.tv_attendance_status, context.getString(R.string.attendance_no_logout));
                            } else {
                                baseViewHolder.setText(R.id.tv_attendance_status, context.getString(R.string.attendance_no_sign_in));
                            }
                            baseViewHolder.setVisible(R.id.tv_attendance_status,true);
                            baseViewHolder.getView(R.id.gp_event_time).setVisibility(View.GONE);
                            baseViewHolder.getView(R.id.gp_leave_event_time).setVisibility(View.GONE);
                            break;
                        case "2":
                            baseViewHolder.getView(R.id.gp_event_time).setVisibility(View.VISIBLE);
                            baseViewHolder.getView(R.id.gp_leave_event_time).setVisibility(View.GONE);
                            baseViewHolder.setVisible(R.id.tv_attendance_status,false);
                            baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_late));
                            if (!TextUtils.isEmpty(dataBean.getClockName())){
                                baseViewHolder.setText(R.id.tv_event_time_title,dataBean.getClockName());
                            }else {
                                baseViewHolder.setText(R.id.tv_event_time_title,context.getString(R.string.attendance_event_name));
                            }
                            final String date1 = DateUtils.formatTime(dataBean.getSignInTime(), null, "HH:mm");
                            baseViewHolder.setText(R.id.tv_event_time,date1);
                            baseViewHolder.setTextColor(R.id.tv_event_time,context.getResources().getColor(R.color.attendance_time_late));
                            break;
                        case "3":
                            baseViewHolder.getView(R.id.gp_event_time).setVisibility(View.VISIBLE);
                            baseViewHolder.getView(R.id.gp_leave_event_time).setVisibility(View.GONE);
                            baseViewHolder.setVisible(R.id.tv_attendance_status,false);
                            baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_leave_early));
                            if (!TextUtils.isEmpty(dataBean.getClockName())){
                                baseViewHolder.setText(R.id.tv_event_time_title,dataBean.getClockName());
                            }else {
                                baseViewHolder.setText(R.id.tv_event_time_title,context.getString(R.string.attendance_event_name));
                            }
                            final String date2 = DateUtils.formatTime(dataBean.getSignInTime(), null, "HH:mm");
                            baseViewHolder.setText(R.id.tv_event_time,date2);
                            baseViewHolder.setTextColor(R.id.tv_event_time,context.getResources().getColor(R.color.attendance_leave_early));
                            break;
                        case "5":
                            baseViewHolder.getView(R.id.gp_event_time).setVisibility(View.GONE);
                            baseViewHolder.getView(R.id.gp_leave_event_time).setVisibility(View.VISIBLE);
                            baseViewHolder.setText(R.id.tv_attendance_status,context.getString(R.string.attendance_ask_for_leave));
                            baseViewHolder.setVisible(R.id.tv_attendance_status,false);
                            baseViewHolder.setText(R.id.tv_leave_event_time_title,context.getString(R.string.attendance_leave_time));
                            baseViewHolder.setTextColor(R.id.tv_leave_event_time,context.getResources().getColor(R.color.attendance_time_leave));
                            final String data1 = DateUtils.formatTime(dataBean.getLeaveStartTime(), null, "MM.dd HH:mm");
                            final String data2 = DateUtils.formatTime(dataBean.getLeaveEndTime(), null, "MM.dd HH:mm");
                            baseViewHolder.setText(R.id.tv_leave_event_time,data1+"-"+data2);
                            break;

                        default:
                            baseViewHolder.setVisible(R.id.tv_attendance_status,false);
                            baseViewHolder.getView(R.id.gp_event_time).setVisibility(View.GONE);
                            baseViewHolder.getView(R.id.gp_leave_event_time).setVisibility(View.GONE);
                            break;
                    }
                }
            };
//            final List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean.CourseInfoFormListBean> list = new ArrayList<>();
            final List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean.CourseInfoFormListBean> courseInfoFormList = weekStatisticsBean.getCourseInfoFormList();
//            if (courseInfoFormList != null && courseInfoFormList.size() <= page) {
//                list.addAll(courseInfoFormList);
//            } else if (courseInfoFormList != null) {
//                list.addAll(courseInfoFormList.subList(index[0], page));
//            }
            holder.viewBinding.childRecyclerview.setAdapter(adapter);
//            holder.viewBinding.childRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    // canScrollVertically(1) 为false 的时候滑动到底部了
//                    Log.e("TAG", "onScrolled: ");
//                    if (!holder.viewBinding.childRecyclerview.canScrollVertically(1)) {
//                        index[0]++;
//                        if (courseInfoFormList == null){
//                            return;
//                        }
//                        if (list.size() >= courseInfoFormList.size()) {
//                            return;
//                        }
//                        if ((index[0] + 1) * page <= courseInfoFormList.size()) {
//                            list.addAll(courseInfoFormList.subList(index[0] * page, (index[0] + 1) * page));
//                            holder.viewBinding.childRecyclerview.post(() -> {
//                                adapter.setList(list);
//                            });
//
//                        } else {
//                            list.addAll(courseInfoFormList.subList(index[0] * page, courseInfoFormList.size()));
//                            holder.viewBinding.childRecyclerview.post(() -> {
//                                adapter.setList(list);
//                            });
//                        }
//                    }
//                }
//            });
            adapter.setList(courseInfoFormList);
        }
        if (weekStatisticsBean.getChecked()) {
            holder.viewBinding.ivDirection.setImageResource(R.drawable.icon_arrow_up);
        }else {
            holder.viewBinding.ivDirection.setImageResource(R.drawable.icon_arrow_down);
        }
        holder.viewBinding.childRecyclerview.setVisibility(weekStatisticsBean.getChecked()?View.VISIBLE:View.GONE);
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
