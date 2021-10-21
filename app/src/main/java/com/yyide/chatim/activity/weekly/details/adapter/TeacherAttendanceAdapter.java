package com.yyide.chatim.activity.weekly.details.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.model.AbnormalDetail;
import com.yyide.chatim.model.AttendItem;
import com.yyide.chatim.model.ValueChild;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.widget.treeview.adapter.SingleLayoutTreeAdapter;
import com.yyide.chatim.widget.treeview.model.TreeNode;
import com.yyide.chatim.widget.treeview.util.DpUtil;

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
        tvName.setText(item.getClockDate() + " " + item.getDetailName());
        tvStatus.setText(item.getStatusName());
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
                    tvEvent.setText(item.getStatusName());
                    break;
                case "3"://早退
                    tvEvent.setText(item.getStatusName());
                    tvStatus.setText(item.getEquipment());
                    holder.setText(R.id.tv_student_time, item.getClockTime());
                    tvTime.setTextColor(Color.parseColor("#63DAAB"));
                    break;
                case "2"://迟到
                    tvEvent.setText(item.getStatusName());
                    tvStatus.setText(item.getEquipment());
                    holder.setText(R.id.tv_student_time, item.getClockTime());
                    tvTime.setTextColor(Color.parseColor("#F66C6C"));
                    break;
                case "5"://请假
                    tvStatus.setText("请假");
                    holder.setText(R.id.tv_student_event, "请假时间");
                    holder.setText(R.id.tv_student_time, item.getLeaveTime());
                    tvTime.setTextColor(Color.parseColor("#F6BD16"));
                    break;
            }
        } else {
            constraintLayout.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            tvName.setEms(8);
            tvName.setLines(1);
            tvName.setText(!TextUtils.isEmpty(item.getDetailName()) ? item.getDetailName() : "未知姓名");
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