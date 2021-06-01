package com.yyide.chatim.net;


import com.yyide.chatim.model.AddUserAnnouncementResponse;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.HomeAttendanceRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.model.LeaveListRsp;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.model.MessageNumberRsp;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.model.ClassesPhotoRsp;
import com.yyide.chatim.model.ConfirmDetailRsp;
import com.yyide.chatim.model.DepartmentScopeRsp;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.GetStuasRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.model.HomeNoticeRsp;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.NoticeDetailRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.SearchRsp;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectTableClassesRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.SmsVerificationRsp;
import com.yyide.chatim.model.StudentHonorRsp;
import com.yyide.chatim.model.StudentScopeRsp;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.TemplateListRsp;
import com.yyide.chatim.model.TemplateTypeRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.model.UniversityScopeRsp;
import com.yyide.chatim.model.UpdateUserInfo;
import com.yyide.chatim.model.UploadRep;
import com.yyide.chatim.model.UserInfoRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.UserMsgNoticeRsp;
import com.yyide.chatim.model.UserNoticeRsp;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.model.ListByAppRsp;
import com.yyide.chatim.model.listTimeDataByAppRsp;
import com.yyide.chatim.model.mobileRsp;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface DingApiStores {

    @GET("/java-painted-screen/api/wechatPaintedScreenManage/selectDeviceOperation")
    Observable<GetStuasRsp> getData();

    @GET("/java-painted-screen/api/wechatPaintedScreenManage/deviceUpdateByAndroid")
    Observable<DeviceUpdateRsp> setpm(@Query("machineCode") String a, @Query("officeId") int b, @Query("deviceDirection") String c);

    //账号密码登入
    @POST("/management/cloud-system/oauth/token")
    Observable<LoginRsp> login(@Query("username") String a, @Query("password") String b);

    //验证码登入
    @POST("/management/cloud-system/authentication/mobile")
    Observable<mobileRsp> loginmobile(@Query("validateCode") String a, @Query("mobile") String b);

    //获取验证码
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/app/smsVerification")
    Observable<SmsVerificationRsp> getCode(@Body RequestBody info);

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
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/timetable/cloud-timetable/schedule/selectSchByTeaid")
    Observable<SelectSchByTeaidRsp> selectSchByTeaid();

    //查询学生课程信息
    @GET("/timetable/cloud-timetable/schedule/selectClassInfoByClassId?")
    Observable<SelectSchByTeaidRsp> selectClassInfoByClassId(@Query("classId") String classId);

    //查询该老师教哪几个班级
    @POST("/timetable/cloud-timetable/schedule/listAllScheduleByTeacherId")
    Observable<ListAllScheduleByTeacherIdRsp> listAllScheduleByTeacherId();

    //班级列表接口
    @POST("/school/cloud-school/classes/listAllBySchoolId")
    Observable<listAllBySchoolIdRsp> listAllBySchoolId();

    //通过班级查询班级课表
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/timetable/cloud-timetable/timetable/listTimeDataByApp")
    Observable<listTimeDataByAppRsp> listTimeDataByApp(@Body RequestBody info);

    //添加用户设备基本信息
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/user/equipment/addUserEquipmentInfo")
    Observable<ResultBean> addUserEquipmentInfo(@Body RequestBody info);

    //查询组织架构列表信息 大学组织结构
    @POST("/school/cloud-school/department/listByApp")
    Observable<ListByAppRsp> listByApp();

    //查询组织架构列表信息 小初高组织结构
    @POST("/school/cloud-school/section/selectListByApp")
    Observable<ListByAppRsp> selectListByApp();

    //通讯录-学生/家长（大学）
    @POST("/school/cloud-school/departmentClass/selectListByApp")
    Observable<ListByAppRsp> universitySelectListByApp();

    //查询应用
    @POST("/backstage/cloud-backstage/application/search")
    Observable<SearchRsp> search();

    //更新用户信息
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/user/updateUserInformation")
    Observable<UpdateUserInfo> updateUserInfo(@Body RequestBody info);

    //获取忘记密码手机短信
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/app/user/updatepwd/smsVerification")
    Observable<ResultBean> getForgotSmsCode(@Body RequestBody info);

    //修改密码
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/app/user/updatepwd")
    Observable<ResultBean> forgotPwd(@Body RequestBody info);

    //教职工列表接口
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/school/cloud-school/app/teacher/list")
    Observable<TeacherlistRsp> teacherlist(@Body RequestBody info);

    //用户头像上传
    @Multipart
    @POST("/management/cloud-system/user/androidToOss")
    Observable<UploadRep> uploadImg(@Part MultipartBody.Part info);

    //扫码登录
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/authentication/scan")
    Observable<ResultBean> scanCodeLogin(@Body RequestBody info);

    //获取首页代办
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @GET("/message-server/cloud-message/user/notice/getIndexMyNotice")
    Observable<NoticeHomeRsp> getIndexMyNotice();

    //https://api.uat.edu.1d1j.net/management/cloud-system/user/notice/getMyNotice
    //https://api.uat.edu.1d1j.net/management/cloud-system/user/announcement/getMyAnnouncement
    //https://api.uat.edu.1d1j.net/message-server/cloud-message/user/announcement/getMyAnnouncement
    //通知公告列表
    //type 1 我收到的 2 我发布的
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/announcement/getMyAnnouncement")
    Observable<NoticeListRsp> getMyNotice(@Query("type") int type, @Query("current") int page, @Query("size") int size);

    //https://api.uat.edu.1d1j.net/management/cloud-system/user/notice/getMyNoticeDetails
    //https://api.uat.edu.1d1j.net/message-server/cloud-message/user/notice/getMyNoticeDetails
    //获取我的公告详情
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/notice/getMyNoticeDetails")
    Observable<NoticeDetailRsp> getMyNoticeDetails(@Query("id") int id);

    //https://api.uat.edu.1d1j.net/message-server/cloud-message/user/announcement/getMyReleaseNotice
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/announcement/getMyReleaseNotice")
    Observable<NoticeDetailRsp> getMyReleaseNotice(@Query("id") int id);

    //https://api.uat.edu.1d1j.net/message-server/cloud-message/user/announcement/getMyNoticeBySignId
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/announcement/getMyNoticeBySignId")
    Observable<NoticeDetailRsp> getMyNoticeBySignId(@Query("signId") long id);

    //消息模板分类类型
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/announcement/type/getAnnouncementTypePage")
    Observable<TemplateTypeRsp> getTemplateTypePage(@Body RequestBody requestBody);

    //获取消息模板
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/message/temp/selectMessagePage")
    Observable<TemplateListRsp> selectMessagePage(@Body RequestBody requestBody);

    //我的应用
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/application/me")
    Observable<MyAppListRsp> getMyApp(@Body RequestBody requestBody);

    //获取应用列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/application/group/list")
    Observable<AppItemBean> getAppList(@Body RequestBody requestBody);

    //查询应用列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/backstage/cloud-backstage/application/search")
    Observable<TemplateListRsp> searchApp(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/announcement/getMyNoticeBacklog")
    Observable<HomeNoticeRsp> getMyNoticeBacklog();

    //添加公告
    //https://api.uat.edu.1d1j.net/message-server/cloud-message/user/announcement/addUserAnnouncement
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message-server/cloud-message/user/announcement/addUserAnnouncement")
    Observable<AddUserAnnouncementResponse> addUserAnnouncement(@Body RequestBody requestBody);

    //删除我发布的通告
    //https://api.uat.edu.1d1j.net/message-server/cloud-message/user/announcement/delAnnouncement
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/announcement/delAnnouncement")
    Observable<BaseRsp> delAnnouncement(@Query("id") int id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/notice/updateMyNoticeDetails")
    Observable<BaseRsp> updateMyNoticeDetails(@Query("id") int id);

    //确认详情（多少人未确认）
    //https://api.uat.edu.1d1j.net/message-server/cloud-message/user/announcement/getConfirmDetails
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/announcement/getConfirmDetails")
    Observable<ConfirmDetailRsp> getConfirmDetails(@Query("confirmType") int confirmType, @Query("signId") long signId, @Query("current") int current, @Query("size") int size);

    //查询学段
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/message/notice/scope/getSectionList")
    Observable<StudentScopeRsp> getSectionList(@Body RequestBody requestBody);

    //查询学段（大学）
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/message/notice/scope/queryDepartmentClassList")
    Observable<UniversityScopeRsp> queryDepartmentClassList(@Body RequestBody requestBody);

    //获取部门列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/message/notice/scope/getDepartmentList")
    Observable<DepartmentScopeRsp> getDepartmentList(@Body RequestBody requestBody);

    //查询是否有备课
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/timetable/cloud-timetable/lessons/searchById")
    Observable<HomeNoticeRsp> getPreparesLesson(@Body RequestBody requestBody);

    //添加备课
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/timetable/cloud-timetable/lessons/addLessons")
    Observable<ResultBean> addLessons(@Body RequestBody requestBody);

    //修改备课
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/timetable/cloud-timetable/lessons/updateLessons")
    Observable<ResultBean> updateLessons(@Body RequestBody requestBody);

    //搜索帮助中心详情
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/backstage/cloud-backstage/app/support/search")
    Observable<ResultBean> searchHelpDetail(@Body RequestBody requestBody);

    //查询帮助中心详情默认返回前十条
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/backstage/cloud-backstage/app/support/selectByHot")
    Observable<HelpItemRep> getHelpList(@Body RequestBody requestBody);

    //帮助中心进阶
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/backstage/cloud-backstage/app/support/selectByAdvanced")
    Observable<HelpItemRep> getHelpAdvanced(@Body RequestBody requestBody);

    //帮助中心入门指南
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/backstage/cloud-backstage/app/support/selectByIntroduction")
    Observable<HelpItemRep> getHelpIntroduction(@Body RequestBody requestBody);

    //获取班级相册
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/app/videoalbum/list")
    Observable<ClassesPhotoRsp> getClassPhotoList(@Body RequestBody requestBody);

    //获取班级相册
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/school/cloud-school/app/teacher/selectAllList")
    Observable<ResultBean> searchBook(@Body RequestBody requestBody);

    //学校所有班级-大学
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/school/cloud-school/departmentClass/selectClassByAllSchool")
    Observable<SelectTableClassesRsp> selectClassByAllSchool();

    //学校所有班级-小初高
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/school/cloud-school/section/selectListBySchoolAll")
    Observable<SelectTableClassesRsp> selectListBySchoolAll();

    //获取应用列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/application/me/list")
    Observable<AppAddRsp> getAppAddList(@Body RequestBody requestBody);

    //添加应用列表
    @POST("/management/cloud-system/app/application/me/add/{id}")
    Observable<ResultBean> addApp(@Path("id") int id);

    //删除应用
    @POST("/management/cloud-system/app/application/me/del/{id}")
    Observable<ResultBean> deleteApp(@Path("id") int id);

    //获取班级作业信息
    @GET("/timetable/cloud-timetable/schedule/selectSchByClassId?")
    Observable<SelectSchByTeaidRsp> getClassesHomeworkInfo(@Query("classId") String id);

    //https://api.uat.edu.1d1j.net/school/cloud-school/app/teacher/selectAllList
    //通讯录搜索-所有
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/school/cloud-school/app/user/list")
    Observable<UserInfoRsp> selectAllList(@Body RequestBody requestBody);

    //通讯录搜索-小初高
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/teacher/cloud-teacher/app/student/list")
    Observable<TeacherlistRsp> getStudentList(@Body RequestBody requestBody);

    //获取首页学生作品
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/android/studentWork/list")
    Observable<UserInfoRsp> getStudentWorkList(@Body RequestBody requestBody);

    //获取消息代办列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/notice/getMessageTransaction")
    Observable<TodoRsp> getMessageTransaction(@QueryMap HashMap<String, Object> map);

    //https://api.uat.edu.1d1j.net/face/cloud-face/face/toStudentOss
    //上传学生face
    @Multipart
    @POST("/face/cloud-face/face/toStudentOss")
    Observable<BaseRsp> toStudentOss(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);

    //https://api.uat.edu.1d1j.net/face/cloud-face/teacher/face/toTeacherOss
    //上传教师face
    @Multipart
    @POST("/face/cloud-face/teacher/face/toTeacherOss")
    Observable<BaseRsp> toTeacherOss(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);

    //https://api.uat.edu.1d1j.net/face/cloud-face/face/getStudentOss
    //查询学生头像
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/face/cloud-face/face/getStudentOss")
    Observable<FaceOssBean> getStudentOss(@Body RequestBody requestBody);

    //https://api.uat.edu.1d1j.net/face/cloud-face/teacher/face/getTeacherOss
    //查询老师头像
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/face/cloud-face/teacher/face/getTeacherOss")
    Observable<FaceOssBean> getTeacherOss(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/app/studentWork/list")
    Observable<StudentHonorRsp> getStudentHonorList(@Body RequestBody requestBody);

    //https://api.edu.1d1j.cn/management/cloud-system/user/notice/getUserNoticePage
    //获取通知消息列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message-server/cloud-message/user/notice/queryUserMessageNotice")
    Observable<UserNoticeRsp> getUserNoticePage(@Body RequestBody requestBody);

    //https://api.uat.edu.1d1j.net/message-server/cloud-message/user/notice/queryUserMessageNotice
    //获取消息分页
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/notice/queryUserMessageNotice")
    Observable<UserMsgNoticeRsp> queryUserMessageNotice(@QueryMap HashMap<String, Object> map);


    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/leave/common/getAskLeaveRecord
    //请假列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/leave-server/cloud-leave/leave/common/getAskLeaveRecord")
    Observable<LeaveListRsp> getAskLeaveRecord(@QueryMap HashMap<String, Object> map);

    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/leave/common/queryLeaveDetailsById
    //请假详情
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/leave-server/cloud-leave/leave/common/queryLeaveDetailsById")
    Observable<LeaveDetailRsp> queryLeaveDetailsById(@QueryMap HashMap<String, Object> map);

    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/student/ondoApplyLeave
    //请假撤销
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/leave-server/cloud-leave/student/ondoApplyLeave")
    Observable<BaseRsp> ondoApplyLeave(@QueryMap HashMap<String, Object> map);

    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/teacher/queryDeptInfoByTeacherId
    //获取所在部门
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/leave-server/cloud-leave/teacher/queryDeptInfoByTeacherId")
    Observable<LeaveDeptRsp> queryDeptInfoByTeacherId();

    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/leave/common/getApprover?classIdOrdeptId=2076
    //获取流程审批人和抄送人
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/leave-server/cloud-leave/leave/common/getApprover")
    Observable<ApproverRsp> getApprover(@QueryMap HashMap<String, Object> map);

    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/teacher/addTeacherLeave
    //教职工请假
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/leave-server/cloud-leave/teacher/addTeacherLeave")
    Observable<BaseRsp> addTeacherLeave(@Body RequestBody requestBody);

    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/student/addStudentLeave
    //学生请假
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/leave-server/cloud-leave/student/addStudentLeave")
    Observable<BaseRsp> addStudentLeave(@Body RequestBody requestBody);

    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/teacher/processExaminationApproval
    //请假审批确认
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/leave-server/cloud-leave/teacher/processExaminationApproval")
    Observable<BaseRsp> processExaminationApproval(@QueryMap HashMap<String, Object> map);

    //获取代办数量
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/notice/queryBocklogNuberByStatus")
    Observable<MessageNumberRsp> queryBocklogNuberByStatus(@QueryMap HashMap<String, Object> map);

    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/leave/phrase/queryLeavePhraseList
    //请假短语 短语类型 1 家长 2教职工
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/leave-server/cloud-leave/leave/phrase/queryLeavePhraseList")
    Observable<LeavePhraseRsp> queryLeavePhraseList(@QueryMap HashMap<String, Object> map);

    //更新我收到的公告
    //https://api.uat.edu.1d1j.net/message-server/cloud-message/user/notice/updateMyNoticeDetails
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message-server/cloud-message/user/notice/updateMyNoticeDetails")
    Observable<ResultBean> updateMyNoticeDetails(@QueryMap HashMap<String, Object> map);

    //首页考勤
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/face/cloud-face/attendance/homeAttendance")
    Observable<HomeAttendanceRsp> homeAttendance(@Body RequestBody requestBody);

    //老师视角的课堂考勤
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/face/cloud-face/attendance/viewAttendance")
    Observable<AttendanceCheckRsp> teacherAttendance(@Body RequestBody requestBody);
}