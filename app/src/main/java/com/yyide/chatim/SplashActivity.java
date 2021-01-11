package com.yyide.chatim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;

import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.yyide.chatim.chat.info.UserInfo;
import com.yyide.chatim.utils.DemoLog;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int SPLASH_TIME = 1500;
    private View mFlashView;
    private UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        mFlashView = findViewById(R.id.flash_view);
        mUserInfo = UserInfo.getInstance();
        Log.e(TAG, "mUserInfo: "+JSON.toJSONString(mUserInfo) );
        handleData();
    }

    private void handleData() {
        if (mUserInfo != null && mUserInfo.isAutoLogin()) {
            login();
        } else {
            mFlashView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startLogin();
                }
            }, SPLASH_TIME);
        }
    }

    private void login() {
        TUIKit.login(mUserInfo.getUserId(), mUserInfo.getUserSig(), new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);

                        Log.e(TAG, "UserInfo: "+JSON.toJSONString(UserInfo.getInstance()) );

//                        startMain();
                        startLogin();

                    }
                });
                DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {
                Log.e(TAG, "UserInfo==》: "+JSON.toJSONString(UserInfo.getInstance()) );
                startMain();
            }
        });
    }

    private void startLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startMain() {
//        OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(getIntent());
//        if (bean != null) {
//            OfflineMessageDispatcher.redirect(bean);
//            finish();
//            return;
//        }

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
