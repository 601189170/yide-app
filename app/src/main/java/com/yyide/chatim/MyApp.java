package com.yyide.chatim;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.Utils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.rtmp.TXLiveBase;

import com.yyide.chatim.chat.helper.ConfigHelper;
import com.yyide.chatim.myrequest.AuthcodeTwo;
import com.yyide.chatim.myrequest.BaseBeanReq;
import com.yyide.chatim.myrequest.FastJsonRequest;
import com.yyide.chatim.myrequest.GetData;
import com.yyide.chatim.myrequest.MyHashMap;
import com.yyide.chatim.myrequest.OkHttpStack;
import com.yyide.chatim.chat.signature.GenerateTestUserSig;
import com.yyide.chatim.utils.PrivateConstants;

import androidx.multidex.MultiDex;
import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

import static com.yyide.chatim.myrequest.Object2Map.object2Map;

/**
 * Created by Administrator on 2020/12/14.
 */

public class MyApp extends Application {
    private static MyApp instance;
    private static final String TAG = MyApp.class.getSimpleName();

    private final String licenceUrl = "";
    private final String licenseKey = "";

    public RequestQueue queue;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        queue = Volley.newRequestQueue(this, new OkHttpStack());
        //blankj初始化
        Utils.init(this);
        MultiDex.install(this);



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
    public static MyApp getInstance() {
        return instance;
    }

    public <T> void requestData(Object tag, BaseBeanReq<T> object, Response.Listener<T> listener,
                                Response.ErrorListener errorListener) {
        MyHashMap map = new MyHashMap();
        map.putAll(object2Map(object));
        String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), GetData.URL_KEY);
        FastJsonRequest<T> request = new FastJsonRequest<>(GetData.requestUrl(object),
                object.myTypeReference(), listener, errorListener,
                encryStr);
        request.setShouldCache(true);
        request.setRetryPolicy(new DefaultRetryPolicy(
                15000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        queue.add(request);
    }
}
