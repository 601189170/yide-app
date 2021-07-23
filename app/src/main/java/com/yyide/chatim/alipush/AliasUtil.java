package com.yyide.chatim.alipush;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.yyide.chatim.SpData;

import java.util.Arrays;

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
        Log.e(TAG, "addAlias: add alias "+alias);
        mPushService.addAlias(alias, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "onSuccess: add alias " + alias + " success. " + s);
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
        Log.e(TAG, "userid: " + userid);
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
        delAlias(mPushService, userid);
    }
}
