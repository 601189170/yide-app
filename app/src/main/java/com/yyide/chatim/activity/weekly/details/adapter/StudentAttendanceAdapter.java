package com.yyide.chatim.activity.weekly.details.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.model.AbnormalDetail;
import com.yyide.chatim.model.StudentValue;
import com.yyide.chatim.widget.treeview.adapter.SingleLayoutTreeAdapter;
import com.yyide.chatim.widget.treeview.model.TreeNode;
import com.yyide.chatim.widget.treeview.util.DpUtil;

import java.util.List;

public class StudentAttendanceAdapter extends SingleLayoutTreeAdapter<StudentValue> {

    public StudentAttendanceAdapter(int layoutResId, @Nullable List<TreeNode<StudentValue>> dataToBind) {
        super(layoutResId, dataToBind);

    }

    @Override
    protected void convert(BaseViewHolder holder, TreeNode<StudentValue> itemParent) {
        super.convert(holder, itemParent);
        StudentValue item = itemParent.getData();
        TextView tvTime = holder.getView(R.id.tv_student_time);
        TextView tvStatus = holder.getView(R.id.tv_status);
        TextView tvName = holder.getView(R.id.tv_student_name);
        ConstraintLayout constraintLayout = holder.getView(R.id.cl_bg);
        holder.setText(R.id.tv_student_time, "");
        holder.setText(R.id.tv_student_event, "");
        holder.setText(R.id.tv_status, "");
        holder.setText(R.id.tv_student_name, "");
        tvName.setText(item.getClockDate() + " " + item.getDetailName());
        if (itemParent.getLevel() == 1) {
            constraintLayout.setBackgroundColor(Color.parseColor("#F5F8FC"));
            if (TextUtils.isEmpty(item.getType())) {
                return;
            }
            switch (item.getType()) {
                case "0"://正常
//                        tvStatus.setText(item.getStatusType());
//                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
//                        tvTime.setTextColor(Color.parseColor("#606266"));
                    break;
                case "1"://缺勤
                    tvStatus.setText("未打卡");
                    break;
                case "2"://迟到
                    tvStatus.setText("迟到");
                    holder.setText(R.id.tv_student_event, item.getName());
                    //holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
                    tvTime.setTextColor(Color.parseColor("#F66C6C"));
                    break;
                case "3"://早退
                    tvStatus.setText("早退");
//                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
//                        tvTime.setTextColor(Color.parseColor("#63DAAB"));
                    break;
                case "4"://请假
                    tvStatus.setText("请假");
//                        String startTime = DateUtils.formatTime(item.getStartDate(), "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm");
//                        String endTime = DateUtils.formatTime(item.getEndDate(), "yyyy-MM-dd HH:mm:ss", "MM.dd HH:mm");
//                        holder.setText(R.id.tv_student_event, "请假时间");
//                        holder.setText(R.id.tv_student_time, startTime + "-" + endTime);
//                        tvTime.setTextColor(Color.parseColor("#F6BD16"));
                    break;
            }
        } else {
            constraintLayout.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            tvName.setEms(8);
            tvName.setLines(1);
            tvName.setText(item.getClockDate() + " " + item.getStatusName() + " " + item.getDetailName());
            // holder.setText(R.id.tv_status, item.specialPeople != null ? item.specialPeople.size() + "节" : "0节");
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