package com.yyide.chatim;


import android.os.Bundle;
import android.view.View;

import com.yyide.chatim.home.ConversationFragment;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public class NoticeActivity extends AppCompatActivity {
//    @BindView(R.id.content)
//    FrameLayout content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_layout);
        ButterKnife.bind(this);

    }
}
