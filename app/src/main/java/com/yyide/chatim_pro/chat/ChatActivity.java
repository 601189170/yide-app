package com.yyide.chatim_pro.chat;

import static com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGINED;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.SplashActivity;
import com.yyide.chatim_pro.base.BaseActivity;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.chat.helper.IBaseLiveListener;
import com.yyide.chatim_pro.chat.helper.TUIKitLiveListenerManager;
import com.yyide.chatim_pro.dialog.ReportPop;
import com.yyide.chatim_pro.model.EventMessage;
import com.yyide.chatim_pro.thirdpush.OfflineMessageDispatcher;
import com.yyide.chatim_pro.utils.Constants;
import com.yyide.chatim_pro.utils.DemoLog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatFragment mChatFragment;
    private ChatInfo mChatInfo;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;

    @Override
    public int getContentViewID() {
        return R.layout.chat_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chat(getIntent());
    }

    @OnClick({R.id.back_layout, R.id.tv_right})
    void click(View view) {
        switch (view.getId()) {
            case R.id.back_layout:
                finish();
                break;
            case R.id.tv_right://举报
                ReportPop reportPop = new ReportPop(this);
                break;
        }

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
        Log.d(TAG, "bundle: " + bundle + " intent: " + intent);
        if (bundle == null) {
            startSplashActivity(null);
        }
        final OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(intent);
        if (bean != null) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancelAll();
            }
        }

        if (V2TIMManager.getInstance().getLoginStatus() != V2TIM_STATUS_LOGINED) {
            Log.d(TAG, "chat: status = "+ V2TIMManager.getInstance().getLoginStatus());
            startSplashActivity(bundle);
            finish();
            return;
        }

        Log.d(TAG, "chat: bean " + bean);

        if (bean != null) {
            if (bean.action == OfflineMessageBean.REDIRECT_ACTION_CALL) {
                DemoLog.i(TAG, "offline push  AV CALL . bean: " + bean);
                startAVCall(bean);
                finish();
                return;
            } else if (bean.action == OfflineMessageBean.REDIRECT_ACTION_CHAT) {
                mChatInfo = new ChatInfo();
                mChatInfo.setType(bean.chatType);
                mChatInfo.setId(bean.sender);
                mChatInfo.setChatName(bean.nickname);
                bundle.putSerializable(Constants.CHAT_INFO, mChatInfo);
                DemoLog.i(TAG, "offline push mChatInfo: " + mChatInfo);
            }
        } else {
            mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
            DemoLog.i(TAG, "start chatActivity chatInfo: " + mChatInfo);
        }
        EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_MESSAGE, ""));
        if (mChatInfo != null) {
            title.setText(mChatInfo.getChatName());
        }
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("举报");

        if (V2TIMManager.getInstance().getLoginStatus() == V2TIM_STATUS_LOGINED) {
            mChatFragment = new ChatFragment();
            mChatFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();
            mChatInfo = null;
        } else {
            finish();
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

    private void startAVCall(OfflineMessageBean bean) {
        IBaseLiveListener baseLiveListener = TUIKitLiveListenerManager.getInstance().getBaseCallListener();
        if (baseLiveListener != null) {
            baseLiveListener.handleOfflinePushCall(bean);
        }
    }

}
