package com.yyide.chatim.net;



import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.model.GetStuasRsp;
import com.yyide.chatim.model.NewsDetailsEntity;
import com.yyide.chatim.model.NewsEntity;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface DingApiStores {
    String API_SERVER_URL = "http://120.76.189.190:8027";

    @GET("/java-painted-screen/api/wechatPaintedScreenManage/selectDeviceOperation")
    Observable<GetStuasRsp> getData();
    @GET("/java-painted-screen/api/wechatPaintedScreenManage/deviceUpdateByAndroid")
    Observable<DeviceUpdateRsp> setpm(@Query("machineCode") String a, @Query("officeId") int b, @Query("deviceDirection") String c);
}
