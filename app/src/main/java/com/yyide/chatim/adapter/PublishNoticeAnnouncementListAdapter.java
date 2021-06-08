package com.yyide.chatim.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.FootView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: PublishNoticeAnnouncementListAdapter
 * @Author: liu tao
 * @CreateDate: 2021/3/25 16:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/25 16:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PublishNoticeAnnouncementListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PublishNotice";
    private Context context;
    private List<NoticeListRsp.DataBean.RecordsBean> data;
    //上拉加载更多布局
    public static final int view_Foot = 1;
    //主要布局
    public static final int view_Normal = 2;
    //是否隐藏
    public boolean isLoadMore = false;

    public boolean isLastPage = false;
    private Timer timer;
    private TimerTask timerTask;

    public void setOnlyOnePage(boolean onlyOnePage) {
        this.onlyOnePage = onlyOnePage;
    }

    //是否是只有一页
    public boolean onlyOnePage = false;
    private View view;

    private OnItemOnClickListener onItemOnClickListener;

    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.onItemOnClickListener = onItemOnClickListener;
    }

    public PublishNoticeAnnouncementListAdapter(Context context, List<NoticeListRsp.DataBean.RecordsBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == view_Normal) {
            view = LayoutInflater.from(context).inflate(R.layout.item_my_publish_announcement, parent, false);
            return new ViewHolder(view);
        } else {
            FootView view = new FootView(context);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            FootView itemView = (FootView) holder.itemView;
            if (isLoadMore && !onlyOnePage) {
                itemView.setVisibility(View.VISIBLE);
                itemView.setLoading(!isLastPage);
            } else {
                itemView.setVisibility(View.GONE);
            }
        } else {
            ViewHolder holder1 = (ViewHolder) holder;
            NoticeListRsp.DataBean.RecordsBean noticeAnnouncementModel = data.get(position);
            String timingTime = noticeAnnouncementModel.getTimingTime();
            Log.e(TAG, "onBindViewHolder: timingTime="+timingTime);
            if (!TextUtils.isEmpty(timingTime)) {
                holder1.tv_notice.setMaxEms(5);
                holder1.tv_notice.setEllipsize(TextUtils.TruncateAt.END);
                // DateUtils.formatTime(timingTime,)
                long[] dateDiff = DateUtils.dateDiff(timingTime);
                if (dateDiff.length == 4) {
                    holder1.tv_timed_send_time.setVisibility(View.VISIBLE);
                    if (dateDiff[0] != 0) {
                        String string = context.getString(R.string.timed_send_long_text);
                        holder1.tv_timed_send_time.setText(String.format(string, dateDiff[0]));
                    } else {
                        String string = context.getString(R.string.timed_send_text);
                        holder1.tv_timed_send_time.setText(String.format(string, dateDiff[1], dateDiff[2],dateDiff[3]));
                    }
                    if (timer == null){
                        timer = new Timer();
                    }
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            long[] dateDiff = DateUtils.dateDiff(timingTime);
                            if (dateDiff.length == 4) {
                                ((Activity) context).runOnUiThread(() -> {
                                    if (dateDiff[0] == 0 && dateDiff[1] == 0 && dateDiff[2] == 0 && dateDiff[3] == 0) {
                                        this.cancel();
                                        noticeAnnouncementModel.setTimingTime(null);
                                        holder1.tv_timed_send_time.setVisibility(View.GONE);
                                        return;
                                    }
                                    if (dateDiff[0] != 0) {
                                        String string = context.getString(R.string.timed_send_long_text);
                                        holder1.tv_timed_send_time.setText(String.format(string, dateDiff[0]));
                                    } else {
                                        String string = context.getString(R.string.timed_send_text);
                                        holder1.tv_timed_send_time.setText(String.format(string, dateDiff[1], dateDiff[2], dateDiff[3]));
                                    }
                                });
                            } else {
                                this.cancel();
                                ((Activity) context).runOnUiThread(() -> {
                                    holder1.tv_notice.setMaxEms(12);
                                    holder1.tv_notice.setEllipsize(TextUtils.TruncateAt.END);
                                    noticeAnnouncementModel.setTimingTime(null);
                                    holder1.tv_timed_send_time.setVisibility(View.GONE);
                                });
                            }

                        }
                    };
                    timer.schedule(timerTask,0,1000);
                }
            } else {
                holder1.tv_notice.setMaxEms(12);
                holder1.tv_notice.setEllipsize(TextUtils.TruncateAt.END);
                holder1.tv_timed_send_time.setVisibility(View.GONE);
            }

            int readNumber = noticeAnnouncementModel.getReadNumber();
            int totalNumber = noticeAnnouncementModel.getTotalNumber();
            String confirm_number_format = context.getResources().getString(R.string.notice_confirm_number);
            String confirmNumber = String.format(confirm_number_format, readNumber, totalNumber);
            String sendObject = noticeAnnouncementModel.getSendObject();
            String notice_send_obj = context.getResources().getString(R.string.notice_send_obj);
            String sendObj = String.format(notice_send_obj, getSendObj(sendObject));

            holder1.tv_notice.setText(noticeAnnouncementModel.getTitle());
            String productionTime = DateUtils.formatTime(noticeAnnouncementModel.getProductionTime(), null, null);
            holder1.tv_notice_time.setText(productionTime);
            holder1.tv_notice_author.setText(String.format(context.getString(R.string.notice_release_obj_text),noticeAnnouncementModel.getProductionTarget()));
            holder1.tv_notice_content.setText(Html.fromHtml(noticeAnnouncementModel.getContent()));
            if (getSendObj(sendObject).equals("学生")) {
                holder1.tv_confirm_number.setVisibility(View.INVISIBLE);
            }else {
                holder1.tv_confirm_number.setVisibility(View.VISIBLE);
                holder1.tv_confirm_number.setText(confirmNumber);
            }
            holder1.tv_send_obj.setText(sendObj);

            holder1.btn_delete.setOnClickListener(v -> {
                onItemOnClickListener.delete(position);
            });
            holder1.itemView.setOnClickListener(v -> {
                onItemOnClickListener.onClicked(position);
            });
        }
    }

    //发送对象 ： 1家长 2学生 3部门
    private String getSendObj(String sendObj) {
        if ("1".equals(sendObj))
            return "家长";
        if ("2".equals(sendObj))
            return "班级";
        if ("3".equals(sendObj))
            return "教职工";
        return "班级";
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public void setIsLoadMore(boolean loadMore) {
        this.isLoadMore = loadMore;
    }

    //设置是最后一页
    public void setIsLastPage(boolean lastPage) {
        this.isLastPage = lastPage;
    }

    public void cancelTimer(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return view_Foot;
        } else {
            return view_Normal;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_pic)
        ImageView iv_pic;//显示图片

        @BindView(R.id.tv_notice)
        TextView tv_notice;

        @BindView(R.id.tv_notice_time)
        TextView tv_notice_time;

        @BindView(R.id.tv_notice_author)
        TextView tv_notice_author;

        @BindView(R.id.tv_notice_content)
        TextView tv_notice_content;

        @BindView(R.id.btn_delete)
        ImageView btn_delete;

        @BindView(R.id.tv_send_obj)
        TextView tv_send_obj;

        @BindView(R.id.tv_confirm_number)
        TextView tv_confirm_number;

        @BindView(R.id.tv_timed_send_time)
        TextView tv_timed_send_time;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemOnClickListener {
        void onClicked(int position);

        void delete(int position);
    }
}
