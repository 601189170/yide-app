package com.yyide.chatim.net;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.yyide.chatim.BaseApplication;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.LoginRsp;

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
    private static OkHttpClient okHttpClient;

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
//            builder.addInterceptor(new TokenHeaderInterceptor());
            builder.connectTimeout(30, TimeUnit.SECONDS). // 设置连接超时时间
                    readTimeout(30, TimeUnit.SECONDS).
                    writeTimeout(30, TimeUnit.SECONDS).build();
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
        LoginRsp user = SpData.User();
        if (user != null) {
            Log.e("TAG", "intercept: " + JSON.toJSONString(user.data.accessToken));
            request = request.newBuilder()
                    .addHeader("Authorization", user.data.accessToken)
                    .cacheControl(cacheControl)
                    .build();
        }

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
            String token = SpData.User().data.accessToken;
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
