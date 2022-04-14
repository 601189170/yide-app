package com.yyide.chatim_pro.activity.weekly.details.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.AbnormalDetail;
import com.yyide.chatim_pro.model.AttendItem;
import com.yyide.chatim_pro.model.ValueChild;
import com.yyide.chatim_pro.utils.DateUtils;
import com.yyide.chatim_pro.widget.treeview.adapter.SingleLayoutTreeAdapter;
import com.yyide.chatim_pro.widget.treeview.model.TreeNode;
import com.yyide.chatim_pro.widget.treeview.util.DpUtil;

import java.util.List;

public class TeacherAttendanceAdapter extends SingleLayoutTreeAdapter<ValueChild> {

    public TeacherAttendanceAdapter(int layoutResId, @Nullable List<TreeNode<ValueChild>> dataToBind) {
        super(layoutResId, dataToBind);

    }

    @Override
    protected void convert(BaseViewHolder holder, TreeNode<ValueChild> itemParent) {
        super.convert(holder, itemParent);
        ValueChild item = itemParent.getData();
        TextView tvTime = holder.getView(R.id.tv_student_time);
        TextView tvStatus = holder.getView(R.id.tv_status);
        TextView tvName = holder.getView(R.id.tv_student_name);
        TextView tvEvent = holder.getView(R.id.tv_student_event);
        ConstraintLayout constraintLayout = holder.getView(R.id.cl_bg);
        holder.setText(R.id.tv_student_time, "");
        holder.setText(R.id.tv_student_event, "");
        holder.setText(R.id.tv_status, "");
        holder.setText(R.id.tv_student_name, "");
        tvStatus.setText(item.getClockName());
        if (itemParent.getLevel() == 1) {
            constraintLayout.setBackgroundColor(Color.parseColor("#F5F8FC"));
            if (TextUtils.isEmpty(item.getType())) {
                return;
            }
            tvName.setText(item.getName());
            switch (item.getType()) {
                case "0"://正常
//                        tvStatus.setText(item.getStatusType());
//                        holder.setText(R.id.tv_student_time, DateUtils.formatTime(item.getTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm"));
//                        tvTime.setTextColor(Color.parseColor("#606266"));
                    break;
                case "1"://缺勤
                    tvStatus.setText("未打卡");
                    tvEvent.setText(item.getClockName());
                    break;
                case "3"://早退
                    tvName.setText(item.getName());
                    tvStatus.setText(item.getClockName());
                    holder.setText(R.id.tv_student_time, item.getTime());
                    tvTime.setTextColor(Color.parseColor("#63DAAB"));
                    break;
                case "2"://迟到
                    tvName.setText(item.getName());
                    tvStatus.setText(item.getClockName());
                    holder.setText(R.id.tv_student_time, item.getTime());
                    tvTime.setTextColor(Color.parseColor("#F66C6C"));
                    break;
                case "5"://请假
                    holder.getView(R.id.constraintLayout).setVisibility(View.GONE);
                    holder.getView(R.id.layout_leave).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_leave_name, item.getName());
                    holder.setText(R.id.tv_leave_event, "请假时间");
                    holder.setText(R.id.tv_leave_time, item.getTime());
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