package com.yyide.chatim.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.StudentScopeRsp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: 学生层级adapter
 * @Author: liu tao
 * @CreateDate: 2021/4/10 14:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/10 14:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ItemStudentScopeAdapter2 extends RecyclerView.Adapter<ItemStudentScopeAdapter2.ViewHolder> {
    private Context context;
    private List<StudentScopeRsp.DataBean.ListBean.GradesBean> data;
    private boolean unfold = false;
    public ItemStudentScopeAdapter2(Context context, List<StudentScopeRsp.DataBean.ListBean.GradesBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_scope,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentScopeRsp.DataBean.ListBean.GradesBean bean = data.get(position);
        holder.tv_title.setText(bean.getName());
        holder.checkBox.setChecked(bean.isChecked());
        if (!bean.getClasses().isEmpty()){
            holder.btn_level.setVisibility(View.VISIBLE);
            holder.btn_level.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_up));
            holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            ItemStudentScopeAdapter3 adapter = new ItemStudentScopeAdapter3(context, bean.getClasses());
//            holder.mRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            holder.mRecyclerView.setAdapter(adapter);
        }
        holder.itemView.setOnClickListener(v -> {
            Log.e("TAG", "onBindViewHolder: "+unfold );
            if (unfold) {
                unfold = false;
                holder.btn_level.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_up));
                holder.mRecyclerView.setVisibility(View.GONE);
            } else {
                unfold = true;
                holder.btn_level.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_down));
                holder.mRecyclerView.setVisibility(View.VISIBLE);
            }

        });

        //选中层级
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bean.setChecked(isChecked);
            List<StudentScopeRsp.DataBean.ListBean.GradesBean.ClassesBean> classes = bean.getClasses();
            for (int i = 0; i < classes.size(); i++) {
                classes.get(i).setChecked(isChecked);
            }
            new Handler().post(() -> notifyDataSetChanged());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkBox)
        CheckBox checkBox;//显示图片
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.recyclerview)
        RecyclerView mRecyclerView;
        @BindView(R.id.btn_level)
        ImageView btn_level;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
