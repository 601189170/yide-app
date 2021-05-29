package com.yyide.chatim.adapter.attendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.DayStatisticsBean;
import com.yyide.chatim.model.LeaveFlowBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/5/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/5/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DayStatisticsListAdapter extends RecyclerView.Adapter<DayStatisticsListAdapter.ViewHolder> {
    private Context context;
    private List<DayStatisticsBean> data;

    public void setOnClickedListener(OnClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    private OnClickedListener onClickedListener;

    public DayStatisticsListAdapter(Context context, List<DayStatisticsBean> data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance_day_statistics,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DayStatisticsBean dayStatisticsBean = data.get(position);
        final String attendanceTime = String.format(context.getString(R.string.attendance_time_text), dayStatisticsBean.getTime());
        holder.tv_attendance_time.setText(attendanceTime);
        holder.tv_attendance_rate.setText(""+dayStatisticsBean.getRate());
        holder.tv_due_num.setText(""+dayStatisticsBean.getDue());
        holder.tv_normal_num.setText(""+dayStatisticsBean.getNormal());
        holder.tv_absence_num.setText(""+dayStatisticsBean.getAbsence());
        holder.tv_ask_for_leave_num.setText(""+dayStatisticsBean.getAsk_for_leave());
        holder.tv_late_num.setText(""+dayStatisticsBean.getLate());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_attendance_time)
        TextView tv_attendance_time;

        @BindView(R.id.tv_attendance_rate)
        TextView tv_attendance_rate;

        @BindView(R.id.tv_due_num)
        TextView tv_due_num;

        @BindView(R.id.tv_normal_num)
        TextView tv_normal_num;

        @BindView(R.id.tv_absence_num)
        TextView tv_absence_num;

        @BindView(R.id.tv_ask_for_leave_num)
        TextView tv_ask_for_leave_num;

        @BindView(R.id.tv_late_num)
        TextView tv_late_num;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickedListener{
        void onClicked(int position);
    }
}
