package com.yyide.chatim.adapter.leave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
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
public class AskForLeaveListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<LeaveListRsp.DataBean.RecordsBean> data;
    //上拉加载更多布局
    public static final int view_Foot = 1;
    //主要布局
    public static final int view_Normal = 2;
    //是否隐藏
    public boolean isLoadMore = false;
    public void setOnlyOnePage(boolean onlyOnePage) {
        this.onlyOnePage = onlyOnePage;
    }

    //是否是只有一页
    public boolean onlyOnePage = false;
    private View view;

    private OnItemOnClickListener onItemOnClickListener;

    public boolean isLastPage = false;

    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.onItemOnClickListener = onItemOnClickListener;
    }
    public AskForLeaveListAdapter(Context context, List<LeaveListRsp.DataBean.RecordsBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == view_Normal){
            view = LayoutInflater.from(context).inflate(R.layout.item_ask_for_leave_record, parent, false);
            return new ViewHolder(view);
        }else {
            FootView view = new FootView(context);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount()-1){
            FootView itemView = (FootView) holder.itemView;
            if (isLoadMore && !onlyOnePage){
                itemView.setVisibility(View.VISIBLE);
                itemView.setLoading(!isLastPage);
            }else {
                itemView.setVisibility(View.GONE);
            }
        }else {
            ViewHolder  holder1= (ViewHolder)holder;
            LeaveListRsp.DataBean.RecordsBean askForLeaveRecordRsp = data.get(position);
            holder1.tv_title.setText(askForLeaveRecordRsp.getName());
            final String formatTime = DateUtils.formatTime(askForLeaveRecordRsp.getInitiateTime(), "yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd");
            holder1.tv_time.setText(formatTime);
            leaveStatus(askForLeaveRecordRsp.getApprovalResult(),holder1.tv_status);
            holder1.itemView.setOnClickListener(v -> {
                onItemOnClickListener.onClicked(position);
            });
        }
    }

    private void leaveStatus(String status,TextView view){
        //审核结果: 0 审批拒绝 1 审批通过 2 审批中 3 已撤销
        switch (status){
            case "0":
                view.setText(context.getString(R.string.refuse_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_refuse_shape);
                view.setTextColor(context.getResources().getColor(R.color.black9));
                break;
            case "1":
                view.setText(context.getString(R.string.pass_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_pass_shape);
                view.setTextColor(context.getResources().getColor(R.color.black9));
                break;
            case "2":
                view.setText(context.getString(R.string.approval_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_approval_shape);
                view.setTextColor(context.getResources().getColor(R.color.black9));
                break;
            case "3":
                view.setText(context.getString(R.string.repeal_text));
                view.setBackgroundResource(R.drawable.ask_for_leave_status_repeal_shape);
                view.setTextColor(context.getResources().getColor(R.color.black11));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }
    public void setIsLoadMore(boolean loadMore) {
        this.isLoadMore = loadMore;
    }

    //设置是最后一页
    public void setIsLastPage(boolean lastPage){
        this.isLastPage = lastPage;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1){
            return view_Foot;
        }else {
            return view_Normal;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_time_start)
        TextView tv_time;

        @BindView(R.id.tv_status)
        TextView tv_status;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemOnClickListener{
        void onClicked(int position);
    }
}
