package com.yyide.chatim.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.tencent.imsdk.v2.V2TIMManager;

import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.R;
import com.yyide.chatim.SplashActivity;
import com.yyide.chatim.utils.Constants;
import com.yyide.chatim.utils.DemoLog;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGINED;

public class ChatActivity extends BaseActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatFragment mChatFragment;
    private ChatInfo mChatInfo;
    @BindView(R.id.title)
    TextView title;

    @Override
    public int getContentViewID() {
        return R.layout.chat_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chat(getIntent());
    }

    @OnClick(R.id.back_layout)
    void click(View view){
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        DemoLog.i(TAG, "onNewIntent");
        super.onNewIntent(intent);
        chat(intent);
    }

    @Override
    protected void onResume() {
        DemoLog.i(TAG, "onResume");
        super.onResume();
    }

    private void chat(Intent intent) {
        Bundle bundle = intent.getExtras();
        DemoLog.i(TAG, "bundle: " + bundle + " intent: " + intent);
//        Log.e(TAG, "chat==>bundle: "+ JSON.toJSONString(bundle));
        if (bundle == null) {
            startSplashActivity(null);
            return;
        }

//        OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(intent);
//        if (bean != null) {
//            mChatInfo = new ChatInfo();
//            mChatInfo.setType(bean.chatType);
//            mChatInfo.setId(bean.sender);
//            bundle.putSerializable(Constants.CHAT_INFO, mChatInfo);
//            DemoLog.i(TAG, "offline mChatInfo: " + mChatInfo);
//        } else {
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
//        }
        if(mChatInfo != null){
            title.setText(mChatInfo.getChatName());
        }

        if (V2TIMManager.getInstance().getLoginStatus() == V2TIM_STATUS_LOGINED) {
            mChatFragment = new ChatFragment();
            mChatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();
        } else {
            startSplashActivity(bundle);
        }
    }

    private void startSplashActivity(Bundle bundle) {
        Intent intent = new Intent(ChatActivity.this, SplashActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }
}
