package com.yyide.chatim.myrequest.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.myrequest.BaseBeanReq;


public class DeviceUpdateReq extends BaseBeanReq<DeviceUpdateRsp> {
//
//    参数
//    deviceId(设备id), officeId(学校id), playTime(轮播时长)

    public String machineCode;
    public int officeId ;
    public String playTime="" ;
    public String deviceDirection="" ;
    @Override
    public String myAddr() {
        return "/java-painted-screen/api/wechatPaintedScreenManage/deviceUpdateByAndroid";
//        return "/api/wechatPaintedScreenManage/deviceUpdateByAndroid";
    }

    @Override
    public TypeReference<DeviceUpdateRsp> myTypeReference() {
        return new TypeReference<DeviceUpdateRsp>() {
        };
    }
}