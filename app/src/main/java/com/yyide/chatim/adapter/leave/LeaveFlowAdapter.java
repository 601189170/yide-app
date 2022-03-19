package com.yyide.chatim.adapter.leave;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yyide.chatim.R;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.model.LeaveFlowBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/5/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LeaveFlowAdapter extends RecyclerView.Adapter<LeaveFlowAdapter.ViewHolder> {
    private Context context;
    private List<LeaveDetailRsp.DataDTO.HiApprNodeListDTO> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public LeaveFlowAdapter(Context context, List<LeaveDetailRsp.DataDTO.HiApprNodeListDTO> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ask_for_leave_flow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaveDetailRsp.DataDTO.HiApprNodeListDTO leaveFlowBean = data.get(position);
        holder.tv_flow_title.setText(leaveFlowBean.getNodeName());
        holder.tv_flow_content.setText(leaveFlowBean.getUsreName());
        holder.tvTime.setText(leaveFlowBean.getApprTime());
        //隐藏最后一条分割线
        if (position == data.size() - 1) {
            holder.v_line2.setVisibility(View.INVISIBLE);
        }

        if ("2".equals(leaveFlowBean.getStatus())) {
            holder.iv_user_head.setBackgroundResource(R.drawable.blue_border_1dp);
            holder.v_line2.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.iv_flow_checked.setVisibility(View.VISIBLE);
        } else {
            holder.v_line2.setBackgroundColor(context.getResources().getColor(R.color.color_B3B3B3));
            holder.iv_flow_checked.setVisibility(View.INVISIBLE);
        }

//        if (!TextUtils.isEmpty(leaveFlowBean.getImage())) {
//            Glide.with(context)
//                    .load(leaveFlowBean.getImage())
//                    .placeholder(R.drawable.default_head)
//                    .error(R.drawable.default_head)
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(holder.iv_user_head);
//        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tv_flow_title)
        TextView tv_flow_title;

        @BindView(R.id.tv_flow_content)
        TextView tv_flow_content;

        @BindView(R.id.iv_flow_checked)
        ImageView iv_flow_checked;

        @BindView(R.id.vEnd)
        View v_line2;

        @BindView(R.id.iv_user_head)
        ImageView iv_user_head;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickedListener {
        void onClicked(int position);
    }
}
