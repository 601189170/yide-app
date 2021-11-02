package com.yyide.chatim.alipush;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.yyide.chatim.SpData;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.net.AppClient;
import com.yyide.chatim.net.DingApiStores;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Description: alibaba push，alias set
 * @Author: liu tao
 * @CreateDate: 2021/7/22 16:11
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/7/22 16:11
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AliasUtil {
    private static final String TAG = "AliasUtil";

    private static void addAlias(CloudPushService mPushService, String alias) {
        final String deviceId = mPushService.getDeviceId();
        Log.e(TAG, "addAlias: add alias "+alias + " deviceId "+deviceId);
        mPushService.addAlias(alias, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "onSuccess: add alias " + alias + " success. " + s);
                //添加设备到服务器
                addUserEquipmentInfo(deviceId,alias);
            }

            @Override
            public void onFailed(String errorCode, String errorMsg) {
                Log.e(TAG, "onFailed :add alias " + alias + " failed." +
                        "errorCode: " + errorCode + ", errorMsg:" + errorMsg + "\n");
            }
        });
    }

    /**
     * 删除别名
     *
     * @param mPushService
     * @param alias
     */
    private static void delAlias(CloudPushService mPushService, String alias) {
        Log.e(TAG, "delAlias: remove alias "+alias);
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

    /**
     * 同步别名
     */
    public static void syncAliases() {
        CloudPushService mPushService = PushServiceFactory.getCloudPushService();
        //保证当前账号的当前身份
        final String userid = String.valueOf(SpData.getIdentityInfo().userId);
        final String deviceId = mPushService.getDeviceId();
        Log.e(TAG, "userid: " + userid+",deviceId："+deviceId);
        mPushService.listAliases(new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e(TAG, "aliases:" + response + " \n");
                if (!TextUtils.isEmpty(response)) {
                    final String[] aliases = response.split(",");
                    if (aliases.length != 0) {
                        // contains ture 不需要重新写入别名
                        final boolean contains = Arrays.asList(aliases).contains(userid);
                        Log.e(TAG, "syncAliases， aliases contains userid " + contains);
                        if (!contains) {
                            addAlias(mPushService, userid);
                        }
                    }

                    for (String alias : aliases) {
                        if (!userid.equals(alias)) {
                            delUserEquipmentInfo(alias);
                            delAlias(mPushService, alias);
                        }
                    }
                } else {
                    addAlias(mPushService, userid);
                }
            }

            @Override
            public void onFailed(String errorCode, String errorMsg) {
                Log.e(TAG, "list aliases failed. errorCode:" + errorCode + " errorMsg:" + errorMsg);
            }
        });
    }

    /**
     * 清空当前别名
     */
    public static void clearAlias() {
        CloudPushService mPushService = PushServiceFactory.getCloudPushService();
        //保证当前账号的当前身份
        final String userid = String.valueOf(SpData.getIdentityInfo().userId);
        delUserEquipmentInfo(userid);
        delAlias(mPushService, userid);
    }

    /**
     * 添加设备
     * @param registrationId 设备id
     * @param alias 别名 userid
     */
    private static void addUserEquipmentInfo(String registrationId, String alias) {
        DingApiStores mDingApiStores = AppClient.getDingRetrofit().create(DingApiStores.class);
        Map<String, String> map = new HashMap();
        map.put("registrationId", registrationId);
        map.put("alias", alias);
        map.put("equipmentType", "1");
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        mDingApiStores.addUserEquipmentInfo(body).enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(@NotNull Call<ResultBean> call, @NotNull Response<ResultBean> response) {
                Log.e(TAG, "onResponse: " + JSON.toJSONString(response.body()));
            }

            @Override
            public void onFailure(@NotNull Call<ResultBean> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * 删除设备
     * @param id userid
     */
    private static void delUserEquipmentInfo(String id) {
        Log.d(TAG, "delUserEquipmentInfo: "+id );
        DingApiStores mDingApiStores = AppClient.getDingRetrofit().create(DingApiStores.class);
        mDingApiStores.delUserEquipmentInfo(id).enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(@NotNull Call<ResultBean> call, @NotNull Response<ResultBean> response) {
                Log.e(TAG, "onResponse: " + JSON.toJSONString(response.body()));
            }

            @Override
            public void onFailure(@NotNull Call<ResultBean> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * 获取用户设备列表分页
     * @param id
     */
    private static void getUserEquipmentInfoPage(String id) {
        DingApiStores mDingApiStores = AppClient.getDingRetrofit().create(DingApiStores.class);
        Map<String, String> map = new HashMap();
        map.put("id", id);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
        mDingApiStores.getUserEquipmentInfoPage(body).enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                Log.e(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

}
