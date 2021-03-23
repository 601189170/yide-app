package com.yyide.chatim.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PowerActivity extends BaseActivity {


    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layout3)
    FrameLayout layout3;
    @BindView(R.id.layout4)
    FrameLayout layout4;
    @BindView(R.id.layout5)
    FrameLayout layout5;
    @BindView(R.id.layout1)
    FrameLayout layout1;
    @BindView(R.id.layout2)
    FrameLayout layout2;
    @BindView(R.id.layout10)
    FrameLayout layout10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_layout);
        ButterKnife.bind(this);
        title.setText("权限设置");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }
}
