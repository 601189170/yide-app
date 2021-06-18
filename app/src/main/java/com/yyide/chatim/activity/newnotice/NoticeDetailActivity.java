package com.yyide.chatim.activity.newnotice;

import android.os.Bundle;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityNoticeDetail2Binding;

public class NoticeDetailActivity extends BaseActivity {

    private ActivityNoticeDetail2Binding detailBinding;

    @Override
    public int getContentViewID() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = ActivityNoticeDetail2Binding.inflate(getLayoutInflater());
        setContentView(detailBinding.getRoot());
        initView();
    }

    private void initView() {
        detailBinding.include.title.setText(R.string.notice_my_push_title);
        detailBinding.include.backLayout.setOnClickListener(v -> finish());
    }

}