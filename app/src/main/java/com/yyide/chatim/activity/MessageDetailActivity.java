package com.yyide.chatim.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityMessageDetailBinding;
import com.yyide.chatim.model.UserMsgNoticeRsp;

/**
 * 消息通知详情页
 *
 */
public class MessageDetailActivity extends BaseActivity {

    private ActivityMessageDetailBinding mViewBinding;
    @Override
    public int getContentViewID() {
        return R.layout.activity_message_detail;
    }

    public static void start(Context context, UserMsgNoticeRsp.DataBean.RecordsBean bean){
        Intent intent = new Intent(context, MessageDetailActivity.class);
        intent.putExtra("detail", bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityMessageDetailBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        initView();
    }

    private void initView(){
        UserMsgNoticeRsp.DataBean.RecordsBean bean = (UserMsgNoticeRsp.DataBean.RecordsBean) getIntent().getSerializableExtra("detail");
        mViewBinding.top.title.setText("消息详情");
        mViewBinding.top.backLayout.setOnClickListener(v -> finish());
        if(bean != null){
            mViewBinding.tvNoticeTitle.setText(bean.getTitle());
//            mViewBinding.tvNoticeAuthor.setText(bean.get);
            mViewBinding.tvNoticeTime.setText(bean.getSendTime());
            mViewBinding.tvNoticeContent.setText(bean.getContent());
        }
    }
}