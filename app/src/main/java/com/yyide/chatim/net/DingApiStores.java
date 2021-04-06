package com.yyide.chatim.net;



import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.model.GetStuasRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.HomeNoticeRsp;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ListScheduleRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.NewsDetailsEntity;
import com.yyide.chatim.model.NewsEntity;
import com.yyide.chatim.model.NoticeDetailRsp;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.SearchRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.TemplateListRsp;
import com.yyide.chatim.model.TemplateTypeRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.addUserEquipmentInfoRsp;
import com.yyide.chatim.model.getUserSigRsp;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.model.listByAppRsp;
import com.yyide.chatim.model.listTimeDataByAppRsp;
import com.yyide.chatim.model.listTimeDataRsp;
import com.yyide.chatim.model.mobileRsp;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */

//    https://api.uat.edu.1d1j.net/management/cloud-system/login

public interface DingApiStores {
//    String API_SERVER_URL = "http://120.76.189.190:8027";
    String API_SERVER_URL = "https://api.uat.edu.1d1j.net";
//    String API_SERVER_URL = "https://192.168.3.120:8010";

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
    @GET("/management/cloud-system/user/getUserSchoolByApp")
    Observable<GetUserSchoolRsp> getUserSchool();
    //查询老师课程信息
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/timetable/cloud-timetable/schedule/selectSchByTeaid")
    Observable<SelectSchByTeaidRsp> selectSchByTeaid();
    //查询该老师教哪几个班级
    @POST("/timetable/cloud-timetable/schedule/listAllScheduleByTeacherId")
    Observable<ListAllScheduleByTeacherIdRsp> listAllScheduleByTeacherId();

    //班级列表接口
    @POST("/school/cloud-school/classes/listAllBySchoolId")
    Observable<listAllBySchoolIdRsp> listAllBySchoolId();


    //计算UserSig
//    @POST("/management/cloud-system/im/getUserSig")
    @POST("/cloud-system/im/getUserSig")
    Observable<getUserSigRsp> getUserSig();

    //通过班级查询生成课表
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/timetable/cloud-timetable/timetable/listTimeDataByApp")
    Observable<listTimeDataByAppRsp> listTimeDataByApp(@Body RequestBody info);

    //添加用户设备基本信息
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("/management/cloud-backstage/jpush/addUserEquipmentInfo")
    Observable<addUserEquipmentInfoRsp> addUserEquipmentInfo(@Body RequestBody info);


    //查询组织架构列表信息
    @POST("/school/cloud-school/department/listByApp")
    Observable<listByAppRsp> listByApp();

    //查询应用
    @POST("/backstage/cloud-backstage/application/search")
    Observable<SearchRsp> Search();

    //更新用户信息
    @POST("/management/cloud-system/user/updateUserInformation")
    Observable<String> updateUserInfo(@Query("id") String id, @Query("birthdayDate") String birthdayDate, @Query("sex") String sex, @Query("email") String email);

    //获取忘记密码手机短信
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/app/user/updatepwd/smsVerification")
    Observable<ResultBean> getForgotSmsCode(@Body RequestBody info);

    //修改密码
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/app/user/updatepwd")
    Observable<ResultBean> forgotPwd(@Body RequestBody info);

    //修改密码
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/school/cloud-school/teacher/list")
    Observable<TeacherlistRsp> teacherlist(@Body RequestBody info);

    //https://api.uat.edu.1d1j.net/management/cloud-system/user/notice/getMyNotice
    //通知公告列表
    //type 1 我收到的 2 我发布的
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/management/cloud-system/user/notice/getMyNotice")
    Observable<NoticeListRsp> getMyNotice(@Query("type") int type,@Query("page") int page,@Query("size") int size);

    //https://api.uat.edu.1d1j.net/management/cloud-system/user/notice/getMyNoticeDetails
    //获取我的公告详情
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/management/cloud-system/user/notice/getMyNoticeDetails")
    Observable<NoticeDetailRsp> getMyNoticeDetails(@Query("id") int id);

    //https://api.uat.edu.1d1j.net/management/cloud-system/notice/temp/getTemplateTypePage
    //消息模板分类类型
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/notice/temp/getTemplateTypePage")
    Observable<TemplateTypeRsp> getTemplateTypePage(@Body RequestBody requestBody);

    //https://api.uat.edu.1d1j.net/management/cloud-system/message/temp/selectMessagePage
    //获取消息模板
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/message/temp/selectMessagePage")
    Observable<TemplateListRsp> selectMessagePage(@Body RequestBody requestBody);

    //https://api.uat.edu.1d1j.net/management/cloud-system/user/notice/getMyNoticeBacklog
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/management/cloud-system/user/notice/getMyNoticeBacklog")
    Observable<HomeNoticeRsp> getMyNoticeBacklog();

}