package com.yyide.chatim_pro.jiguang;


import android.os.Bundle;

import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.base.BaseActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class NoticeActivity extends BaseActivity {
//    @BindView(R.id.content)
//    FrameLayout content;


    @Override
    public int getContentViewID() {
        return R.layout.activity_notice_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
