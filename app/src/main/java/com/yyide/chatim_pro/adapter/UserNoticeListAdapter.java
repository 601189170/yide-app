package com.yyide.chatim_pro.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.model.UserMsgNoticeRsp;
import com.yyide.chatim_pro.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @Description: adapter
 * @Author: liu tao
 * @CreateDate: 2021/3/25 16:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/25 16:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UserNoticeListAdapter extends BaseQuickAdapter<UserMsgNoticeRsp.DataBean.DataItemBean, BaseViewHolder> implements LoadMoreModule {

    public UserNoticeListAdapter() {
        super(R.layout.item_message_notice);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, UserMsgNoticeRsp.DataBean.DataItemBean recordsBean) {
        String createdDateTime = recordsBean.getSendTime();
        holder.setText(R.id.tv_date_start, DateUtils.formatTime(createdDateTime, "", "yyyy-MM-dd HH:mm:ss"));
        holder.setText(R.id.tv_title, recordsBean.getTitle());
        holder.setText(R.id.tv_msg_type, switchAttributeType(recordsBean.getModuleType()));
        View view = holder.getView(R.id.rl_detail);
        final String content = recordsBean.getContent();
        final String templateContent = parseTemplateContent(content);
        Log.e("TAG", "onBindViewHolder: " + content);
        if (TextUtils.isEmpty(templateContent)) {
            holder.setText(R.id.tv_content, content);
        } else {
            holder.setText(R.id.tv_content, templateContent);
        }

        //是否显示查看详情
        switch (recordsBean.getModuleType()) {
            case 1:
            case 2:
                view.setVisibility(View.GONE);
                break;
        }
    }

    private String parseTemplateContent(String content) {
        final StringBuilder buffer = new StringBuilder();
        try {
            final JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                final String jsonObject = jsonArray.getString(i);
                if (i != jsonArray.length() - 1) {
                    buffer.append(jsonObject).append("\n").append("\n");
                } else {
                    buffer.append(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    //模块类型模块类型 1闸机推送 2考勤 3考勤周报 4请假 5通知公告 6失物招领 7作业发布
    private String switchAttributeType(int type) {
        String attributeType;
        switch (type) {
            case 4:
                attributeType = "请假";
                break;
            case 5:
                attributeType = "通知公告";
                break;
            default:
                attributeType = "消息通知";
                break;
        }
        return attributeType;
    }

}
