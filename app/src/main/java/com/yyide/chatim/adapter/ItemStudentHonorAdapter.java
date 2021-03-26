package com.yyide.chatim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.StudentHonorBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: 学生荣誉证书列表adapter
 * @Author: liu tao
 * @CreateDate: 2021/3/25 16:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/25 16:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ItemStudentHonorAdapter extends RecyclerView.Adapter<ItemStudentHonorAdapter.ViewHolder> {
    private Context context;
    private List<StudentHonorBean> data;

    public ItemStudentHonorAdapter(Context context, List<StudentHonorBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_honor_content,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentHonorBean bean = data.get(position);
        holder.honor_name_tv.setText(bean.getName());
        holder.honor_time_tv.setText(bean.getTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.honor_pic_iv)
        ImageView honor_pic_iv;//显示图片

        @BindView(R.id.honor_name_tv)
        TextView honor_name_tv;

        @BindView(R.id.honor_time_tv)
        TextView honor_time_tv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
