package com.yyide.chatim.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.R;
import com.yyide.chatim.model.NoticeAnnouncementModel;
import com.yyide.chatim.model.UserMsgNoticeRsp;
import com.yyide.chatim.model.UserNoticeRsp;
import com.yyide.chatim.utils.DateUtils;
import com.yyide.chatim.view.FootView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
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
public class UserNoticeListAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<UserMsgNoticeRsp.DataBean.RecordsBean> data;
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

    public UserNoticeListAdapter(Context context, List<UserMsgNoticeRsp.DataBean.RecordsBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == view_Normal) {
            view = LayoutInflater.from(context).inflate(R.layout.item_message_notice, parent, false);
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
            UserMsgNoticeRsp.DataBean.RecordsBean recordsBean = data.get(position);
            String createdDateTime = recordsBean.getSendTime();
            holder1.tv_date.setText(DateUtils.formatTime(createdDateTime, "", "yyyy-MM-dd"));
            holder1.tv_title.setText(recordsBean.getTitle());
            holder1.tv_leave.setText(recordsBean.getFirstData());
            final String content = recordsBean.getContent();
            final String templateContent = parseTemplateContent(content);
            Log.e("TAG", "onBindViewHolder: " + content);
            if (TextUtils.isEmpty(templateContent)) {
                holder1.tv_content.setText(content);
            } else {
                holder1.tv_content.setText(templateContent);
            }
            //不是纯文本则可以跳转详情
            if ("2".equals(recordsBean.getIsText())) {
                holder1.rl_detail.setVisibility(View.VISIBLE);
            } else {
                holder1.rl_detail.setVisibility(View.GONE);
            }
            //消息类型
            holder1.tv_msg_type.setText(switchAttributeType(recordsBean.getAttributeType()));
            holder1.itemView.setOnClickListener(v -> {
                onItemOnClickListener.onClicked(position);
            });
        }
    }

    private String parseTemplateContent(String content) {
        final StringBuffer buffer = new StringBuffer();
        try {
            final JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                final String jsonObject = jsonArray.getString(i);
                if (i != jsonArray.length() - 1) {
                    buffer.append(jsonObject + "\n" + "\n");
                } else {
                    buffer.append(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


    private String switchAttributeType(String attributeType) {
        if (TextUtils.isEmpty(attributeType)) {
            return "";
        }
        switch (attributeType) {
            case "1":
                attributeType = "请假";
                break;
            case "2":
                attributeType = "调课";
                break;
            default:
                attributeType = "通知";
                break;
        }
        return attributeType;
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

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_leave)
        TextView tv_leave;

        @BindView(R.id.tv_msg_type)
        TextView tv_msg_type;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.rl_detail)
        RelativeLayout rl_detail;

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
    }
}
