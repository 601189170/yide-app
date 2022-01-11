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
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSON;
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
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.UnreadCountTextView;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.yyide.chatim.alipush.AliasUtil;
import com.yyide.chatim.alipush.MyMessageReceiver;
import com.yyide.chatim.alipush.NotifyUtil;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.base.MMKVConstant;
import com.yyide.chatim.chat.helper.IBaseLiveListener;
import com.yyide.chatim.chat.helper.TUIKitLiveListenerManager;
import com.yyide.chatim.fragment.schedule.ScheduleFragment;
import com.yyide.chatim.home.AppFragment;
import com.yyide.chatim.home.HelpFragment;
import com.yyide.chatim.home.HomeFragment;
import com.yyide.chatim.home.MessageFragment;
import com.yyide.chatim.jiguang.ExampleUtil;
import com.yyide.chatim.jiguang.LocalBroadcastManager;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.model.GetAppVersionResponse;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.UserInfo;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.UserSigRsp;
import com.yyide.chatim.model.WeeklyDateBean;
import com.yyide.chatim.model.WeeklyDescBean;
import com.yyide.chatim.net.AppClient;
import com.yyide.chatim.presenter.MainPresenter;
import com.yyide.chatim.thirdpush.HUAWEIHmsMessageService;
import com.yyide.chatim.thirdpush.OPPOPushImpl;
import com.yyide.chatim.thirdpush.ThirdPushTokenMgr;
import com.yyide.chatim.utils.BrandUtil;
import com.yyide.chatim.utils.Constants;
import com.yyide.chatim.utils.DemoLog;
import com.yyide.chatim.utils.LogUtil;
import com.yyide.chatim.utils.PrivateConstants;
import com.yyide.chatim.view.DialogUtil;
import com.yyide.chatim.view.MainView;
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel;
import com.yyide.chatim.widget.LoadingButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.tab3)
    CheckedTextView tab3;
    @BindView(R.id.tab4)
    CheckedTextView tab4;
    @BindView(R.id.tab5)
    CheckedTextView tab5;
    @BindView(R.id.msg_total_unread)
    UnreadCountTextView msgTotalUnread;
    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "cn.jiguang.demo.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public String TAG = "MainActivity";
    private boolean isShow = false;
    private long firstTime = 0;
    private CallModel mCallModel;
    private ScheduleMangeViewModel scheduleMangeViewModel;
    private DateTime curDateTime;

    @Override
    public int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        registerMessageReceiver();  // used for receive msg
//        permission();
        // 未读消息监视器
        //登录IM
        getUserSig();
        setTab(0, 0);
        //注册极光别名
