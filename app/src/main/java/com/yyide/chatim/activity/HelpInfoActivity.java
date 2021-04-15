package com.yyide.chatim.activity;


import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyide.chatim.R;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.model.HelpItemRep;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpInfoActivity extends BaseActivity {

    @BindView(R.id.back_layout)
    LinearLayout backLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_helpTitle)
    TextView tv_helpTitle;
    @BindView(R.id.tc_content)
    TextView tc_content;

    @Override
    public int getContentViewID() {
        return R.layout.activity_help_info_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("入门指南");
        HelpItemRep.Records.HelpItemBean itemBean = (HelpItemRep.Records.HelpItemBean) getIntent().getSerializableExtra("itemBean");
        if (itemBean != null) {
            tv_helpTitle.setText(itemBean.getName());
            tc_content.setText(Html.fromHtml(itemBean.getMessage()));
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


    @OnClick(R.id.back_layout)
    public void onViewClicked() {
        finish();
    }
}
