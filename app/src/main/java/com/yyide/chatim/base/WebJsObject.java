package com.yyide.chatim.base;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.yyide.chatim.SpData;
import com.yyide.chatim.model.WebModel;

public class WebJsObject {
    private Context mContext;

    public WebJsObject(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public String postMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            WebModel webModel = JSON.parseObject(msg, WebModel.class);
            if (webModel != null) {
                if ("backApp".equalsIgnoreCase(webModel.getEnentName())) {
                    ((Activity) mContext).finish();
                } else if ("getToken".equalsIgnoreCase(webModel.getEnentName())) {
                    return SpData.User() != null ? SpData.User().getToken() : "";
                } else if ("save".equalsIgnoreCase(webModel.getEnentName())) {
                    //跳转至发布范围人员选择
                }
            }
        }
        return "";
    }

}
