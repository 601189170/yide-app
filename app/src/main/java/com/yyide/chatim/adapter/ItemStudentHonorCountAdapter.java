package com.yyide.chatim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.StudentHonorCountBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: 学生荣誉搜索列表adapter
 * @Author: liu tao
 * @CreateDate: 2021/3/26 10:23
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/26 10:23
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ItemStudentHonorCountAdapter extends RecyclerView.Adapter<ItemStudentHonorCountAdapter.ViewHolder> {
    private Context context;
    private List<StudentHonorCountBean> data;

    public void setItemOnClickedListener(ItemOnClickedListener itemOnClickedListener) {
        this.itemOnClickedListener = itemOnClickedListener;
    }

    private ItemOnClickedListener itemOnClickedListener;

    public ItemStudentHonorCountAdapter(Context context, List<StudentHonorCountBean> data) {
        this.context = context;
        this.data = data;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_honor_count_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentHonorCountBean bean = data.get(position);
        holder.studentName.setText(bean.getStudentName());
        holder.honorCount.setText(String.format(context.getString(R.string.student_honor_count),bean.getHonorCount()));
        holder.itemView.setOnClickListener(v -> {
            if (itemOnClickedListener != null){
                itemOnClickedListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.student_name)
        TextView studentName;

        @BindView(R.id.honor_count)
        TextView honorCount;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface ItemOnClickedListener{
        void onItemClick(int position);
    }
}
