package com.yyide.chatim;


import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.heytap.msp.push.HeytapPushManager;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.shehuan.nicedialog.BaseNiceDialog;
import com.shehuan.nicedialog.NiceDialog;
import com.shehuan.nicedialog.ViewConvertListener;
import com.shehuan.nicedialog.ViewHolder;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.tencent.imsdk.v2.V2TIMSignalingInfo;
import com.tencent.liteav.model.CallModel;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.UnreadCountTextView;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.yyide.chatim.alipush.AliasUtil;
import com.yyide.chatim.alipush.MyMessageReceiver;
import com.yyide.chatim.alipush.NotifyUtil;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.home.AppFragment;
import com.yyide.chatim.home.HelpFragment;
import com.yyide.chatim.home.HomeFragment;
import com.yyide.chatim.home.MessageFragment;
import com.yyide.chatim.jiguang.ExampleUtil;
import com.yyide.chatim.jiguang.LocalBroadcastManager;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetAppVersionResponse;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.UserInfo;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.UserSigRsp;
import com.yyide.chatim.net.AppClient;
import com.yyide.chatim.presenter.MainPresenter;
import com.yyide.chatim.thirdpush.HUAWEIHmsMessageService;
import com.yyide.chatim.thirdpush.OPPOPushImpl;
import com.yyide.chatim.thirdpush.OfflineMessageDispatcher;
import com.yyide.chatim.thirdpush.ThirdPushTokenMgr;
import com.yyide.chatim.utils.BrandUtil;
import com.yyide.chatim.utils.Constants;
import com.yyide.chatim.utils.DemoLog;
import com.yyide.chatim.utils.LogUtil;
import com.yyide.chatim.utils.PrivateConstants;
import com.yyide.chatim.view.DialogUtil;
import com.yyide.chatim.view.MainView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements ConversationManagerKit.MessageUnreadWatcher, MainView, HomeFragment.FragmentListener {

    @BindView(R.id.content)
    FrameLayout content;
    //for receive customer msg from jpush server
    public static boolean isForeground = false;
    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.tab1_layout)
    ConstraintLayout tab1Layout;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.tab2_layout)
    ConstraintLayout tab2Layout;
    @BindView(R.id.tab3)
    CheckedTextView tab3;
    @BindView(R.id.tab3_layout)
    ConstraintLayout tab3Layout;
    @BindView(R.id.tab4)
    CheckedTextView tab4;
    @BindView(R.id.tab4_layout)
    ConstraintLayout tab4Layout;
    @BindView(R.id.msg_total_unread)
    UnreadCountTextView msgTotalUnread;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "cn.jiguang.demo.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public String TAG = "MainActivity";

    private long firstTime = 0;
    private CallModel mCallModel;

    @Override
    public int getContentViewID() {
        return R.layout.activity_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        registerMessageReceiver();  // used for receive msg
//        permission();
        // 未读消息监视器
        //登录IM
        getUserSig();
        setTab(1, 0);
        setTab(0, 0);
        //注册极光别名
        //registerAlias();
        AliasUtil.syncAliases();
        //登录IM
        //处理失败时点击切换重新登录IM
        prepareThirdPushToken();

        //离线消息推送处理
        final String extras = getIntent().getStringExtra("extras");
        if (!TextUtils.isEmpty(extras)) {
            content.postDelayed(() -> {
                final Intent intent = new Intent(this, MyMessageReceiver.class);
                intent.setAction("notification_clicked");
                intent.putExtra("extras", extras);
                sendBroadcast(intent);
            }, 3000);
        }

        //应用更新检测
        new Handler().postDelayed(() -> mvpPresenter.getVersionInfo(), 5000);
        //检查是否开启消息通知
        showNotificationPermission();
    }

    private void prepareThirdPushToken() {
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
        if (BrandUtil.isBrandHuawei()) {
            // 华为离线推送
            new Thread() {
                @Override
                public void run() {
                    try {
                        // read from agconnect-services.json
                        String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                        String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                        DemoLog.i(TAG, "huawei get token:" + token);
                        if(!TextUtils.isEmpty(token)) {
                            ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
                            ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                        }
                    } catch (ApiException e) {
                        DemoLog.e(TAG, "huawei get token failed, " + e);
                    }
                }
            }.start();
        } else if (BrandUtil.isBrandVivo()) {
            // vivo离线推送
            DemoLog.i(TAG, "vivo support push: " + PushClient.getInstance(getApplicationContext()).isSupport());
            PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int state) {
                    if (state == 0) {
                        String regId = PushClient.getInstance(getApplicationContext()).getRegId();
                        DemoLog.i(TAG, "vivopush open vivo push success regId = " + regId);
                        ThirdPushTokenMgr.getInstance().setThirdPushToken(regId);
                        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                    } else {
                        // 根据vivo推送文档说明，state = 101 表示该vivo机型或者版本不支持vivo推送，链接：https://dev.vivo.com.cn/documentCenter/doc/156
                        DemoLog.i(TAG, "vivopush open vivo push fail state = " + state);
                    }
                }
            });
        } else if (HeytapPushManager.isSupportPush()) {
            // oppo离线推送
            OPPOPushImpl oppo = new OPPOPushImpl();
            oppo.createNotificationChannel(this);
            HeytapPushManager.register(this, PrivateConstants.OPPO_PUSH_APPKEY, PrivateConstants.OPPO_PUSH_APPSECRET, oppo);
        } else if (BrandUtil.isGoogleServiceSupport()) {
            // 谷歌推送
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DemoLog.i(TAG, "onNewIntent");
        mCallModel = (CallModel) intent.getSerializableExtra(Constants.CHAT_INFO);
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onStop() {
        ConversationManagerKit.getInstance().destroyConversation();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果是全屏就关闭全屏
        if (GSYVideoManager.backFromWindowFull(this)) {
            return false;
        }
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                ActivityUtils.finishAllActivities();
            } else {
                Toast.makeText(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        if (BaseConstant.TYPE_CHECK_HELP_CENTER.equals(messageEvent.getCode())) {
            setTab(3, 0);
        } else if (BaseConstant.TYPE_SELECT_MESSAGE_TODO.equals(messageEvent.getCode())) {
            setTab(1, 1);
        } else if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            //registerAlias();
            AliasUtil.syncAliases();
            ConversationManagerKit.getInstance().addUnreadWatcher(this);
        } else if (BaseConstant.TYPE_MAIN.equals(messageEvent.getCode())) {
            ActivityUtils.finishToActivity(MainActivity.class, false);
            setTab(0, 0);
        } else if (BaseConstant.TYPE_UPDATE_APP.equals(messageEvent.getCode())) {
            //模拟数据测试应用更新
            mvpPresenter.getVersionInfo();
        } else if (BaseConstant.TYPE_MESSAGE_TODO_NUM.equals(messageEvent.getCode())) {
            todoCount = messageEvent.getCount();
            setMessageCount(todoCount + messageCount + noticeCount);
        } else if (BaseConstant.TYPE_NOTICE_NUM.equals(messageEvent.getCode())) {
            noticeCount = messageEvent.getCount();
            setMessageCount(todoCount + messageCount + noticeCount);
        }
    }

    /**
     * @Author: Berlin
     * @Date: 2018/12/19 14:37
     * @Description:动态授权
     */
    String[] mPermissionList;

    private void permission() {//https://blog.csdn.net/android_code/article/details/82027500
        if (Build.VERSION.SDK_INT >= 23) {
            mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.WRITE_APN_SETTINGS,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.RECORD_AUDIO//音频
            };
            requestPermissions(mPermissionList, 1);
            //ActivityCompat.requestPermissions(getActivity(), mPermissionList, 123);
        }
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    //这是来自 JPush Example 的设置别名的 Activity 里的代码，更详细的示例请参考 JPush Example。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void registerAlias() {
        //获取当前设备注册的别名 去重
        CloudPushService mPushService = PushServiceFactory.getCloudPushService();
        delOtherAliases(mPushService);
        String alias = SPUtils.getInstance().getString(BaseConstant.JG_ALIAS_NAME);
        //int sequence = SPUtils.getInstance().getInt(BaseConstant.JG_SEQUENCE, 1);
        GetUserSchoolRsp.DataBean identityInfo = SpData.getIdentityInfo();
        if (identityInfo != null) {
            if (!String.valueOf(SpData.getIdentityInfo().userId).equals(alias)) {
                // 极光推送释放代码
                //JPushInterface.setAlias(this, ++sequence, SpData.getIdentityInfo().userId);
                if (TextUtils.isEmpty(alias)) {
                    SPUtils.getInstance().put(BaseConstant.JG_ALIAS_NAME, String.valueOf(SpData.getIdentityInfo().userId));
                    alias = String.valueOf(SpData.getIdentityInfo().userId);
                }
                //阿里云消息推送

                String finalAlias = alias;
                mPushService.addAlias(alias, new CommonCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e(TAG, "onSuccess: add alias " + finalAlias + " success. " + s);
                    }

                    @Override
                    public void onFailed(String errorCode, String errorMsg) {
                        Log.e(TAG, "onFailed :add alias " + finalAlias + " failed." +
                                "errorCode: " + errorCode + ", errorMsg:" + errorMsg + "\n");
                    }
                });
            }
        }
    }

    private void delOtherAliases(CloudPushService mPushService) {
        mPushService.listAliases(new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e(TAG, "aliases:" + response + " \n");
                if (!TextUtils.isEmpty(response)) {
                    final String[] aliases = response.split(",");
                    for (String alias : aliases) {
                        if (!String.valueOf(SpData.getIdentityInfo().userId).equals(alias)) {
                            mPushService.removeAlias(alias, new CommonCallback() {
                                @Override
                                public void onSuccess(String s) {
                                    Log.e(TAG, "remove alias " + alias + " success\n");
                                }

                                @Override
                                public void onFailed(String errorCode, String errorMsg) {
                                    Log.e(TAG, "remove alias " + alias + " failed." +
                                            "errorCode: " + errorCode + ", errorMsg:" + errorMsg + "\n");
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailed(String errorCode, String errorMsg) {
                Log.e(TAG, "list aliases failed. errorCode:" + errorCode + " errorMsg:" + errorMsg);
            }
        });
    }

    @Override
    public void updateUnread(int count) {
        messageCount = count;
        setMessageCount(todoCount + messageCount + noticeCount);
        // 华为离线推送角标
        HUAWEIHmsMessageService.updateBadge(this, count);
    }

    private int messageCount = 0;//消息数量
    private int noticeCount = 0;//消息通知数量
    private int todoCount = 0;//代办数量

    private void setMessageCount(int count) {
        Log.e("Chatim", "updateUnread==>: " + count);
        if (count > 0) {
            msgTotalUnread.setVisibility(View.VISIBLE);
        } else {
            msgTotalUnread.setVisibility(View.GONE);
        }
        String unreadStr = "" + count;
        if (count >= 100) {
            unreadStr = "99+";
        }
        msgTotalUnread.setText(unreadStr);
    }

    @Override
    public void showError() {

    }

    @Override
    public void getData(GetAppVersionResponse rsp) {
        Log.e("TAG", "getData==》: " + JSON.toJSONString(rsp));
        if (rsp.getCode() == BaseConstant.REQUEST_SUCCES2) {
            download(rsp.getData());
        }
    }

    @Override
    public void UserLogoutData(UserLogoutRsp rsp) {
        Log.e("TAG", "UserLogoutData==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void fail(String msg) {
        Log.e("TAG", "fail==》: " + JSON.toJSONString(msg));
    }

    @Override
    public void selectSchByTeaid(SelectSchByTeaidRsp rsp) {
        Log.e("TAG", "selectSchByTeaid==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void updateVersion(SelectUserRsp rsp) {

    }

    @Override
    public void listAllScheduleByTeacherId(ListAllScheduleByTeacherIdRsp rsp) {
        Log.e("TAG", "listAllScheduleByTeacherId==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void addUserEquipmentInfo(ResultBean rsp) {
        Log.e("TAG", "addUserEquipmentInfo==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void jumpFragment(int index) {
        setTab(index, 1);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : ").append(messge).append("\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : ").append(extras).append("\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setCostomMsg(String msg) {
        Log.e("TAG", "setCostomMsg: " + msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        isForeground = true;
        super.onResume();
        if (mCallModel != null) {
            TRTCAVCallImpl impl = (TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(BaseApplication.getInstance());
            impl.stopCall();
            final V2TIMSignalingInfo info = new V2TIMSignalingInfo();
            info.setInviteID(mCallModel.callId);
            info.setInviteeList(mCallModel.invitedList);
            info.setGroupID(mCallModel.groupId);
            info.setInviter(mCallModel.sender);
            info.setData(mCallModel.data);
            ((TRTCAVCallImpl) (TRTCAVCallImpl.sharedInstance(BaseApplication.getInstance()))).processInvite(
                    info.getInviteID(), info.getInviter(), info.getGroupID(), info.getInviteeList(), info.getData());
            mCallModel = null;
        }
        GSYVideoManager.onResume();

    }

    /**
     * 打开应用通知设置
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationPermission() {
        final boolean enabled = NotifyUtil.checkNotificationsEnabled(this);
        final boolean channelEnabled = NotifyUtil.checkNotificationsChannelEnabled(this, "1");
        Log.e(TAG, "showNotificationPermission: enabled " + enabled + ",channelEnabled " + channelEnabled);
        if (!enabled || !channelEnabled) {
            DialogUtil.notificationHintDialog(this, new DialogUtil.OnClickListener() {
                @Override
                public void onCancel(View view) {

                }

                @Override
                public void onEnsure(View view) {
                    NotifyUtil.openNotificationSettings(MainActivity.this);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        GSYVideoManager.onPause();
    }

    void setTab(int position, int type) {
        tab1.setChecked(false);
        tab2.setChecked(false);
        tab3.setChecked(false);
        tab4.setChecked(false);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fg1 = fm.findFragmentByTag(String.valueOf(tab1.getId()));
        Fragment fg2 = fm.findFragmentByTag(String.valueOf(tab2.getId()));
        Fragment fg3 = fm.findFragmentByTag(String.valueOf(tab3.getId()));
        Fragment fg4 = fm.findFragmentByTag(String.valueOf(tab4.getId()));
        if (fg1 != null) ft.hide(fg1);
        if (fg2 != null) ft.hide(fg2);
        if (fg3 != null) ft.hide(fg3);
        if (fg4 != null) ft.hide(fg4);
        switch (position) {
            case 0:
                if (fg1 == null) {
                    //身份切换
                    fg1 = new HomeFragment();
                    ft.add(R.id.content, fg1, String.valueOf(tab1.getId()));
                } else
                    ft.show(fg1);
                tab1.setChecked(true);
                break;
            case 1:

                if (fg2 == null) {
                    fg2 = new MessageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    fg2.setArguments(bundle);
                    ft.add(R.id.content, fg2, String.valueOf(tab2.getId()));
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    fg2.setArguments(bundle);
                    ft.show(fg2);
                }
                tab2.setChecked(true);
                break;
            case 2:
                if (fg3 == null) {
                    fg3 = new AppFragment();
                    ft.add(R.id.content, fg3, String.valueOf(tab3.getId()));
                } else
                    ft.show(fg3);
                tab3.setChecked(true);
                break;
            case 3:
                if (fg4 == null) {
                    fg4 = new HelpFragment();
                    ft.add(R.id.content, fg4, String.valueOf(tab4.getId()));
                } else
                    ft.show(fg4);
                tab4.setChecked(true);
                break;

        }
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @OnClick({R.id.tab1_layout, R.id.tab2_layout, R.id.tab3_layout, R.id.tab4_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1_layout:
                setTab(0, 0);
                break;
            case R.id.tab2_layout:
                //处理失败时点击切换重新登录IM
                if (!UserInfo.getInstance().isAutoLogin()) {
                    getUserSig();
                }
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, "", 1));
                setTab(1, 0);
                break;
            case R.id.tab3_layout:
                setTab(2, 0);
                break;
            case R.id.tab4_layout:
                setTab(3, 0);
                break;
        }
    }

    OkHttpClient mOkHttpClient = new OkHttpClient();

    private void getUserSig() {
        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
        //请求组合创建
        Request request = new Request.Builder()
                .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/im/getUserSig")
                .addHeader("Authorization", SpData.User().getToken())
                .post(body)
                .build();
        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideLoading();
                Log.e(TAG, "getUserSigonFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e(TAG, "getUserSig==>: " + data);
                UserSigRsp bean = JSON.parseObject(data, UserSigRsp.class);
                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
                    SPUtils.getInstance().put(SpData.USERSIG, bean.data);
                    initIm(bean.data);
                } else {
                    hideLoading();
                    ToastUtils.showShort(bean.msg);
                }
            }
        });
    }

    private void initIm(String userSig) {
        TUIKit.login(SpData.getIdentityInfo().userId + "", userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(() -> {
                    //ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                    SPUtils.getInstance().put(BaseConstant.LOGINNAME, SPUtils.getInstance().getString(BaseConstant.LOGINNAME));
                    SPUtils.getInstance().put(BaseConstant.PASSWORD, SPUtils.getInstance().getString(BaseConstant.PASSWORD));
                    UserInfo.getInstance().setAutoLogin(false);
                    UserInfo.getInstance().setUserSig(userSig);
                    UserInfo.getInstance().setUserId(String.valueOf(SpData.getIdentityInfo().userId));
                    Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功");
                });
                DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {
                //腾讯IM离线调度
                OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(getIntent());
                if (bean != null) {
                    OfflineMessageDispatcher.redirect(bean);
                }
                SPUtils.getInstance().put(BaseConstant.LOGINNAME, SPUtils.getInstance().getString(BaseConstant.LOGINNAME));
                SPUtils.getInstance().put(BaseConstant.PASSWORD, SPUtils.getInstance().getString(BaseConstant.PASSWORD));
                UserInfo.getInstance().setAutoLogin(true);
                UserInfo.getInstance().setUserSig(userSig);
                UserInfo.getInstance().setUserId(String.valueOf(SpData.getIdentityInfo().userId));
                Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功");
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_IM_LOGIN, ""));
            }
        });
    }

    private void download(GetAppVersionResponse.DataBean data) {
        LogUtil.d("download", "device:" + AppUtils.getAppVersionCode() + "  data:" + data.versionCode);
//        if (AppUtils.getAppVersionName() != data.versionCode) {
        if (AppUtils.getAppVersionCode() < data.versionCode) {
            NiceDialog.init().setLayoutId(R.layout.dialog_update).setConvertListener(new ViewConvertListener() {
                @Override
                protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                    TextView tv = holder.getView(R.id.tv);
                    TextView tvVersionName = holder.getView(R.id.tv_versionName);
                    tvVersionName.setText(getString(R.string.update_versionName, data.versionName));
                    tv.setText(data.versionDesc);
                    ((TextView) holder.getView(R.id.tv)).setMovementMethod(new ScrollingMovementMethod());
                    TextView tvUpdate = holder.getView(R.id.tvUpdate);
                    FrameLayout flUpgrade = holder.getView(R.id.flUpgrade);
                    downloadOrInstall(tvUpdate, flUpgrade, data);

                }
            }).setDimAmount(0.5f).setOutCancel(data.isCompulsory == 0).show(getSupportFragmentManager());
        } else {
            ToastUtils.showShort(R.string.newestVersion);
        }
    }

    private long max1;

    private void downloadOrInstall(TextView tvUpdate, FrameLayout flUpgrade, GetAppVersionResponse.DataBean data) {
        flUpgrade.setOnClickListener(v -> {
            RxPermissions rxPermissions = new RxPermissions(this);
            mDisposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
                if (granted) {
                    //后台下载APK并更新
                    flUpgrade.setClickable(false);
                    AppClient.downloadFile("yide_" + data.versionCode, data.filePath, new AppClient.DownloadListener() {
                        @Override
                        public void onStart(long max) {
                            max1 = max;
                            ToastUtils.showShort("开始下载");
                        }

                        @Override
                        public void onProgress(long progress) {
                            runOnUiThread(() -> tvUpdate.setText((int) ((float) progress / max1 * 100) + "%"));
                        }

                        @Override
                        public void onSuccess() {
                            runOnUiThread(() -> {
                                flUpgrade.setClickable(true);
                                tvUpdate.setText("点击安装");
                                flUpgrade.setOnClickListener(v1 -> {
                                    File file = new File(Utils.getApp().getCacheDir(), data.versionCode + ".apk");// 设置路径
                                    String[] command = {"chmod", "777", file.getPath()};
                                    ProcessBuilder builder = new ProcessBuilder(command);
                                    try {
                                        builder.start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Intent intent = installIntent(file.getPath());
                                    if (intent != null) {
                                        startActivity(intent);
                                    }
                                });
                                flUpgrade.performClick();
                            });
                        }

                        @Override
                        public void onFailure() {
                            runOnUiThread(() -> {
                                File file = new File(Utils.getApp().getCacheDir(), data.versionCode + ".apk");
                                if (file.exists()) {
                                    file.delete();
                                }
                                downloadOrInstall(tvUpdate, flUpgrade, data);
                                flUpgrade.setClickable(true);
                                tvUpdate.setText("点击重试");
                            });
                            ToastUtils.showShort("更新失败");
                        }
                    });
                } else {
                    // 权限被拒绝
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage(R.string.permission_file)
                            .setPositiveButton("开启", (dialog, which) -> {
                                Intent localIntent = new Intent();
                                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(localIntent);
                            })
                            .setNegativeButton("取消", null)
                            .create().show();
                }
            });

        });
    }

    private Intent installIntent(String path) {
        try {
            File file = new File(path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".FileProvider", file),
                        "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
