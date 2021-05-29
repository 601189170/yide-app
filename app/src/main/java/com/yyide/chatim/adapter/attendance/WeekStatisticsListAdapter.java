package com.yyide.chatim.adapter.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.databinding.ListItemBinding;
import com.yyide.chatim.model.DayStatisticsBean;
import com.yyide.chatim.model.WeekStatisticsBean;

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
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickedListener{
        void onClicked(int position);
    }
}
