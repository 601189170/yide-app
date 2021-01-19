package com.yyide.chatim;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.example.zhouwei.library.CustomPopWindow;
import com.yyide.chatim.base.BaseActivity;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.home.adress.AdressFragment;
import com.yyide.chatim.home.home.HomeFragment;
import com.yyide.chatim.home.user.UserFragment;
import com.yyide.chatim.jiguang.ExampleUtil;
import com.yyide.chatim.jiguang.LocalBroadcastManager;
import com.yyide.chatim.jiguang.NoticeActivity;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.myrequest.requestbean.DeviceUpdateReq;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.work)
    CheckedTextView work;
    @BindView(R.id.work_layout)
    FrameLayout workLayout;
    @BindView(R.id.adress)
    CheckedTextView adress;
    @BindView(R.id.adress_layout)
    FrameLayout adressLayout;
    @BindView(R.id.user)
    CheckedTextView user;
    @BindView(R.id.user_layout)
    FrameLayout userLayout;


    //for receive customer msg from jpush server
    public static boolean isForeground = false;
    @BindView(R.id.title)
    TextView title;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "cn.jiguang.demo.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        registerMessageReceiver();  // used for receive msg


//        getSupportFragmentManager().beginTransaction().replace(R.id.content, new QrCodeFragment()).commitAllowingStateLoss();
//        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, fragment).commitAllowingStateLoss();
//        showNotice(this);

//        setPm("1");

//        setTab(0);


        permission();

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopBottom();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            showPopBottom();
            }
        },5000);


    }
    private void showPopBottom(){
        View contentView = LayoutInflater.from(this).inflate(R.layout.msg_layout2, null);
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)

                .setView(R.layout.msg_layout2)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .create();
        popWindow.showAtLocation(contentView, Gravity.TOP,0,0);

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
        work.setChecked(false);
        adress.setChecked(false);
        user.setChecked(false);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fg1 = fm.findFragmentByTag(String.valueOf(work.getId()));
        Fragment fg2 = fm.findFragmentByTag(String.valueOf(adress.getId()));
        Fragment fg3 = fm.findFragmentByTag(String.valueOf(user.getId()));
        if (fg1 != null) ft.hide(fg1);
        if (fg2 != null) ft.hide(fg2);
        if (fg3 != null) ft.hide(fg3);

        switch (position) {
            case 0:

                if (fg1 == null) {
                    fg1 = new HomeFragment();
                    ft.add(R.id.content, fg1, String.valueOf(work.getId()));
                } else
                    ft.show(fg1);
                work.setChecked(true);
                break;
            case 1:
                if (fg2 == null) {
                    fg2 = new AdressFragment();
                    ft.add(R.id.content, fg2, String.valueOf(adress.getId()));
                } else
                    ft.show(fg2);
                adress.setChecked(true);
                break;
            case 2:
                if (fg3 == null) {
                    fg3 = new UserFragment();
                    ft.add(R.id.content, fg3, String.valueOf(user.getId()));
                } else
                    ft.show(fg3);
                user.setChecked(true);
                break;

        }
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();

    }

    private void setPm(String type) {
        DeviceUpdateReq req = new DeviceUpdateReq();
        req.machineCode = DeviceUtils.getAndroidID();
//        req.officeId = SpData.getDiviceName().office_id;
        req.deviceDirection = type;

        MyApp.getInstance().requestData(this, req, new listener22(), new registerErrorListener());
    }

    @OnClick({R.id.work_layout, R.id.adress_layout, R.id.user_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.work_layout:
                setTab(0);

                break;
            case R.id.adress_layout:
                setTab(1);
                break;
            case R.id.user_layout:
                setTab(2);
                break;
        }
    }


    class listener22 implements Response.Listener<DeviceUpdateRsp> {
        @Override
        public void onResponse(DeviceUpdateRsp rsp) {

            if (rsp.status == BaseConstant.REQUEST_SUCCES || rsp.status == BaseConstant.REQUEST_SUCCES2) {

            }
        }
    }

    class registerErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("fortime", "GetDeviceNameRsp+Error:" + JSON.toJSONString(volleyError));
        }
    }

    private void showNotice(Context context) {
//        String title = indexAd.getTitle();
        // 使用remoteViews去加载自定义布局
        RemoteViews remoteViews = new RemoteViews(AppUtils.getAppPackageName(), R.layout.notification_custom);
//        remoteViews.setTextViewText(R.id.tv_title, title);
//        remoteViews.setTextViewText(R.id.tv_price, "￥"+indexAd.getEndPrice()+"元");
//        remoteViews.setTextViewText(R.id.tv_time, TimeUtils.tsToMs(indexAd.getTime()));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.fff, null);//将资源文件转化为bitmap
        remoteViews.setImageViewBitmap(R.id.iv_pic, bitmap);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, NoticeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("渠道ID", "优惠券商品", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, "渠道ID");
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        Notification notification = builder
                .setSmallIcon(R.mipmap.fff)
                .setContentTitle("aaa")
                .setContentText("bbb")
                .setContentIntent(contentIntent)
                .setContent(remoteViews)
                .setAutoCancel(true)
                .build();
        manager.notify(0, notification);
    }
}
