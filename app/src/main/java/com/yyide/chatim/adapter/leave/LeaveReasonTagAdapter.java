package com.yyide.chatim.adapter.leave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.utils.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/4/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LeaveReasonTagAdapter extends RecyclerView.Adapter<LeaveReasonTagAdapter.ViewHolder> {
    private Context context;
    private List<LeavePhraseRsp.DataBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public LeaveReasonTagAdapter(Context context, List<LeavePhraseRsp.DataBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leave_reason_tag,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeavePhraseRsp.DataBean tag = data.get(position);
        holder.tv_tag.setText(tag.getTag());
        if (tag.isChecked()){
            holder.itemView.setBackgroundResource(R.drawable.leave_reason_tag_selected_bg_shape);
        }else {
            holder.itemView.setBackgroundResource(R.drawable.leave_reason_tag_bg_shape);
        }
        holder.itemView.setOnClickListener(v -> {
            onClickedListener.onClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tag)
        TextView tv_tag;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickedListener{
        void onClicked(int position);
    }
}
