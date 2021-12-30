package com.yyide.chatim.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tencent.mmkv.MMKV;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BasePresenter;
import com.yyide.chatim.base.MMKVConstant;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.model.gate.GateDataPermissionRsp;
import com.yyide.chatim.net.ApiCallback;
import com.yyide.chatim.view.AppView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class AppPresenter extends BasePresenter<AppView> {
    private static final String TAG = AppPresenter.class.getSimpleName();
    public AppPresenter(AppView view) {
        attachView(view);
    }
    public void getMyAppList() {
        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
//        mvpView.showLoading();
        addSubscription(dingApiStores.getMyApp(body), new ApiCallback<MyAppListRsp>() {
            @Override
            public void onSuccess(MyAppListRsp model) {
                mvpView.getMyAppListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getMyAppFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getAppList() {
//        {"size": 10,"current": 1}
        Map<String, Object> map = new HashMap<>();
        map.put("size", 100);
        map.put("current", 1);
        RequestBody body = RequestBody.create(BaseConstant.JSON, JSON.toJSONString(map));
//        mvpView.showLoading();
        addSubscription(dingApiStores.getAppList(body), new ApiCallback<AppItemBean>() {
            @Override
            public void onSuccess(AppItemBean model) {
                mvpView.getAppListSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getAppListFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    /**
     * 查询闸机通行数据查看权限
     */
    public void queryUserBarrierPermissions() {
        addSubscription(dingApiStores.queryUserBarrierPermissions(), new ApiCallback<GateDataPermissionRsp>() {
            @Override
            public void onSuccess(GateDataPermissionRsp model) {
                Log.e(TAG, "onSuccess: " + JSON.toJSONString(model));
                if (model.getCode() == 200 && model.getData() != null) {
                    final String permission = model.getData().getPermission();
                    MMKV.defaultMMKV().encode(MMKVConstant.YD_GATE_DATA_ACCESS_PERMISSION,permission);
                }
            }

            @Override
            public void onFailure(String msg) {
                Log.e(TAG, "onFailure: " + msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
