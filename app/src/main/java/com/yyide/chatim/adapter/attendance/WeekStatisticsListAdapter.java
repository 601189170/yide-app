package com.yyide.chatim.adapter.attendance;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.databinding.ListItemBinding;
import com.yyide.chatim.model.DayStatisticsBean;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.model.WeekStatisticsBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: 考勤>周统计
 * @Author: liu tao
 * @CreateDate: 2021/5/29 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/29 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class WeekStatisticsListAdapter extends RecyclerView.Adapter<WeekStatisticsListAdapter.ViewHolder> {
    private Context context;
    private List<WeekStatisticsBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public WeekStatisticsListAdapter(Context context, List<WeekStatisticsBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_statistics_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final WeekStatisticsBean weekStatisticsBean = data.get(position);
        final String name = weekStatisticsBean.getName();
        holder.tv_name.setText(name);
        final String string = context.getString(R.string.attendance_time_format);
        holder.tv_attendance_time.setText(String.format(string,weekStatisticsBean.getTime()));
        holder.child_recyclerview.setLayoutManager(new LinearLayoutManager(context));
        BaseQuickAdapter adapter = new BaseQuickAdapter<WeekStatisticsBean.DataBean, BaseViewHolder>(R.layout.item_statistics_child_list) {
            @Override
            protected void convert(@NotNull BaseViewHolder baseViewHolder, WeekStatisticsBean.DataBean dataBean) {
                baseViewHolder
                        .setText(R.id.tv_name, dataBean.getTime()+" "+dataBean.getTitle())
                        .setText(R.id.tv_attendance_time, dataBean.getStatus());
            }
        };
        holder.child_recyclerview.setAdapter(adapter);
        adapter.setList(weekStatisticsBean.getDataBeanList());
        holder.child_recyclerview.setVisibility(weekStatisticsBean.isChecked()?View.VISIBLE:View.GONE);
        if (weekStatisticsBean.isChecked()) {
            holder.iv_direction.setImageResource(R.drawable.icon_arrow_up);
        }else {
            holder.iv_direction.setImageResource(R.drawable.icon_arrow_down);
        }
        //设置一级点击事件
        holder.itemView.setOnClickListener(v -> {
                onClickedListener.onClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_attendance_time)
        TextView tv_attendance_time;
        @BindView(R.id.child_recyclerview)
        RecyclerView child_recyclerview;
        @BindView(R.id.iv_direction)
        ImageView iv_direction;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickedListener{
        void onClicked(int position);
    }
}
