package com.yyide.chatim.adapter;

import android.content.Context;
import android.text.Html;
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
            if (isLoadMore) {
                itemView.setVisibility(View.VISIBLE);
                itemView.setLoading(!isLastPage);
            } else {
                itemView.setVisibility(View.GONE);
            }
        } else {
            ViewHolder holder1 = (ViewHolder) holder;
            NoticeListRsp.DataBean.RecordsBean noticeAnnouncementModel = data.get(position);
            String timingTime = noticeAnnouncementModel.getTimingTime();
            if (!TextUtils.isEmpty(timingTime)) {
                // DateUtils.formatTime(timingTime,)
                long[] dateDiff = DateUtils.dateDiff(timingTime);
                if (dateDiff.length == 3) {
                    holder1.tv_timed_send_time.setVisibility(View.VISIBLE);
                    if (dateDiff[0] != 0) {
                        String string = context.getString(R.string.timed_send_long_text);
                        holder1.tv_timed_send_time.setText(String.format(string, dateDiff[0]));
                    } else {
                        String string = context.getString(R.string.timed_send_text);
                        holder1.tv_timed_send_time.setText(String.format(string, dateDiff[1], dateDiff[2]));
                    }
                }
            } else {
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
            holder1.tv_notice_author.setText(noticeAnnouncementModel.getProductionTarget());
            holder1.tv_notice_content.setText(Html.fromHtml(noticeAnnouncementModel.getContent()));
            holder1.tv_confirm_number.setText(confirmNumber);
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
            return "学生";
        if ("3".equals(sendObj))
            return "教职工";
        return "学生";
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
