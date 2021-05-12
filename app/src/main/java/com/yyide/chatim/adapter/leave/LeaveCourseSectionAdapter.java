package com.yyide.chatim.adapter.leave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.CourseSectionBean;

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
public class LeaveCourseSectionAdapter extends RecyclerView.Adapter<LeaveCourseSectionAdapter.ViewHolder> {
    private Context context;
    private List<CourseSectionBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public LeaveCourseSectionAdapter(Context context, List<CourseSectionBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_box, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseSectionBean courseSectionBean = data.get(position);
        holder.tv_course_section.setText(courseSectionBean.getSection());
        holder.tv_course_name.setText(courseSectionBean.getName());
        holder.itemView.setSelected(courseSectionBean.isChecked());
        holder.tv_checked_mark.setVisibility(courseSectionBean.isChecked() ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(v -> {
            onClickedListener.onClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_course_section)
        TextView tv_course_section;

        @BindView(R.id.tv_course_name)
        TextView tv_course_name;

        @BindView(R.id.tv_checked_mark)
        ImageView tv_checked_mark;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickedListener {
        void onClicked(int position);
    }
}
