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
import com.yyide.chatim.activity.book.BookTeacherDetailActivity;
import com.yyide.chatim.model.BookTeacherItem;
import com.yyide.chatim.model.Teacher;
import com.yyide.chatim.utils.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: 通讯录搜索adapter
 * @Author: liu tao
 * @CreateDate: 2021/4/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ItemBookSearchAdapter extends RecyclerView.Adapter<ItemBookSearchAdapter.ViewHolder> {
    private Context context;
    private List<Teacher> data;

    public ItemBookSearchAdapter(Context context, List<Teacher> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Teacher bean = data.get(position);
        GlideUtil.loadImageHead(context, bean.getFaceInformation(), holder.ivHead);
        holder.tv_name.setText(bean.getName() + " (" + bean.getTypeName() + ")");
        holder.itemView.setOnClickListener(v -> {
            BookTeacherItem teacherItem1 = bean.getList();
            BookTeacherItem teacherItem = new BookTeacherItem(
                    teacherItem1.getName(),
                    teacherItem1.getSex(),
                    teacherItem1.getPhone(),
                    teacherItem1.getUserId(),
                    teacherItem1.getEmail(),
                    teacherItem1.getSubjectName(),
                    teacherItem1.getTeachingSubjects(),
                    teacherItem1.getFaceInformation());
            BookTeacherDetailActivity.start(context, teacherItem);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivHead)
        ImageView ivHead;//显示图片
        ImageView iv_call;
        @BindView(R.id.tv_name)
        TextView tv_name;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
