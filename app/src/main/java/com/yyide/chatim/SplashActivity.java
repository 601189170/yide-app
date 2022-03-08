package com.yyide.chatim;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;
import com.yyide.chatim.activity.GuidePageActivity;
import com.yyide.chatim.activity.WebViewActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.login.NewLoginActivity;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.UserInfo;
import com.yyide.chatim.thirdpush.OfflineMessageDispatcher;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int SPLASH_TIME = 1500;
    private View mFlashView;
    private UserInfo mUserInfo;
    public String loginName;
    public String passWord;
    private String refresh_token;
    //是否第一次打开软件
    private boolean firstOpenApp;
    OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int vis = getWindow().getDecorView().getSystemUiVisibility();
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(vis);
        }

        //设置固定字体大小
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

        mFlashView = findViewById(R.id.flash_view);
        mUserInfo = UserInfo.getInstance();
        //Log.e(TAG, "mUserInfo: " + JSON.toJSONString(mUserInfo));
        initData();
        //Log.e(TAG, "loginName: " + JSON.toJSONString(loginName));
        //Log.e(TAG, "passWord: " + JSON.toJSONString(passWord));
        if (!MMKV.defaultMMKV().decodeBool(BaseConstant.SP_PRIVACY, false)) {
            showPrivacy();
        } else if (firstOpenApp) {
            //第一次打开app
            new Handler().postDelayed(() -> startGuidePage(), 500);
        } else {
//            if (SpData.User() != null && SpData.User() != null && !TextUtils.isEmpty(SpData.User().refreshToken)) {
//                toLogin();
//            } else {
            new Handler().postDelayed(() -> startLogin(), 3000);
//            }
        }
    }

    void initData() {
        loginName = SPUtils.getInstance().getString(BaseConstant.LOGINNAME, null);
        passWord = SPUtils.getInstance().getString(BaseConstant.PASSWORD, null);
        firstOpenApp = SPUtils.getInstance().getBoolean(BaseConstant.FIRST_OPEN_APP, true);
    }

//    void toLogin() {
//        if (SpData.User() != null && SpData.User() != null && !TextUtils.isEmpty(SpData.User().refreshToken)) {
//            RequestBody body = new FormBody.Builder()
//                    .add("client_id", "yide-cloud")
//                    .add("grant_type", "refresh_token")
//                    .add("refresh_token", SpData.User().refreshToken)
//                    .add("client_secret", "yide1234567")
//                    .build();
//            //请求组合创建
//            Request request = new Request.Builder()
//                    .url(BaseConstant.API_SERVER_URL + "/auth/oauth/token")
//                    .post(body)
//                    .build();
//            //发起请求
//            mOkHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    startLogin();
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String data = response.body().string();
//                    Log.e(TAG, "mOkHttpClient==>: " + data);
//                    if (response.code() == BaseConstant.REQUEST_SUCCES2) {
//                        LoginRsp bean = JSON.parseObject(data, LoginRsp.class);
//                        if (bean.code == BaseConstant.REQUEST_SUCCES2) {
//                            //存储登录信息
//                            SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(bean));
//                            getUserSchool();
//                        } else {
//                            startLogin();
//                        }
//                    } else {
//                        startLogin();
//                    }
//                }
//            });
//        } else {
//            startLogin();
//        }
//    }

    private void handleData() {
        mFlashView.postDelayed(() -> startMain(), SPLASH_TIME);
    }

    private void startLogin() {
        Intent intent = new Intent(SplashActivity.this, NewLoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 去引导页
     */
    private void startGuidePage() {
        Intent intent = new Intent(SplashActivity.this, GuidePageActivity.class);
        startActivityForResult(intent, 100);
    }

    private void startMain() {
        OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(getIntent());
        if (bean != null) {
            OfflineMessageDispatcher.redirect(bean);
            finish();
            return;
        }
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 显示用户协议和隐私政策
     */
    private void showPrivacy() {

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_privacy);
        TextView tv_privacy_tips = dialog.findViewById(R.id.tv_privacy_tips);
        TextView btn_exit = dialog.findViewById(R.id.btn_exit);
        TextView btn_enter = dialog.findViewById(R.id.btn_enter);
        dialog.show();

        String string = getResources().getString(R.string.privacy_tips);
        String key1 = getResources().getString(R.string.privacy_tips_key1);
        String key2 = getResources().getString(R.string.privacy_tips_key2);
        int index1 = string.indexOf(key1);
        int index2 = string.indexOf(key2);

        //需要显示的字串
        SpannableString spannedString = new SpannableString(string);
        //设置点击字体颜色
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.blue));
        spannedString.setSpan(colorSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.blue));
        spannedString.setSpan(colorSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击字体大小
        AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(14, true);
        spannedString.setSpan(sizeSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        AbsoluteSizeSpan sizeSpan2 = new AbsoluteSizeSpan(14, true);
        spannedString.setSpan(sizeSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击事件
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                WebViewActivity.startTitle(SplashActivity.this, BaseConstant.AGREEMENT_URL, getString(R.string.agreement_title));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                WebViewActivity.startTitle(SplashActivity.this, BaseConstant.PRIVACY_URL, getString(R.string.privacy_title));

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置点击后的颜色为透明，否则会一直出现高亮
        tv_privacy_tips.setHighlightColor(Color.TRANSPARENT);
        //开始响应点击事件
        tv_privacy_tips.setMovementMethod(LinkMovementMethod.getInstance());

        tv_privacy_tips.setText(spannedString);

        //设置弹框宽度占屏幕的80%
        WindowManager m = getWindowManager();
        Display defaultDisplay = m.getDefaultDisplay();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (defaultDisplay.getWidth() * 0.90);
        dialog.getWindow().setAttributes(params);

        btn_exit.setOnClickListener(v -> {
            dialog.dismiss();
            MMKV.defaultMMKV().encode(BaseConstant.SP_PRIVACY, false);
            finish();
        });

        btn_enter.setOnClickListener(v -> {
            dialog.dismiss();
            MMKV.defaultMMKV().encode(BaseConstant.SP_PRIVACY, true);
            BaseApplication.getInstance().initSdk();
            if (firstOpenApp) {
                //第一次打开app
                new Handler().postDelayed(this::startGuidePage, 500);
            } else {
//                if (SpData.User() != null && SpData.User() != null && !TextUtils.isEmpty(SpData.User().refreshToken)) {
//                    toLogin();
//                } else {
//                    startLogin();
//                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult requestCode " + requestCode + ",resultCode " + resultCode);
        if (RESULT_OK == resultCode && 100 == requestCode) {
            if (data != null) {
                final boolean interrupt = data.getBooleanExtra("interrupt", false);
                if (interrupt) {
                    //引导没有看完直接退出。
                    finish();
                    return;
                }
            }
            /*if (SpData.User() != null && SpData.User() != null && !TextUtils.isEmpty(SpData.User().refreshToken)) {
                toLogin();
            } else {
                startLogin();
            }*/
        }
    }
}
