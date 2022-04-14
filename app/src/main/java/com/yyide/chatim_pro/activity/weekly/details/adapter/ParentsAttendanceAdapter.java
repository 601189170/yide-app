package com.yyide.chatim_pro.activity.weekly.details.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.AttendItem;
import com.yyide.chatim_pro.model.AttendanceCheckRsp;
import com.yyide.chatim_pro.utils.DateUtils;
import com.yyide.chatim_pro.utils.TimeUtil;
import com.yyide.chatim_pro.widget.treeview.adapter.SingleLayoutTreeAdapter;
import com.yyide.chatim_pro.widget.treeview.model.TreeNode;
import com.yyide.chatim_pro.widget.treeview.util.DpUtil;

import java.util.List;

public class ParentsAttendanceAdapter extends SingleLayoutTreeAdapter<AttendItem> {

    public ParentsAttendanceAdapter(int layoutResId, @Nullable List<TreeNode<AttendItem>> dataToBind) {
        super(layoutResId, dataToBind);

    }

    @Override
    protected void convert(BaseViewHolder holder, TreeNode<AttendItem> itemParent) {
        super.convert(holder, itemParent);
        AttendItem item = itemParent.getData();
        TextView tvTime = holder.getView(R.id.tv_student_time);
        TextView tvStatus = holder.getView(R.id.tv_status);
        TextView tvEvent = holder.getView(R.id.tv_student_event);
        TextView tvName = holder.getView(R.id.tv_student_name);
        ConstraintLayout constraintLayout = holder.getView(R.id.cl_bg);
        holder.setText(R.id.tv_student_time, "");
        holder.setText(R.id.tv_student_event, "");
        holder.setText(R.id.tv_status, "");
        holder.setText(R.id.tv_student_name, item.getName());
        /**
         * 考勤详情状态(0正常 1缺勤 2迟到 3早退 4无效打卡 5请假 6未打卡)
         */
        if (itemParent.getLevel() == 1) {
            constraintLayout.setBackgroundColor(Color.parseColor("#F5F8FC"));
            switch (item.getValue()) {
                case 0://正常
//                        tvStatus.setText(item.getStatusType());
//                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
//                        tvTime.setTextColor(Color.parseColor("#606266"));
                    break;
                case 1://缺勤
                    tvStatus.setText("未打卡");
                    break;
                case 3://早退
                    tvTime.setTextColor(Color.parseColor("#63DAAB"));
                    tvTime.setText(item.getTime());
                    tvStatus.setText(item.getClockName());
                    break;
                case 2://迟到
                    tvStatus.setText(item.getClockName());
                    tvTime.setText(item.getTime());
                    tvTime.setTextColor(Color.parseColor("#F66C6C"));
                    break;
                case 4:
                    break;
                case 5://请假
                    tvTime.setText(item.getTime());
                    tvStatus.setText("请假时间");
                    tvTime.setTextColor(Color.parseColor("#F6BD16"));
                    break;
            }
        } else {
            constraintLayout.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            tvName.setEms(8);
            tvName.setLines(1);
            tvName.setText(!TextUtils.isEmpty(item.getName()) ? item.getName() : "未知姓名");
            tvStatus.setText(getContext().getString(R.string.attendance_bout, itemParent.getChildren() != null ? itemParent.getChildren().size() : 0));
        }
        if (!itemParent.isLeaf()) {
            if (itemParent.isExpand()) {
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.mipmap.icon_up), null);
            } else {
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, getContext().getResources().getDrawable(R.mipmap.icon_down), null);
            }
        } else {
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tvStatus.setTextColor(Color.parseColor("#909399"));
        }
    }

    @Override
    protected int getTreeNodeMargin() {
        return DpUtil.dip2px(getContext(), 0);
    }
}