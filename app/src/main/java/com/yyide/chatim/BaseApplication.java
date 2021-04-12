package com.yyide.chatim;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.rtmp.TXLiveBase;

import com.yyide.chatim.chat.helper.ConfigHelper;

import com.yyide.chatim.chat.signature.GenerateTestUserSig;
import com.yyide.chatim.utils.PrivateConstants;


import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;



/**
 * Created by Administrator on 2020/12/14.
 */

public class BaseApplication extends Application {
    private static BaseApplication instance;
    private static final String TAG = BaseApplication.class.getSimpleName();

    private final String licenceUrl = "";
    private final String licenseKey = "";


    OkHttpClient mOkHttpClient = new OkHttpClient();
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //blankj初始化
        Utils.init(this);
//        MultiDex.install(this);

        MMKV.initialize(this); //初始化mmkv

        /**
         * TUIKit的初始化函数
         *
         * @param context  应用的上下文，一般为对应应用的ApplicationContext
         * @param sdkAppID 您在腾讯云注册应用时分配的sdkAppID
         * @param configs  TUIKit的相关配置项，一般使用默认即可，需特殊配置参考API文档
         */

        // bugly上报
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion(V2TIMManager.getInstance().getVersion());
        CrashReport.initCrashReport(getApplicationContext(), PrivateConstants.BUGLY_APPID, true, strategy);
        TXLiveBase.getInstance().setLicence(instance, licenceUrl, licenseKey);
        /**
         * TUIKit的初始化函数
         *
         * @param context  应用的上下文，一般为对应应用的ApplicationContext
         * @param sdkAppID 您在腾讯云注册应用时分配的sdkAppID
         * @param configs  TUIKit的相关配置项，一般使用默认即可，需特殊配置参考API文档
         */
        TUIKit.init(this, GenerateTestUserSig.SDKAPPID, new ConfigHelper().getConfigs());


        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);


    }

    public static boolean isNetworkAvailable(Context context) {
        if(context !=null){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info !=null){
                return info.isAvailable();
            }
        }
        return false;
    }
    public static BaseApplication getInstance() {
        return instance;
    }


}
