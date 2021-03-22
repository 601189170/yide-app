package com.yyide.chatim.net;



import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.model.GetStuasRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ListScheduleRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.NewsDetailsEntity;
import com.yyide.chatim.model.NewsEntity;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.model.listTimeDataRsp;
import com.yyide.chatim.model.mobileRsp;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */

//    https://api.uat.edu.1d1j.net/management/cloud-system/login

public interface DingApiStores {
//    String API_SERVER_URL = "http://120.76.189.190:8027";
    String API_SERVER_URL = "https://api.uat.edu.1d1j.net";

    @GET("/java-painted-screen/api/wechatPaintedScreenManage/selectDeviceOperation")
    Observable<GetStuasRsp> getData();

    @GET("/java-painted-screen/api/wechatPaintedScreenManage/deviceUpdateByAndroid")
    Observable<DeviceUpdateRsp> setpm(@Query("machineCode") String a, @Query("officeId") int b, @Query("deviceDirection") String c);
    //账号密码登入
    @POST("/management/cloud-system/login")
    Observable<LoginRsp> login(@Query("username") String a, @Query("password") String b);
    //验证码登入
    @POST("/management/cloud-system/authentication/mobile")
    Observable<mobileRsp> loginmobile(@Query("validateCode") String a, @Query("mobile") String b);
    //获取验证码
    @POST("/management/cloud-system/app/smsVerification")
    Observable<SmsVerificationRsp> getcode(@Query("phone") String a);
    //登出
    @POST("/management/cloud-system/login/userLogout")
    Observable<UserLogoutRsp> Logout();
    //识别用户角色
    @POST("/cloud-system/user/selectUser")
    Observable<SelectUserRsp> getSelectUser();
    //查询登入用户学校
    @GET("/management/cloud-system/user/getUserSchool")
    Observable<GetUserSchoolRsp> getUserSchool(@Header("access_token") String accesToken);
    //查询老师课程信息
    @POST("/timetable/cloud-timetable/schedule/selectSchByTeaid")
    Observable<SelectSchByTeaidRsp> selectSchByTeaid();
    //查询该老师教哪几个班级
    @POST("/timetable/cloud-timetable/schedule/listAllScheduleByTeacherId")
    Observable<ListAllScheduleByTeacherIdRsp> listAllScheduleByTeacherId();

    //班级列表接口
    @POST("/school/cloud-school/classes/listAllBySchoolId")
    Observable<listAllBySchoolIdRsp> listAllBySchoolId();
}
