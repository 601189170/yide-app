package com.yyide.chatim.adapter.leave;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.databinding.ItemAskForLeaveRecordBinding;
import com.yyide.chatim.model.LeaveListRsp;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.FootView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/5/11 14:24
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/11 14:24
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AskForLeaveListAdapter extends BaseQuickAdapter<LeaveListRsp.DataBean.RecordsBean, BaseViewHolder> implements LoadMoreModule {

    public AskForLeaveListAdapter(@Nullable List<LeaveListRsp.DataBean.RecordsBean> data) {
        super(R.layout.item_ask_for_leave_record, data);
    }

    private void leaveStatus(String status, TextView view) {
        //审核结果: 0 审批拒绝 1 审批通过 2 审批中 3 已撤销
        //0已拒绝 1待审批 2已通过
        switch (status) {
            case "0":
                view.setText(getContext().getString(R.string.refuse_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_refuse_shape);
                view.setTextColor(getContext().getResources().getColor(R.color.black9));
                break;
            case "1":
                view.setText(getContext().getString(R.string.approval_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_approval_shape);
                view.setTextColor(getContext().getResources().getColor(R.color.black9));
                break;
            case "2":
                view.setText(getContext().getString(R.string.pass_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_pass_shape);
                view.setTextColor(getContext().getResources().getColor(R.color.black9));
                break;
            case "3":
                view.setText(getContext().getString(R.string.repeal_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_repeal_shape);
                view.setTextColor(getContext().getResources().getColor(R.color.black11));
                break;
            default:
                break;
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, LeaveListRsp.DataBean.RecordsBean askForLeaveRecordRsp) {
        holder.setText(R.id.tv_title, askForLeaveRecordRsp.getTitle());
        LeaveListRsp.DataBean.RecordsBean.LeaveDetail jsonData = askForLeaveRecordRsp.getLeaveDetail();
        if (jsonData != null && !TextUtils.isEmpty(jsonData.getStartTime())) {
            holder.setText(R.id.tv_time_start, jsonData.getStartTime());
        }
        leaveStatus(askForLeaveRecordRsp.getStatus(), holder.getView(R.id.tv_status));
    }

}
