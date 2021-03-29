package com.yyide.chatim.activity;


import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpInfoActivity extends BaseActivity {


    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;

    @Override
    public int getContentViewID() {
        return R.layout.activity_help_info_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("入门指南");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        finish();
    }
}
