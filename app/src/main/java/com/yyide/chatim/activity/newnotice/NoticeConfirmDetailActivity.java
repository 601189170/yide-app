package com.yyide.chatim.activity.newnotice;

import android.os.Bundle;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.databinding.ActivityNoticeConfirmDetailBinding;

public class NoticeConfirmDetailActivity extends BaseActivity {

    private ActivityNoticeConfirmDetailBinding confirmDetailBinding;

    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_confirm_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confirmDetailBinding = ActivityNoticeConfirmDetailBinding.inflate(getLayoutInflater());
        setContentView(confirmDetailBinding.getRoot());
        initView();
    }

    private void initView() {
        confirmDetailBinding.include.title.setText(R.string.notice_announcement_title);
        confirmDetailBinding.include.backLayout.setOnClickListener(v -> finish());
        confirmDetailBinding.btnConfirm.setOnClickListener(v -> {});
    }


}