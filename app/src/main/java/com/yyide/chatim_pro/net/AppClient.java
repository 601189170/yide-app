package com.yyide.chatim_pro.net;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.yyide.chatim_pro.BaseApplication;
import com.yyide.chatim_pro.SpData;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.kotlin.network.interceptor.CommonResponseInterceptor;
import com.yyide.chatim_pro.model.LoginRsp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public class AppClient {
    public static Retrofit mDingRetrofit;
    public static Retrofit mDingRetrofit2;
    private static OkHttpClient okHttpClient;
    private static OkHttpClient okHttpClient2;
    //    public static String token="Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxMzcwMDk5ODg5OSIsInNjb3BlIjpbImFsbCJdLCJyb2xlcyI6WyI1NDYzMjVfdGVhY2hlciIsIjU0NjMyNV90ZWFjaGVyIiwiNTQ2MzI1X3RlYWNoZXIiLCI1NDYzMjVfdGVhY2hlciIsIjU0NjMyNV90ZWFjaGVyIiwiNTQ2MzI1X3RlYWNoZXIiLCI1NDYzMjVfdGVhY2hlciIsIjU0NjMyNV9hY2FkZW1pY3MiLCI1NDYzMjVfYWNhZGVtaWNzIl0sIm5hbWUiOiLlvKDkuInlvKDkuInogIHluIjlvKDkuInlvKDkuInogIHluIjlvKDkuInlvKDkuInogIHluIjlvKDkuIkiLCJpZCI6MTUwMzY0ODY1NzE4NDk0MDAzMywiZXhwIjoxNjQ4MjY4NjM3LCJiaWQiOjE0NTgyNTc2MjQwNDE1NDYzMjUsImF1dGhvcml0aWVzIjpbIjU0NjMyNV9hY2FkZW1pY3MiLCI1NDYzMjVfdGVhY2hlciJdLCJqdGkiOiI4YmM5NDBmYS1hOWFhLTRmNTEtOWZkNi1hNjZhZjkxOTI1MjIiLCJjbGllbnRfaWQiOiJ5aWRlLWNsb3VkIiwidXNlcm5hbWUiOiIxMzcwMDk5ODg5OSJ9.GclbUuGfVwQ8Gflde_K8egNAkP8qgqrPI28aGxvq1dQ5JQgNLQ2eE8RGLlhd0rIXtz-1v7l5qzg_h6Ff9wLBgL9JMAw2jL34M31OU1dZROQbeDJRClm5T9syUqx4WE1BkNqKL1OSavake6lkbz1UfPZjzMo-6mEqnW5VMV4dQPDMuJM73eLgCKQYTowhEW3h8yb4xbvrJ_wbAHII8Aav62ssSv-1N84TsDuC4JLiyC-avc34LbwwueM6RzdoUa5qyq6TnbS7AXiUMoGBeFPtqUhx_CJUQcwe-LWIcHbiiORylNscI9pxin9fAC1XBVlfp4MuumUfDN3l4omUNC0gWg";
    public static String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxMzcxMjM0MDAxMCIsInNjb3BlIjpbImFsbCJdLCJyb2xlcyI6WyI1NDYzMjVfdGVhY2hlciIsIjU0NjMyNV90ZWFjaGVyIiwiNTQ2MzI1X3RlYWNoZXIiLCI1NDYzMjVfdGVhY2hlciIsIjU0NjMyNV90ZWFjaGVyIiwiNTQ2MzI1X2FkbWluIl0sIm5hbWUiOiLpgJrorq_lvZXnmoTlrabnlJ_lrrbplb8xIiwiaWQiOjE1MDQzNDI1NDIxNDQwMDQwOTgsImV4cCI6MTY0ODQzNjQwNywiYmlkIjoxNDU4MjU3NjI0MDQxNTQ2MzI1LCJhdXRob3JpdGllcyI6WyI1NDYzMjVfYWRtaW4iLCI1NDYzMjVfdGVhY2hlciJdLCJqdGkiOiI2NTgzNDE2OS0zMzQ1LTQxN2UtODFmOS1jNDViMDQxYjIzODkiLCJjbGllbnRfaWQiOiJ5aWRlLWNsb3VkIiwidXNlcm5hbWUiOiIxMzcxMjM0MDAxMCJ9.rFdv_8YXcr55JGd3db8XAUZizIRbwqWvhgNyftBO75uCDEjdhbVeP1z7_uDxFhek_Ak-mw0eWApbPaB-naJs0L9q5TYr0SaVDwpA2bi1MZ3nU8VHlnMKz1usxNNXGsrewe0DmBxMpJ27QbAqnpRcnqsrfP0AV1O4_ZQ3mW_judxJr9yMI5BoVHExmg9APREnLdwedbn9l1Mgo7tigDO1rjY6n6xVJqWzf28iOcdgI_T-Xf9SNyj_64YsA3A97cbrpzQpiA8uZ3PCWvEbZpiXisdYKehjRbaYLIM-ZuK4-jaqFYDIdVsIsiosUuDDSwJJJsgP0xFlh4RE_VT-a74M7w";


    public static Retrofit getDingRetrofit() {
        if (mDingRetrofit == null) {
            mDingRetrofit = new Retrofit.Builder()
                    .baseUrl(BaseConstant.API_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return mDingRetrofit;
    }

    /**
     * 临时使用固定token 正式上线替换getDingRetrofit()
     *
     * @return mDingRetrofit
     */
    @Deprecated
    public static Retrofit getDingRetrofit2() {
        if (mDingRetrofit2 == null) {
            mDingRetrofit2 = new Retrofit.Builder()
                    .baseUrl(BaseConstant.API_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(getOkHttpClient2())
                    .build();
        }
        return mDingRetrofit2;
    }

    @Deprecated
    public static OkHttpClient getOkHttpClient2() {
        if (okHttpClient2 == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (AppUtils.isAppDebug()) {
                // Log信息拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);
            }
            //cache url
            File httpCacheDirectory = new File(BaseApplication.getInstance().getExternalCacheDir(), "responses");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            builder.cache(cache);
            builder.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR2);
//            builder.addInterceptor(new TokenHeaderInterceptor());
            builder.connectTimeout(30, TimeUnit.SECONDS). // 设置连接超时时间
                    readTimeout(30, TimeUnit.SECONDS).
                    writeTimeout(30, TimeUnit.SECONDS).build();
//            builder.hostnameVerifier(new AllowAllHostnameVerifier());
            okHttpClient2 = builder.build();
        }
        return okHttpClient2;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (AppUtils.isAppDebug()) {
                // Log信息拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);
            }
            //cache url
            File httpCacheDirectory = new File(BaseApplication.getInstance().getExternalCacheDir(), "responses");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            builder.cache(cache);
            builder.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
            builder.addInterceptor(new CommonResponseInterceptor());
//            builder.addInterceptor(new TokenHeaderInterceptor());
            builder.connectTimeout(30, TimeUnit.SECONDS). // 设置连接超时时间
                    readTimeout(30, TimeUnit.SECONDS).
                    writeTimeout(30, TimeUnit.SECONDS).build();
//            builder.hostnameVerifier(new AllowAllHostnameVerifier());
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    //cache
    static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(0, TimeUnit.SECONDS);
        cacheBuilder.maxStale(365, TimeUnit.DAYS);
        CacheControl cacheControl = cacheBuilder.build();

        Request request = chain.request();
        LoginRsp user = SpData.getLogin();
        if (user != null) {
            Log.e("TAG", "intercept: " + JSON.toJSONString(user.getAccessToken()));
            request = request.newBuilder()
                    .addHeader("Authorization", user.getAccessToken())
                    .cacheControl(cacheControl)
                    .build();
        }
//        else {
//        request = request.newBuilder()
//                .addHeader("Authorization", token)
//                .cacheControl(cacheControl)
//                .build();
//        }

        Response originalResponse = chain.proceed(request);
        if (BaseApplication.isNetworkAvailable(BaseApplication.getInstance())) {
            int maxAge = 0; // read from cache
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-xcached, max-stale=" + maxStale)
                    .build();
        }
    };
    //cache
    static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR2 = chain -> {
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(0, TimeUnit.SECONDS);
        cacheBuilder.maxStale(365, TimeUnit.DAYS);
        CacheControl cacheControl = cacheBuilder.build();

        Request request = chain.request();
        //LoginRsp user = SpData.User();
        String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJyb2xlcyI6WyI1NDYzMjVfcHJpbmNpcGFsIiwiNTQ2MzI1X2RlcHRfaGVhZCIsIjU0NjMyNV9jYW1wdXNfaGVhZCIsIjU0NjMyNV9hY2FkZW1pY3MiLCI1NDYzMjVfZ3JhZGVfaGVhZCIsIjU0NjMyNV9oZWFkbWFzdGVyIiwiNTQ2MzI1X3RlYWNoZXIiXSwibmFtZSI6IueuoeeQhuWRmCIsImlkIjoxNDI1NDU4NjYyMzUxNjM5OTUyLCJleHAiOjE2NDYyNzQwNjAsImJpZCI6MTQ1ODI1NzYyNDA0MTU0NjMyNSwiYXV0aG9yaXRpZXMiOlsiNTQ2MzI1X2dyYWRlX2hlYWQiLCI1NDYzMjVfYWNhZGVtaWNzIiwiNTQ2MzI1X2hlYWRtYXN0ZXIiLCI1NDYzMjVfZGVwdF9oZWFkIiwiNTQ2MzI1X3RlYWNoZXIiLCI1NDYzMjVfY2FtcHVzX2hlYWQiLCI1NDYzMjVfcHJpbmNpcGFsIl0sImp0aSI6IjUzN2QxMzNhLWI5MzUtNDE4Ny04MTI1LTU0Mjk5ZWFkZTc4ZSIsImNsaWVudF9pZCI6InlpZGUtY2xvdWQiLCJ1c2VybmFtZSI6ImFkbWluIn0.iBgj6khGqWfnFrn1qFMB8tXVmAwnIdozHvyEotubCu9aXpoToCOiI5adKU9zdwUozDJtY7YK7aHayC74cIhnaUvhjJLGTEVx84-cz7Ykm7vQ8CxbHZt67PkfcfUWoz5DA2NOX_pvwoKWMGZtlebz9GvmFD7BFdLNFP-Buyp36FeZodilKWOaQcIhZXmQkgGY5yem3Gdut1HFfnEsgSdFouJP-lQclPTw2x53kl9wyIVEgUt-FcYgPC0K6OLwTm7jQ4rEFo2N56YiF5Su6TVnDgvsxXWULgwxk1nmkkYBlwg4pJlRMB68YYUrt5O_qI991VjKB-fmaM8nVWdNSjbXdg";
        //if (user != null) {
        Log.e("TAG1", "intercept: " + JSON.toJSONString(token));
        request = request.newBuilder()
                .addHeader("Authorization", token)
                .cacheControl(cacheControl)
                .build();
        //}

        Response originalResponse = chain.proceed(request);
        if (BaseApplication.isNetworkAvailable(BaseApplication.getInstance())) {
            int maxAge = 0; // read from cache
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-xcached, max-stale=" + maxStale)
                    .build();
        }
    };

    public static class TokenHeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            String token = SpData.getLogin().accessToken;
            if (TextUtils.isEmpty(token)) {
                Request originalRequest = chain.request();
                return chain.proceed(originalRequest);
            } else {
                Request originalRequest = chain.request();
                Request updateRequest = originalRequest.newBuilder().header("access_token", token).build();
                return chain.proceed(updateRequest);
            }
        }
    }

    private static void downloadFile(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void downloadFile(String version, String url, DownloadListener downloadListener) {
        downloadFile(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                downloadListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() != 200) {
                    downloadListener.onFailure();
                    return;
                }
                InputStream is = null;//输入流
                FileOutputStream fos = null;//输出流
                try {
                    is = response.body().byteStream();//获取输入流
                    long total = response.body().contentLength();//获取文件大小
                    downloadListener.onStart(total);
                    File file = new File(Utils.getApp().getCacheDir(), version + ".apk");
                    fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int ch;
                    int process = 0;
                    while ((ch = is.read(buf)) != -1) {
                        fos.write(buf, 0, ch);
                        process += ch;
                        downloadListener.onProgress(process);
                    }
                    fos.flush();
                    downloadListener.onSuccess();
                } catch (Exception e) {
                    downloadListener.onFailure();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        downloadListener.onFailure();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        downloadListener.onFailure();
                    }
                }
            }
        });
    }

    public interface DownloadListener {
        void onStart(long max);

        void onProgress(long progress);

        void onSuccess();

        void onFailure();
    }

}
