package com.yyide.chatim.leave;


import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeaveInfoActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.startTime)
    TextView startTime;
    @BindView(R.id.endTime)
    TextView endTime;

    @Override
    public int getContentViewID() {
        return R.layout.activity_leaveinfo_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("详情");

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
