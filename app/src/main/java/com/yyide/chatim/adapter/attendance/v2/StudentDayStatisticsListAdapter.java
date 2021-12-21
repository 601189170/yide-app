package com.yyide.chatim.adapter.attendance.v2;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyide.chatim.R;
import com.yyide.chatim.activity.PhotoViewActivity;
import com.yyide.chatim.databinding.ItemStudentAttendanceDayStatisticsBinding;
import com.yyide.chatim.model.AttendanceDayStatsRsp;
import com.yyide.chatim.model.attendance.StudentAttendanceDayRsp;
import com.yyide.chatim.utils.DateUtils;

import java.util.List;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/5/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: v2
 */
public class StudentDayStatisticsListAdapter extends RecyclerView.Adapter<StudentDayStatisticsListAdapter.ViewHolder> {
    private static final String TAG = StudentDayStatisticsListAdapter.class.getSimpleName();
    private Context context;
    private List<StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public StudentDayStatisticsListAdapter(Context context, List<StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_attendance_day_statistics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final StudentAttendanceDayRsp.DataBean.AppStudentDailyStatisticalFormBean.EventFormListBean dayStatisticsBean = data.get(position);
        final String requiredTime = DateUtils.formatTime(dayStatisticsBean.getRequiredTime(), "", "HH:mm");
        final String attendanceTime = String.format(context.getString(R.string.attendance_time_text), requiredTime);
        holder.mViewBanding.tvAttendanceTime.setText(attendanceTime);

        if (dayStatisticsBean.getType() == 2) {
            holder.mViewBanding.tvEventName.setText(dayStatisticsBean.getCourseName());
        } else {
            holder.mViewBanding.tvEventName.setText(dayStatisticsBean.getSortName());
        }
        final String time = dayStatisticsBean.getSignTime();
        final String formatTime = DateUtils.formatTime(time, "", "HH:mm");
        final String eventTime = String.format(context.getString(R.string.attendance_punch_card_text), formatTime);

        //考勤状态 1 正常，2迟到，3早退，4，未签到，5请假
        //0正常、1缺勤、2迟到/3早退,4请假）
        //考勤类型 0正常 1缺勤、2迟到 3早退 4 无效打卡 5 请假 6 未打卡
        switch (Integer.parseInt(dayStatisticsBean.getAttendanceType())) {
            case 0:
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_normal));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_normal));
                holder.mViewBanding.ivEventStatus.setImageResource(R.drawable.icon_attendance_normal);
                //showFaceImage(holder.mViewBanding.ivEventFaceRecognize, dayStatisticsBean.getPath());
                holder.mViewBanding.tvEventTime.setVisibility(View.VISIBLE);
                holder.mViewBanding.tvEventTime.setText(eventTime);
                break;
            case 2:
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_late));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_late));
                holder.mViewBanding.ivEventStatus.setImageResource(R.drawable.icon_attendance_late);
                //showFaceImage(holder.mViewBanding.ivEventFaceRecognize, dayStatisticsBean.getPath());
                holder.mViewBanding.tvEventTime.setVisibility(View.VISIBLE);
                holder.mViewBanding.tvEventTime.setText(eventTime);
                break;
            case 3:
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_leave_early));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_leave_early));
                holder.mViewBanding.ivEventStatus.setImageResource(R.drawable.icon_attendance_leave_early);
                //showFaceImage(holder.mViewBanding.ivEventFaceRecognize, dayStatisticsBean.getPath());
                holder.mViewBanding.tvEventTime.setVisibility(View.VISIBLE);
                holder.mViewBanding.tvEventTime.setText(eventTime);
                break;
            case 1:
//                final String goOutStatus = dayStatisticsBean.getAttendanceSignOut();
//                if ("1".equals(goOutStatus)) {
//                    holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_no_logout));
//                } else {
//                    holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_absence));
//                }
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_absence));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_no_sign_in));
                holder.mViewBanding.tvEventTime.setVisibility(View.GONE);
                holder.mViewBanding.ivEventFaceRecognize.setVisibility(View.GONE);
                holder.mViewBanding.ivEventStatus.setImageResource(R.drawable.icon_attendance_no_sign_in);
                break;
            case 6:
//                final String goOutStatus1 = dayStatisticsBean.getAttendanceSignOut();
//                if ("1".equals(goOutStatus1)) {
//                    holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_no_logout));
//                } else {
//                    holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_no_sign_in));
//                }
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_absence));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_no_sign_in));
                holder.mViewBanding.tvEventTime.setVisibility(View.GONE);
                holder.mViewBanding.ivEventFaceRecognize.setVisibility(View.GONE);
                holder.mViewBanding.ivEventStatus.setImageResource(R.drawable.icon_attendance_no_sign_in);
                break;
            case 5:
                holder.mViewBanding.tvEventStatus.setText(context.getString(R.string.attendance_ask_for_leave));
                holder.mViewBanding.tvEventStatus.setTextColor(context.getResources().getColor(R.color.attendance_ask_for_leave));
                holder.mViewBanding.ivEventFaceRecognize.setVisibility(View.GONE);
                holder.mViewBanding.ivEventStatus.setImageResource(R.drawable.icon_attendance_ask_for_leave);
                holder.mViewBanding.tvEventTime.setVisibility(View.VISIBLE);
                final String data1 = DateUtils.formatTime(dayStatisticsBean.getLeaveStartTime(), null, "MM.dd HH:mm");
                final String data2 = DateUtils.formatTime(dayStatisticsBean.getLeaveEndTime(), null, "MM.dd HH:mm");
                final String eventTime2 = String.format(context.getString(R.string.attendance_ask_leave_text), data1 + "-" + data2);
                holder.mViewBanding.tvEventTime.setText(eventTime2);
                break;
            default:
                break;
        }
    }

    /**
     * 显示打卡图片
     *
     * @param imageView
     * @param path
     */
    private void showFaceImage(ImageView imageView, String path) {
        Log.e(TAG, "showFaceImage: " + path);
        if (TextUtils.isEmpty(path)) {
            imageView.setVisibility(View.GONE);
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        String facePath = path;
        if (!path.contains("https://") && !path.contains("http://")) {
            facePath = "http://" + path;
        }
        Glide.with(context)
                .load(facePath)
//                .placeholder(R.drawable.default_head)
                //.error(R.drawable.default_head)
                // .skipMemoryCache(true)
                // .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
        String finalFacePath = facePath;
        imageView.setOnClickListener((v) -> {
            PhotoViewActivity.start(context, finalFacePath);
        });
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

    public interface OnClickedListener {
        void onClicked(int position);
    }
}
