package com.yyide.chatim_pro.adapter.leave;

import android.content.Context;
import android.media.Image;
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
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.ApproverRsp;
import com.yyide.chatim_pro.model.LeaveDetailRsp;
import com.yyide.chatim_pro.model.LeavePhraseRsp;

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
public class LeaveCarbonCopyPeopleAdapter extends RecyclerView.Adapter<LeaveCarbonCopyPeopleAdapter.ViewHolder> {
    private Context context;
    private List<ApproverRsp.DataBean.ListBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public LeaveCarbonCopyPeopleAdapter(Context context, List<ApproverRsp.DataBean.ListBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carbon_copy_people,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApproverRsp.DataBean.ListBean listBean = data.get(position);
        holder.tv_name.setText(listBean.getName());
        if (!TextUtils.isEmpty(listBean.getImage())){
            Glide.with(context)
                    .load(listBean.getImage())
                    .placeholder(R.drawable.default_head)
                    .error(R.drawable.default_head)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.iv_user_head);
        }
        if (position == data.size()-1){
            //holder.tv_add.setVisibility(View.GONE);
        }
//        holder.itemView.setOnClickListener(v -> {
//            onClickedListener.onClicked(position);
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.iv_user_head)
        ImageView iv_user_head;
        //@BindView(R.id.tv_add)
        //TextView tv_add;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickedListener{
        void onClicked(int position);
    }
}
