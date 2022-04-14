package com.yyide.chatim_pro.adapter.attendance.v2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.attendance.TeacherAttendanceWeekMonthRsp;
import com.yyide.chatim_pro.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TeacherWeekStatisticsExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> mGroup;
    private ArrayList<List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean.CourseInfoFormListBean>> mItemList;
    private final LayoutInflater mInflater;

    public TeacherWeekStatisticsExpandableListAdapter(Context context, ArrayList<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean> group, ArrayList<List<TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean.CourseInfoFormListBean>> itemList) {
        this.mContext = context;
        this.mGroup = group;
        this.mItemList = itemList;
        mInflater = LayoutInflater.from(context);
    }

    //父项的个数
    @Override
    public int getGroupCount() {
        return mGroup.size();
    }

    //某个父项的子项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return mItemList.get(groupPosition).size();
    }

    //获得某个父项
    @Override
    public Object getGroup(int groupPosition) {
        return mGroup.get(groupPosition);
    }

    //获得某个子项
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mItemList.get(groupPosition).get(childPosition);
    }

    //父项的Id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //子项的id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //获取父项的view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_statistics_list_group, parent, false);
        }
        TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean group = mGroup.get(groupPosition);
        Log.e("TAG", "getGroupView: groupPosition="+groupPosition +",name="+group.getUserName());
        TextView tvGroup = convertView.findViewById(R.id.tv_group);
        final TextView tvAttendanceTime = convertView.findViewById(R.id.tv_attendance_time);
        final ImageView direction = convertView.findViewById(R.id.iv_direction);
        tvGroup.setText(group.getUserName());
        final String string = mContext.getString(R.string.attendance_time_format);
        tvAttendanceTime.setText(String.format(string, group.getSectionNumber()));
        direction.setImageResource(isExpanded ? R.drawable.icon_arrow_up : R.drawable.icon_arrow_down);
        return convertView;
    }

    //获取子项的view
    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final TeacherAttendanceWeekMonthRsp.DataBean.WeeksEvenListBean.EventBean.CourseInfoFormListBean dataBean = mItemList.get(groupPosition).get(childPosition);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_statistics_child_list, parent, false);
        }
        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_F9FAF9));
        final TextView tvName = convertView.findViewById(R.id.tv_name);
        final TextView tvCourseName = convertView.findViewById(R.id.tv_course_name);

        if (!TextUtils.isEmpty(dataBean.getSortName())) {
            String date = DateUtils.formatTime(dataBean.getRequiredTime(), null, "MM.dd");
            tvName.setText(date + " " + dataBean.getSortName());
        } else if (TextUtils.isEmpty(dataBean.getSortName()) && !TextUtils.isEmpty(dataBean.getEventName())) {
            String date = DateUtils.formatTime(dataBean.getRequiredTime(), null, "MM.dd");
            tvName.setText(date + " " + dataBean.getEventName());
        } else {
            String date = DateUtils.formatTime(dataBean.getCourseStartTime(), null, "MM.dd");
            if (Objects.equals(dataBean.getAttendanceType(), "1") || "6".equals(dataBean.getAttendanceType())) {
                tvName.setText(date + " " + dataBean.getCourseInfo());
                tvCourseName.setVisibility(View.VISIBLE);
                tvCourseName.setText(dataBean.getCourseName());
            } else {
                tvCourseName.setVisibility(View.VISIBLE);
                tvName.setText(date + " " + dataBean.getCourseInfo());
                tvCourseName.setText(dataBean.getCourseName());
            }

        }

        String status = dataBean.getAttendanceType();
        if (status == null) {
            status = "";
        }
        //0正常、1缺勤、2迟到/3早退,4请假）
        //考勤类型 0正常 1缺勤、2迟到 3早退 4 无效打卡 5 请假 6 未打卡
        final TextView tvAttendanceStatus = convertView.findViewById(R.id.tv_attendance_status);
        final TextView tvEventTimeTitle = convertView.findViewById(R.id.tv_event_time_title);
        final TextView tvEventTime = convertView.findViewById(R.id.tv_event_time);
        final TextView tvLeaveEventTime = convertView.findViewById(R.id.tv_leave_event_time);
        final TextView tvLeaveEventTimeTitle = convertView.findViewById(R.id.tv_leave_event_time_title);
        final Group gpEventTime = convertView.findViewById(R.id.gp_event_time);
        final Group gpLeaveEventTime = convertView.findViewById(R.id.gp_leave_event_time);
        switch (status) {
            case "0":
                tvAttendanceStatus.setVisibility(View.VISIBLE);
                tvAttendanceStatus.setText(mContext.getString(R.string.attendance_normal));
                gpEventTime.setVisibility(View.GONE);
                gpLeaveEventTime.setVisibility(View.GONE);
                break;
            case "1":
                tvAttendanceStatus.setVisibility(View.VISIBLE);
                tvAttendanceStatus.setText(mContext.getString(R.string.attendance_absence));
                gpEventTime.setVisibility(View.GONE);
                gpLeaveEventTime.setVisibility(View.GONE);
                break;
            case "6":
                final String attendanceSignInOut = dataBean.getAttendanceSignInOut();
                if ("1".equals(attendanceSignInOut)) {
                    tvAttendanceStatus.setText(mContext.getString(R.string.attendance_no_logout));
                } else {
                    tvAttendanceStatus.setText(mContext.getString(R.string.attendance_no_sign_in));
                }
                tvAttendanceStatus.setVisibility(View.VISIBLE);
                gpEventTime.setVisibility(View.GONE);
                gpLeaveEventTime.setVisibility(View.GONE);
                break;
            case "2":
                gpEventTime.setVisibility(View.VISIBLE);
                gpLeaveEventTime.setVisibility(View.GONE);
                tvAttendanceStatus.setVisibility(View.GONE);
                tvAttendanceStatus.setText(mContext.getString(R.string.attendance_late));
                if (!TextUtils.isEmpty(dataBean.getClockName())) {
                    tvEventTimeTitle.setText(dataBean.getClockName());
                } else {
                    tvEventTimeTitle.setText(mContext.getString(R.string.attendance_event_name));
                }
                final String date1 = DateUtils.formatTime(dataBean.getSignInTime(), null, "HH:mm");
                tvEventTime.setText(date1);
                tvEventTime.setTextColor(mContext.getResources().getColor(R.color.attendance_time_late));
                break;
            case "3":
                gpEventTime.setVisibility(View.VISIBLE);
                gpLeaveEventTime.setVisibility(View.GONE);
                tvAttendanceStatus.setVisibility(View.GONE);
                tvAttendanceStatus.setText(mContext.getString(R.string.attendance_leave_early));
                if (!TextUtils.isEmpty(dataBean.getClockName())) {
                    tvEventTimeTitle.setText(dataBean.getClockName());
                } else {
                    tvEventTimeTitle.setText(mContext.getString(R.string.attendance_event_name));
                }
                final String date2 = DateUtils.formatTime(dataBean.getSignInTime(), null, "HH:mm");
                tvEventTime.setText(date2);
                tvEventTime.setTextColor(mContext.getResources().getColor(R.color.attendance_leave_early));
                break;
            case "5":
                gpEventTime.setVisibility(View.GONE);
                gpLeaveEventTime.setVisibility(View.VISIBLE);
                tvAttendanceStatus.setText(mContext.getString(R.string.attendance_ask_for_leave));
                tvAttendanceStatus.setVisibility(View.GONE);
                tvLeaveEventTimeTitle.setText(mContext.getString(R.string.attendance_leave_time));
                tvLeaveEventTimeTitle.setTextColor(mContext.getResources().getColor(R.color.attendance_time_leave));
                final String data1 = DateUtils.formatTime(dataBean.getLeaveStartTime(), null, "MM.dd HH:mm");
                final String data2 = DateUtils.formatTime(dataBean.getLeaveEndTime(), null, "MM.dd HH:mm");
                tvLeaveEventTime.setText(data1 + "-" + data2);
                break;

            default:
                tvAttendanceStatus.setVisibility(View.GONE);
                gpEventTime.setVisibility(View.GONE);
                gpLeaveEventTime.setVisibility(View.GONE);
                break;
        }

        return convertView;
    }

    //子项是否可选中,如果要设置子项的点击事件,需要返回true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
