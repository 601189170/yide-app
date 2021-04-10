package com.yyide.chatim;


import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;

import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.UnreadCountTextView;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.chat.info.UserInfo;
import com.yyide.chatim.home.AppFragment;
import com.yyide.chatim.home.HelpFragment;
import com.yyide.chatim.home.HomeFragment;
import com.yyide.chatim.home.HomeFragmentXZ;
import com.yyide.chatim.home.MessageFragment;
import com.yyide.chatim.jiguang.ExampleUtil;
import com.yyide.chatim.jiguang.LocalBroadcastManager;
import com.yyide.chatim.jiguang.TagAliasOperatorHelper;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ScheduleRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.addUserEquipmentInfoRsp;
import com.yyide.chatim.model.EventMessage;
import com.yyide.chatim.presenter.MainPresenter;
import com.yyide.chatim.utils.DemoLog;
import com.yyide.chatim.view.MainView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.OnClick;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements ConversationManagerKit.MessageUnreadWatcher, MainView {

    @BindView(R.id.content)
    FrameLayout content;
    //for receive customer msg from jpush server
    public static boolean isForeground = false;
    @BindView(R.id.tab1)
    CheckedTextView tab1;
    @BindView(R.id.tab1_layout)
    FrameLayout tab1Layout;
    @BindView(R.id.tab2)
    CheckedTextView tab2;
    @BindView(R.id.tab2_layout)
    FrameLayout tab2Layout;
    @BindView(R.id.tab3)
    CheckedTextView tab3;
    @BindView(R.id.tab3_layout)
    FrameLayout tab3Layout;
    @BindView(R.id.tab4)
    CheckedTextView tab4;
    @BindView(R.id.tab4_layout)
    FrameLayout tab4Layout;
    @BindView(R.id.msg_total_unread)
    UnreadCountTextView msgTotalUnread;

    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "cn.jiguang.demo.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public int IdType = 1;
    public String TAG = "MainActivity";

    private Dialog mDialog;
    /**
     * request Code 从相册选择照片不裁切
     **/
    private final static int SELECT_ORIGINAL_PIC = 126;
    /**
     * request Code 拍取照片不裁切
     **/
    private final static int REQ_CODE = 127;
    private Uri imageUri;
    private long firstTime = 0;
    private UserInfo mUserInfo;

    @Override
    public int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerMessageReceiver();  // used for receive msg
        permission();
        // 未读消息监视器
        ConversationManagerKit.getInstance().addUnreadWatcher(this);

        mUserInfo = UserInfo.getInstance();
        EventBus.getDefault().register(this);
        setTab(0);
//        startActivity(new Intent(this, ResetPassWordActivity.class));
//        new Handler().postDelayed(new Runnable() {
//                @Override
//            public void run() {
    //                mvpPresenter.Login("13659896596","896596");
//            }
//        },5000);


//        mvpPresenter.getselectUser();

//        mvpPresenter.ToUserLogout();

//        mvpPresenter.getUserSchool();
//        mvpPresenter.SelectSchByTeaid();


//        mvpPresenter.listAllScheduleByTeacherId();

//        CheacklistSchedule("99","1","10");
        //注册极光用户
        RegistJiGuang();
//        getUserSchool();
    }

    void RegistJiGuang(){
        if(SpData.getIdentityInfo() != null){
            int userId = SpData.getIdentityInfo().userId;
            String rid = JPushInterface.getRegistrationID(getApplicationContext());
            int alias = userId;
            String equipmentType = "1";
            mvpPresenter.addUserEquipmentInfo(userId,rid, String.valueOf(alias),equipmentType);
        }
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码，更详细的示例请参考 JPush Example。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
//    private void handleAction(int sequence, TagAliasOperatorHelper.TagAliasBean tagAliasBean) {
//        if(tagAliasBean == null){
//            Log.w(TAG,"tagAliasBean was null");
//            return;
//        }
//        if(tagAliasBean.isAliasAction){
//            switch (tagAliasBean.action){
//                case ACTION_GET:
//                    JPushInterface.getAlias(getApplicationContext(),sequence);
//                    break;
//                case ACTION_DELETE:
//                    JPushInterface.deleteAlias(getApplicationContext(),sequence);
//                    break;
//                case ACTION_SET:
//                    JPushInterface.setAlias(getApplicationContext(),sequence,tagAliasBean.alias);
//                    break;
//                default:
//                    Log.w(TAG,"unsupport alias action type");
//                    return;
//            }
//        }else {
//            //tag operation
//        }
//    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onStop() {
        DemoLog.i("TAG", "onStop");
        ConversationManagerKit.getInstance().destroyConversation();
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        if (BaseConstant.CheakId.equals(messageEvent.getCode())) {
            IdType = 2;
            ActivityUtils.finishAllActivities();
            startActivity(new Intent(this, MainActivity.class));
            Log.e("TAG", "messageEvent: " + IdType);
            setTab(0);
        } else if(BaseConstant.TYPE_CHECK_HELP_CENTER.equals(messageEvent.getCode())){
            setTab(3);
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
                    Manifest.permission.ACCESS_FINE_LOCATION,
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

    @Override
    public void updateUnread(int count) {
        Log.e("Chatim", "updateUnread==>: " + count);
        if (count > 0) {
            msgTotalUnread.setVisibility(View.VISIBLE);
        } else {
            msgTotalUnread.setVisibility(View.GONE);
        }
        String unreadStr = "" + count;
        if (count > 100) {
            unreadStr = "99+";
        }
        msgTotalUnread.setText(unreadStr);
        // 华为离线推送角标
//        HUAWEIHmsMessageService.updateBadge(this, count);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void getData(SelectUserRsp rsp) {
        Log.e("TAG", "getData==》: " + JSON.toJSONString(rsp));

    }

    @Override
    public void getDataFail(String msg) {
        Log.e("TAG", "getDataFail==》: " + JSON.toJSONString(msg));
    }

    @Override
    public void UserLogoutData(UserLogoutRsp rsp) {
        Log.e("TAG", "UserLogoutData==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void UserLogoutDataFail(String msg) {
        Log.e("TAG", "UserLogoutDataFail==》: " + JSON.toJSONString(msg));
    }

//    @Override
//    public void getUserSchool(GetUserSchoolRsp rsp) {
//        Log.e("TAG", "GetUserSchoolRsp==》: " + JSON.toJSONString(rsp));
//        SPUtils.getInstance().put(SpData.SCHOOLINFO, JSON.toJSONString(rsp));
//        if (rsp.data.size() > 0 && TextUtils.isEmpty(SpData.SchoolId())) {
//            SPUtils.getInstance().put(SpData.SCHOOLID, rsp.data.get(0).schoolId + "");
//
//
////            handleData(rsp.data.get(0).userId,SpData.UserSig());
//        }
//
//
//    }

    private void handleData(int userId,String UserSig) {
        if (mUserInfo != null && mUserInfo.isAutoLogin()) {
//            login();
//            initIm(userId,UserSig);
            Log.e(TAG, "handleData: Im已经激活，无需激活" );
        } else {
            initIm(userId,UserSig);
        }
    }

    @Override
    public void getUserSchoolDataFail(String rsp) {
        Log.e("TAG", "getUserSchoolDataFail==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void selectSchByTeaid(SelectSchByTeaidRsp rsp) {
        Log.e("TAG", "selectSchByTeaid==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void selectSchByTeaidDataFail(String rsp) {
        Log.e("TAG", "selectSchByTeaidDataFail==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void listAllScheduleByTeacherId(ListAllScheduleByTeacherIdRsp rsp) {
        Log.e("TAG", "listAllScheduleByTeacherId==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void listAllScheduleByTeacherIdDataFail(String rsp) {
        Log.e("TAG", "listAllScheduleByTeacherIdDataFail==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void addUserEquipmentInfo(addUserEquipmentInfoRsp rsp) {
        Log.e("TAG", "addUserEquipmentInfo==》: " + JSON.toJSONString(rsp));
    }

    @Override
    public void addUserEquipmentInfoFail(String rsp) {
        Log.e("TAG", "addUserEquipmentInfoFail==》: " + JSON.toJSONString(rsp));
    }

    OkHttpClient mOkHttpClient = new OkHttpClient();

    void CheacklistSchedule(String teacherId, String current, String size) {
        ScheduleRsp rsp = new ScheduleRsp();
        rsp.teacherId = teacherId;
        rsp.current = current;
        rsp.size = size;
        RequestBody requestBody = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(rsp));

        //请求组合创建
        Request request = new Request.Builder()
//                .url(BaseConstant.URL_IP + "/brand/class-brand-management/android/class/selectClassesInfo")
                .url(BaseConstant.URL_IP + "/timetable/cloud-timetable/schedule/listSchedule")
                .addHeader("Authorization", SpData.User().token)
                .post(requestBody)
                .build();

        //发起请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                Log.e("TAG", "mOkHttpClientlistSchedule==>: " + data);
//                SelectUserSchoolRsp bean = JSON.parseObject(data, SelectUserSchoolRsp.class);
//                if (bean.code==BaseConstant.REQUEST_SUCCES2){
//                    Tologin(bean.data.username,bean.data.password, String.valueOf(schoolId));
//                }
            }
        });
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setCostomMsg(String msg) {
        Log.e("TAG", "setCostomMsg: " + msg);
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    void setTab(int position) {

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
                    if (IdType == 1) {
                        fg1 = new HomeFragment();
//                        fg1 = new HomeFragment2();
                    } else {
                        fg1 = new HomeFragmentXZ();
                    }
                    ft.add(R.id.content, fg1, String.valueOf(tab1.getId()));
                } else
                    ft.show(fg1);
                tab1.setChecked(true);
                break;
            case 1:
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

        }
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @OnClick({R.id.tab1_layout, R.id.tab2_layout, R.id.tab3_layout, R.id.tab4_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1_layout:
                setTab(0);
                break;
            case R.id.tab2_layout:
                setTab(1);
                break;
            case R.id.tab3_layout:
                setTab(2);
                break;
            case R.id.tab4_layout:
                setTab(3);
                break;
        }
    }
    void initIm(int userid,String userSig) {
        TUIKit.login(String.valueOf(userid), userSig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                    }
                });
                DemoLog.i(TAG, "imLogin errorCode = " + code + ", errorInfo = " + desc);
            }

            @Override
            public void onSuccess(Object data) {
                UserInfo.getInstance().setAutoLogin(true);
                UserInfo.getInstance().setUserSig(userSig);
                UserInfo.getInstance().setUserId(String.valueOf(userid));

                Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功" );
            }
        });
    }

}
