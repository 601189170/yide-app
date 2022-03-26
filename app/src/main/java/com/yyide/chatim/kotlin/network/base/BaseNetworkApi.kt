package com.yyide.chatim.kotlin.network.base

import android.util.Log
import com.alibaba.fastjson.JSON
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.kotlin.network.exception.NetworkException
import com.yyide.chatim.kotlin.network.interceptor.CommonRequestInterceptor
import com.yyide.chatim.kotlin.network.interceptor.CommonResponseInterceptor
import com.yyide.chatim.BuildConfig
import com.yyide.chatim.SpData
import com.yyide.chatim.kotlin.network.ErrorCode
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.ParameterizedType
import java.net.SocketException
import java.util.concurrent.TimeUnit

/**
 * 网络请求封装
 */
abstract class BaseNetworkApi<I>(private val baseUrl: String) : IService<I> {

    protected val service: I by lazy {
        getRetrofit().create(getServiceClass())
    }

    protected open fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getServiceClass(): Class<I> {
        val genType = javaClass.genericSuperclass as ParameterizedType
        return genType.actualTypeArguments[0] as Class<I>
    }

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClient = getCustomOkHttpClient()
        if (null != okHttpClient) {
            return okHttpClient
        }
        return defaultOkHttpClient
    }

    protected open fun getCustomOkHttpClient(): OkHttpClient? {
        return null
    }

    protected open fun getCustomInterceptor(): Interceptor? {
        return null
    }

    protected suspend fun <T> getResult(block: suspend () -> BaseResponse<T>): Result<T> {
//        for (i in 1..RETRY_COUNT) {
        try {
            val response = block()
            if (response.code != ErrorCode.OK) {
                throw NetworkException.of(response.code, "${response.message}")
            }
            if (response.data == null) {
                throw NetworkException.of(ErrorCode.VALUE_IS_NULL, "response value is null")
            }
            return Result.success(response.data)
        } catch (throwable: Throwable) {
            if (throwable is NetworkException) {
                return Result.failure(throwable)
            }
            if (throwable is SocketException) {
                return Result.failure(throwable)
            }
            if ((throwable is HttpException && throwable.code() == ErrorCode.UNAUTHORIZED)) {
                // 这里刷新token，然后重试
            }

        }
//        }
        return Result.failure(NetworkException.of(ErrorCode.VALUE_IS_NULL, "请求异常"))
    }

    companion object {
        const val TAG = "BaseNetworkApi"
        private const val RETRY_COUNT = 2
        private val defaultOkHttpClient by lazy {
            val builder = OkHttpClient.Builder()
                .callTimeout(30L, TimeUnit.SECONDS)
                .connectTimeout(30L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .writeTimeout(30L, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
            builder.addInterceptor(CommonRequestInterceptor())
            builder.addInterceptor(CommonResponseInterceptor())
            builder.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(loggingInterceptor)
            }
            builder.build()
        }

        //cache
        private var REWRITE_CACHE_CONTROL_INTERCEPTOR = Interceptor { chain: Interceptor.Chain ->
            val cacheBuilder = CacheControl.Builder()
            cacheBuilder.maxAge(0, TimeUnit.SECONDS)
            cacheBuilder.maxStale(365, TimeUnit.DAYS)
            val cacheControl = cacheBuilder.build()
            var request = chain.request()
            val user = SpData.getLogin()
            if (user != null) {
                Log.e("TAG", "intercept: " + JSON.toJSONString(user))
                request = request.newBuilder()
                    .addHeader("Authorization", user.getAccessToken())
                    .cacheControl(cacheControl)
                    .build()
            }
            val originalResponse = chain.proceed(request)
            if (BaseApplication.isNetworkAvailable(BaseApplication.getInstance())) {
                val maxAge = 0 // read from cache
                return@Interceptor originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=$maxAge")
                    .build()
            } else {
                val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                return@Interceptor originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-xcached, max-stale=$maxStale")
                    .build()
            }
        }
    }

}