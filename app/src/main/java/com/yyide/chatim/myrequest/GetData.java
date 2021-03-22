package com.yyide.chatim.myrequest;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;


import java.net.URLEncoder;

import static com.yyide.chatim.myrequest.Object2Map.object2Map;



/**
 * Created by Hao on 16/9/6.
 */
public class GetData {
//    public static final String url = "http://120.76.189.190";
    public static final String url = "https://api.uat.edu.1d1j.net";
    /**
     * 网络请求的解析的密匙key
     **/
    public final static String URL_KEY = "24ca8a8a8a8888439b926572b5fb6233fb";

//    public static String url() {
//        return "http://" + SPUtils.getInstance().getString(BaseConstant.NUMBER, URL_IP) + ":8027";
//    }

    public static String imageUrl() {
        return "http://" + "120.76.189.190" + "/";
    }
    /**
     * 独立操作190端口请求
     */
    public static <T> String requestUrl(BaseBeanReq<T> bean) {
        try {
            MyHashMap map = new MyHashMap();
            map.putAll(object2Map(bean));
            String encryStr = AuthcodeTwo.authcodeEncode(map.toString(), URL_KEY);
            String urlRequest = url + bean.myAddr() + "?input=" + URLEncoder.encode(encryStr, "UTF-8");
            Log.d("JSON", urlRequest);
            Log.d("JSON", JSON.toJSONString(bean));
            return urlRequest;
        } catch (Exception e) {
            return null;
        }
    }



}
