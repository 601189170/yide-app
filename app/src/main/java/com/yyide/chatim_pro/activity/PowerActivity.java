package com.yyide.chatim_pro.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PowerActivity extends BaseActivity {

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
    public int getContentViewID() {
        return R.layout.activity_power_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    @OnClick({R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
        }
    }
}
