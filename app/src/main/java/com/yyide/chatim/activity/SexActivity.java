package com.yyide.chatim.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.EventMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SexActivity extends BaseActivity {

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
    public int getContentViewID() {
        return R.layout.activity_sex_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("性別");
        String sexStr = getIntent().getStringExtra("sex");
        if (!TextUtils.isEmpty(sexStr)) {
            if ("男".equals(sexStr)) {
                type1.setVisibility(View.VISIBLE);
                type2.setVisibility(View.GONE);
            } else {
                type1.setVisibility(View.GONE);
                type2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.back, R.id.back_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout1://男
                type1.setVisibility(View.VISIBLE);
                type2.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_USER_SEX, "0"));
                finish();
                break;
            case R.id.layout2://女
                type1.setVisibility(View.GONE);
                type2.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_USER_SEX, "1"));
                finish();
                break;
            case R.id.back_layout:
                finish();
                break;
        }
    }


}
