package com.yyide.chatim.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyide.chatim.R;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.view.FootView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/3/25 16:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/25 16:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NoticeAnnouncementListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<NoticeAnnouncementModel> data;
    //上拉加载更多布局
    public static final int view_Foot = 1;
    //主要布局
    public static final int view_Normal = 2;
    //是否隐藏
    public boolean isLoadMore = false;

    private View view;

    private OnItemOnClickListener onItemOnClickListener;

    public boolean isLastPage = false;

    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.onItemOnClickListener = onItemOnClickListener;
    }
    public NoticeAnnouncementListAdapter(Context context, List<NoticeAnnouncementModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == view_Normal){
            view = LayoutInflater.from(context).inflate(R.layout.item_notice_announcement, parent, false);
            return new ViewHolder(view);
        }else {
            FootView view = new FootView(context);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount()-1){
            FootView itemView = (FootView) holder.itemView;
            if (isLoadMore){
                itemView.setVisibility(View.VISIBLE);
                itemView.setLoading(!isLastPage);
            }else {
                itemView.setVisibility(View.GONE);
            }
        }else {
            ViewHolder  holder1= (ViewHolder)holder;
            NoticeAnnouncementModel noticeAnnouncementModel = data.get(position);
            holder1.tv_notice.setText(noticeAnnouncementModel.getNoticeTitle());
            holder1.tv_notice_time.setText(noticeAnnouncementModel.getNoticeTime());
            holder1.tv_notice_author.setText(String.format(context.getString(R.string.notice_release_obj_text),noticeAnnouncementModel.getNoticeAuthor()));
            holder1.tv_notice_content.setText(noticeAnnouncementModel.getNoticeContent());

            if ("1".equals(noticeAnnouncementModel.getStatus())){
                holder1.tv_confirm.setText("已确认");
                holder1.tv_confirm.setBackground(context.getDrawable(R.drawable.bg_corners_gray2_18));
                holder1.tv_confirm.setTextColor(context.getResources().getColor(R.color.black10));
                holder1.iv_pic.setImageDrawable(context.getDrawable(R.drawable.ic_announcement_mark_read));
            }else {
                holder1.tv_confirm.setText("去确认");
                holder1.tv_confirm.setBackground(context.getDrawable(R.drawable.bg_corners_blue_18));
                holder1.tv_confirm.setTextColor(context.getResources().getColor(R.color.white));
                holder1.iv_pic.setImageDrawable(context.getDrawable(R.drawable.ic_announcement));
            }

            holder1.tv_confirm.setOnClickListener(v -> {
                onItemOnClickListener.onClicked(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }
    public void setIsLoadMore(boolean loadMore) {
        this.isLoadMore = loadMore;
    }

    //设置是最后一页
    public void setIsLastPage(boolean lastPage){
        this.isLastPage = lastPage;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1){
            return view_Foot;
        }else {
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

        @BindView(R.id.tv_confirm)
        TextView tv_confirm;



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

    public interface OnItemOnClickListener{
        void onClicked(int position);
    }
}
