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

public class SexActivity extends BaseActivity {


    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.type1)
    TextView type1;
    @BindView(R.id.layout1)
    FrameLayout layout1;
    @BindView(R.id.type2)
    TextView type2;
    @BindView(R.id.layout2)
    FrameLayout layout2;
    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_layout);
        ButterKnife.bind(this);
        title.setText("性別");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @OnClick({R.id.layout1, R.id.layout2,R.id.back, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout1:
                type1.setVisibility(View.VISIBLE);
                type2.setVisibility(View.GONE);
                break;
            case R.id.layout2:
                type1.setVisibility(View.GONE);
                type2.setVisibility(View.VISIBLE);
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }


}
