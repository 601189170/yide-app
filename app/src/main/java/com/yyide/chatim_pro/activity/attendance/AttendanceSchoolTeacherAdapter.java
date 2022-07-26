package com.yyide.chatim_pro.activity.attendance;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.AttendanceCheckRsp;
import com.yyide.chatim_pro.model.AttendanceRsp;
import com.yyide.chatim_pro.utils.DateUtils;
import com.yyide.chatim_pro.utils.TimeUtil;
import com.yyide.chatim_pro.widget.treeview.adapter.SingleLayoutTreeAdapter;
import com.yyide.chatim_pro.widget.treeview.model.TreeNode;
import com.yyide.chatim_pro.widget.treeview.util.DpUtil;

import java.util.List;

public class AttendanceSchoolTeacherAdapter extends SingleLayoutTreeAdapter<AttendanceRsp.TeacherItemBean.CourseInfoFormListBean> {

    public AttendanceSchoolTeacherAdapter(int layoutResId, @Nullable List<TreeNode<AttendanceRsp.TeacherItemBean.CourseInfoFormListBean>> dataToBind) {
        super(layoutResId, dataToBind);
    }

    @Override
    protected void convert(BaseViewHolder holder, TreeNode<AttendanceRsp.TeacherItemBean.CourseInfoFormListBean> itemParent) {
        super.convert(holder, itemParent);
        AttendanceRsp.TeacherItemBean.CourseInfoFormListBean item = itemParent.getData();
        TextView tvTime = holder.getView(R.id.tv_student_time);
        TextView tvStatus = holder.getView(R.id.tv_status);
        TextView tvName = holder.getView(R.id.tv_student_name);
        ConstraintLayout constraintLayout = holder.getView(R.id.cl_bg);
        holder.setText(R.id.tv_student_name, "");
        holder.setText(R.id.tv_student_event, "");
        holder.setText(R.id.tv_status, "");
        holder.setText(R.id.tv_student_time, "");
        holder.getView(R.id.group).setVisibility(View.VISIBLE);
        if (itemParent.getLevel() == 1) {
            constraintLayout.setBackgroundColor(Color.parseColor("#F9FAF9"));
            if (!TextUtils.isEmpty(item.getAttendanceType())) {
                //0正常 1缺勤、2迟到 3早退 4 无效打卡 5 请假 6 未打卡
                tvName.setText(item.getCourseInfo() + " " + item.getCourseName());
                switch (item.getAttendanceType()) {
                    case "0"://正常
//                        tvStatus.setText(item.getStatusType());
//                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
//                        tvTime.setTextColor(Color.parseColor("#606266"));
                        break;
                    case "1"://缺勤
                        holder.getView(R.id.group).setVisibility(View.GONE);
                        tvStatus.setText("缺勤");
                        break;
                    case "6":
                        holder.getView(R.id.group).setVisibility(View.GONE);
                        holder.setText(R.id.tv_status, "1".equals(item.getAttendanceSignInOut()) ? "缺勤" : "未签到");
                        break;
                    case "2"://迟到
                        holder.setText(R.id.tv_student_event, item.getClockName());
                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getSignInTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                        tvTime.setTextColor(Color.parseColor("#F66C6C"));
                        break;
                    case "3"://早退
//                        tvStatus.setText(item.getStatusType());
                        holder.setText(R.id.tv_student_event, item.getClockName());
                        tvTime.setGravity(Gravity.RIGHT);
                        tvTime.setTextColor(Color.parseColor("#63DAAB"));
                        tvTime.setText(DateUtils.formatTime(item.getSignInTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                        break;
                    case "5"://请假
                        //tvStatus.setText(item.getStatusType());
                        String startTime = DateUtils.formatTime(item.getLeaveStartTime(), "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm");
                        String endTime = DateUtils.formatTime(item.getLeaveEndTime(), "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm");
                        holder.setText(R.id.tv_student_event, "请假时间");
                        tvTime.setText(startTime + "-" + endTime);
                        tvTime.setTextColor(Color.parseColor("#F6BD16"));
                        break;
                }
            }
        } else {
            constraintLayout.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            holder.getView(R.id.group).setVisibility(View.GONE);
            tvName.setText(!TextUtils.isEmpty(item.getCourseName()) ? item.getCourseName() : "未知姓名");
            holder.setText(R.id.tv_status, item.getSection() != null ? item.getSection() + "节" : "0节");
        }
        if (!itemParent.isLeaf()) {
            //helper.setImageResource(R.id.level_icon, R.drawable.video);
            tvStatus.setText(getContext().getString(R.string.attendance_node, itemParent.getChildren() != null ? itemParent.getChildren().size() : 0));
            if (itemParent.isExpand()) {
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.mipmap.icon_up), null);
            } else {
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.mipmap.icon_down), null);
            }
        } else {
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    @Override
    protected int getTreeNodeMargin() {
        return DpUtil.dip2px(getContext(), 0);
    }
}