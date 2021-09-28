package com.yyide.chatim.net;


import com.yyide.chatim.model.ActivateRsp;
import com.yyide.chatim.model.AddUserAnnouncementResponse;
import com.yyide.chatim.model.AddressBookRsp;
import com.yyide.chatim.model.AppAddRsp;
import com.yyide.chatim.model.AppItemBean;
import com.yyide.chatim.model.ApproverRsp;
import com.yyide.chatim.model.AttendanceCheckRsp;
import com.yyide.chatim.model.AttendanceDayStatsRsp;
import com.yyide.chatim.model.AttendanceSchoolGradeRsp;
import com.yyide.chatim.model.AttendanceWeekStatsRsp;
import com.yyide.chatim.model.BaseRsp;
import com.yyide.chatim.model.BookRsp;
import com.yyide.chatim.model.BookSearchRsp;
import com.yyide.chatim.model.BrandSearchRsp;
import com.yyide.chatim.model.ClassBrandInfoRsp;
import com.yyide.chatim.model.ClassesPhotoBannerRsp;
import com.yyide.chatim.model.ClassesPhotoRsp;
import com.yyide.chatim.model.ConfirmDetailRsp;
import com.yyide.chatim.model.DepartmentScopeRsp;
import com.yyide.chatim.model.DepartmentScopeRsp2;
import com.yyide.chatim.model.DeviceUpdateRsp;
import com.yyide.chatim.model.FaceOssBean;
import com.yyide.chatim.model.GetAppVersionResponse;
import com.yyide.chatim.model.GetStuasRsp;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.HelpItemRep;
import com.yyide.chatim.model.HomeNoticeRsp;
import com.yyide.chatim.model.LeaveDeptRsp;
import com.yyide.chatim.model.LeaveDetailRsp;
import com.yyide.chatim.model.LeaveListRsp;
import com.yyide.chatim.model.LeavePhraseRsp;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ListByAppRsp;
import com.yyide.chatim.model.LoginAccountBean;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.MessageNumberRsp;
import com.yyide.chatim.model.MyAppListRsp;
import com.yyide.chatim.model.NoticeBrandBean;
import com.yyide.chatim.model.NoticeDetailRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.model.NoticeItemBean;
import com.yyide.chatim.model.NoticeListRsp;
import com.yyide.chatim.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim.model.NoticePermissionBean;
import com.yyide.chatim.model.NoticePersonnelBean;
import com.yyide.chatim.model.NoticeReleaseTemplateBean;
import com.yyide.chatim.model.NoticeUnreadPeopleBean;
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
import com.yyide.chatim.model.UserSigRsp;
import com.yyide.chatim.model.WeeklyDateBean;
import com.yyide.chatim.model.WeeklyDescBean;
import com.yyide.chatim.model.listAllBySchoolIdRsp;
import com.yyide.chatim.model.listTimeDataByAppRsp;
import com.yyide.chatim.model.schedule.LabelListRsp;
import com.yyide.chatim.model.schedule.Settings;
import com.yyide.chatim.model.schedule.SiteNameRsp;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface DingApiStores {

    //获取应用更新接口
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/backstage/cloud-backstage/app/version/selectVersionByTerminal")
    Observable<GetAppVersionResponse> updateVersion(@Body RequestBody info);

    @GET("/java-painted-screen/api/wechatPaintedScreenManage/selectDeviceOperation")
    Observable<GetStuasRsp> getData();

    @GET("/java-painted-screen/api/wechatPaintedScreenManage/deviceUpdateByAndroid")
    Observable<DeviceUpdateRsp> setpm(@Query("machineCode") String a, @Query("officeId") int b, @Query("deviceDirection") String c);

    //账号密码登入
    @POST("/management/cloud-system/oauth/token")
    Observable<LoginRsp> login(@Body RequestBody info);

    //验证码登入
    @POST("/management/cloud-system/authentication/mobile")
    Observable<LoginRsp> loginmobile(@Body RequestBody info);

    //获取验证码
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/app/smsVerification")
    Observable<SmsVerificationRsp> getCode(@Body RequestBody info);

    //获取Im签名
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/im/getUserSig")
    Observable<UserSigRsp> getUserSig(@Body RequestBody info);

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
    @POST("/management/cloud-system/classes/listAllBySchoolId")
    Observable<listAllBySchoolIdRsp> listAllBySchoolId();

    //通过班级查询班级课表
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/timetable/cloud-timetable/timetable/listTimeDataByApp")
    Observable<listTimeDataByAppRsp> listTimeDataByApp(@Body RequestBody info);

    //添加用户设备基本信息
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/user/equipment/addUserEquipmentInfo")
    Call<ResultBean> addUserEquipmentInfo(@Body RequestBody info);

    /**
     * https://api.uat.edu.1d1j.net/management/cloud-system/user/equipment/delUserEquipmentInfo
     * 删除用户设备
     *
     * @param id 删除的id
     * @return call
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/management/cloud-system/user/equipment/delUserEquipmentInfo")
    Call<ResultBean> delUserEquipmentInfo(@Query("id") String id);

    /**
     * https://api.uat.edu.1d1j.net/management/cloud-system/user/equipment/getUserEquipmentInfoPage
     * 用户设备列表分页
     *
     * @param info
     * @return call
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/user/equipment/getUserEquipmentInfoPage")
    Call<ResultBean> getUserEquipmentInfoPage(@Body RequestBody info);

    //查询组织架构列表信息 大学组织结构
    @POST("/management/cloud-system/department/listByApp")
    Observable<ListByAppRsp> listByApp();

    //查询组织架构列表信息 小初高组织结构
    @POST("/management/cloud-system/section/selectListByApp")
    Observable<ListByAppRsp> selectListByApp();

    //通讯录-学生/家长（大学）
    @POST("/management/cloud-system/departmentClass/selectListByApp")
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
    @POST("/management/cloud-system/app/teacher/list")
    Observable<TeacherlistRsp> teacherlist(@Body RequestBody info);

    //用户头像上传
    @Multipart
    @POST("/management/cloud-system/user/uploadPic")
    Observable<UploadRep> uploadImg(@Part MultipartBody.Part info, @Part("studentId") Long studentId);

    //扫码登录
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/authentication/scan")
    Observable<ResultBean> scanCodeLogin(@Body RequestBody info);

    //获取首页代办
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @GET("/message/cloud-message/user/notice/getIndexMyNotice")
    Observable<NoticeHomeRsp> getIndexMyNotice();

    //https://api.uat.edu.1d1j.net/management/cloud-system/user/notice/getMyNotice
    //https://api.uat.edu.1d1j.net/management/cloud-system/user/announcement/getMyAnnouncement
    //https://api.uat.edu.1d1j.net/message/cloud-message/user/announcement/getMyAnnouncement
    //通知公告列表
    //type 1 我收到的 2 我发布的
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/announcement/getMyAnnouncement")
    Observable<NoticeListRsp> getMyNotice(@Query("type") int type, @Query("current") int page, @Query("size") int size);

    //https://api.uat.edu.1d1j.net/management/cloud-system/user/notice/getMyNoticeDetails
    //https://api.uat.edu.1d1j.net/message/cloud-message/user/notice/getMyNoticeDetails
    //获取我的公告详情
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/notice/getMyNoticeDetails")
    Observable<NoticeDetailRsp> getMyNoticeDetails(@Query("id") long id);

    //https://api.uat.edu.1d1j.net/message/cloud-message/user/announcement/getMyReleaseNotice
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/announcement/getMyReleaseNotice")
    Observable<NoticeDetailRsp> getMyReleaseNotice(@Query("id") long id);

    //https://api.uat.edu.1d1j.net/message/cloud-message/user/announcement/getMyNoticeBySignId
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/announcement/getMyNoticeBySignId")
    Observable<NoticeDetailRsp> getMyNoticeBySignId(@Query("signId") long id);

    //消息模板分类类型
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/announcement/type/getAnnouncementTypePage")
    Observable<TemplateTypeRsp> getTemplateTypePage(@Body RequestBody requestBody);

    //获取消息模板
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/message/temp/selectMessagePage")
    Observable<TemplateListRsp> selectMessagePage(@Body RequestBody requestBody);

    //我的应用
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/application/me")
    Observable<MyAppListRsp> getMyApp(@Body RequestBody requestBody);

    //获取应用列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/application/group/list")
    Observable<AppItemBean> getAppList(@Body RequestBody requestBody);

    //应用排序
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/application/me/sort")
    Observable<ResultBean> appSort(@Body RequestBody requestBody);

    //查询应用列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/backstage/cloud-backstage/application/search")
    Observable<TemplateListRsp> searchApp(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/announcement/getMyNoticeBacklog")
    Observable<HomeNoticeRsp> getMyNoticeBacklog();

    //添加公告
    //https://api.uat.edu.1d1j.net/message/cloud-message/user/announcement/addUserAnnouncement
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/user/announcement/addUserAnnouncement")
    Observable<AddUserAnnouncementResponse> addUserAnnouncement(@Body RequestBody requestBody);

    //删除我发布的通告
    //https://api.uat.edu.1d1j.net/message/cloud-message/user/announcement/delAnnouncement
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/announcement/delAnnouncement")
    Observable<BaseRsp> delAnnouncement(@Query("id") long id);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/notice/updateMyNoticeDetails")
    Observable<BaseRsp> updateMyNoticeDetails(@Query("id") long id);

    //确认详情（多少人未确认）
    //https://api.uat.edu.1d1j.net/message/cloud-message/user/announcement/getConfirmDetails
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/announcement/getConfirmDetails")
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

    //https://api.uat.edu.1d1j.net/management/cloud-system/message/notice/scope/v1/app/queryDepartmentOverrideList
    //获取部门列表-权限校验
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/message/notice/scope/v1/app/queryDepartmentOverrideList")
    Observable<DepartmentScopeRsp2> queryDepartmentOverrideList(@Body RequestBody requestBody);

    ////https://api.uat.edu.1d1j.net/school-server/cloud-school/notice/scope/app/v1/queryOrganizationStructure
    //获取部门列表-无权限校验
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/notice/scope/app/v1/queryOrganizationStructure")
    Observable<DepartmentScopeRsp2> queryOrganizationStructure(@Body RequestBody requestBody);

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
    @POST("/management/cloud-system/app/teacher/selectAllList")
    Observable<ResultBean> searchBook(@Body RequestBody requestBody);

    //学校所有班级-大学
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/departmentClass/selectClassByAllSchool")
    Observable<SelectTableClassesRsp> selectClassByAllSchool();

    //学校所有班级-小初高
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/section/selectListBySchoolAll")
    Observable<SelectTableClassesRsp> selectListBySchoolAll();

    //获取应用列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/application/me/list")
    Observable<AppAddRsp> getAppAddList(@Body RequestBody requestBody);

    //添加应用列表
    @POST("/management/cloud-system/app/application/me/add/{id}")
    Observable<ResultBean> addApp(@Path("id") long id);

    //删除应用
    @POST("/management/cloud-system/app/application/me/del/{id}")
    Observable<ResultBean> deleteApp(@Path("id") long id);

    //获取班级作业信息
    @GET("/timetable/cloud-timetable/schedule/selectSchByClassId?")
    Observable<SelectSchByTeaidRsp> getClassesHomeworkInfo(@Query("classId") String id);

    //https://api.uat.edu.1d1j.net/management/cloud-system/app/teacher/selectAllList
    //通讯录搜索-所有
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/v1/app/addressBookApp/searchAddressBook")
    Observable<BookSearchRsp> selectAllList(@Body RequestBody requestBody);

    //通讯录搜索-小初高
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/student/list")
    Observable<TeacherlistRsp> getStudentList(@Body RequestBody requestBody);

    //获取首页学生作品
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/android/studentWork/list")
    Observable<UserInfoRsp> getStudentWorkList(@Body RequestBody requestBody);

    //获取消息代办列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/notice/getMessage/android")
    Observable<TodoRsp> getMessageTransaction(@QueryMap HashMap<String, Object> map);

    //https://api.uat.edu.1d1j.net/face/cloud-face/face/toStudentOss
    //上传学生face
    @Multipart
    @POST("/face/cloud-face/face/toStudentOss/{studentId}")
    Observable<BaseRsp> toStudentOss(@Path("studentId") String studentId, @Part MultipartBody.Part file);

    //https://api.uat.edu.1d1j.net/face/cloud-face/teacher/face/toTeacherOss
    //上传教师face
    @Multipart
    @POST("/face/cloud-face/teacher/face/toTeacherOss")
    Observable<BaseRsp> toTeacherOss(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part file);

    //https://api.uat.edu.1d1j.net/face/cloud-face/face/getStudentOss
    //查询学生头像
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/face/cloud-face/face/getStudentOss/{studentId}")
    Observable<FaceOssBean> getStudentOss(@Path("studentId") String studentId);

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
    @POST("/message/cloud-message/user/notice/queryUserMessageNotice")
    Observable<UserNoticeRsp> getUserNoticePage(@Body RequestBody requestBody);

    //https://api.uat.edu.1d1j.net/message/cloud-message/user/notice/queryUserMessageNotice
    //获取消息分页
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/notice/queryUserMessageNotice")
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
    @GET("/message/cloud-message/user/notice/queryBocklogNuberByStatus")
    Observable<MessageNumberRsp> queryBocklogNuberByStatus(@QueryMap HashMap<String, Object> map);

    //https://api.uat.edu.1d1j.net/leave-server/cloud-leave/leave/phrase/queryLeavePhraseList
    //请假短语 短语类型 1 家长 2教职工
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/leave-server/cloud-leave/leave/phrase/queryLeavePhraseList")
    Observable<LeavePhraseRsp> queryLeavePhraseList(@QueryMap HashMap<String, Object> map);

    //更新我收到的公告
    //https://api.uat.edu.1d1j.net/message/cloud-message/user/notice/updateMyNoticeDetails
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/user/notice/updateMyNoticeDetails")
    Observable<ResultBean> updateMyNoticeDetails(@QueryMap HashMap<String, Object> map);

    //首页考勤 老师视角的课堂考勤
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/face/cloud-face/attendance/viewAttendance")
    Observable<AttendanceCheckRsp> viewAttendance(@Body RequestBody requestBody);

    //https://api.uat.edu.1d1j.net/management/cloud-system/notice/scope/app/v1/queryDeptMemberByDeptId
    //查询部门所属成员
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/management/cloud-system/notice/scope/app/v1/queryDeptMemberByDeptId")
    Observable<AddressBookRsp> queryDeptMemberByDeptId(@QueryMap HashMap<String, Object> map);

    /**
     * 考勤统计 dateType 1：日，2：周，3：月
     * https://api.uat.edu.1d1j.net/face/cloud-face/attendance/viewAttStatistics
     *
     * @param requestBody body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/face/cloud-face/attendance/viewAttStatistics")
    Observable<AttendanceDayStatsRsp> viewAttDayStatistics(@Body RequestBody requestBody);

    /**
     * 考勤统计 dateType 1：日，2：周，3：月
     * https://api.uat.edu.1d1j.net/face/cloud-face/attendance/viewAttStatistics
     *
     * @param requestBody body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/face/cloud-face/attendance/viewAttStatistics")
    Observable<AttendanceWeekStatsRsp> viewAttWeekMonthStatistics(@Body RequestBody requestBody);

    //查询我发布的通知公告列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/app/message/publish/list")
    Observable<NoticeItemBean> myNoticeList(@Body RequestBody requestBody);

    //发布通知公告详情
    @GET("/message/cloud-message/app/message/publish/{id}")
    Observable<NoticeMyReleaseDetailBean> publishDetail(@Path("id") long id);

    //发布通知公告详情
    @GET("/message/cloud-message/app/message/publish/retract/{id}")
    Observable<ResultBean> retract(@Path("id") long id);

    //发布通知未读未确认人员列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/app/message/publish/unconfirm/users")
    Observable<NoticeUnreadPeopleBean> noticeUnreadList(@Body RequestBody requestBody);

    //我收到的通知列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/app/message/receive/list")
    Observable<NoticeItemBean> myReceiverNoticeList(@Body RequestBody requestBody);

    //我收到的-确认通知
    @GET("/message/cloud-message/app/message/receive/confirm/{id}")
    Observable<ResultBean> confirmNotice(@Path("id") long id);

    //我收到的确认詳情
    @GET("/message/cloud-message/app/message/receive/{id}")
    Observable<NoticeMyReleaseDetailBean> confirmNoticeDetail(@Path("id") long id);

    //首页通知弹窗
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/message/cloud-message/app/message/publish/indexShow")
    Observable<NoticeMyReleaseDetailBean> noticeShow();

    //通知模板列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/app/message/template/list")
    Observable<NoticeReleaseTemplateBean> templateNoticeList(@Body RequestBody requestBody);

    //通知模板分类列表
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/app/message/template/type/list")
    Observable<NoticeReleaseTemplateBean> templateNoticeClassifyList(@Body RequestBody requestBody);

    //发布空白模板通知
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/app/message/publish/do")
    Observable<ResultBean> releaseNotice(@Body RequestBody requestBody);

    //我收到的确认詳情
    @GET("/message/cloud-message/app/message/template/{id}")
    Observable<NoticeReleaseTemplateBean> templateDetail(@Path("id") long id);

    //通知范围学籍部门树形结构
    @GET("/message/cloud-message/app/message/publish/specifieType/{specifieType}")
    Observable<NoticePersonnelBean> specifieType(@Path("specifieType") String specifieType);

    //班牌班级
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/notification/notificationClasses")
    Observable<NoticeBrandBean> notificationClasses();

    //班牌场地
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/notification/notificationSite")
    Observable<NoticeBrandBean> notificationSite();

    //未读通知提醒
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/message/cloud-message/app/message/publish/unconfirm/users/notify")
    Observable<ResultBean> unNoticeNotify(@Body RequestBody requestBody);

    //通知权限
    @GET("/message/cloud-message/app/message/publish/permission")
    Observable<NoticePermissionBean> noticePermission();

    //获取登录页面开关
    @GET("/management/cloud-system/app/sys/switch/{key}")
    Observable<LoginAccountBean> accountSwitch(@Path("key") String key);

    //获取注册手机短信
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/app/user/reg/smsVerification")
    Observable<ResultBean> getRegisterSms(@Body RequestBody info);

    //注册
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/management/cloud-system/app/user/reg")
    Observable<ResultBean> register(@Body RequestBody info);

    /**
     * app获取码验证
     * https://api.uat.edu.1d1j.net/brand/class-brand-management/android/appRegistraCode/getRegistrationCodeByNumber
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/android/appRegistraCode/getRegistrationCodeByNumber")
    Observable<ClassBrandInfoRsp> getRegistrationCodeByOffice(@Body RequestBody info);

    /**
     * app注册码绑定
     * /brand/class-brand-management/android/appRegistraCode/updateRegistrationCodeByCode
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/android/appRegistraCode/updateRegistrationCodeByCode")
    Observable<BaseRsp> updateRegistrationCodeByCode(@Body RequestBody requestBody);

    /**
     * 获取班牌列表
     * https://api.uat.edu.1d1j.net/brand/class-brand-management/app/loginCheck/getClassBrand
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/app/loginCheck/getClassBrand")
    Observable<BrandSearchRsp> getClassBrand(@Body RequestBody requestBody);

    /**
     * 二维码扫码接口
     * https://api.uat.edu.1d1j.net/brand/class-brand-management/app/loginCheck/scan/3c954c?userName=admin
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/brand/class-brand-management/app/loginCheck/scan/{code}")
    Observable<BaseRsp> qrcodeLoginVerify(@Path("code") String code, @Query("userName") String userName);

    /**
     * app验证接口
     * /brand/class-brand-management/Android/Authority/verify
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/brand/class-brand-management/Android/Authority/verify")
    Call<BaseRsp> brandVerify(@Body RequestBody requestBody);

    /**
     * https://api.uat.edu.1d1j.net/management/cloud-system/app/face/authorize/findActivationCode
     * 查找激活码
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/face/authorize/findActivationCode")
    Observable<ActivateRsp> findActivationCode(@Query("macId") String macId);

    /**
     * 通讯录
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/v1/app/addressBookApp/getAddressBook")
    Observable<BookRsp> getAddressBook(@Body RequestBody requestBody);

    /**
     * 查询标签列表
     * https://api.uat.edu.1d1j.net/management/cloud-system/app/schedule/selectLabelList
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/schedule/selectLabelList")
    Call<LabelListRsp> selectLabelList();

    /**
     * 新增标签
     * https://api.uat.edu.1d1j.net/management/cloud-system/app/schedule/addLabel
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/schedule/addLabel")
    Call<BaseRsp> addLabel(@Body RequestBody requestBody);

    /**
     * 修改标签
     * https://api.uat.edu.1d1j.net/management/cloud-system/app/schedule/editLabel
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/schedule/editLabel")
    Call<BaseRsp> editLabel(@Body RequestBody requestBody);

    /**
     * 删除标签
     * https://api.uat.edu.1d1j.net/management/cloud-system/app/schedule/deleteLabelById/1437700895651627009
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/schedule/deleteLabelById/{id}")
    Call<BaseRsp> deleteLabelById(@Path("id") String id);

    /**
     * 获取场地信息-one
     * https://api.uat.edu.1d1j.net/management/cloud-system/app/schedule/getSiteName
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/schedule/getSiteName")
    Call<SiteNameRsp> getSiteName();

    /**
     * 日程新增修改
     * https://api.uat.edu.1d1j.net/management/cloud-system/app/schedule/saveSchedule
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/schedule/saveSchedule")
    Call<BaseRsp> saveSchedule(@Body RequestBody requestBody);

    /**
     * 查询个人设置
     * https://api.uat.edu.1d1j.net/management/cloud-system/app/schedule/getScheduleSetting
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("/management/cloud-system/app/schedule/getScheduleSetting")
    Call<Settings> getScheduleSetting();

    /**
     * 修改添加个人设置
     * https://api.uat.edu.1d1j.net/management/cloud-system/app/schedule/saveScheduleSetting
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/management/cloud-system/app/schedule/saveScheduleSetting")
    Call<BaseRsp> saveScheduleSetting(@Body RequestBody requestBody);

    /**
     * 获取周报描述
     *
     * @return
     */
    @GET("/face/cloud-face/copywriter")
    Observable<WeeklyDescBean> copywriter();

    /**
     * 获取周报时间
     *
     * @return
     */
    @GET("/face/cloud-face/statistic/time")
    Observable<WeeklyDateBean> getWeeklyDate();
}