//        registerAlias();
        AliasUtil.syncAliases();
        //登录IM
        //处理失败时点击切换重新登录IM
        prepareThirdPushToken();
        //ConversationManagerKit.getInstance().addUnreadWatcher(this);
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
        new Handler().postDelayed(() -> mvpPresenter.getVersionInfo(), 2000);
        //检查是否开启消息通知
        showNotificationPermission();
        mvpPresenter.copywriter();
        mvpPresenter.getWeeklyDate();

        //请求日程数据 并保存本地
        scheduleMangeViewModel = new ViewModelProvider(this).get(ScheduleMangeViewModel.class);
        scheduleMangeViewModel.getAllScheduleList();
        scheduleMangeViewModel.getCurDateTime().observe(this, dateTime -> {
            Log.e(TAG, "时间改变: " + dateTime.toString());
            if (curDateTime != null) {
                scheduleMangeViewModel.getCurDateTime().setValue(dateTime);
                curDateTime = dateTime;
            }
        });
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
                        if (!TextUtils.isEmpty(token)) {
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
                setTab(0, 0);
                Toast.makeText(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventMessage messageEvent) {
        Log.e(TAG, "Event: " + messageEvent.getCode());
        if (BaseConstant.TYPE_CHECK_HELP_CENTER.equals(messageEvent.getCode())) {
            setTab(3, 0);
        } else if (BaseConstant.TYPE_SELECT_MESSAGE_TODO.equals(messageEvent.getCode())) {
            ActivityUtils.finishToActivity(MainActivity.class, false);
            setTab(1, 1);
        } else if (BaseConstant.TYPE_UPDATE_HOME.equals(messageEvent.getCode())) {
            //registerAlias();
            AliasUtil.syncAliases();
        } else if (BaseConstant.TYPE_REGISTER_UNREAD.equals(messageEvent.getCode())) {
            //ConversationManagerKit.getInstance().addUnreadWatcher(this);
        } else if (BaseConstant.TYPE_MAIN.equals(messageEvent.getCode())) {
            ActivityUtils.finishToActivity(MainActivity.class, false);
            setTab(0, 0);
        } else if (BaseConstant.TYPE_MESSAGE.equals(messageEvent.getCode())) {
            setTab(1, 0);
        } else if (BaseConstant.TYPE_SCHEDULE.equals(messageEvent.getCode())) {
            setTab(4, 0);
        } else if (BaseConstant.TYPE_UPDATE_APP.equals(messageEvent.getCode())) {
            //模拟数据测试应用更新
            isShow = true;
            mvpPresenter.getVersionInfo();
        } else if (BaseConstant.TYPE_MESSAGE_TODO_NUM.equals(messageEvent.getCode())) {
            todoCount = messageEvent.getCount();
            setMessageCount(todoCount + messageCount + noticeCount);
        } else if (BaseConstant.TYPE_NOTICE_NUM.equals(messageEvent.getCode())) {
            noticeCount = messageEvent.getCount();
            setMessageCount(todoCount + messageCount + noticeCount);
        } else if (BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA.equals(messageEvent.getCode())) {
            //更新日程数据并存本地
            if (scheduleMangeViewModel != null) {
                scheduleMangeViewModel.getAllScheduleList();
            }
        } else if (BaseConstant.TYPE_HOME_CHECK_SCHEDULE.equals(messageEvent.getCode())) {
            setTab(4, 0);
        }
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void updateUnread(int count) {
        Log.e("Chatim", "updateUnread==>: " + count);
        messageCount = count;
        setMessageCount(todoCount + messageCount + noticeCount);
        // 华为离线推送角标
        HUAWEIHmsMessageService.updateBadge(this, count);
    }

    private void handleOfflinePush() {
        boolean isFromOfflinePush = getIntent().getBooleanExtra(Constants.IS_OFFLINE_PUSH_JUMP, false);
        if (isFromOfflinePush) {
            IBaseLiveListener baseLiveListener = TUIKitLiveListenerManager.getInstance().getBaseCallListener();
            if (baseLiveListener != null) {
                baseLiveListener.handleOfflinePushCall(getIntent());
            }
        }
    }

    private int messageCount = 0;//消息数量
    private int noticeCount = 0;//消息通知数量
    private int todoCount = 0;//代办数量

    private void setMessageCount(int count) {
        //Log.e("Chatim", "setMessageCount==>: " + count);
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
    public void getCopywriter(WeeklyDescBean model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
            if (model.getData() != null && model.getData().size() > 0) {
                List<String> data = model.getData();
                Collections.addAll(data);//填充
                Set<String> set = new HashSet<>(data);
                MMKV.defaultMMKV().encode(MMKVConstant.YD_WEEKLY_DESC, set);
            }
        }
    }

    @Override
    public void getWeeklyDate(WeeklyDateBean model) {
        if (model.getCode() == BaseConstant.REQUEST_SUCCES2) {
            if (model.getData() != null) {
                MMKV.defaultMMKV().encode(MMKVConstant.YD_WEEKLY_DATE, JSON.toJSONString(model.getData()));
            }
        }
    }

    @Override
    public void getVersionInfo(GetAppVersionResponse rsp) {
        Log.e("TAG", "getData==》: " + JSON.toJSONString(rsp));
        if (rsp.getCode() == BaseConstant.REQUEST_SUCCES2) {
            if (rsp.getData() != null) {
                download(rsp.getData());
            } else if (isShow) {
                ToastUtils.showShort(R.string.newestVersion);
            }
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
        Log.e(TAG, "jumpFragment: "+index );
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
        Log.e("TAG", "setCustomMsg: " + msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        isForeground = true;
        super.onResume();
        GSYVideoManager.onResume();
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
        handleOfflinePush();
    }

    /**
     * 打开应用通知设置
     */
    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationPermission() {
        final boolean enabled = NotifyUtil.checkNotificationsEnabled(this);
        boolean channelEnabled = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelEnabled = NotifyUtil.checkNotificationsChannelEnabled(this, "1");
        }
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
        Log.e(TAG, "setTab: "+position+", "+type );
        tab1.setChecked(false);
        tab2.setChecked(false);
        tab3.setChecked(false);
        tab4.setChecked(false);
        tab5.setChecked(false);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        //getSupportFragmentManager().popBackStack();

        Fragment fg1 = fm.findFragmentByTag(String.valueOf(tab1.getId()));
        Fragment fg2 = fm.findFragmentByTag(String.valueOf(tab2.getId()));
        Fragment fg3 = fm.findFragmentByTag(String.valueOf(tab3.getId()));
        Fragment fg4 = fm.findFragmentByTag(String.valueOf(tab4.getId()));
        Fragment fg5 = fm.findFragmentByTag(String.valueOf(tab5.getId()));

        if (fg1 != null) ft.hide(fg1);
        if (fg2 != null) ft.hide(fg2);
        if (fg3 != null) ft.hide(fg3);
        if (fg4 != null) ft.hide(fg4);
        if (fg5 != null) ft.hide(fg5);

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
                MMKV.defaultMMKV().encode(MMKVConstant.YD_MAIN_JUMP_TYPE, type);
                if (fg2 == null) {
                    fg2 = new MessageFragment();
                    ft.add(R.id.content, fg2, String.valueOf(tab2.getId()));
                } else
                    ft.show(fg2);
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
            case 4:
                if (fg5 == null) {
                    fg5 = new ScheduleFragment();
                    ft.add(R.id.content, fg5, String.valueOf(tab5.getId()));
                } else {
                    ft.show(fg5);
                }
                tab5.setChecked(true);
                break;
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (fg1 != null) {
//            fg1.onActivityResult(requestCode, resultCode, data);
//        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @OnClick({R.id.tab1_layout, R.id.tab2_layout, R.id.tab3_layout, R.id.tab4_layout, R.id.tab5_layout})
    public void onViewClicked(View view) {
        Log.e(TAG, "onViewClicked: "+view.getId());
        switch (view.getId()) {
            case R.id.tab1_layout:
                setTab(0, 0);
                break;
            case R.id.tab2_layout:
                //处理失败时点击切换重新登录IM
                if (!UserInfo.getInstance().isAutoLogin()) {
                    getUserSig();
                }
                //EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, "", 1));
                setTab(1, 0);
                break;
            case R.id.tab3_layout:
                setTab(2, 0);
                break;
            case R.id.tab4_layout:
                setTab(3, 0);
                break;
            case R.id.tab5_layout:
                setTab(4, 0);
                break;
            default:
                break;
        }
    }

    private OkHttpClient mOkHttpClient = new OkHttpClient();

    private void getUserSig() {
        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
        //请求组合创建
        Request request = new Request.Builder()
                .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/im/getUserSig")
                .addHeader("Authorization", SpData.User().data.accessToken)
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
        TUIKit.login(SpData.getUserId() + "", userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(() -> {
                    //ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                    SPUtils.getInstance().put(BaseConstant.LOGINNAME, SPUtils.getInstance().getString(BaseConstant.LOGINNAME));
                    SPUtils.getInstance().put(BaseConstant.PASSWORD, SPUtils.getInstance().getString(BaseConstant.PASSWORD));
                    UserInfo.getInstance().setAutoLogin(false);
                    UserInfo.getInstance().setUserSig(userSig);
                    UserInfo.getInstance().setUserId(String.valueOf(SpData.getUserId()));
                    Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活失败code：" + code);
                });
                DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {
                //腾讯IM离线调度
//                OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(getIntent());
//                 if (bean != null) {
//                    OfflineMessageDispatcher.redirect(bean);
//                }
                prepareThirdPushToken();
                SPUtils.getInstance().put(BaseConstant.LOGINNAME, SPUtils.getInstance().getString(BaseConstant.LOGINNAME));
                SPUtils.getInstance().put(BaseConstant.PASSWORD, SPUtils.getInstance().getString(BaseConstant.PASSWORD));
                UserInfo.getInstance().setAutoLogin(true);
                UserInfo.getInstance().setUserSig(userSig);
                UserInfo.getInstance().setUserId(String.valueOf(SpData.getUserId()));
                Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功");
                EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_IM_LOGIN, ""));
            }
        });
    }

    private void download(GetAppVersionResponse.DataBean data) {
        LogUtil.d("download", "device:" + AppUtils.getAppVersionCode() + "  data:" + data.versionCode);
//        if (AppUtils.getAppVersionName() != data.versionCode) {
        if (AppUtils.getAppVersionName().compareTo(data.versionCode) < 0) {
            NiceDialog.init().setLayoutId(R.layout.dialog_update).setConvertListener(new ViewConvertListener() {
                @Override
                protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                    TextView cancel = holder.getView(R.id.tv_cancel);
                    cancel.setOnClickListener(v -> dialog.dismiss());
//                    TextView tv = holder.getView(R.id.tv);
//                    tv.setText(data.versionDesc);
//                    ((TextView) holder.getView(R.id.tv)).setMovementMethod(new ScrollingMovementMethod());
                    LoadingButton btnUpdate = holder.getView(R.id.btn_update);
                    downloadOrInstall(btnUpdate, cancel, data);
                    cancel.setVisibility(data.isCompulsory == 0 ? View.VISIBLE : View.GONE);
                }
            }).setDimAmount(0.5f).setOutCancel(data.isCompulsory == 0).show(getSupportFragmentManager());
        } else if (isShow) {
            ToastUtils.showShort(R.string.newestVersion);
        }
    }

    private long max1;

    private void downloadOrInstall(LoadingButton btnUpdate, TextView cancel, GetAppVersionResponse.DataBean data) {
        btnUpdate.setOnClickListener(v -> {
            RxPermissions rxPermissions = new RxPermissions(this);
            mDisposable = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
                if (granted) {
                    //后台下载APK并更新
                    cancel.setVisibility(View.GONE);
                    btnUpdate.startLoading("");
                    btnUpdate.setClickable(false);
                    AppClient.downloadFile("yide_app", data.filePath, new AppClient.DownloadListener() {
                        @Override
                        public void onStart(long max) {
                            max1 = max;
                            //ToastUtils.showShort("开始下载");
                            runOnUiThread(() -> btnUpdate.stopLoading("start"));
                        }

                        @Override
                        public void onProgress(long progress) {
                            runOnUiThread(() -> btnUpdate.setText("已下载 " + (int) ((float) progress / max1 * 100) + "%"));
                        }

                        @Override
                        public void onSuccess() {
                            runOnUiThread(() -> {
                                btnUpdate.stopLoading("点击安装");
                                btnUpdate.setClickable(true);
                                btnUpdate.setOnClickListener(v1 -> {
                                    File file = new File(Utils.getApp().getCacheDir(), "yide_app.apk");// 设置路径
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
                                btnUpdate.performClick();
                            });
                        }

                        @Override
                        public void onFailure() {
                            runOnUiThread(() -> {
                                File file = new File(Utils.getApp().getCacheDir(), "yide_app.apk");
                                if (file.exists()) {
                                    file.delete();
                                }
                                downloadOrInstall(btnUpdate, cancel, data);
                                btnUpdate.setClickable(true);
                                btnUpdate.stopLoading("点击重试");
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
