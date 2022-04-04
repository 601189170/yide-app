package com.yyide.chatim.adapter.leave;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.utils.GlideUtil;
import com.yyide.chatim.widget.RadiusImageView;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/5/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LeaveFlowAdapter extends BaseQuickAdapter<LeaveDetailRsp.DataDTO.HiApprNodeListDTO, BaseViewHolder> {

    public LeaveFlowAdapter() {
        super(R.layout.item_ask_for_leave_flow);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, LeaveDetailRsp.DataDTO.HiApprNodeListDTO leaveFlowBean) {
        if (!leaveFlowBean.isCc()) {
            holder.setText(R.id.tv_flow_title, leaveFlowBean.getNodeName());
            holder.setText(R.id.tvTime, leaveFlowBean.getApprTime());
            holder.setText(R.id.tv_flow_content, leaveFlowBean.getUserName());
            //隐藏最后一条分割线
            if (getItemPosition(leaveFlowBean) == getData().size() - 1) {
                holder.getView(R.id.vEnd).setVisibility(View.INVISIBLE);
            }
            holder.getView(R.id.tvComment).setVisibility(View.GONE);
            RadiusImageView imageView = holder.getView(R.id.iv_user_head);
            ImageView ivCheck = holder.getView(R.id.iv_flow_checked);
            if ("2".equals(leaveFlowBean.getStatus())) {
                imageView.setPadding(4, 4, 4, 4);
                imageView.setBackground(getContext().getResources().getDrawable(R.drawable.blue_border_1dp));
                ivCheck.setVisibility(View.VISIBLE);
                ivCheck.setImageResource(R.drawable.icon_flow_checked);
                holder.setText(R.id.tv_flow_content, leaveFlowBean.getUserName() + "  已同意");
                holder.getView(R.id.vEnd).setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            } else if ("0".equals(leaveFlowBean.getStatus())) {
                holder.getView(R.id.tvComment).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_flow_content, leaveFlowBean.getUserName() + "  已拒绝");
                holder.setText(R.id.tvComment, leaveFlowBean.getComment());
                ivCheck.setVisibility(View.VISIBLE);
                ivCheck.setImageResource(R.mipmap.icon_leave_cancel);
                imageView.setPadding(4, 4, 4, 4);
                imageView.setBackground(getContext().getResources().getDrawable(R.drawable.red_border_1dp));
                holder.getView(R.id.vEnd).setBackgroundColor(getContext().getResources().getColor(R.color.color_B3B3B3));
            } else if ("4".equals(leaveFlowBean.getStatus())) {
                holder.setText(R.id.tv_flow_content, "审批人  已退回");
                holder.getView(R.id.vEnd).setBackgroundColor(getContext().getResources().getColor(R.color.color_B3B3B3));
                ivCheck.setVisibility(View.INVISIBLE);
            } else {
                holder.getView(R.id.vEnd).setBackgroundColor(getContext().getResources().getColor(R.color.color_B3B3B3));
                ivCheck.setVisibility(View.INVISIBLE);
            }
            GlideUtil.loadImageHead(getContext(), leaveFlowBean.getAvatar(), imageView);
            //第一条线默认高亮
            if (getItemPosition(leaveFlowBean) == 0) {
                imageView.setPadding(4, 4, 4, 4);
                ivCheck.setVisibility(View.VISIBLE);
                ivCheck.setImageResource(R.drawable.icon_flow_checked);
                imageView.setBackground(getContext().getResources().getDrawable(R.drawable.blue_border_1dp));
                holder.getView(R.id.vEnd).setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                if (!TextUtils.isEmpty(leaveFlowBean.getAvatar())) {
                    imageView.setVisibility(View.INVISIBLE);
                    holder.getView(R.id.tvName).setVisibility(View.VISIBLE);
                    String name = leaveFlowBean.getUserName();
                    if (TextUtils.isEmpty(name) && name.length() > 2) {
                        name = name.substring(name.length() - 2, name.length());
                    }
                    holder.setText(R.id.tvName, name);
                }
            }
        } else {
            ((ImageView) holder.getView(R.id.iv_user_head)).setImageResource(R.mipmap.icon_leave_cc);
            ((ImageView) holder.getView(R.id.iv_user_head)).setBackground(getContext().getResources().getDrawable(R.drawable.bg_blue));
            holder.setText(R.id.tv_flow_title, "抄送人");
            if (leaveFlowBean.getCcList() != null) {
                holder.setText(R.id.tv_flow_content, "共抄送" + leaveFlowBean.getCcList().size() + "人");
            } else {
                holder.setText(R.id.tv_flow_content, "共抄送" + 0 + "人");
            }
            holder.getView(R.id.iv_flow_checked).setVisibility(View.INVISIBLE);
            holder.getView(R.id.vEnd).setVisibility(View.INVISIBLE);

            RecyclerView recyclerView = holder.getView(R.id.recyclerView);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 6));
            recyclerView.setAdapter(mAdapter);
            if (leaveFlowBean.getCcList() != null && leaveFlowBean.getCcList().size() > 6) {
                mAdapter.setList(leaveFlowBean.getCcList().subList(0, 6));
                ImageView ivOpen = holder.getView(R.id.ivUp);
                ivOpen.setVisibility(View.VISIBLE);
                //展开查看全部抄送人
                ivOpen.setOnClickListener(v -> {
                    if (mAdapter.getData().size() > 6) {
                        ivOpen.setRotation(0);
                        mAdapter.setList(leaveFlowBean.getCcList().subList(0, 6));
                    } else {
                        ivOpen.setRotation(-180);
                        mAdapter.setList(leaveFlowBean.getCcList());
                    }

                });
            } else {
                mAdapter.setList(leaveFlowBean.getCcList());
            }
        }

    }

    private BaseQuickAdapter<LeaveDetailRsp.DataDTO.Cc, BaseViewHolder> mAdapter =
            new BaseQuickAdapter<LeaveDetailRsp.DataDTO.Cc, BaseViewHolder>(R.layout.item_leave_detail_cc) {

                @Override
                protected void convert(@NonNull BaseViewHolder holder, LeaveDetailRsp.DataDTO.Cc item) {
                    holder.setText(R.id.tvName, item.getName());
                    ImageView imageView = holder.getView(R.id.iv_user_head);
                    GlideUtil.loadImageHead(getContext(), item.getAvatar(), imageView);
                }
            };
}
